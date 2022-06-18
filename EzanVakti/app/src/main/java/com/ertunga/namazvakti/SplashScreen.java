package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.Special.Functions;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    Functions functions = new Functions();
    private SharedPreferences sharedPreferences;
    RequestQueue rg;
    private CheckBox check;
    private TextView sozlesme;
    private Button devam;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(this);



        if (!sharedPreferences.contains("login")) {
            sharedPreferences.edit().putInt("login", 0).apply();
        }

        if (!sharedPreferences.contains("gun")) {
            sharedPreferences.edit().putInt("gun", 0).apply();
        }

        if (!sharedPreferences.contains("bg")) {
            sharedPreferences.edit().putInt("bg", 0).apply();
        }

        if (!sharedPreferences.contains("check")) {
            sharedPreferences.edit().putInt("check", 0).apply();
        }


        if (!sharedPreferences.contains("bildirim_cubugu")) {
            sharedPreferences.edit().putInt("bildirim_cubugu", 1).apply();
        }

        if (!sharedPreferences.contains("sabah_bildirim")) {
            sharedPreferences.edit().putInt("sabah_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("oglen_bildirim")) {
            sharedPreferences.edit().putInt("oglen_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("ikindi_bildirim")) {
            sharedPreferences.edit().putInt("ikindi_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("aksam_bildirim")) {
            sharedPreferences.edit().putInt("aksam_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("yatsi_bildirim")) {
            sharedPreferences.edit().putInt("yatsi_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("vakit")) {
            sharedPreferences.edit().putInt("vakit", 0).apply();
        }

        if (!sharedPreferences.contains("gunun_hadisi")) {
            sharedPreferences.edit().putString("gunun_hadisi", null).apply();
        }

        if (!sharedPreferences.contains("gunun_hadisi_baslik")) {
            sharedPreferences.edit().putString("gunun_hadisi_baslik", null).apply();
        }

        if (!sharedPreferences.contains("gunun_duasi_baslik")) {
            sharedPreferences.edit().putString("gunun_duasi_baslik", null).apply();
        }


        if (!sharedPreferences.contains("gunun_duasi")) {
            sharedPreferences.edit().putString("gunun_duasi", null).apply();
        }


        if (!sharedPreferences.contains("imsak_dk")) {
            sharedPreferences.edit().putInt("imsak_dk", 15).apply();
        }

        if (!sharedPreferences.contains("oglen_dk")) {
            sharedPreferences.edit().putInt("oglen_dk", 15).apply();
        }

        if (!sharedPreferences.contains("gunes_dk")) {
            sharedPreferences.edit().putInt("gunes_dk", 15).apply();
        }

        if (!sharedPreferences.contains("ikindi_dk")) {
            sharedPreferences.edit().putInt("ikindi_dk", 15).apply();
        }

        if (!sharedPreferences.contains("aksam_dk")) {
            sharedPreferences.edit().putInt("aksam_dk", 15).apply();
        }

        if (!sharedPreferences.contains("yatsi_dk")) {
            sharedPreferences.edit().putInt("yatsi_dk", 15).apply();
        }


        if (!sharedPreferences.contains("imsak_dk_bildirim")) {
            sharedPreferences.edit().putInt("imsak_dk_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("gunes_dk_bildirim")) {
            sharedPreferences.edit().putInt("gunes_dk_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("oglen_dk_bildirim")) {
            sharedPreferences.edit().putInt("oglen_dk_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("ikindi_dk_bildirim")) {
            sharedPreferences.edit().putInt("ikindi_dk_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("aksam_dk_bildirim")) {
            sharedPreferences.edit().putInt("aksam_dk_bildirim", 1).apply();
        }

        if (!sharedPreferences.contains("yatsi_dk_bildirim")) {
            sharedPreferences.edit().putInt("yatsi_dk_bildirim", 1).apply();
        }


        if (!sharedPreferences.contains("imsak_melodi")) {
            sharedPreferences.edit().putInt("imsak_melodi", 11).apply();
        }

        if (!sharedPreferences.contains("gunes_melodi")) {
            sharedPreferences.edit().putInt("gunes_melodi", 11).apply();
        }

        if (!sharedPreferences.contains("oglen_melodi")) {
            sharedPreferences.edit().putInt("oglen_melodi", 11).apply();
        }

        if (!sharedPreferences.contains("ikindi_melodi")) {
            sharedPreferences.edit().putInt("ikindi_melodi", 11).apply();
        }

        if (!sharedPreferences.contains("aksam_melodi")) {
            sharedPreferences.edit().putInt("aksam_melodi", 11).apply();
        }

        if (!sharedPreferences.contains("yatsi_melodi")) {
            sharedPreferences.edit().putInt("yatsi_melodi", 11).apply();
        }


        check = findViewById(R.id.check);

        devam = findViewById(R.id.devam);
        sozlesme = findViewById(R.id.sozlesme);
        if(sharedPreferences.getInt("check",0) == 0){
            check.setVisibility(View.VISIBLE);
            sozlesme.setVisibility(View.VISIBLE);
            devam.setVisibility(View.VISIBLE);
            devam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(check.isChecked()){
                        sharedPreferences.edit().putInt("check", 1).apply();
                        Login();
                    } else {
                        sharedPreferences.edit().putInt("check", 0).apply();
                        Toast.makeText(SplashScreen.this, "Lütfen sözleşmeyi okuyunuz ve onaylayınız.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
/*
            sozlesme.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://mehmetcanertugrul.com/privacy_policy.php");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
*/
        } else {
            check.setVisibility(View.GONE);
            sozlesme.setVisibility(View.GONE);
            devam.setVisibility(View.GONE);
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {

                    Login();
                }
            }.start();

        }





        KazaOlustur();
    }

    public void AylikNamaz(String ilceid){
        String url = DefineUrl.Url+"?data=AylikNamaz&ilceid="+ilceid;
        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("data");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        //  UList.add(new IlceList(obj.getString("ilce"),obj.getString("ilceid")));
                        String gun = obj.getString("gun");
                        String ay = obj.getString("ay");
                        String yil = obj.getString("yil");
                        String imsak = obj.getString("imsak");
                        String gunes = obj.getString("gunes");
                        String oglen = obj.getString("oglen");
                        String ikindi = obj.getString("ikindi");
                        String aksam = obj.getString("aksam");
                        String yatsi = obj.getString("yatsi");
                        String hicri = obj.getString("hicri");
                        String guntxt = obj.getString("gun_txt");
                        String kible     = obj.getString("kible");
                        String hadis     = obj.getString("hadis");
                        String hadis_adi     = obj.getString("hadis_adi");
                        String dua     = obj.getString("dua");
                        String dua_adi     = obj.getString("dua_adi");
                        DataBase veritabanı=new DataBase(SplashScreen.this);
                        veritabanı.veriEkle(gun,ay,yil,imsak,gunes,oglen,ikindi,aksam,yatsi,hicri,guntxt,kible,hadis,hadis_adi,dua,dua_adi);

                        sharedPreferences.edit().putInt("kible",obj.getInt("kible")).apply();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        }
                    },1000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        } );
        rg.add( json );
    }

    private void KazaOlustur(){
        if (!sharedPreferences.contains("sabah")) {
            sharedPreferences.edit().putInt("sabah", 0).apply();
        }

        if (!sharedPreferences.contains("oglen")) {
            sharedPreferences.edit().putInt("oglen", 0).apply();
        }

        if (!sharedPreferences.contains("ikindi")) {
            sharedPreferences.edit().putInt("ikindi", 0).apply();
        }

        if (!sharedPreferences.contains("aksam")) {
            sharedPreferences.edit().putInt("aksam", 0).apply();
        }

        if (!sharedPreferences.contains("yatsi")) {
            sharedPreferences.edit().putInt("yatsi", 0).apply();
        }

        if (!sharedPreferences.contains("vitir")) {
            sharedPreferences.edit().putInt("vitir", 0).apply();
        }
    }

    private void Login(){
        if (sharedPreferences.getInt("login", 0) == 1) {
            AdsControl.getInstance().adsGecisLoading(this);
            if(functions.InternetControl(SplashScreen.this) == true) {

                Calendar calendar = Calendar.getInstance();
                DateFormat date= new SimpleDateFormat("EEEE", Locale.getDefault());
                date= new SimpleDateFormat("dd", Locale.getDefault());
                int dayNumber = Integer.parseInt(date.format(calendar.getTime()));

                if(sharedPreferences.getInt("gun",0) == dayNumber){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else{
                    sharedPreferences.edit().putInt("gun", dayNumber).apply();
                    AylikNamaz(sharedPreferences.getString("ilceid",null));
                }



            } else {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
      //  Toast.makeText(this, "login var", Toast.LENGTH_SHORT).show();

    } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, Sehirler.class));
                    finish();
                }
            },500);

        }
    }


}
