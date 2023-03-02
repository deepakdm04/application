package com.example.demo.Utils;

import android.Manifest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.example.demo.Activity.Dashboard;
import com.example.demo.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;
import static com.rvalerio.fgchecker.Utils.postLollipop;

public class Permission_Activity extends AppCompatActivity {

    static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 125;
    Switch swt_usageAccess, swt_auto_start, swt_adminstrate, swt_launcher, swt_displaypopup, swt_gps;
    Button sub_btn;
    private static final int ADMIN_INTENT = 1;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    SharedPreferences permission_share;
    public static final String permission_det = "permission_details";
    SharedPreferences Login_share;
    public static final String Login_de = "Login_details";
    Toolbar toolbar;
    SharedPreferences.Editor login_edit;
    String phonenum, IMEIno,IMEIno2,phone;
    static final Integer PHONESTATS = 0x1;
    String getphoneno="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Login_share = getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        login_edit=Login_share.edit();
        swt_usageAccess = (Switch) findViewById(R.id.usageAccess);
        swt_auto_start = (Switch) findViewById(R.id.autostart);
        swt_adminstrate = (Switch) findViewById(R.id.administ);
        swt_launcher = (Switch) findViewById(R.id.homeLauncher);
        swt_displaypopup = (Switch) findViewById(R.id.displaaypopup);
        swt_gps = (Switch) findViewById(R.id.gpsswitch);
        sub_btn = (Button) findViewById(R.id.submit);
        permission_share = getApplicationContext().getSharedPreferences(permission_det, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.Lead_toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
            }
        }

        if((phonenum == null || phonenum.trim().equals(""))&&(IMEIno == null || IMEIno.trim().equals(""))){
            getPhoneNumber(android.Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        }

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Admin.class);
        checkcondition();
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDevicePolicyManager.removeActiveAdmin(mComponentName);
                Log.d("TestTag", "okkkkk");
                return false;
            }
        });
        swt_usageAccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swt_adminstrate.isChecked()) {
                    if (needsUsageStatsPermission()) {
                        common.showtoast("Please Enable Demo Usage Access", Permission_Activity.this);
                        requestUsageStatsPermission();
                    }
                }
                else
                {
                    common.showtoast("Please enable the Device Administration first",Permission_Activity.this);
                    swt_usageAccess.setChecked(false);
                }
            }
        });
        swt_auto_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swt_launcher.isChecked())
                {
                    autostart();
                }
                else
                {
                    common.showtoast("Please enable the Demo as Home Launcher first",Permission_Activity.this);
                    swt_auto_start.setChecked(false);
                }
            }
        });
        swt_adminstrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)) {

                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator description");
                    startActivityForResult(intent, ADMIN_INTENT);
                }
            }
        });
        swt_launcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(swt_usageAccess.isChecked()) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    final String currentHomePackage = resolveInfo.activityInfo.packageName;
                    if (!currentHomePackage.equals("com.example.demo")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            final Intent intent1 = new Intent(Settings.ACTION_HOME_SETTINGS);
                            startActivity(intent1);
                        } else {
                            final Intent intent1 = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent1);
                        }
                    }
                }
                else
                {
                    common.showtoast("Please enable the Device Usage Access first",Permission_Activity.this);
                    swt_launcher.setChecked(false);
                }
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                permissionsallowed();
            }
        });
    }

    private boolean needsUsageStatsPermission() {
        return postLollipop() && !hasUsageStatsPermission(this);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestUsageStatsPermission() {
        if (!hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        if((phonenum == null || phonenum.trim().equals(""))&&(IMEIno == null || IMEIno.trim().equals(""))){
            getPhoneNumber(android.Manifest.permission.READ_PHONE_STATE, PHONESTATS);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {

            }
        }
        checkcondition();
    }

    private boolean checkAndRequestPermissions() {
        int readPhonepermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<String>();
        if (readPhonepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.SYSTEM_ALERT_WINDOW, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SYSTEM_ALERT_WINDOW) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            showDialogOK("Sorry, SMS and Phone call services Permission required for this app. So please ensure the SMS and Phone call permissions are enabled in settings",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            showDialogOK("Sorry, permissions are enabled in settings",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    appSettings();
                                                    break;
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("LET'S DO THIS", okListener)
                .create()
                .show();
    }
    private void autostart() {
        new AlertDialog.Builder(Permission_Activity.this)
                .setCancelable(true)
                .setMessage("Please Enable Autostart in Demo App")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent();
                            String manufacturer = Build.MANUFACTURER;
                            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                            } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                                intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActâ€Œâ€‹ivity"));
                            }
                            List<ResolveInfo> list = Permission_Activity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (list.size() > 0) {
                                Permission_Activity.this.startActivity(intent);
                                dialog.cancel();
                            }
                        } catch (Exception e) {
                        }
                    }
                })

                .create()
                .show();
    }
    public void appSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void permissionsallowed() {
       if(mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName))
       {
            swt_adminstrate.setChecked(true);
            if (!needsUsageStatsPermission()) {
                swt_usageAccess.setChecked(true);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                final String currentHomePackage = resolveInfo.activityInfo.packageName;
             if(!currentHomePackage.equals("com.example.demo")) {
                    swt_launcher.setChecked(false);
                    common.showtoast("Please Select Home app for Demo", getApplicationContext());
                }
                else {
                    swt_launcher.setChecked(true);
                    if (swt_launcher.isChecked() && swt_usageAccess.isChecked() && swt_adminstrate.isChecked() && swt_auto_start.isChecked()) {
                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        login_edit.putBoolean("isLoggedin",true);
                        login_edit.apply();
                        SharedPreferences.Editor perEdit1 = permission_share.edit();
                        perEdit1.putString("permissions", "true");
                        perEdit1.putString("setting", "false");
                        perEdit1.apply();
                        startActivity(i);
                    }
                }
            } else {
                swt_usageAccess.setChecked(false);
                common.showtoast("Please Enable Demo Usage Access", Permission_Activity.this);
            }
        }
        else {
            swt_adminstrate.setChecked(false);
            common.showtoast("Please Activate Adminstrate", Permission_Activity.this);
        }
    }
    public void checkcondition() {

        if (mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)) {
            swt_adminstrate.setChecked(true);
        } else {
            swt_adminstrate.setChecked(false);
        }
        if (!needsUsageStatsPermission()) {
            swt_usageAccess.setChecked(true);
        } else {
            swt_usageAccess.setChecked(false);
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        final String currentHomePackage = resolveInfo.activityInfo.packageName;
        if (!currentHomePackage.equals("com.example.demo")) {
            swt_launcher.setChecked(false);
        } else {
            swt_launcher.setChecked(true);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void getPhoneNumber(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Permission_Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Permission_Activity.this, permission)) {
                ActivityCompat.requestPermissions(Permission_Activity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(Permission_Activity.this, new String[]{permission}, requestCode);
            }
        } else {
            final TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyManager telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                Method getFirstMethod = telephonyClass.getMethod("getDeviceId", parameter);
                Object[] obParameter = new Object[1];
                obParameter[0] = 0;
                IMEIno = (String) getFirstMethod.invoke(telephony, obParameter);
                obParameter[0] = 1;
                IMEIno2 = (String) getFirstMethod.invoke(telephony, obParameter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            phonenum = telephonyManager.getLine1Number();
            if(phonenum == null || phonenum.trim().equals("")){
                phonenum = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }

            login_edit.putString("imei",IMEIno);
            login_edit.apply();

        }
    }
}
