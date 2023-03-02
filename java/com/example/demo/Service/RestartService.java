package com.example.demo.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.demo.Activity.Login;
import com.example.demo.model.Constants;

public class RestartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TestTag","wakeup");

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent i = new Intent(context, BackgroundService.class);
            i.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            context.startService(i);
        }

    }
}
