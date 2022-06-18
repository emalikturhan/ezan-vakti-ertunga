package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.ertunga.namazvakti.Special.DefineUrl;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Kazalar extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TextView sabah_count,oglen_count,ikindi_count,aksam_count,yatsi_count,vitir_count;
    private TextView arti_sabah,arti_oglen,arti_ikindi,arti_aksam,arti_yatsi,arti_vitir;
    private TextView eksi_sabah,eksi_oglen,eksi_ikindi,eksi_aksam,eksi_yatsi,eksi_vitir;
    private ImageView back,reset;
    private RelativeLayout reset_content;
    private LinearLayout reset_box;
    private Button vazgec,sifirla;
    private Animation op_on,op_off;
    int sabah  = 0;
    int oglen  = 0;
    int ikindi = 0;
    int aksam  = 0;
    int yatsi  = 0;
    int vitir  = 0;
    Vibrator v;
    RequestQueue rg;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kazalar);

        Init();
        Intents();
        Clicks();
        KazaGetir();
        Animations();
        //PremiumControl();
    }


    private void Init(){
        back = findViewById(R.id.back);
        sabah_count = findViewById(R.id.sabah_count);
        oglen_count = findViewById(R.id.oglen_count);
        ikindi_count = findViewById(R.id.ikindi_count);
        aksam_count = findViewById(R.id.aksam_count);
        yatsi_count = findViewById(R.id.yatsi_count);
        vitir_count = findViewById(R.id.vitir_count);

        arti_sabah = findViewById(R.id.arti_sabah);
        arti_oglen = findViewById(R.id.arti_oglen);
        arti_ikindi = findViewById(R.id.arti_ikindi);
        arti_aksam = findViewById(R.id.arti_aksam);
        arti_yatsi = findViewById(R.id.arti_yatsi);
        arti_vitir = findViewById(R.id.arti_vitir);

        eksi_sabah = findViewById(R.id.eksi_sabah);
        eksi_oglen = findViewById(R.id.eksi_oglen);
        eksi_ikindi = findViewById(R.id.eksi_ikindi);
        eksi_aksam = findViewById(R.id.eksi_aksam);
        eksi_yatsi = findViewById(R.id.eksi_yatsi);
        eksi_vitir = findViewById(R.id.eksi_vitir);

        reset = findViewById(R.id.reset);
        reset_content = findViewById(R.id.reset_content);
        reset_box = findViewById(R.id.reset_box);
        vazgec = findViewById(R.id.vazgec);
        sifirla = findViewById(R.id.sifirla);

    }

    private void Intents() {
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(this);
    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arti_sabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sabah = sabah+1;
                sharedPreferences.edit().putInt("sabah",sabah).apply();
                sabah_count.setText(String.valueOf(sabah));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_sabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sabah > 0) {
                    sabah = sabah - 1;
                } else {
                    sabah = 0;
                }
                sharedPreferences.edit().putInt("sabah", sabah).apply();
                sabah_count.setText(String.valueOf(sabah));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });


        arti_oglen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oglen = oglen+1;
                sharedPreferences.edit().putInt("oglen",oglen).apply();
                oglen_count.setText(String.valueOf(oglen));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_oglen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oglen > 0) {
                    oglen = oglen - 1;
                } else {
                    oglen = 0;
                }
                sharedPreferences.edit().putInt("oglen", oglen).apply();
                oglen_count.setText(String.valueOf(oglen));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        arti_ikindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ikindi = ikindi+1;
                sharedPreferences.edit().putInt("ikindi",ikindi).apply();
                ikindi_count.setText(String.valueOf(ikindi));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_ikindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ikindi > 0) {
                    ikindi = ikindi - 1;
                } else {
                    ikindi = 0;
                }
                sharedPreferences.edit().putInt("ikindi", ikindi).apply();
                ikindi_count.setText(String.valueOf(ikindi));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });


        arti_aksam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksam = aksam+1;
                sharedPreferences.edit().putInt("aksam",aksam).apply();
                aksam_count.setText(String.valueOf(aksam));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_aksam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aksam > 0) {
                    aksam = aksam - 1;
                } else {
                    aksam = 0;
                }
                sharedPreferences.edit().putInt("aksam", aksam).apply();
                aksam_count.setText(String.valueOf(aksam));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        arti_yatsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yatsi = yatsi+1;
                sharedPreferences.edit().putInt("yatsi",yatsi).apply();
                yatsi_count.setText(String.valueOf(yatsi));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_yatsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(yatsi > 0) {
                    yatsi = yatsi - 1;
                } else {
                    yatsi = 0;
                }
                sharedPreferences.edit().putInt("yatsi", yatsi).apply();
                yatsi_count.setText(String.valueOf(yatsi));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });


        arti_vitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vitir = vitir+1;
                sharedPreferences.edit().putInt("vitir",vitir).apply();
                vitir_count.setText(String.valueOf(vitir));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });

        eksi_vitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vitir > 0) {
                    vitir = vitir - 1;
                } else {
                    vitir = 0;
                }
                sharedPreferences.edit().putInt("vitir", vitir).apply();
                vitir_count.setText(String.valueOf(vitir));
                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
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
                        sharedPreferences.edit().putInt("sabah",0).apply();
                        sharedPreferences.edit().putInt("oglen",0).apply();
                        sharedPreferences.edit().putInt("ikindi",0).apply();
                        sharedPreferences.edit().putInt("aksam",0).apply();
                        sharedPreferences.edit().putInt("yatsi",0).apply();
                        sharedPreferences.edit().putInt("vitir",0).apply();

                        sabah_count.setText("0");
                        oglen_count.setText("0");
                        ikindi_count.setText("0");
                        aksam_count.setText("0");
                        yatsi_count.setText("0");
                        vitir_count.setText("0");


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
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

    private void KazaGetir(){
        sabah  = sharedPreferences.getInt("sabah",0);
        oglen  = sharedPreferences.getInt("oglen",0);
        ikindi = sharedPreferences.getInt("ikindi",0);
        aksam  = sharedPreferences.getInt("aksam",0);
        yatsi  = sharedPreferences.getInt("yatsi",0);
        vitir  = sharedPreferences.getInt("vitir",0);
        sabah_count.setText(String.valueOf(sabah));
        oglen_count.setText(String.valueOf(oglen));
        ikindi_count.setText(String.valueOf(ikindi));
        aksam_count.setText(String.valueOf(aksam));
        yatsi_count.setText(String.valueOf(yatsi));
        vitir_count.setText(String.valueOf(vitir));
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
