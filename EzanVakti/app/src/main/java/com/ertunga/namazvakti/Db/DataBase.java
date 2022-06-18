package com.ertunga.namazvakti.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

    private static final String TABLE_NAME    = "Namazlar";
    private static final String DATABASE_NAME = "NamazDB";
    private static final int DATABASE_VERSION = 1;
    private static final String IMSAK  ="Imsak";
    private static final String GUNES  ="Gunes";
    private static final String OGLEN  ="Oglen";
    private static final String IKINDI ="Ikindi";
    private static final String AKSAM  ="Aksam";
    private static final String YATSI  ="Yatsi";
    private static final String HICRI  ="Hicri";
    private static final String AY     ="Ay";
    private static final String GUN    = "Gun";
    private static final String YIL    = "Yil";
    private static final String GUNTXT = "GunTxt";
    private static final String KIBLE  = "Kible";
    private static final String HADIS  = "Hadis";
    private static final String HADIS_ADI  = "HadisAdi";
    private static final String DUA    = "Dua";
    private static final String DUA_ADI    = "DuaAdi";


    public DataBase(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION); };
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT,"+GUN+" TEXT,"+AY+" TEXT,"+YIL+" TEXT,"+IMSAK+" TEXT,"+GUNES+" TEXT,"+OGLEN+" TEXT,"+IKINDI+" TEXT,"+AKSAM+" TEXT,"+YATSI+" TEXT,"+HICRI+" TEXT,"+GUNTXT+" TEXT,"+KIBLE+" TEXT,"+HADIS+" TEXT,"+HADIS_ADI+" TEXT,"+DUA+" TEXT,"+DUA_ADI+" TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE İF EXISTS "+TABLE_NAME);
        this.onCreate(sqLiteDatabase);

    }


    public void Sifirla(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }

    public void veriEkle(String gun,String ay,String yil,String imsak,String gunes,String oglen,String ikindi,String aksam,String yatsi,String hicri,String guntxt,String kible,String hadis,String hadis_adi,String dua,String dua_adi){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(GUN,gun.trim());
        cv.put(AY,ay.trim());
        cv.put(YIL,yil.trim());
        cv.put(IMSAK,imsak.trim());
        cv.put(GUNES,gunes.trim());
        cv.put(OGLEN,oglen.trim());
        cv.put(IKINDI,ikindi.trim());
        cv.put(AKSAM,aksam.trim());
        cv.put(YATSI,yatsi.trim());
        cv.put(HICRI,hicri.trim());
        cv.put(GUNTXT,guntxt.trim());
        cv.put(KIBLE,kible);
        cv.put(HADIS,hadis);
        cv.put(HADIS_ADI,hadis_adi);
        cv.put(DUA,dua);
        cv.put(DUA_ADI,dua_adi);

        long r=db.insert(TABLE_NAME,null,cv);
        if(r>-1)
            Log.i("tag","İşlem Başarılı");
        else
            Log.e("tag","İşlem Başarısız");
        db.close();
    }



}
