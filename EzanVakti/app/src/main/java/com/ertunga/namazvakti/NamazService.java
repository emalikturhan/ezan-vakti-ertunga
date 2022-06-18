package com.ertunga.namazvakti;

import static androidx.core.app.NotificationCompat.PRIORITY_LOW;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.Functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NamazService extends Service {

    Functions functions = new Functions();
    SharedPreferences sharedPreferences;
    public RemoteViews remoteViews;
    Handler handler;
    Runnable runnable;
    String zaman = "";
    String zaman2 = "";
    int tur = 0;
    Notification.Builder notification;
    Cursor cr;
    Date normal_time, imsak_time, gunes_time, oglen_time, ikindi_time, aksam_time, yatsi_time;
    int vakit = 0;
    int i = 0;
    boolean isServiceRunning = false;

    public static MediaPlayer mediaPlayer;
    int sendNtfTimes;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNtfTimes = 0;
        isServiceRunning = true;
        sharedPreferences = getApplication().getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        stopService(new Intent(getApplicationContext(), ReceiverService.class));
        Intent notificationIntent = new Intent(this, MainActivity.class);
        remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        remoteViews.setTextColor(R.id.imsak_saat, getColor(R.color.sari));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification = new Notification.Builder(this, App.CHANNEL_ID);
        notification.setSmallIcon(R.drawable.ic_camii);
        notification.setCustomContentView(remoteViews);
        notification.setContentIntent(pendingIntent);
        notification.setPriority(Notification.PRIORITY_MIN);
        notification.setSound(null);

        NamazDb();


        Timer timer;
        TimerTask timerTask;
        final Handler handler = new Handler();
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        initializeTimerTask();
                    }
                });
            }
        };
        timer.schedule(timerTask, 45000, 45000);

        startForeground();
        //startForeground(100, notification.build());


        //Timer();

        // sendNotification("Test", 0);
        return START_STICKY;
    }

    private void startForeground() {
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           channelId = createChannel("100", "My Background Service");
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            channelId ="";
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId);
        notification.setSmallIcon(R.drawable.ic_camii);
        notification.setCustomContentView(remoteViews);
        notification.setContentIntent(pendingIntent);
        notification.setPriority(Notification.PRIORITY_MIN);
        notification.setSound(null);



        startForeground(101, notification.build());
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private synchronized String createChannel(String channelId, String channelName) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_NONE;

        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return channelId;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //   isServiceRunning = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeTimerTask() {
        if (isServiceRunning == true) {
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
                if (normal_time.getSeconds() == 0) {
                    sec = 0;
                } else {
                    sec = 60 - normal_time.getSeconds();
                }

                if (tur == 0) {
                    difference = after_time.getTime() - normal_time.getTime();
                    days = (int) (difference / (1000 * 60 * 60 * 24));
                    hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                } else {
                    if (x > y) {
                        int hesapla1 = ((24 - x) * 60) - z;
                        int hesapla2 = (y * 60) + q;
                        Date new_time = dateFormat.parse(minuteToTime(hesapla1 + hesapla2, sec));
                        difference = new_time.getTime() - reset_time.getTime();
                        days = (int) (difference / (1000 * 60 * 60 * 24));
                        hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                        min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    } else {
                        difference = after_time.getTime() - normal_time.getTime();
                        days = (int) (difference / (1000 * 60 * 60 * 24));
                        hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                        min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    }
                }

                String saat = "00";
                String dakika = "00";
                String saniye = "00";
                if (hours < 10) {
                    saat = "0" + String.valueOf(hours);
                } else {
                    saat = String.valueOf(hours);
                }
                if (min < 10) {
                    dakika = "0" + String.valueOf(min);
                } else {
                    dakika = String.valueOf(min);
                }
                if (sec < 10) {
                    if (sec == 0) {
                        saniye = "00";
                    } else {
                        saniye = "0" + (60 - normal_time.getSeconds());
                    }
                } else {
                    saniye = String.valueOf((60 - normal_time.getSeconds()));
                }

                remoteViews.setTextViewText(R.id.vakit_txt, "" + saat + ":" + dakika);

                if (sharedPreferences.getInt("bildirim_cubugu", 0) == 0) {
                    stopForegroundService();
                } else {

                    NamazDb();
                    if (normal_time.getSeconds() == 1) {
                        startForeground(100, notification.build());
                        Log.d("asd", "çalıştı");
                    }
                }

                if (vakit == 7) {
                    // Namazlar(Sabah(zaman2).getHours(), Sabah(zaman2).getMinutes(), Sabah(zaman2).getSeconds());
                    // Log.d("asd", String.valueOf(Sabah(zaman2).getHours()) + ":" + String.valueOf(Sabah(zaman2).getMinutes()) + ":" + String.valueOf(Sabah(zaman2).getSeconds()) + " - vakit:" + String.valueOf(vakit));
                } else {
                    //  Log.d("asd", String.valueOf(hours) + ":" + String.valueOf(min) + ":" + String.valueOf(sec) + " - vakit:" + String.valueOf(vakit) + " - Namaz Servis");
                    //Namazlar(hours, min, sec);
                }


                String suan = String.valueOf(normal_time.getHours() + ":" + normal_time.getMinutes() + ":" + normal_time.getSeconds());
                String simsak = String.valueOf(imsak_time.getHours() + ":" + imsak_time.getMinutes() + ":0");
                String sgunes = String.valueOf(gunes_time.getHours() + ":" + gunes_time.getMinutes() + ":0");
                String soglen = String.valueOf(oglen_time.getHours() + ":" + oglen_time.getMinutes() + ":0");
                String sikindi = String.valueOf(ikindi_time.getHours() + ":" + ikindi_time.getMinutes() + ":0");
                String saksam = String.valueOf(aksam_time.getHours() + ":" + aksam_time.getMinutes() + ":0");
                String syatsi = String.valueOf(yatsi_time.getHours() + ":" + yatsi_time.getMinutes() + ":0");

                java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                Date new_time = timeFormat.parse(sgunes + ":00");
                final long ONE_MINUTE_IN_MILLIS = 60000;
                long curTimeInMs = new_time.getTime();
                Date new_sabah = new Date(curTimeInMs - (60 * ONE_MINUTE_IN_MILLIS));
                String ssabah = String.valueOf(new_sabah.getHours() + ":" + new_sabah.getMinutes() + ":0");

                Log.d("husox", saksam + " " + suan);

                if (simsak.equals(suan)) {

                } else if (ssabah.equals(suan)) {
                    if (sharedPreferences.getInt("sabah_bildirim", 0) == 1) {
                        Bundle b = new Bundle();
                        b.putInt("namaz_sesi", 1);
                        b.putString("title", "Sabah Namazı Vakti");
                        b.putString("title2", "Yatsı Namazını Kıldınmı");
                        Intent in = new Intent(this, Info.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);

                    }
                } else if (soglen.equals(suan)) {
                    if (sharedPreferences.getInt("oglen_bildirim", 0) == 1) {
                        Bundle b = new Bundle();
                        b.putInt("namaz_sesi", 2);
                        b.putString("title", "Öğlen Namazı Vakti");
                        b.putString("title2", "Sabah Namazını Kıldınmı");
                        Intent in = new Intent(this, Info.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else if (sikindi.equals(suan)) {
                    if (sharedPreferences.getInt("ikindi_bildirim", 0) == 1) {
                        Bundle b = new Bundle();
                        b.putInt("namaz_sesi", 3);
                        b.putString("title", "İkindi Namazı Vakti");
                        b.putString("title2", "Öğlen Namazını Kıldınmı");
                        Intent in = new Intent(this, Info.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else if (saksam.equals(suan)) {
                    if (sharedPreferences.getInt("aksam_bildirim", 0) == 1) {
                        Bundle b = new Bundle();
                        b.putInt("namaz_sesi", 4);
                        b.putString("title", "Akşam Namazı Vakti");
                        b.putString("title2", "İkindi Namazını Kıldınmı");
                        Intent in = new Intent(this, Info.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                } else if (syatsi.equals(suan)) {
                    if (sharedPreferences.getInt("yatsi_bildirim", 0) == 1) {
                        Bundle b = new Bundle();
                        b.putInt("namaz_sesi", 5);
                        b.putString("title", "Yatsı Namazı Vakti");
                        b.putString("title2", "Akşam Namazını Kıldınmı");
                        Intent in = new Intent(this, Info.class);
                        in.putExtras(b);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(in);
                    }
                }


                Uyarilar(hours, min, sec);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    private void NamazDb() {
        DataBase veritabanı = new DataBase(getApplicationContext());
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Gun", "Ay", "Yil", "Imsak", "Gunes", "Oglen", "Ikindi", "Aksam", "Yatsi", "Hicri", "GunTxt", "Kible", "Hadis", "HadisAdi", "Dua", "DuaAdi"};
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
                    sharedPreferences.edit().putString("gunun_duasi_baslik", cr.getString(15)).apply();

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

                        if (normal_time.getTime() <= new_sabah.getTime()) {
                            vakit = 7;
                            zaman2 = String.valueOf(new_sabah.getHours()) + ":" + String.valueOf(new_sabah.getMinutes()) + ":00";
                            //  Log.d("asd",zaman2);
                        } else {
                            vakit = 2;
                        }
                        zaman = cr.getString(4) + ":00";

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
        cr.close();
        veritabanı.close();
    }

    public Date Sabah(String zaman2) {
        java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date newtime = null;
        int hours = 0;
        int min = 0;
        int days;
        int sec = 0;
        String saat = "00";
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
            if (normal_time.getSeconds() == 0) {
                sec = 0;
            } else {
                sec = 60 - normal_time.getSeconds();
            }

            if (tur == 0) {
                difference = after_time.getTime() - normal_time.getTime();
                days = (int) (difference / (1000 * 60 * 60 * 24));
                hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            } else {
                if (x > y) {
                    int hesapla1 = ((24 - x) * 60) - z;
                    int hesapla2 = (y * 60) + q;
                    Date new_time = dateFormat.parse(minuteToTime(hesapla1 + hesapla2, sec));
                    difference = new_time.getTime() - reset_time.getTime();
                    days = (int) (difference / (1000 * 60 * 60 * 24));
                    hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                } else {
                    difference = after_time.getTime() - normal_time.getTime();
                    days = (int) (difference / (1000 * 60 * 60 * 24));
                    hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                    min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                }
            }


            if (hours < 10) {
                saat = "0" + String.valueOf(hours);
            } else {
                saat = String.valueOf(hours);
            }
            if (min < 10) {
                dakika = "0" + String.valueOf(min);
            } else {
                dakika = String.valueOf(min);
            }
            if (sec < 10) {
                if (sec == 0) {
                    saniye = "00";
                } else {
                    saniye = "0" + (60 - normal_time.getSeconds());
                }
            } else {
                saniye = String.valueOf((60 - normal_time.getSeconds()));
            }

            newtime = dateFormat.parse(saat + ":" + dakika + ":" + saniye);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newtime;

    }

    public static String minuteToTime(int minute, int sec) {
        int hour = minute / 60;
        minute %= 60;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + String.valueOf(sec);
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

    public void Timer() {
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                i++;
                if (i == 180) {
                    getApplicationContext().startService(new Intent(getApplicationContext(), ReceiverService.class));
                    getApplicationContext().stopService(new Intent(getApplicationContext(), NamazService.class));
                    isServiceRunning = false;
                    handler.removeCallbacks(runnable);
                    stopForeground(true);
                    stopSelf();
                }
                initializeTimerTask();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Uyarilar(int hours, int min, int sec) {
        min = (hours * 60) + min;
        if (vakit == 1) {
            if (sharedPreferences.getInt("imsak_dk_bildirim", 0) == 1) {

                int imsak_dk = sharedPreferences.getInt("imsak_dk", 0);
                if (imsak_dk == min && sendNtfTimes != 1) {
                    sendNtfTimes = 1;
                    sendNotification("İmsak vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("imsak_melodi", 0));
                }

            }
        }

        if (vakit == 2) {
            // Toast.makeText(this, "Güneş " + min, Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getInt("gunes_dk_bildirim", 0) == 1) {

                int gunes_dk = sharedPreferences.getInt("gunes_dk", 0);
                if (gunes_dk == min && sendNtfTimes != 2) {
                    sendNtfTimes = 2;
                    sendNotification("Güneş vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("gunes_melodi", 0));
                }

            }
        }

        if (vakit == 3) {
            // Toast.makeText(this, "Öğlen " + min, Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getInt("oglen_dk_bildirim", 0) == 1) {

                int oglen_dk = sharedPreferences.getInt("oglen_dk", 0);
                if (oglen_dk == min && sendNtfTimes != 3) {
                    sendNtfTimes = 3;
                    sendNotification("Öğlen vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("oglen_melodi", 0));
                }


            }
        }

        if (vakit == 4) {
            // Toast.makeText(this, "İkindi " + min, Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getInt("ikindi_dk_bildirim", 0) == 1) {

                int ikindi_dk = sharedPreferences.getInt("ikindi_dk", 0);
                if (ikindi_dk == min && sendNtfTimes != 4) {
                    sendNtfTimes = 4;
                    sendNotification("İkindi vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("ikindi_melodi", 0));
                }

            }
        }

        if (vakit == 5) {
            // Toast.makeText(this, "Akşam " + min, Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getInt("aksam_dk_bildirim", 0) == 1) {

                int aksam_dk = sharedPreferences.getInt("aksam_dk", 0);
                if (aksam_dk == min && sendNtfTimes != 5) {
                    sendNtfTimes = 5;
                    sendNotification("Akşam vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("aksam_melodi", 0));
                }

            }
        }

        if (vakit == 6) {
            //  Toast.makeText(this, "Yatsı " + min, Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getInt("yatsi_dk_bildirim", 0) == 1) {

                int yatsi_dk = sharedPreferences.getInt("yatsi_dk", 0);
                if (yatsi_dk == min && sendNtfTimes != 6) {
                    sendNtfTimes = 6;
                    sendNotification("Yatsı vaktine kalan: " + min + " Dakika", sharedPreferences.getInt("yatsi_melodi", 0));
                }

            }
        }

    }

    private void Namazlar(int hours, int min, int sec) {
        if (vakit == 1) {
            //İmsak Vakti
        }

        if (vakit == 2) {
            if (sharedPreferences.getInt("sabah_bildirim", 0) == 1) {
                if ((hours == 0) && (min == 59) && (sec == 59)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi", 1);
                    b.putString("title", "Sabah Namazı Vakti");
                    b.putString("title2", "Yatsı Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if (vakit == 3) {
            if (sharedPreferences.getInt("oglen_bildirim", 0) == 1) {
                if ((hours == 0) && (min == 0) && (sec == 1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi", 2);
                    b.putString("title", "Öğlen Namazı Vakti");
                    b.putString("title2", "Sabah Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if (vakit == 4) {
            if (sharedPreferences.getInt("ikindi_bildirim", 0) == 1) {
                if ((hours == 0) && (min == 0) && (sec == 1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi", 3);
                    b.putString("title", "İkindi Namazı Vakti");
                    b.putString("title2", "Öğlen Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if (vakit == 5) {
            if (sharedPreferences.getInt("aksam_bildirim", 0) == 1) {
                if ((hours == 2) && (min == 40) && (sec == 30)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi", 4);
                    b.putString("title", "Akşam Namazı Vakti");
                    b.putString("title2", "İkindi Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }

        if (vakit == 6) {
            if (sharedPreferences.getInt("yatsi_bildirim", 0) == 1) {
                if ((hours == 0) && (min == 0) && (sec == 1)) {
                    Bundle b = new Bundle();
                    b.putInt("namaz_sesi", 5);
                    b.putString("title", "Yatsı Namazı Vakti");
                    b.putString("title2", "Akşam Namazını Kıldınmı");
                    Intent in = new Intent(this, Info.class);
                    in.putExtras(b);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(in);
                }
            }
        }


    }

    public void playMelodi(int id) {

        if (id == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.davul);
            mediaPlayer.start();
        } else if (id == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.essalat);
            mediaPlayer.start();
        } else if (id == 3) {
            mediaPlayer = MediaPlayer.create(this, R.raw.ezan1);
            mediaPlayer.start();
        } else if (id == 4) {
            mediaPlayer = MediaPlayer.create(this, R.raw.ezan2);
            mediaPlayer.start();
        } else if (id == 5) {
            mediaPlayer = MediaPlayer.create(this, R.raw.ezandua);
            mediaPlayer.start();
        } else if (id == 6) {
            mediaPlayer = MediaPlayer.create(this, R.raw.halaka1);
            mediaPlayer.start();
        } else if (id == 7) {
            mediaPlayer = MediaPlayer.create(this, R.raw.halaka2);
            mediaPlayer.start();
        } else if (id == 8) {
            mediaPlayer = MediaPlayer.create(this, R.raw.kisaezan1);
            mediaPlayer.start();
        } else if (id == 9) {
            mediaPlayer = MediaPlayer.create(this, R.raw.kisaezan2);
            mediaPlayer.start();
        } else if (id == 10) {
            mediaPlayer = MediaPlayer.create(this, R.raw.mekkeezani);
            mediaPlayer.start();
        } else if (id == 11) {
            mediaPlayer = MediaPlayer.create(this, R.raw.melodi1);
            mediaPlayer.start();
        } else if (id == 12) {
            mediaPlayer = MediaPlayer.create(this, R.raw.melodi2);
            mediaPlayer.start();
        } else if (id == 13) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sela);
            mediaPlayer.start();
        } else if (id == 14) {
            mediaPlayer = MediaPlayer.create(this, R.raw.uyandirma1);
            mediaPlayer.start();
        } else if (id == 15) {
            mediaPlayer = MediaPlayer.create(this, R.raw.uyandirma2);
            mediaPlayer.start();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(String content, int melodi) {


        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = getApplicationContext().getResources();
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), App.CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_camii)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_camii))
                .setTicker("Hatırlatma")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Hatırlatma")
                .setContentText(content);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSound(null);

        Intent onClickIntent = new Intent(this, ClearNotificationSound.class);
        PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, onClickIntent, 0);
        builder.setDeleteIntent(onClickPendingIntent);
        builder.setContentIntent(onClickPendingIntent);

        final int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]


        Notification n = builder.build();
        nm.notify(random, n);

        playMelodi(melodi);


    }


}
