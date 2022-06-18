package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Exit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        if(getIntent().getBooleanExtra("Exit",true)){
            finish();
        }
    }
}