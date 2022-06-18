package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BilgiDetay extends AppCompatActivity {

    private ImageView back;
    private TextView title,data;
    RequestQueue rg;
    int id = 0;
    private RelativeLayout loaderbox;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgi_detay);
        Init();
        Clicks();
        ////PremiumControl();
    }

    private void Init(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        data = findViewById(R.id.data);
        rg = Volley.newRequestQueue(this);
        loaderbox = findViewById(R.id.loaderbox);
        id = getIntent().getExtras().getInt("id");

        if(id == 1){
            title.setText("İbadet Nedir");
        } else  if(id == 2){
            title.setText("İslamın Şartları");
        } else  if(id == 3){
            title.setText("İmanın Şartları");
        } else  if(id == 4){
            title.setText("Farz Nedir");
        } else  if(id == 5){
            title.setText("Sünnet Nedir");
        } else  if(id == 6){
            title.setText("Kelime-i Şehadet");
        } else  if(id == 7){
            title.setText("Ezan");
        }

        GetDiniBilgiler();
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

    public void GetDiniBilgiler(){
        loaderbox.setVisibility(View.VISIBLE);
        String url = DefineUrl.Url+"?data=GetDiniBilgiler";
        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("state");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        if(id == 1){
                            data.setText(Html.fromHtml(obj.getString("ibadet_nedir")));
                        } else  if(id == 2){
                            data.setText(Html.fromHtml(obj.getString("islamin_sartlari")));
                        } else  if(id == 3){
                            data.setText(Html.fromHtml(obj.getString("imanin_sartlari")));
                        } else  if(id == 4){
                            data.setText(Html.fromHtml(obj.getString("farz_nedir")));
                        } else  if(id == 5){
                            data.setText(Html.fromHtml(obj.getString("sunnet_nedir")));
                        } else  if(id == 6){
                            data.setText(Html.fromHtml(obj.getString("sehadet")));
                        } else  if(id == 7){
                            data.setText(Html.fromHtml(obj.getString("ezan")));
                        }
                    }

                    loaderbox.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    loaderbox.setVisibility(View.GONE);
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        } );
        rg.add( json );
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
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_key));;
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
