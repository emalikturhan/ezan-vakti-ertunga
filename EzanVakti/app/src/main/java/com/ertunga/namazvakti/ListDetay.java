package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDetay extends AppCompatActivity {

    private ImageView back;
    private TextView data,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detay);

        Init();
        Intents();
        Clicks();
    }

    private void Init(){
        back = findViewById(R.id.back);
        data = findViewById(R.id.data);
        title = findViewById(R.id.title);
    }

    private void Intents(){
        if(getIntent().getExtras().getString("baslik").length() > 30){
            title.setText(getIntent().getExtras().getString("baslik").substring(0,30)+"...");
        } else {
            title.setText(getIntent().getExtras().getString("baslik"));
        }
        data.setText(Html.fromHtml(getIntent().getExtras().getString("data")));
    }

    private void Clicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
