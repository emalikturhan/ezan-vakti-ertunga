package com.ertunga.namazvakti;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.Functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiverService extends Service {

    Functions functions = new Functions();
    SharedPreferences sharedPreferences;
    public RemoteViews remoteViews;
    Handler handler;
    Runnable runnable;
    String zaman = "";
    String zaman2 = "";
    int tur = 0;
    NotificationCompat.Builder notification;
    Cursor cr;
    Date normal_time,imsak_time,gunes_time,oglen_time,ikindi_time,aksam_time,yatsi_time;
    int vakit = 0;
    int i = 0;
    boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRunning = true;
        sharedPreferences = getApplication().getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        stopService(new Intent(getApplicationContext(), NamazService.class));
        Intent notificationIntent = new Intent(this, MainActivity.class);
        remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.sari));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this, App.CHANNEL_ID);
        notification.setSmallIcon(R.drawable.ic_camii);
        notification.setCustomContentView(remoteViews);
        notification.setContentIntent(pendingIntent);
        notification.setSound(null);
        NamazDb();
        if(sharedPreferences.getInt("bildirim_cubugu",0) == 0){
            stopForegroundService();
        } else {
            startForeground(100, notification.build());
        }
      //  Timer();
        getApplicationContext().startService(new Intent(getApplicationContext(), NamazService.class));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initializeTimerTask() {
        java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int hours = 0;
        int min = 0;
        int days;
        int sec = 0;
        long difference;
        try {
            Date reset_time = dateFormat.parse("00:00:00");
            Date normal_time = dateFormat.parse(dateFormat.format(new Date()));
            Date after_time = dateFormat.parse(zaman);
            int x = normal_time.getHours();
            int y = after_time.getHours();
            int z = normal_time.getMinutes();
            int q = after_time.getMinutes();
            if(normal_time.getSeconds() == 0){
                sec = 0;
            } else {
                sec = 60-normal_time.getSeconds();
            }

            if(tur == 0){
                difference = after_time.getTime() - normal_time.getTime();
                days = (int) (difference / (1000*60*60*24));
                hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            } else {
                if(x>y){
                    int hesapla1 = ((24-x)*60)-z;
                    int hesapla2 = (y*60)+q;
                    Date new_time = dateFormat.parse(minuteToTime(hesapla1+hesapla2,sec));
                    difference = new_time.getTime() - reset_time.getTime();
                    days = (int) (difference / (1000*60*60*24));
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                } else {
                    difference = after_time.getTime() - normal_time.getTime();
                    days = (int) (difference / (1000*60*60*24));
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                }
            }

            String saat   = "00";
            String dakika = "00";
            String saniye = "00";
            if(hours < 10){
                saat = "0"+String.valueOf(hours);
            } else {
                saat = String.valueOf(hours);
            }
            if(min < 10){
                dakika = "0"+String.valueOf(min);
            } else {
                dakika = String.valueOf(min);
            }
            if(sec < 10){
                if(sec == 0){
                    saniye = "00";
                } else {
                    saniye = "0"+(60-normal_time.getSeconds());
                }
            } else {
                saniye = String.valueOf((60-normal_time.getSeconds()));
            }

            remoteViews.setTextViewText(R.id.vakit_txt,""+saat+":"+dakika);

            if(sharedPreferences.getInt("bildirim_cubugu",0) == 0){
                stopForegroundService();
            } else {

                if (normal_time.getSeconds() == 59) {
                    NamazDb();
                    startForeground(100, notification.build());
                }
            }

            if(vakit == 7){
                Namazlar(Sabah(zaman2).getHours(),Sabah(zaman2).getMinutes(),Sabah(zaman2).getSeconds());
               // Log.d("asd", String.valueOf(Sabah(zaman2).getHours())+":"+String.valueOf(Sabah(zaman2).getMinutes())+":"+String.valueOf(Sabah(zaman2).getSeconds())+" - vakit:"+String.valueOf(vakit));
            } else {
               // Log.d("asd", String.valueOf(hours)+":"+String.valueOf(min)+":"+String.valueOf(sec)+" - vakit:"+String.valueOf(vakit)+" - Receiver Servis");
                Namazlar(hours,min,sec);
            }

            Uyarilar(hours,min,sec);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void NamazDb() {
        DataBase veritabanı=new DataBase(getApplicationContext());
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Gun", "Ay", "Yil", "Imsak", "Gunes", "Oglen", "Ikindi", "Aksam", "Yatsi", "Hicri","GunTxt","Kible","Hadis","HadisAdi","Dua"};
        Cursor cr = db.query("Namazlar", sutunlar, null, null, null, null, null);
        while (cr.moveToNext()) {
            String aynum = functions.AyKontrol(cr.getString(1));
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(new Date());
            String date = cr.getString(2) + "-" + aynum + "-" + cr.getString(0);

            if (today.equals(date)) {
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                try {
                    Date new_time = timeFormat.parse(cr.getString(4) + ":00");
                    final long ONE_MINUTE_IN_MILLIS = 60000;
                    long curTimeInMs = new_time.getTime();
                    Date new_sabah = new Date(curTimeInMs - (60 * ONE_MINUTE_IN_MILLIS));

                    normal_time = dateFormat.parse(dateFormat.format(new Date()));
                    imsak_time = dateFormat.parse(date + " " + cr.getString(3) + ":00");
                    gunes_time = dateFormat.parse(date + " " + cr.getString(4) + ":00");
                    oglen_time = dateFormat.parse(date + " " + cr.getString(5) + ":00");
                    ikindi_time = dateFormat.parse(date + " " + cr.getString(6) + ":00");
                    aksam_time = dateFormat.parse(date + " " + cr.getString(7) + ":00");
                    yatsi_time = dateFormat.parse(date + " " + cr.getString(8) + ":00");

                    remoteViews.setTextViewText(R.id.il_txt, sharedPreferences.getString("ilce", null));
                    remoteViews.setTextViewText(R.id.imsak_saat, cr.getString(3));
                    remoteViews.setTextViewText(R.id.gunes_saat, cr.getString(4));
                    remoteViews.setTextViewText(R.id.oglen_saat, cr.getString(5));
                    remoteViews.setTextViewText(R.id.ikindi_saat, cr.getString(6));
                    remoteViews.setTextViewText(R.id.aksam_saat, cr.getString(7));
                    remoteViews.setTextViewText(R.id.yatsi_saat, cr.getString(8));

                    sharedPreferences.edit().putString("gunun_hadisi", cr.getString(12)).apply();
                    sharedPreferences.edit().putString("gunun_hadisi_baslik", cr.getString(13)).apply();
                    sharedPreferences.edit().putString("gunun_duasi", cr.getString(14)).apply();

                    if ((normal_time.after(imsak_time)) && (normal_time.before(gunes_time))) {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.beyaz));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.beyaz));

                        tur = 0;

                        if(normal_time.getTime() <= new_sabah.getTime()){
                            vakit = 7;
                            zaman2 = String.valueOf(new_sabah.getHours())+":"+String.valueOf(new_sabah.getMinutes()) + ":00";
                           // Log.d("asd",zaman2);
                        } else {
                            vakit = 2;
                        }
                        zaman = cr.getString(4)+ ":00";

                    } else if ((normal_time.after(gunes_time)) && (normal_time.before(oglen_time))) {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.beyaz));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.beyaz));

                        tur = 0;
                        vakit = 3;
                        zaman = cr.getString(5) + ":00";
                    } else if ((normal_time.after(oglen_time)) && (normal_time.before(ikindi_time))) {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.beyaz));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.beyaz));

                        tur = 0;
                        vakit = 4;
                        zaman = cr.getString(6) + ":00";
                    } else if ((normal_time.after(ikindi_time)) && (normal_time.before(aksam_time))) {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.beyaz));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.beyaz));

                        tur = 0;
                        vakit = 5;
                        zaman = cr.getString(7) + ":00";
                    } else if ((normal_time.after(aksam_time)) && (normal_time.before(yatsi_time))) {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.beyaz));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.sari));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.beyaz));

                        tur = 0;
                        vakit = 6;
                        zaman = cr.getString(8) + ":00";
                    } else {
                        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_saat, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_saat, getColor(R.color.sari));

                        remoteViews.setTextColor(R.id.imsak_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.gunes_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.oglen_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.ikindi_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.aksam_txt, getColor(R.color.beyaz));
                        remoteViews.setTextColor(R.id.yatsi_txt, getColor(R.color.sari));

                        tur = 1;
                        vakit = 1;
                        zaman = cr.getString(3) + ":00";
                    }

                   // Log.d("asd",String.valueOf(vakit));
                    sharedPreferences.edit().putInt("vakit", vakit).apply();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        veritabanı.close();
    }

    public Date Sabah(String zaman2){
        java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date newtime = null;
        int hours = 0;
        int min = 0;
        int days;
        int sec = 0;
        String saat   = "00";
        String dakika = "00";
        String saniye = "00";

        long difference;
        try {
            Date reset_time = dateFormat.parse("00:00:00");
            Date normal_time = dateFormat.parse(dateFormat.format(new Date()));
            Date after_time = dateFormat.parse(zaman2);
            int x = normal_time.getHours();
            int y = after_time.getHours();
            int z = normal_time.getMinutes();
            int q = after_time.getMinutes();
            if(normal_time.getSeconds() == 0){
                sec = 0;
            } else {
                sec = 60-normal_time.getSeconds();
            }

            if(tur == 0){
                difference = after_time.getTime() - normal_time.getTime();
                days = (int) (difference / (1000*60*60*24));
                hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            } else {
                if(x>y){
                    int hesapla1 = ((24-x)*60)-z;
                    int hesapla2 = (y*60)+q;
                    Date new_time = dateFormat.parse(minuteToTime(hesapla1+hesapla2,sec));
                    difference = new_time.getTime() - reset_time.getTime();
                    days = (int) (difference / (1000*60*60*24));
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                } else {
                    difference = after_time.getTime() - normal_time.getTime();
                    days = (int) (difference / (1000*60*60*24));
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                }
            }



            if(hours < 10){
                saat = "0"+String.valueOf(hours);
            } else {
                saat = String.valueOf(hours);
            }
            if(min < 10){
                dakika = "0"+String.valueOf(min);
            } else {
                dakika = String.valueOf(min);
            }
            if(sec < 10){
                if(sec == 0){
                    saniye = "00";
                } else {
                    saniye = "0"+(60-normal_time.getSeconds());
                }
            } else {
                saniye = String.valueOf((60-normal_time.getSeconds()));
            }

            newtime  =  dateFormat.parse(saat+":"+dakika+":"+saniye);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newtime;

    }

    public static String minuteToTime(int minute,int sec) {
        int hour = minute / 60;
        minute %= 60;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":"+String.valueOf(sec);
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void Timer(){
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                i++;
                if(i==60){
                    getApplicationContext().startService(new Intent(getApplicationContext(), NamazService.class));
                    getApplicationContext().stopService(new Intent(getApplicationContext(), ReceiverService.class));
                    isServiceRunning = false;
                    handler.removeCallbacks(runnable);
                    stopForeground(true);
                    stopSelf();
                }
                initializeTimerTask();
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }

    private void Uyarilar(int hours, int min, int sec){
        if(vakit == 1){
            if(sharedPreferences.getInt("imsak_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("imsak_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("imsak_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("imsak_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("imsak_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("imsak_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("imsak_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

        if(vakit == 2){
            if(sharedPreferences.getInt("gunes_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("gunes_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("gunes_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("gunes_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("gunes_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("gunes_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("gunes_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

        if(vakit == 3){
            if(sharedPreferences.getInt("oglen_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("oglen_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("oglen_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("oglen_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("oglen_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("oglen_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("oglen_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

        if(vakit == 4){
            if(sharedPreferences.getInt("ikindi_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("ikindi_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("ikindi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("ikindi_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("ikindi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("ikindi_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("ikindi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

        if(vakit == 5){
            if(sharedPreferences.getInt("aksam_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("aksam_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("aksam_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("aksam_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("aksam_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("aksam_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("aksam_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

        if(vakit == 6){
            if(sharedPreferences.getInt("yatsi_dk_bildirim",0) == 1){
                if(sharedPreferences.getInt("yatsi_dk",0) == 1){
                    if ((hours == 0) && (min == 14) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("yatsi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 15dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("yatsi_dk",0) == 2){
                    if ((hours == 0) && (min == 29) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("yatsi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 30dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else  if(sharedPreferences.getInt("yatsi_dk",0) == 3){
                    if ((hours == 0) && (min == 44) && (sec==59)) {
                        Bundle b = new Bundle();
                        b.putInt("melodi",sharedPreferences.getInt("yatsi_melodi",0));
                        b.putString("title","Vaktin Çıkmasına 45dk Kaldı");
                        Intent in = new Intent(this, Uyarilar.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }
            }
        }

    }

    private void Namazlar(int hours, int min, int sec){
        if(vakit == 1){
            //İmsak Vakti
        }

        if(vakit == 2){
            if(sharedPreferences.getInt("sabah_bildirim",0) == 1){
                if ((hours == 0) && (min == 59) && (sec==59)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi",1);
                    b.putString("title","Sabah Namazı Vakti");
                    b.putString("title2","Yatsı Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if(vakit == 3){
            if(sharedPreferences.getInt("oglen_bildirim",0) == 1){
                if ((hours == 0) && (min == 0) && (sec==1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi",2);
                    b.putString("title","Öğlen Namazı Vakti");
                    b.putString("title2","Sabah Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if(vakit == 4){
            if(sharedPreferences.getInt("ikindi_bildirim",0) == 1){
                if ((hours == 0) && (min == 0) && (sec==1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi",3);
                    b.putString("title","İkindi Namazı Vakti");
                    b.putString("title2","Öğlen Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if(vakit == 5){
            if(sharedPreferences.getInt("aksam_bildirim",0) == 1){
                if ((hours == 0) && (min == 0) && (sec==1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi",4);
                    b.putString("title","Akşam Namazı Vakti");
                    b.putString("title2","İkindi Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if(vakit == 6){
            if(sharedPreferences.getInt("yatsi_bildirim",0) == 1){
                if ((hours == 0) && (min == 0) && (sec==1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi",5);
                    b.putString("title","Yatsı Namazı Vakti");
                    b.putString("title2","Akşam Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }


    }

}
