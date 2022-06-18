package com.ertunga.namazvakti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences sharedPreferences = context.getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("bildirim_cubugu", 0) == 1) {
            Intent serviceIntent = new Intent(context, NamazService.class);
            context.stopService(serviceIntent);
            ContextCompat.startForegroundService(context, serviceIntent);
        }

    }
}