package com.ertunga.namazvakti.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CountryDb extends SQLiteOpenHelper {

    private static final String TABLE_NAME    = "Country";
    private static final String DATABASE_NAME = "NamazDB";
    private static final int DATABASE_VERSION = 1;
    private static final String country ="Country";


    public CountryDb(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION); };
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT,"+country+" TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE İF EXISTS "+TABLE_NAME);
        this.onCreate(sqLiteDatabase);

    }


    public void Reset(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }

    public void AddCountry(String Country){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(country,Country.trim());

        long r=db.insert(TABLE_NAME,null,cv);
        if(r>-1)
            Log.i("tag","İşlem Başarılı");
        else
            Log.e("tag","İşlem Başarısız");
        db.close();
    }


    public List<String> Listing(){
        List<String> veriler=new ArrayList<String>();
        SQLiteDatabase db=this.getWritableDatabase();
        String[] sutunlar={country};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);//query fonksiyonu ile aldığımız parametreler yoluyla komutu kendi içerisinde yapılandırıyoruz.
        while(cr.moveToNext()){
            veriler.add(cr.getString(0)+"-"+cr.getString(1));
        }
        return veriler;
    }

    public void Remove(String Country){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,country + "=?",new String[]{String.valueOf(Country)});
        db.close();
    }

}
