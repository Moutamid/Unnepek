package com.moutamid.unnepek.services;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Restarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mServiceIntent = new Intent(context, BackgroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(mServiceIntent);
        } else {
            context.startService(mServiceIntent);
        }
    }
}