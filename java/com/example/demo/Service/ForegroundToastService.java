package com.example.demo.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.example.demo.Activity.Login;
import com.example.demo.Activity.SplashScreen;
import com.example.demo.R;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.List;

public class ForegroundToastService extends Service {
    private final static int NOTIFICATION_ID = 1234;
 // private final static String STOP_SERVICE = ForegroundToastService.class.getPackage()+".stop";
    static ArrayList<String> packageList=new ArrayList<String>();
    private BroadcastReceiver stopServiceReceiver;
    private AppChecker appChecker;
    static Context context1;
    static String contactpackage, pkgnam;
    static ArrayList<String> runningAppList=new ArrayList<>();
    static String runningApps= "Our App Running";
    static String runningApps1= "Our App Running";
    SharedPreferences permission_share;
    public static final String permission_det = "permission_details";
    public static void start(Context context) {
        List<String> packageNames = new ArrayList<>();
        // Declare action which target application listen to initiate phone call
        Intent i = new Intent();
        i.setAction(Intent.ACTION_CALL);
     //   i.setData(Uri.parse("tel:" + no));
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(i, 0);
        for (ResolveInfo info : list) {
             pkgnam = info.activityInfo.packageName;
        }
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        // Query for all those applications
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        // Read package name of all those applications
        for(ResolveInfo resolveInfo : resolveInfos){
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            contactpackage= activityInfo.applicationInfo.packageName;
            //packageNames.add(activityInfo.applicationInfo.packageName);
        }
        Intent intent1= new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo defaultLauncher =context.getPackageManager().resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY);
        String defaultLauncherStr = defaultLauncher.activityInfo.packageName;

