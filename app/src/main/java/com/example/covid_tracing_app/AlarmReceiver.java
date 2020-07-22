package com.example.covid_tracing_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = ".AlarmReceiver called";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onTaskRemoved() called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, BeaconService.class);
            context.startService(in);
        }
    }
}
