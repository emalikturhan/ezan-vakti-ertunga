package com.ertunga.namazvakti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.Functions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class home extends AppCompatActivity {

    Functions functions = new Functions();
    Button btn,btn1,btn2;
    EditText edt1,edt2;
    ListView listView;
    LinearLayout linearLayout;

    public void init(){
        edt1=(EditText)findViewById(R.id.editText);
        edt2=(EditText)findViewById(R.id.editText2);
        btn=(Button)findViewById(R.id.button);
        btn1=(Button)findViewById(R.id.button2);
        btn2=(Button)findViewById(R.id.button3);
        listView=(ListView)findViewById(R.id.veriler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button bas = findViewById(R.id.bas);
        bas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(home.this, NamazService.class);
                serviceIntent.putExtra("name","deneme");
                startService(serviceIntent);
            }
        });

        init();
        buton_click();

    }

    public void buton_click(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase veritabanı=new DataBase(home.this);
                veritabanı.veriEkle("","","","dgdfg","fdgdfgdf","","","","","gdfgd","","","","","","");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase veritabanı=new DataBase(home.this);
                SQLiteDatabase db = veritabanı.getWritableDatabase();
                String[] sutunlar={"Gun","Ay","Yil","Imsak","Gunes","Oglen","Ikindi","Aksam","Yatsi","Hicri","Kible"};
                Cursor cr=db.query("Namazlar",sutunlar,null,null,null,null,null);
                while(cr.moveToNext()){
                    String aynum = functions.AyKontrol(cr.getString(1));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String today = df.format(new Date());
                    String date = cr.getString(2)+"-"+aynum+"-"+cr.getString(0);
                    if(today.equals(date)){
                       // Log.d("asd",today+" "+date+" [Aynı]");
                    } else {
                      //  Log.d("asd",today+" "+date);
                    }


                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBase veritabanı=new DataBase(home.this);
                veritabanı.Sifirla();
            }
        });
    }



}
