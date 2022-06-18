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
import android.widget.Toast;

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

public class Info extends AppCompatActivity {

    int sabah = 0;
    int oglen = 0;
    int ikindi = 0;
    int aksam = 0;
    int yatsi = 0;
    int vitir = 0;
    int namaz_mod = 0;

    SharedPreferences sharedPreferences;
    private TextView evet, hayir, simdiki_vakit, onceki_vakit, hadis, hadis_baslik, gunun_duasi, dua_baslik;
    private ImageView bg;
    MediaPlayer mediaPlayer;
    private ImageView hadis_paylas, dua_paylas;
    String ghadis, gdua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        AdView footer_reklam = findViewById(R.id.footer_reklam);
        footer_reklam.loadAd(new AdRequest.Builder().build());

        Init();
        Intents();
        Clicks();
        KazaGetir();
        getReklamJson();
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
                                    Glide.with(Info.this).load(reklam.getImage_url()).into(iv_ads);
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


    private void Init() {
        simdiki_vakit = findViewById(R.id.simdiki_vakit);
        onceki_vakit = findViewById(R.id.onceki_vakit);
        evet = findViewById(R.id.evet);
        hayir = findViewById(R.id.hayir);
        bg = findViewById(R.id.bg);
        hadis = findViewById(R.id.gunun_hadisi);
        hadis_baslik = findViewById(R.id.hadis_adi);
        gunun_duasi = findViewById(R.id.gunun_duasi);
        hadis_paylas = findViewById(R.id.hadis_paylas);
        dua_paylas = findViewById(R.id.dua_paylas);
        dua_baslik = findViewById(R.id.dua_adi);
    }

    private void Intents() {
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Picasso.get().load(R.drawable.mosquesplash_2).transform(new BlurTransform(Info.this)).fit().centerInside().into(bg);
        }
        hadis.setText(sharedPreferences.getString("gunun_hadisi", null));
        hadis_baslik.setText(sharedPreferences.getString("gunun_hadisi_baslik", null));
        gunun_duasi.setText(sharedPreferences.getString("gunun_duasi", null));
        dua_baslik.setText(sharedPreferences.getString("gunun_duasi_baslik", null));

        ghadis = sharedPreferences.getString("gunun_hadisi", null) + "\n" + sharedPreferences.getString("gunun_hadisi_baslik", null);
        gdua = sharedPreferences.getString("gunun_duasi", null) + "\n" + sharedPreferences.getString("gunun_duasi_baslik", null);


        simdiki_vakit.setText(getIntent().getExtras().getString("title"));
        onceki_vakit.setText(getIntent().getExtras().getString("title2"));

        stopPlaying();
        if (getIntent().getExtras().getInt("namaz_sesi", 0) == 1) {
            mediaPlayer = MediaPlayer.create(Info.this, R.raw.sabah);
            mediaPlayer.start();
            namaz_mod = 5;
        } else if (getIntent().getExtras().getInt("namaz_sesi", 0) == 2) {
            mediaPlayer = MediaPlayer.create(Info.this, R.raw.oglen);
            mediaPlayer.start();
            namaz_mod = 1;
        } else if (getIntent().getExtras().getInt("namaz_sesi", 0) == 3) {
            mediaPlayer = MediaPlayer.create(Info.this, R.raw.ikindi);
            mediaPlayer.start();
            namaz_mod = 2;
        } else if (getIntent().getExtras().getInt("namaz_sesi", 0) == 4) {
            mediaPlayer = MediaPlayer.create(Info.this, R.raw.aksam);
            mediaPlayer.start();
            namaz_mod = 3;
        } else if (getIntent().getExtras().getInt("namaz_sesi", 0) == 5) {
            mediaPlayer = MediaPlayer.create(Info.this, R.raw.yatsi);
            mediaPlayer.start();
            namaz_mod = 4;
        }

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

    @Override
    protected void onPause() {
        super.onPause();
        stopPlaying();
    }

    private void Clicks() {
        evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Info.this, "Allah kıldığınız namazınızı derğahı izzetinde kabul eylesin.", Toast.LENGTH_SHORT).show();
                Info.this.finishAffinity();
            }
        });

        hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaz_mod == 1) {
                    sabah = sabah + 1;
                    sharedPreferences.edit().putInt("sabah", sabah).apply();
                    Toast.makeText(Info.this, "Kaza namazınız sisteme eklendi", Toast.LENGTH_SHORT).show();
                    Info.this.finishAffinity();
                } else if (namaz_mod == 2) {
                    oglen = oglen + 1;
                    sharedPreferences.edit().putInt("oglen", oglen).apply();
                    Toast.makeText(Info.this, "Kaza namazınız sisteme eklendi", Toast.LENGTH_SHORT).show();
                    Info.this.finishAffinity();
                } else if (namaz_mod == 3) {
                    ikindi = ikindi + 1;
                    sharedPreferences.edit().putInt("ikindi", ikindi).apply();
                    Toast.makeText(Info.this, "Kaza namazınız sisteme eklendi", Toast.LENGTH_SHORT).show();
                    Info.this.finishAffinity();
                } else if (namaz_mod == 4) {
                    aksam = aksam + 1;
                    sharedPreferences.edit().putInt("aksam", aksam).apply();
                    Toast.makeText(Info.this, "Kaza namazınız sisteme eklendi", Toast.LENGTH_SHORT).show();
                    Info.this.finishAffinity();
                } else if (namaz_mod == 5) {
                    yatsi = yatsi + 1;
                    vitir = vitir + 1;
                    sharedPreferences.edit().putInt("yatsi", yatsi).apply();
                    sharedPreferences.edit().putInt("vitir", vitir).apply();
                    Toast.makeText(Info.this, "Kaza namazınız sisteme eklendi", Toast.LENGTH_SHORT).show();
                    Info.this.finishAffinity();
                }

            }
        });


    }

    private void KazaGetir() {
        sabah = sharedPreferences.getInt("sabah", 0);
        oglen = sharedPreferences.getInt("oglen", 0);
        ikindi = sharedPreferences.getInt("ikindi", 0);
        aksam = sharedPreferences.getInt("aksam", 0);
        yatsi = sharedPreferences.getInt("yatsi", 0);
        vitir = sharedPreferences.getInt("vitir", 0);
    }


}
