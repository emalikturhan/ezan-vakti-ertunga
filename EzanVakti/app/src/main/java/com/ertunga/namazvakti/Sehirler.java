package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Adaptors.SehirAdaptors;
import com.ertunga.namazvakti.InterFace.SehirInterFace;
import com.ertunga.namazvakti.Lists.SehirList;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.Special.Functions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sehirler extends AppCompatActivity implements SehirInterFace {

    Functions functions = new Functions();
    private SharedPreferences sharedPreferences;
    RequestQueue rg;
    RecyclerView rc;
    ArrayList<SehirList> UList;
    LinearLayoutManager LManager;
    private SehirAdaptors sehirAdaptors;
    private RelativeLayout loaderbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sehirler);

        Init();
        Intents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(functions.InternetControl(Sehirler.this) == true){
            GetSehir();
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

    public void GetSehir(){
        loaderbox.setVisibility(View.VISIBLE);
        UList.clear();
        String url = DefineUrl.Url+"?data=GetSehir";
        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("state");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        UList.add(new SehirList(obj.getString("sehir"),obj.getString("sehirid")));
                        sehirAdaptors = new SehirAdaptors(UList);
                        LManager = new LinearLayoutManager(getApplicationContext());
                        rc.setLayoutManager(LManager);
                        rc.setItemAnimator(new DefaultItemAnimator());
                        rc.setAdapter(sehirAdaptors);
                        sehirAdaptors.notifyDataSetChanged();
                    }
                    loaderbox.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    loaderbox.setVisibility(View.GONE);

                } finally {
                    loaderbox.setVisibility(View.GONE);
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
    public void Sehir(String sehir,String sehirid) {
        sharedPreferences.edit().putString("sehir",sehir).apply();
        Bundle b = new Bundle();
        Intent intent = new Intent(Sehirler.this,Ilceler.class);
        b.putString("sehirid",sehirid);
        intent.putExtras(b);
        startActivity(intent);
    }


}
