package com.example.demo.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseApp extends Application {

    public static final String TAG = BaseApp.class.getSimpleName();

    Handler collapseNotificationHandler;
    private Thread.UncaughtExceptionHandler androidDefaultUEH;
    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            // log it & phone home.
            // androidDefaultUEH.uncaughtException(thread, ex);
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.demo");
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
            startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("TestTag","deviceid :"+androidId);
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }



}
