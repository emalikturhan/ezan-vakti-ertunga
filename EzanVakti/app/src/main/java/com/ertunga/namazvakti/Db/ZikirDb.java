package com.ertunga.namazvakti.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ZikirDb extends SQLiteOpenHelper {

    private static final String TABLE_NAME    = "Zikirler";
    private static final String DATABASE_NAME = "ZikirDB";
    private static final int DATABASE_VERSION = 1;
    private static final String OKUNAN  = "Okunan";
    private static final String ADET    =  "Adet";
    private static final String TOPLAM  =  "Toplam";
    private static final String ZIKIRID =  "ZikirId";

    public ZikirDb(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION); };
    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "+OKUNAN+" INTEGER, "+ADET+" INTEGER, "+TOPLAM+" INTEGER,"+ZIKIRID+" INTEGER);");

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

    public void veriEkle(int okunan,int adet,int toplam,int zikirid){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(OKUNAN,okunan);
        cv.put(ADET,adet);
        cv.put(TOPLAM,toplam);
        cv.put(ZIKIRID,zikirid);

        long r=db.insert(TABLE_NAME,null,cv);
        if(r>-1)
            Log.i("tag","İşlem Başarılı");
        else
            Log.e("tag","İşlem Başarısız");
        db.close();
    }

    public void Update(int okunan,int adet,int toplam,int zikirid){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(OKUNAN,okunan);
        cv.put(ADET,adet);
        cv.put(TOPLAM,toplam);

        long r=db.update(TABLE_NAME, cv, "ZikirId" + " = "+zikirid , null);
        if(r>-1)
            Log.i("tag","İşlem Başarılı");
        else
            Log.e("tag","İşlem Başarısız");
        db.close();
    }


}
