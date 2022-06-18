package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Adaptors.SureAdaptor;
import com.ertunga.namazvakti.InterFace.ListInterface;
import com.ertunga.namazvakti.Lists.OneList;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.Special.Functions;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NamazSureleri extends AppCompatActivity implements ListInterface {

    Functions functions = new Functions();
    private SharedPreferences sharedPreferences;
    RequestQueue rg;
    RecyclerView rc;
    ArrayList<OneList> UList;
    LinearLayoutManager LManager;
    private SureAdaptor adaptor;
    private RelativeLayout loaderbox;
    private InterstitialAd mInterstitialAd;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namaz_sureleri);

        Init();
        Intents();
        Clicks();
        //PremiumControl();
    }

    private void Init(){
        back = findViewById(R.id.back);
        rc = findViewById(R.id.rc);
        loaderbox = findViewById(R.id.loaderbox);
    }

    private void Intents(){
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(this);
        UList = new ArrayList<>();
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
    protected void onStart() {
        super.onStart();
        GetSureler();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GetSureler(){
        loaderbox.setVisibility(View.VISIBLE);
        UList.clear();
        String url = DefineUrl.Url+"?data=GetNamazSureleri";
        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("state");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        UList.add(new OneList(obj.getString("baslik"),obj.getString("data"),R.drawable.ic_sureler));
                        adaptor = new SureAdaptor(UList);
                        LManager = new LinearLayoutManager(getApplicationContext());
                        rc.setLayoutManager(LManager);
                        rc.setItemAnimator(new DefaultItemAnimator());
                        rc.setAdapter(adaptor);
                        adaptor.notifyDataSetChanged();
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

    @Override
    public void Data(final String baslik, final String data) {

        Bundle b = new Bundle();
        b.putString("baslik",baslik);
        b.putString("data",data);
        Intent i = new Intent(NamazSureleri.this,ListDetay.class);
        i.putExtras(b);
        startActivity(i);

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
