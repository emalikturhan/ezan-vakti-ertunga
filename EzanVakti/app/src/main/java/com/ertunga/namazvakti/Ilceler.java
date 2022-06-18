package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Adaptors.IlceAdaptors;
import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.InterFace.IlceInterFace;
import com.ertunga.namazvakti.Lists.IlceList;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.Special.Functions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ilceler extends AppCompatActivity implements IlceInterFace {

    Functions functions = new Functions();
    private SharedPreferences sharedPreferences;
    RequestQueue rg;
    RecyclerView rc;
    ArrayList<IlceList> UList;
    LinearLayoutManager LManager;
    private IlceAdaptors ilceAdaptor;
    private RelativeLayout loaderbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilceler);

        Init();
        Intents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(functions.InternetControl(Ilceler.this) == true){
            GetIlce();
        } else {
            Toast.makeText(this, "İnternet bağlantınızı kontrol ediniz !..", Toast.LENGTH_SHORT).show();
        }

    }

    private void Init(){
        rc = findViewById(R.id.rc);
        loaderbox = findViewById(R.id.loaderbox);
    }

    private void Intents(){
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(this);
        UList = new ArrayList<>();

    }

    public void GetIlce(){
        UList.clear();
        String url = DefineUrl.Url+"?data=GetIlce&sehir="+getIntent().getExtras().getString("sehirid");

        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("state");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        UList.add(new IlceList(obj.getString("ilce"),obj.getString("ilceid")));
                        ilceAdaptor = new IlceAdaptors(UList);
                        LManager = new LinearLayoutManager(getApplicationContext());
                        rc.setLayoutManager(LManager);
                        rc.setItemAnimator(new DefaultItemAnimator());
                        rc.setAdapter(ilceAdaptor);
                        ilceAdaptor.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

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
    public void Ilce(String ilce, String ilceid) {
        //Toast.makeText(this, ilce, Toast.LENGTH_SHORT).show();
        sharedPreferences.edit().putInt("login", 1).apply();
        sharedPreferences.edit().putString("ilce",ilce).apply();
        sharedPreferences.edit().putString("ilceid",ilceid).apply();
        AylikNamaz(ilceid);
        Handler hnd = new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                GotoMain();
            }
        },2000);
    }

    public void AylikNamaz(String ilceid){
        loaderbox.setVisibility(View.VISIBLE);
        String url = DefineUrl.Url+"?data=AylikNamaz&ilceid="+ilceid;
        Log.d("asd",url);
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
                        String gunes =  obj.getString("gunes");
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
                        DataBase veritabanı=new DataBase(Ilceler.this);
                        veritabanı.veriEkle(gun,ay,yil,imsak,gunes,oglen,ikindi,aksam,yatsi,hicri,guntxt,kible,hadis,hadis_adi,dua,dua_adi);

                        sharedPreferences.edit().putInt("kible",obj.getInt("kible")).apply();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
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


    private void GotoMain(){
        loaderbox.setVisibility(View.GONE);
        Intent serviceIntent = new Intent(Ilceler.this, NamazService.class);
        serviceIntent.putExtra("name","deneme");
        startService(serviceIntent);
        Intent intent = new Intent(Ilceler.this,MainActivity.class);
        startActivity(intent);
        ActivityCompat.finishAffinity(Ilceler.this);
    }

}