       /* Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        ResolveInfo resolveInfo =context.getPackageManager().resolveActivity(browserIntent,PackageManager.MATCH_DEFAULT_ONLY);

// This is the default browser's packageName
        String packageName = resolveInfo.activityInfo.packageName;*/
      //  PackageManager pm = context.getPackageManager();
        @SuppressLint("WrongConstant") List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_GIDS);

        for (ApplicationInfo app : apps) {
            if(pm.getLaunchIntentForPackage(app.packageName) != null) {
                // apps with launcher intent
                if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                    // updated system apps
                   // Log.d("TestTag","pppppp111: "+app);

                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    // system apps
                    if(!app.packageName.equals("com.android.settings")) {
                        packageList.add(app.packageName);
                    }
                   // Log.d("TestTag","pppppp: "+app);
                } else {
                    // user installed apps

                }
                // appsList.add(app);
            }

        }


        final String Login_de = "Login_details";
        SharedPreferences Login_share = context.getSharedPreferences(Login_de, Context.MODE_PRIVATE);
       String taskmodule = Login_share.getString("module", "");
        String accessList[] = taskmodule.split(",");
      //  Log.d("TestTag","ppp: "+contactpackage);
        packageList.add(Telephony.Sms.getDefaultSmsPackage(context));
        packageList.add(contactpackage);
        //packageList.add("com.android.incallui");
        //packageList.add("com.android.contacts");
        /*packageList.add("com.indiafilings.brochure");
        packageList.add("com.indiafilings.addcandidate");
        packageList.add("com.indiafilings.sales");
        packageList.add("com.indiafilings.ledgers");
        packageList.add("com.google.android.packageinstaller");
        packageList.add("com.google.android.apps.docs");
        packageList.add("com.google.android.gm");
        packageList.add("com.google.android.apps.maps");
        packageList.add("im.thebot.messenger");
        packageList.add("com.android.vending");
        packageList.add("com.android.documentsui");
        packageList.add("com.truecaller");
        packageList.add("android");
        packageList.add("com.google.android.gms");*/

        packageList.add(defaultLauncherStr);
        packageList.add(pkgnam);
        for (String aa : accessList) {
            packageList.add(aa.toString());
        }
        context1=context;
        context.startService(new Intent(context, ForegroundToastService.class));
    }
    public static void stop(Context context) {
        context.stopService(new Intent(context, ForegroundToastService.class));
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //registerReceivers();
        startChecker();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
     //   createStickyNotification();
       //Log.d("TestTag","p");
        permission_share = getApplicationContext().getSharedPreferences(permission_det, Context.MODE_PRIVATE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       removeNotification();
        unregisterReceivers();
       }
    private void startChecker() {
        appChecker = new AppChecker();
        appChecker.when(getPackageName(), new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        runningApps = packageName;
                    }
                })
                .whenOther(new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        /*String fileName = packageName.substring(0, packageName.lastIndexOf("."));
                        String extension = packageName.substring(packageName.lastIndexOf(".") + 1);
                        // System.out.println(beforeDot);
                        Log.d("TestTag","beforeDot: 11 "+extension);
                        if(packageList.contains(packageName)||extension.equals("incallui")){
                            runningApps= packageName;
                           //Log.d("TestTag","packageName:"+packageName);
                        }else {
                            String permissionEnable = permission_share.getString("permissions", "");
                            String setting = permission_share.getString("setting", "");
                            if(permissionEnable.length()!=0&&permissionEnable.equals("true")&&setting.equals("false")) {
                                Intent i = new Intent(getApplicationContext(), Welcome.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                runningApps = "" + packageName;
                                runningAppList.add(packageName);
                            }else if(permissionEnable.length()!=0&&permissionEnable.equals("true")&&setting.equals("true")){
                                if(!packageName.equals("com.android.settings")&&!packageName.equals("com.android.phone")&&!packageName.equals("com.android.thememanager")){
                                    Intent i = new Intent(getApplicationContext(), Welcome.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }else{
                                }
                            }
                        }
                       Log.d("TestTag","packageName: 11"+packageName);
                    }
                })
                .timeout(1000)
                .start(this);*/
                        Log.d("TestTag","paaaaaaaaa:"+packageName);
//                        String fileName = packageName.substring(0, packageName.lastIndexOf("."));
                        String extension = packageName.substring(packageName.lastIndexOf(".") + 1);
                        /*if(packageList.contains(packageName)||extension.equals("incallui")||extension.equals("messaging")){
                            runningApps= packageName;
                            Log.d("TestTag","packageNameqqq 11:"+packageName);
                        }else {
                            Log.d("TestTag","packageNameqqq 22:"+packageName);*/
                            String  permissionEnable = permission_share.getString("permissions", "");
                            String  setting = permission_share.getString("setting", "");
                            if(permissionEnable.length()!=0&&permissionEnable.equals("true")&&setting.equals("false")&&!packageName.equals("com.android.thememanager")) {
                                Intent i = new Intent(getApplicationContext(), SplashScreen.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                //runningApps = "" + packageName;
                                //runningAppList.add(packageName);
                                Log.d("TestTag","packageNameqqq 33:"+packageName);
                            }else if(permissionEnable.length()!=0&&permissionEnable.equals("false")&&setting.equals("true")){
                                if(!packageName.equals("com.android.settings")&&
                                        !packageName.equals("com.android.thememanager")&&
                                        !packageName.equals("com.miui.securitycenter")&&
                                        !packageName.equals("com.letv.android.letvsafe")&&
                                        !packageName.equals("com.huawei.systemmanager")&&
                                        !packageName.equals("com.coloros.safecenter")&&
                                        !packageName.equals("com.vivo.permissionmanager")&&
                                        !packageName.equals("com.oppo.safe")&&
                                        !packageName.equals("com.iqoo.secure")&&
                                        !packageName.equals("com.asus.mobilemanager"))
                                {

                                    Intent i = new Intent(getApplicationContext(), SplashScreen.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                                else
                                {
                                    Log.d("TestTag","packageNameqqq 55:"+packageName);
                                }
                            }
                        //}
                        Log.d("TestTag","packageName: 11"+packageName);
                    }
                })
                .timeout(1000)
                .start(this);
    }
    private void stopChecker() {
        appChecker.stop();
    }
    private void registerReceivers() {
        stopServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // stopSelf();
                ////Log.d("TestTag","stoptself");
            }
        };
       // registerReceiver(stopServiceReceiver, new IntentFilter(STOP_SERVICE));
    }
    private void unregisterReceivers() {
        unregisterReceiver(stopServiceReceiver);
    }
    private Notification createStickyNotification() {
        NotificationManager manager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name))
               // .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent(STOP_SERVICE), PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(0)
                .build();
        manager.notify(NOTIFICATION_ID, notification);
        return notification;
    }
    private void removeNotification() {
        NotificationManager manager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        manager.cancel(NOTIFICATION_ID);
    }
    public  static String runningApp(String status){
        StringBuilder builder = new StringBuilder();
        if(runningAppList.size()!=0) {
            if(status.equals("true")){
                runningAppList.clear();
            }else
            for (String details : runningAppList) {
                builder.append(details + ", ");
            }
             runningApps1 = builder.toString();
        }else{
            runningApps1=runningApps;
        }
        return runningApps1;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.demo";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Demo")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}
