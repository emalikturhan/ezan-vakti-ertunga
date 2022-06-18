package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ertunga.namazvakti.Special.BlurTransform;
import com.ertunga.namazvakti.entity.Reklam;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.picasso.Picasso;

public class Uyarilar extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TextView tamam,title,hadis,hadis_baslik,gunun_duasi,dua_baslik;
    private ImageView bg;
    MediaPlayer mediaPlayer;
    private ImageView hadis_paylas,dua_paylas;
    String ghadis,gdua;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyarilar);

        AdView footer_reklam = findViewById(R.id.footer_reklam);
        footer_reklam.loadAd(new AdRequest.Builder().build());

        Init();
        Intents();
        Clicks();


    }

    private void getReklamJson() {
        final ImageView iv_ads = findViewById(R.id.iv_ads);

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, "https://mehmetcanertugrul.com/namazvakti/reklam/ads.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (response != null && !response.equals("")) {
                                Gson gson = new Gson();
                                final Reklam reklam = gson.fromJson(response, Reklam.class);

                                if (reklam != null) {
                                    iv_ads.setVisibility(View.VISIBLE);
                                    Glide.with(Uyarilar.this).load(reklam.getImage_url()).into(iv_ads);
                                    iv_ads.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(reklam.getSite_url()));
                                            startActivity(i);
                                        }
                                    });
                                }
                            }
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(postRequest);
    }


    private void Init(){
        title = findViewById(R.id.title);
        tamam = findViewById(R.id.tamam);
        bg = findViewById(R.id.bg);
        hadis = findViewById(R.id.gunun_hadisi);
        hadis_baslik = findViewById(R.id.hadis_adi);
        gunun_duasi = findViewById(R.id.gunun_duasi);
        hadis_paylas = findViewById(R.id.hadis_paylas);
        dua_paylas = findViewById(R.id.dua_paylas);
        dua_baslik = findViewById(R.id.dua_adi);
    }

    private void Intents(){
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Picasso.get().load(R.drawable.mosquesplash_2).transform(new BlurTransform(Uyarilar.this)).fit().centerInside().into(bg);
        }
        hadis.setText(sharedPreferences.getString("gunun_hadisi",null));
        hadis_baslik.setText(sharedPreferences.getString("gunun_hadisi_baslik",null));
        gunun_duasi.setText(sharedPreferences.getString("gunun_duasi",null));
        dua_baslik.setText(sharedPreferences.getString("gunun_duasi_baslik",null));

        title.setText(getIntent().getExtras().getString("title"));

        stopPlaying();
        if(getIntent().getExtras().getInt("melodi",0) == 1){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.davul);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 2){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.essalat);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 3){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.ezan1);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 4){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.ezan2);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 5){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.ezandua);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 6){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.halaka1);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 7){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.halaka2);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 8){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.kisaezan1);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 9){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.kisaezan2);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 10){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.mekkeezani);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 11){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.melodi1);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 12){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.melodi2);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 13){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.sela);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 14){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.uyandirma1);
            mediaPlayer.start();
        }
        else if(getIntent().getExtras().getInt("melodi",0) == 15){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.uyandirma2);
            mediaPlayer.start();
        }
        if(getIntent().getExtras().getInt("melodi",0) == 16){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.tek_tik);
            mediaPlayer.start();
        }
        if(getIntent().getExtras().getInt("melodi",0) == 17){
            mediaPlayer = MediaPlayer.create(Uyarilar.this, R.raw.tek_tik_versiyon2);
            mediaPlayer.start();
        }

        ghadis = sharedPreferences.getString("gunun_hadisi",null)+"\n"+sharedPreferences.getString("gunun_hadisi_baslik",null);
        gdua =  sharedPreferences.getString("gunun_duasi",null)+"\n"+sharedPreferences.getString("gunun_duasi_baslik",null);


        hadis_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ghadis);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        dua_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, gdua);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlaying();
    }

    private void Clicks(){
        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
               Uyarilar.this.finishAffinity();
            }
        });
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


}