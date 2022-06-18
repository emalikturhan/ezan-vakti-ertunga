 package com.ertunga.namazvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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

 public class DiniGunler extends AppCompatActivity {


    private ImageView back;
    private TextView title2,title3;
    private LinearLayout tab2,tab3;
    private InterstitialAd mInterstitialAd;
    RequestQueue rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dini_gunler);
        loadAd();
        Init();
        Intents();
        Clicks();

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
                         DiniGunler.this.mInterstitialAd = interstitialAd;
                         showInterstitial();
                         interstitialAd.setFullScreenContentCallback(
                                 new FullScreenContentCallback() {
                                     @Override
                                     public void onAdDismissedFullScreenContent() {
                                         // Called when fullscreen content is dismissed.
                                         // Make sure to set your reference to null so you don't
                                         // show it a second time.
                                         DiniGunler.this.mInterstitialAd = null;
                                         Log.d("TAG", "The ad was dismissed.");
                                     }

                                     @Override
                                     public void onAdFailedToShowFullScreenContent(AdError adError) {
                                         // Called when fullscreen content failed to show.
                                         // Make sure to set your reference to null so you don't
                                         // show it a second time.
                                         DiniGunler.this.mInterstitialAd = null;
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
        title2 = findViewById(R.id.title2);
        title3 = findViewById(R.id.title3);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
    }

    private void Intents(){
        rg = Volley.newRequestQueue(this);
    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title2.setTextColor(getColor(R.color.sari));
                title3.setTextColor(getColor(R.color.siyah));

                tab2.setVisibility(View.VISIBLE);
                tab3.setVisibility(View.GONE);
            }
        });

        title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title2.setTextColor(getColor(R.color.siyah));
                title3.setTextColor(getColor(R.color.sari));


                tab2.setVisibility(View.GONE);
                tab3.setVisibility(View.VISIBLE);
            }
        });
    }


     @Override
     public void onBackPressed() {
         super.onBackPressed();
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

//         MobileAds.initialize(this, new OnInitializationCompleteListener() {
//             @Override
//             public void onInitializationComplete(InitializationStatus initializationStatus) {}
//         });
//         mInterstitialAd = new InterstitialAd(this);
//         mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_key));
//         if (mInterstitialAd.isLoaded()) {
//             mInterstitialAd.show();
//         } else {
//             Log.d("TAG", "The interstitial wasn't loaded yet.");
//         }
//         mInterstitialAd.setAdListener(new AdListener() {
//             @Override
//             public void onAdLoaded() {
//                 // Code to be executed when an ad finishes loading.
//             }
//
//             @Override
//             public void onAdFailedToLoad(int errorCode) {
//                 // Code to be executed when an ad request fails.
//             }
//
//             @Override
//             public void onAdOpened() {
//                 // Code to be executed when the ad is displayed.
//             }
//
//             @Override
//             public void onAdClicked() {
//                 // Code to be executed when the user clicks on an ad.
//             }
//
//             @Override
//             public void onAdLeftApplication() {
//                 // Code to be executed when the user has left the app.
//             }
//
//             @Override
//             public void onAdClosed() {
//                 // Code to be executed when the interstitial ad is closed.
//             }
//         });
     }

 }