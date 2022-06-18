package com.ertunga.namazvakti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ServiceWorkManager extends Worker {
    public ServiceWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Worker.Result doWork() {
        Context context = getApplicationContext();


        Boolean isServiceRunning = MainActivity.ServiceTools.isServiceRunning(context, NamazService.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("bildirim_cubugu", 0) == 1) {
            Intent serviceIntent = new Intent(context, NamazService.class);
            context.startService(serviceIntent);
        }


        Log.d("RefreshDataWorker", "refreshing data....");
        return ListenableWorker.Result.success();
    }
}