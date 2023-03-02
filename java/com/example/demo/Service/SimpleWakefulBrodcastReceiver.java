package com.example.demo.Service;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.demo.model.Constants;

public class SimpleWakefulBrodcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        Intent service = new Intent(context, BackgroundService.class);

        service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);        // Start the service, keeping the device awake while it is launching.
        Log.d("TestTag", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}
