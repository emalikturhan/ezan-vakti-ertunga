package com.ertunga.namazvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ertunga.namazvakti.Db.ZikirDb;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ZikirDetay extends AppCompatActivity {


    private TextView title,tab1,tab2,tab3,icerik1,icerik2,icerik3,okunan_txt,adet_txt,toplam_txt;
    private ImageView back,menu;
    private RelativeLayout counter;
    private Button uygula;
    Vibrator v;
    int adet = 0;
    int toplam = 0;
    int okunan = 0;
    int durum = 0;
    private RelativeLayout reset_content;
    private LinearLayout reset_box;
    private Button vazgec,sifirla;
    private Animation op_on,op_off;
    private InterstitialAd mInterstitialAd;
    RequestQueue rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikir_detay);
        loadAd();
        Init();
        Intents();
        Clicks();
        Animations();
        ZikirDb();


    }


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
                        ZikirDetay.this.mInterstitialAd = interstitialAd;
                        showInterstitial();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        ZikirDetay.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        ZikirDetay.this.mInterstitialAd = null;
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

    private void Init(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        icerik1 = findViewById(R.id.icerik1);
        icerik2 = findViewById(R.id.icerik2);
        icerik3 = findViewById(R.id.icerik3);
        counter = findViewById(R.id.counter);
        okunan_txt = findViewById(R.id.okunan_txt);
        adet_txt = findViewById(R.id.adet_txt);
        toplam_txt = findViewById(R.id.toplam_txt);
        uygula = findViewById(R.id.uygula);

        menu = findViewById(R.id.menu);
        reset_content = findViewById(R.id.reset_content);
        reset_box = findViewById(R.id.reset_box);
        vazgec = findViewById(R.id.vazgec);
        sifirla = findViewById(R.id.sifirla);
    }

    private void Intents(){

        adet = getIntent().getExtras().getInt("adet");
        adet_txt.setText(String.valueOf(adet));
        icerik1.setText(getIntent().getExtras().getString("okunus"));
        icerik2.setText(getIntent().getExtras().getString("meali"));
        icerik3.setText(getIntent().getExtras().getString("arapca"));
        if(getIntent().getExtras().getString("baslik").length() > 15){
            title.setText(getIntent().getExtras().getString("baslik").substring(0,15)+"...");
        } else {
            title.setText(getIntent().getExtras().getString("baslik"));
        }
        rg = Volley.newRequestQueue(this);
    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setTextColor(getColor(R.color.sari));
                tab2.setTextColor(getColor(R.color.acik_mor));
                tab3.setTextColor(getColor(R.color.acik_mor));

                icerik1.setVisibility(View.VISIBLE);
                icerik2.setVisibility(View.GONE);
                icerik3.setVisibility(View.GONE);
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setTextColor(getColor(R.color.acik_mor));
                tab2.setTextColor(getColor(R.color.sari));
                tab3.setTextColor(getColor(R.color.acik_mor));

                icerik1.setVisibility(View.GONE);
                icerik2.setVisibility(View.VISIBLE);
                icerik3.setVisibility(View.GONE);
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setTextColor(getColor(R.color.acik_mor));
                tab2.setTextColor(getColor(R.color.acik_mor));
                tab3.setTextColor(getColor(R.color.sari));

                icerik1.setVisibility(View.GONE);
                icerik2.setVisibility(View.GONE);
                icerik3.setVisibility(View.VISIBLE);
            }
        });


        uygula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZikirDb veritabanı=new ZikirDb(getApplicationContext());

                okunan = okunan+1;
                okunan_txt.setText(String.valueOf(okunan));

                if(okunan == adet){
                    toplam = toplam+1;
                    toplam_txt.setText(String.valueOf(toplam));
                    okunan = 0;
                }

                if(durum == 1){
                    veritabanı.Update(okunan,adet,toplam,getIntent().getExtras().getInt("id"));
                } else {
                    veritabanı.veriEkle(okunan,adet,toplam,getIntent().getExtras().getInt("id"));
                    durum = 1;
                }
            }
        });

        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZikirDb veritabanı=new ZikirDb(getApplicationContext());
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);

                okunan = okunan+1;
                okunan_txt.setText(String.valueOf(okunan));

                if(okunan == adet){
                    toplam = toplam+1;
                    toplam_txt.setText(String.valueOf(toplam));
                    okunan = 0;
                }

                if(durum == 1){
                    veritabanı.Update(okunan,adet,toplam,getIntent().getExtras().getInt("id"));
                } else {
                    veritabanı.veriEkle(okunan,adet,toplam,getIntent().getExtras().getInt("id"));
                    durum = 1;
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_content.setVisibility(View.VISIBLE);
                reset_box.startAnimation(op_on);
            }
        });

        reset_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reset_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reset_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        sifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reset_content.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ZikirDb veritabanı=new ZikirDb(getApplicationContext());
                veritabanı.Update(0,adet,0,getIntent().getExtras().getInt("id"));
                okunan = 0;
                toplam = 0;
                okunan_txt.setText("0");
                toplam_txt.setText("0");
            }
        });


    }

    private void Animations(){
        op_on = AnimationUtils.loadAnimation(this,R.anim.opacity_on);
        op_off = AnimationUtils.loadAnimation(this,R.anim.opacity_off);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ZikirDb(){
        ZikirDb veritabanı = new ZikirDb(getApplicationContext());
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Okunan", "Adet", "Toplam", "ZikirId"};
        Cursor cr = db.query("Zikirler", sutunlar, null, null, null, null, null);
        while (cr.moveToNext()) {
            if(getIntent().getExtras().getInt("id") == cr.getInt(3)){
                okunan = cr.getInt(0);
                adet   = cr.getInt(1);
                toplam = cr.getInt(2);
                okunan_txt.setText(String.valueOf(okunan));
                toplam_txt.setText(String.valueOf(toplam));
                durum = 1;
            }
        }
    }

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