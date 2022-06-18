package com.ertunga.namazvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.ertunga.namazvakti.Adaptors.ImsakAdaptor;
import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Lists.ImsakList;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Imsakiye extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    RecyclerView rc;
    ArrayList<ImsakList> UList;
    LinearLayoutManager LManager;
    private ImsakAdaptor adaptor;
    TextView title;
    private ImageView back;
    RequestQueue rg;
    private InterstitialAd mInterstitialAd;
    DataBase veritabanı=new DataBase(Imsakiye.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imsakiye);

        loadAd();

        Init();
        Clicks();
        veritabanı.Sifirla();
        AylikNamaz();
      //  //PremiumControl();
    }

    public void AylikNamaz() {

        String url = DefineUrl.Url + "?data=AylikNamaz&ilceid=" + sharedPreferences.getString("ilceid", null);
        Log.d("asdx", url);
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("data");
                    for (int i = 0; i <= ar.length(); i++) {
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
                        String kible = obj.getString("kible");
                        String hadis = obj.getString("hadis");
                        String hadis_adi = obj.getString("hadis_adi");
                        String dua = obj.getString("dua");
                        String dua_adi = obj.getString("dua_adi");
                        DataBase veritabanı = new DataBase(Imsakiye.this);
                        veritabanı.veriEkle(gun, ay, yil, imsak, gunes, oglen, ikindi, aksam, yatsi, hicri, guntxt, kible, hadis, hadis_adi, dua, dua_adi);

                        sharedPreferences.edit().putInt("kible", obj.getInt("kible")).apply();



                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   NamazDb();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rg.add(json);
    }

    private void Init(){
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rc = findViewById(R.id.rc);
        UList = new ArrayList<>();
        title = findViewById(R.id.title);
        title.setText(sharedPreferences.getString("ilce",null)+" İmsak Saatleri");
        back = findViewById(R.id.back);
        rg = Volley.newRequestQueue(this);
    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void NamazDb() {
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Gun", "Ay", "Yil", "Imsak", "Gunes", "Oglen", "Ikindi", "Aksam", "Yatsi", "Hicri","GunTxt"};
        Cursor cr = db.query("Namazlar", sutunlar, null, null, null, null, null);
        while (cr.moveToNext()) {
            UList.add(new ImsakList(cr.getString(0),cr.getString(10),cr.getString(1),cr.getString(9),cr.getString(3),cr.getString(4),cr.getString(5),cr.getString(7)));
            adaptor = new ImsakAdaptor(UList);
            LManager = new LinearLayoutManager(getApplicationContext());
            rc.setLayoutManager(LManager);
            rc.setItemAnimator(new DefaultItemAnimator());
            rc.setAdapter(adaptor);
            adaptor.notifyDataSetChanged();
        }
    }
/*
    public void PremiumControl(){
        String url = DefineUrl.Url+"?data=PremiumControl";
        StringRequest json = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    if(jsonResponse.getInt("state") == 1){
                    } else {
                        Reklam();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }


        };
        json.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rg.add(json);
    }*/

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-8620269889999726/1571357884",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Imsakiye.this.mInterstitialAd = interstitialAd;
                        showInterstitial();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Imsakiye.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Imsakiye.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }

    private void Reklam(){
        Log.e("Rece Reklam","Text");
        AdsControl.getInstance().adsGecisLoading(this);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {}
//        });
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_key));
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the interstitial ad is closed.
//            }
//        });
    }


}
