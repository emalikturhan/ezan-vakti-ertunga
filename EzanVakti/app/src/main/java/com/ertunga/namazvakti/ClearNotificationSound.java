package com.ertunga.namazvakti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClearNotificationSound extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (NamazService.mediaPlayer.isPlaying()) {
                NamazService.mediaPlayer.stop();
            }
        } catch (Exception e) {

        }

    }
}