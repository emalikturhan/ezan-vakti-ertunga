package com.ertunga.namazvakti;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class App extends Application {
    public static final String CHANNEL_ID = "sabitbildirimcubugu";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        PeriodicWorkRequest refreshWork =   new PeriodicWorkRequest.Builder(ServiceWorkManager.class, 30, TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueue(refreshWork);

    }


    @SuppressLint("NewApi")
    private void createNotificationChannel(){

        try{
            @SuppressLint({"WrongConstant", "NewApi", "LocalSuppress"}) NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Sabit Bildirim Çubuğu", NotificationManager.IMPORTANCE_NONE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            serviceChannel.setSound(null,null);
            notificationManager.createNotificationChannel(serviceChannel);
        }
        catch (Exception ex){

        }


    }


}
