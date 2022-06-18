package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Radio extends AppCompatActivity {

    private LinearLayout radio1,radio2,radio3,radio4,radio5,radio6,radio7,radio8,radio9;
    MediaPlayer mediaPlayer;
    WebView webView;
    int durum = 0;
    TextView radio_adi;
    ImageView back;
    RequestQueue rg;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        AdView footer_reklam = findViewById(R.id.footer_reklam);
        footer_reklam.loadAd(new AdRequest.Builder().build());

        Init();
        Intents();
        Clickc();
        //PremiumControl();

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/huzur_pinari.html");
        radio_adi.setText("");

    }


    private void Init(){
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);
        radio6 = findViewById(R.id.radio6);
        radio7 = findViewById(R.id.radio7);
        radio8 = findViewById(R.id.radio8);
        radio9 = findViewById(R.id.radio9);
        radio_adi = findViewById(R.id.radio_adi);
        back = findViewById(R.id.back);
    }

    private void Intents(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        rg = Volley.newRequestQueue(this);
    }

    private void  Clickc(){
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/mevlanafm.html");
                radio_adi.setText("Mevlana Fm Çalıyor");
            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/bayramfm.html");
                radio_adi.setText("Bayram Fm Çalıyor");
            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/serhadfm.html");
                radio_adi.setText("Serhad Fm Çalıyor");
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/lalegul.html");
                radio_adi.setText("Lalegül Fm Çalıyor");
            }
        });

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/gonulfm.html");
                radio_adi.setText("Gönül Fm Çalıyor");
            }
        });

        radio6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/faruki.html");
                radio_adi.setText("Huzur Pınarı Çalıyor");
            }
        });

        radio7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/rahmetfm.html");
                radio_adi.setText("Rahmet Fm Çalıyor");
            }
        });

        radio8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/radionebi.html");
                radio_adi.setText("Radyo Nebi Çalıyor");
            }
        });


        radio9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView = (WebView) findViewById(R.id.webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://mehmetcanertugrul.com/namazvakti/radiolar/gozyasi.html");
                radio_adi.setText("Gözyaşı Fm Çalıyor");
            }
        });

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
        try{
            webView.loadUrl("google.com");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            webView.loadUrl("google.com");
        }catch (Exception e){
            e.printStackTrace();
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
