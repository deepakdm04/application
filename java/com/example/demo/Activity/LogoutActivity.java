package com.example.demo.Activity;

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

import com.example.demo.R;
import com.example.demo.Service.BackgroundService;
import com.example.demo.Utils.Admin;
import com.example.demo.Utils.common;
import com.example.demo.model.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;
import static com.rvalerio.fgchecker.Utils.postLollipop;

public class LogoutActivity extends AppCompatActivity {

    static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 125;
    Switch swt_usageAccess, swt_auto_start, swt_adminstrate, swt_launcher, swt_displaypopup, swt_gps;
    Button sub_btn;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    SharedPreferences permission_share;
    public static final String permission_det = "permission_details";
    SharedPreferences Login_share;
    SharedPreferences.Editor login_edit;
    public static final String Login_de = "Login_details";
    Toolbar toolbar;
    SharedPreferences Demo_share;
    public static final String Demo_de = "Demo_details";
    SharedPreferences.Editor demo_edit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        Login_share = getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        login_edit=Login_share.edit();
        Demo_share = getApplicationContext().getSharedPreferences(Demo_de,Context.MODE_PRIVATE);
        demo_edit=Demo_share.edit();

        swt_usageAccess = (Switch) findViewById(R.id.usageAccess);
        swt_auto_start = (Switch) findViewById(R.id.autostart);
        swt_adminstrate = (Switch) findViewById(R.id.administ);
        swt_launcher = (Switch) findViewById(R.id.homeLauncher);
        swt_displaypopup = (Switch) findViewById(R.id.displaaypopup);
        swt_gps = (Switch) findViewById(R.id.gpsswitch);

        swt_usageAccess.setChecked(true);
        swt_auto_start.setChecked(true);
        swt_adminstrate.setChecked(true);
        swt_launcher.setChecked(true);
        swt_displaypopup.setChecked(true);

        sub_btn = (Button) findViewById(R.id.submit);
        Login_share = getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        permission_share = getApplicationContext().getSharedPreferences(permission_det, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.Lead_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
            }
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
                if(!swt_adminstrate.isChecked()) {
                    if (!needsUsageStatsPermission()) {
                        common.showtoast("Please disable Demo Usage Access", LogoutActivity.this);
                        requestUsageStatsPermission();
                    }
                }
                else
                {
                    common.showtoast("Please disable the Device Administration first",LogoutActivity.this);
                    swt_usageAccess.setChecked(true);
                }

            }
        });
        swt_auto_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!swt_launcher.isChecked()) {
                    autostart();
                }
                else
                {
                    common.showtoast("Please enable the Demo as Home Launcher first",LogoutActivity.this);
                    swt_auto_start.setChecked(true);
                }
            }
        });
        swt_adminstrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)) {
                    mDevicePolicyManager.removeActiveAdmin(mComponentName);
                }
            }
        });
        swt_launcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!swt_usageAccess.isChecked()) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    final String currentHomePackage = resolveInfo.activityInfo.packageName;
                    if (currentHomePackage.equals("com.example.demo")) {
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
                    common.showtoast("Please disable the Device Usage Access first",LogoutActivity.this);
                    swt_launcher.setChecked(true);
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
        if (hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
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
        new AlertDialog.Builder(LogoutActivity.this)
                .setCancelable(false)
                .setMessage("Please Disable Autostart in Demo App")
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
                            List<ResolveInfo> list = LogoutActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (list.size() > 0) {
                                LogoutActivity.this.startActivity(intent);
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
        if(mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)){
            swt_adminstrate.setChecked(true);
            common.showtoast("Please DeActivate Adminstrate", LogoutActivity.this);
        }
        else {
            swt_adminstrate.setChecked(false);
            if (needsUsageStatsPermission()) {
                swt_usageAccess.setChecked(false);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                final String currentHomePackage = resolveInfo.activityInfo.packageName;
                if(currentHomePackage.equals("com.example.demo")) {
                    swt_launcher.setChecked(true);
                    common.showtoast("Please Deselect Home app from Demo", getApplicationContext());
                }else {
                    swt_launcher.setChecked(false);
                    if (!swt_launcher.isChecked() && !swt_usageAccess.isChecked() && !swt_adminstrate.isChecked() && !swt_auto_start.isChecked()) {
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        login_edit.putBoolean("isLoggedin",false);
                        login_edit.apply();
                        demo_edit.putString("display","0");
                        demo_edit.commit();
                        startActivity(i);
                        stopService(new Intent(getApplicationContext(),
                                BackgroundService.class));
                        Intent in = new Intent(LogoutActivity.this, BackgroundService.class);
                        in.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        stopService(in);
                    }
                }
            } else {
                swt_usageAccess.setChecked(true);
                common.showtoast("Please Disable Demo Usage Access", LogoutActivity.this);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        checkcondition();
        super.onResume();
    }
}
