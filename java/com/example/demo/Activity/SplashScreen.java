package com.example.demo.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.demo.Activity.Login;
import com.example.demo.R;
import com.example.demo.Utils.Admin;
import com.example.demo.Utils.Permission_Activity;
import com.example.demo.Utils.common;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;
import static com.rvalerio.fgchecker.Utils.postLollipop;


public class SplashScreen extends AppCompatActivity {
    private Animation animation;
    private Handler mHandler = new Handler();
    SharedPreferences Login_share;
    public static final String Login_de = "Login_details";
    SharedPreferences permission_share;
    public static final String permission_det = "permission_details";
    String username;
    String permissionEnable;
    boolean isLoggedin;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
        /*animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_go_up);
        animation.setStartTime(3000);
        animation.setDuration(3500);*/

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Admin.class);

        Login_share = getApplicationContext().getSharedPreferences(Login_de, Context.MODE_PRIVATE);
        permission_share = getApplicationContext().getSharedPreferences(permission_det, Context.MODE_PRIVATE);

        permissionEnable = permission_share.getString("permissions", "").trim();
        isLoggedin=Login_share.getBoolean("isLoggedin",false);

        if (permissionEnable.length() == 0) {
            permissionEnable = "false";
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                findCurrent();
                //  overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void findCurrent() {
        if(isLoggedin || permissionEnable.equals("true"))
        {
            Intent i = new Intent(SplashScreen.this, Dashboard.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else
        {
            Intent in = new Intent(getApplicationContext(), Login.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }
}
