package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MelodiSec extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private RelativeLayout m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16,m17;
    private TextView onayla,vazgec;
    private ImageView back,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17;
    int type = 0;
    int durum = 0;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melodi_sec);

        Init();
        Intents();
        Clicks();
    }

    private void Init(){
        back = findViewById(R.id.back);
        m1 = findViewById(R.id.m1);
        m2 = findViewById(R.id.m2);
        m3 = findViewById(R.id.m3);
        m4 = findViewById(R.id.m4);
        m5 = findViewById(R.id.m5);
        m6 = findViewById(R.id.m6);
        m7 = findViewById(R.id.m7);
        m8 = findViewById(R.id.m8);
        m9 = findViewById(R.id.m9);
        m10 = findViewById(R.id.m10);
        m11 = findViewById(R.id.m11);
        m12 = findViewById(R.id.m12);
        m13 = findViewById(R.id.m13);
        m14 = findViewById(R.id.m14);
        m15 = findViewById(R.id.m15);
        m16 = findViewById(R.id.m16);
        m17 = findViewById(R.id.m17);

        onayla = findViewById(R.id.onayla);
        vazgec = findViewById(R.id.vazgec);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        c9 = findViewById(R.id.c9);
        c10 = findViewById(R.id.c10);
        c11 = findViewById(R.id.c11);
        c12 = findViewById(R.id.c12);
        c13 = findViewById(R.id.c13);
        c14 = findViewById(R.id.c14);
        c15 = findViewById(R.id.c15);
        c16 = findViewById(R.id.c16);
        c17 = findViewById(R.id.c17);

    }

    private void Intents(){
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        type = getIntent().getExtras().getInt("type");

    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.davul);
                mediaPlayer.start();
                durum = 1;
                Check(c1);
            }
        });

        m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.essalat);
                mediaPlayer.start();
                durum = 2;
                Check(c2);
            }
        });

        m3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.ezan1);
                mediaPlayer.start();
                durum = 3;
                Check(c3);
            }
        });

        m4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.ezan2);
                mediaPlayer.start();
                durum = 4;
                Check(c4);
            }
        });

        m5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.ezandua);
                mediaPlayer.start();
                durum = 5;
                Check(c5);
            }
        });

        m6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.halaka1);
                mediaPlayer.start();
                durum = 6;
                Check(c6);
            }
        });


        m7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.halaka2);
                mediaPlayer.start();
                durum = 7;
                Check(c7);
            }
        });

        m8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.kisaezan1);
                mediaPlayer.start();
                durum = 8;
                Check(c8);
            }
        });


        m9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.kisaezan2);
                mediaPlayer.start();
                durum = 9;
                Check(c9);
            }
        });


        m10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.mekkeezani);
                mediaPlayer.start();
                durum = 10;
                Check(c10);
            }
        });


        m11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.melodi1);
                mediaPlayer.start();
                durum = 11;
                Check(c11);
            }
        });

        m12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.melodi2);
                mediaPlayer.start();
                durum = 12;
                Check(c12);
            }
        });

        m13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.sela);
                mediaPlayer.start();
                durum = 13;
                Check(c13);
            }
        });

        m14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.uyandirma1);
                mediaPlayer.start();
                durum = 14;
                Check(c14);
            }
        });

        m15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.uyandirma2);
                mediaPlayer.start();
                durum = 15;
                Check(c15);
            }
        });

        m16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.tek_tik);
                mediaPlayer.start();
                durum = 16;
                Check(c16);
            }
        });


        m17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(MelodiSec.this, R.raw.tek_tik_versiyon2);
                mediaPlayer.start();
                durum = 17;
                Check(c17);
            }
        });



        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                Melodi(durum);
                onBackPressed();
            }
        });

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                durum = 0;
                c1.setVisibility(View.GONE);
                c2.setVisibility(View.GONE);
                c3.setVisibility(View.GONE);
                c4.setVisibility(View.GONE);
                c5.setVisibility(View.GONE);
                c6.setVisibility(View.GONE);
                c7.setVisibility(View.GONE);
                c8.setVisibility(View.GONE);
                c9.setVisibility(View.GONE);
                c10.setVisibility(View.GONE);
                c11.setVisibility(View.GONE);
                c12.setVisibility(View.GONE);
                c13.setVisibility(View.GONE);
                c14.setVisibility(View.GONE);
                c15.setVisibility(View.GONE);
                c16.setVisibility(View.GONE);
                c17.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Melodi(int melodi){
        if(type == 1){
            sharedPreferences.edit().putInt("imsak_melodi", melodi).apply();
        } else if(type==2){
            sharedPreferences.edit().putInt("gunes_melodi", melodi).apply();
        } else if(type==3){
            sharedPreferences.edit().putInt("oglen_melodi", melodi).apply();
        } else if(type==4){
            sharedPreferences.edit().putInt("ikindi_melodi", melodi).apply();
        } else if(type==5){
            sharedPreferences.edit().putInt("aksam_melodi", melodi).apply();
        } else if(type==6){
            sharedPreferences.edit().putInt("yatsi_melodi", melodi).apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaying();
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void Check(ImageView num){
        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);
        c4.setVisibility(View.GONE);
        c5.setVisibility(View.GONE);
        c6.setVisibility(View.GONE);
        c7.setVisibility(View.GONE);
        c8.setVisibility(View.GONE);
        c9.setVisibility(View.GONE);
        c10.setVisibility(View.GONE);
        c11.setVisibility(View.GONE);
        c12.setVisibility(View.GONE);
        c13.setVisibility(View.GONE);
        c14.setVisibility(View.GONE);
        c15.setVisibility(View.GONE);
        c16.setVisibility(View.GONE);
        c17.setVisibility(View.GONE);

        num.setVisibility(View.VISIBLE);

    }

}