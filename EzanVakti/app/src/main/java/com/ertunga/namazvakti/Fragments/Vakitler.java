package com.ertunga.namazvakti.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ertunga.namazvakti.InterFace.MenuInterFace;
import com.ertunga.namazvakti.MainActivity;
import com.ertunga.namazvakti.Sehirler;
import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.BlurTransform;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.ertunga.namazvakti.Special.Functions;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.R;
import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Vakitler extends Fragment {

    View view;
    Functions functions = new Functions();
    private SharedPreferences sharedPreferences;
    RequestQueue rg;
    int menu_statu = 0;
    private Animation op_on,op_off;
    private Activity mActivity;
    private TextView vakit_txt,sayac_txt,sayac_copy_txt,lokasyon_txt,islamic_takvim_txt,takvim_txt,vakit_txt_copy,sayac_small,small_tarih,hicri_copy,guntxt;
    private TextView imsak_txt,imsak_saat,gunes_txt,gunes_saat,oglen_txt,oglen_saat,ikindi_txt,ikindi_saat,aksam_txt,aksam_saat,yatsi_txt,yatsi_saat;
    private TextView tab2_imsak_saat,tab2_gunes_saat,tab2_oglen_saat,tab2_ikindi_saat,tab2_aksam_saat,tab2_yatsi_saat,tab2_imsak_txt,tab2_gunes_txt,tab2_oglen_txt,tab2_ikindi_txt,tab2_aksam_txt,tab2_yatsi_txt;
    private TextView hadis,hadis_baslik,gunun_duasi,dua_baslik;
    private LinearLayout imsak_box,gunes_box,oglen_box,ikindi_box,aksam_box,yatsi_box;
    private RelativeLayout tab1,tab2;
    private ImageView menu,list,bg,bg_blur;
    private String vakit_adi = "";
    Handler handler;
    Runnable runnable;
    String zaman = "";
    int tur = 0;
    Date after;
    private ImageView hadis_paylas,dua_paylas;
    String ghadis,gdua;
    com.google.android.gms.ads.AdView footer_reklam;


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vakitler, container, false);
        Init();
        Intents();
        Fonts();
        Clicks();
        Animations();
        NamazDb();
        startTimer();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    public void onStart() {
        super.onStart();
        if(sharedPreferences.getInt("bg",0) == 0){
            bg.setImageResource(R.drawable.mosquesplash_1);
            Picasso.get().load(R.drawable.mosquesplash_1).transform(new BlurTransform(mActivity)).fit().centerInside().into(bg_blur);
        } else {
            bg.setImageResource(R.drawable.mosquesplash_2);
            Picasso.get().load(R.drawable.mosquesplash_2).transform(new BlurTransform(mActivity)).fit().centerInside().into(bg_blur);
        }

        //PremiumControl();
    }

    private void Init(){
        bg = view.findViewById(R.id.bg);
        bg_blur = view.findViewById(R.id.bg_blur);
        vakit_txt = view.findViewById(R.id.vakit_txt);
        sayac_txt = view.findViewById(R.id.sayac_txt);
        sayac_copy_txt = view.findViewById(R.id.sayac_copy_txt);
        menu = view.findViewById(R.id.menu);
        list = view.findViewById(R.id.list);
        imsak_txt = view.findViewById(R.id.imsak_txt);
        imsak_saat = view.findViewById(R.id.imsak_saat);
        gunes_txt = view.findViewById(R.id.gunes_txt);
        gunes_saat = view.findViewById(R.id.gunes_saat);
        oglen_txt = view.findViewById(R.id.oglen_txt);
        oglen_saat = view.findViewById(R.id.oglen_saat);
        ikindi_txt = view.findViewById(R.id.ikindi_txt);
        ikindi_saat = view.findViewById(R.id.ikindi_saat);
        aksam_txt = view.findViewById(R.id.aksam_txt);
        aksam_saat = view.findViewById(R.id.aksam_saat);
        yatsi_txt = view.findViewById(R.id.yatsi_txt);
        yatsi_saat = view.findViewById(R.id.yatsi_saat);
        lokasyon_txt = view.findViewById(R.id.lokasyon_txt);
        islamic_takvim_txt = view.findViewById(R.id.islamic_takvim_txt);
        takvim_txt = view.findViewById(R.id.takvim_txt);
        tab1 = view.findViewById(R.id.tab1);
        tab2 = view.findViewById(R.id.tab2);
        guntxt = view.findViewById(R.id.guntxt);
        imsak_box  = view.findViewById(R.id.imsak_box);
        gunes_box  = view.findViewById(R.id.gunes_box);
        oglen_box  = view.findViewById(R.id.oglen_box);
        ikindi_box = view.findViewById(R.id.ikindi_box);
        aksam_box  = view.findViewById(R.id.aksam_box);
        yatsi_box  = view.findViewById(R.id.yatsi_box);

        vakit_txt_copy  = view.findViewById(R.id.vakit_txt_copy);
        sayac_small  = view.findViewById(R.id.sayac_small);
        small_tarih  = view.findViewById(R.id.small_tarih);
        hicri_copy  = view.findViewById(R.id.hicri_copy);

        tab2_imsak_saat = view.findViewById(R.id.tab2_imsak_saat);
        tab2_gunes_saat = view.findViewById(R.id.tab2_gunes_saat);
        tab2_oglen_saat = view.findViewById(R.id.tab2_oglen_saat);
        tab2_ikindi_saat = view.findViewById(R.id.tab2_ikindi_saat);
        tab2_aksam_saat = view.findViewById(R.id.tab2_aksam_saat);
        tab2_yatsi_saat = view.findViewById(R.id.tab2_yatsi_saat);

        tab2_imsak_txt = view.findViewById(R.id.tab2_imsak_txt);
        tab2_gunes_txt = view.findViewById(R.id.tab2_gunes_txt);
        tab2_oglen_txt = view.findViewById(R.id.tab2_oglen_txt);
        tab2_ikindi_txt = view.findViewById(R.id.tab2_ikindi_txt);
        tab2_aksam_txt = view.findViewById(R.id.tab2_aksam_txt);
        tab2_yatsi_txt = view.findViewById(R.id.tab2_yatsi_txt);

        hadis = view.findViewById(R.id.gunun_hadisi);
        hadis_baslik = view.findViewById(R.id.hadis_adi);
        gunun_duasi = view.findViewById(R.id.gunun_duasi);
        dua_baslik = view.findViewById(R.id.dua_adi);

        hadis_paylas = view.findViewById(R.id.hadis_paylas);
        dua_paylas = view.findViewById(R.id.dua_paylas);

        footer_reklam = view.findViewById(R.id.footer_reklam);
        footer_reklam.loadAd(new AdRequest.Builder().build());


    }

    private void Intents(){
        sharedPreferences = mActivity.getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(mActivity);
        lokasyon_txt.setText(sharedPreferences.getString("ilce",null));

    }

    private void Fonts(){
        functions.MyRiadRegular(mActivity,vakit_txt);
        functions.MyRiadRegular(mActivity,sayac_txt);
        functions.MyRiadRegular(mActivity,sayac_copy_txt);

        functions.MyRiadRegular(mActivity,imsak_txt);
        functions.MyRiadRegular(mActivity,imsak_saat);
        functions.MyRiadRegular(mActivity,gunes_txt);
        functions.MyRiadRegular(mActivity,gunes_saat);
        functions.MyRiadRegular(mActivity,oglen_txt);
        functions.MyRiadRegular(mActivity,oglen_saat);
        functions.MyRiadRegular(mActivity,ikindi_txt);
        functions.MyRiadRegular(mActivity,ikindi_saat);
        functions.MyRiadRegular(mActivity,aksam_txt);
        functions.MyRiadRegular(mActivity,aksam_saat);
        functions.MyRiadRegular(mActivity,yatsi_txt);
        functions.MyRiadRegular(mActivity,yatsi_saat);
        functions.MyRiadRegular(mActivity,lokasyon_txt);
        functions.MyRiadRegular(mActivity,islamic_takvim_txt);
        functions.MyRiadRegular(mActivity,takvim_txt);
        functions.MyRiadRegular(mActivity,vakit_txt_copy);
        functions.MyRiadRegular(mActivity,sayac_small);
        functions.MyRiadRegular(mActivity,small_tarih);
        functions.MyRiadRegular(mActivity,hicri_copy);
        functions.MyRiadRegular(mActivity,tab2_imsak_saat);
        functions.MyRiadRegular(mActivity,tab2_gunes_saat);
        functions.MyRiadRegular(mActivity,tab2_oglen_saat);
        functions.MyRiadRegular(mActivity,tab2_ikindi_saat);
        functions.MyRiadRegular(mActivity,tab2_aksam_saat);
        functions.MyRiadRegular(mActivity,tab2_yatsi_saat);
        functions.MyRiadRegular(mActivity,tab2_imsak_txt);
        functions.MyRiadRegular(mActivity,tab2_gunes_txt);
        functions.MyRiadRegular(mActivity,tab2_oglen_txt);
        functions.MyRiadRegular(mActivity,tab2_ikindi_txt);
        functions.MyRiadRegular(mActivity,tab2_aksam_txt);
        functions.MyRiadRegular(mActivity,tab2_yatsi_txt);
        functions.MyRiadRegular(mActivity,guntxt);
        functions.MyRiadRegular(mActivity,hadis);
        functions.MyRiadRegular(mActivity,hadis_baslik);
        functions.MyRiadRegular(mActivity,gunun_duasi);
    }

    private void Clicks(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuInterFace menuInterFace = (MenuInterFace)getActivity();
                menuInterFace.Statu(true);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(menu_statu == 0) {
                    menu_statu = 1;
                    tab1.setVisibility(View.GONE);
                    bg.setVisibility(View.GONE);
                    tab2.startAnimation(op_on);
                    bg_blur.setVisibility(View.VISIBLE);
                    tab2.setVisibility(View.VISIBLE);
                    op_on.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                } else {
                    menu_statu = 0;
                    tab2.setVisibility(View.GONE);
                    bg_blur.setVisibility(View.GONE);
                    bg.setVisibility(View.VISIBLE);
                    tab1.startAnimation(op_on);
                    tab1.setVisibility(View.VISIBLE);
                    op_on.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });

        lokasyon_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(functions.InternetControl(mActivity) == true){
                    Intent intent = new Intent(getActivity(), Sehirler.class);
                    ((MainActivity) getActivity()).startActivity(intent);
                } else {
                    Toast.makeText(mActivity, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hadis_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ghadis);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        dua_paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, gdua);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    private void Animations(){
        op_on = AnimationUtils.loadAnimation(mActivity,R.anim.opacity_on);
        op_off = AnimationUtils.loadAnimation(mActivity,R.anim.opacity_off);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    private void NamazDb() {
        DataBase veritabanı=new DataBase(mActivity);
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Gun", "Ay", "Yil", "Imsak", "Gunes", "Oglen", "Ikindi", "Aksam", "Yatsi", "Hicri","GunTxt","Kible","Hadis","HadisAdi","Dua","DuaAdi"};
        Cursor cr = db.query("Namazlar", sutunlar, null, null, null, null, null);
        while (cr.moveToNext()) {
            String aynum = functions.AyKontrol(cr.getString(1));
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(new Date());
            String date = cr.getString(2) + "-" + aynum + "-" + cr.getString(0);

            if (today.equals(date)) {
                takvim_txt.setText(cr.getString(0)+" "+functions.AyConvert(aynum)+" "+cr.getString(2));
                small_tarih.setText(cr.getString(0)+" "+cr.getString(1)+" "+cr.getString(2));
                guntxt.setText(cr.getString(10));
                hadis.setText(cr.getString(12));
                hadis_baslik.setText(cr.getString(13));
                gunun_duasi.setText(cr.getString(14));
                dua_baslik.setText(cr.getString(15));

                ghadis = cr.getString(12)+"\n"+cr.getString(13);
                gdua = cr.getString(14)+"\n"+cr.getString(15);

                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date normal_time = dateFormat.parse(dateFormat.format(new Date()));
                    Date imsak_time = dateFormat.parse(date+" "+cr.getString(3)+":00");
                    Date gunes_time = dateFormat.parse(date+" "+cr.getString(4)+":00");
                    Date oglen_time = dateFormat.parse(date+" "+cr.getString(5)+":00");
                    Date ikindi_time = dateFormat.parse(date+" "+cr.getString(6)+":00");
                    Date aksam_time = dateFormat.parse(date+" "+cr.getString(7)+":00");
                    Date yatsi_time = dateFormat.parse(date+" "+cr.getString(8)+":00");


                    if ((normal_time.after(imsak_time)) && (normal_time.before(gunes_time))) {
                        imsak_box.setBackgroundResource(R.drawable.aktif_namaz);
                        gunes_box.setBackgroundResource(R.drawable.transparent);
                        oglen_box.setBackgroundResource(R.drawable.transparent);
                        ikindi_box.setBackgroundResource(R.drawable.transparent);
                        aksam_box.setBackgroundResource(R.drawable.transparent);
                        yatsi_box.setBackgroundResource(R.drawable.transparent);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.beyaz));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        vakit_adi = "GÜNEŞİN DOĞUŞUNA";

                        tur = 0;
                        zaman = cr.getString(4) + ":00";

                    } else if ((normal_time.after(gunes_time)) && (normal_time.before(oglen_time))) {
                        imsak_box.setBackgroundResource(R.drawable.transparent);
                        gunes_box.setBackgroundResource(R.drawable.aktif_namaz);
                        oglen_box.setBackgroundResource(R.drawable.transparent);
                        ikindi_box.setBackgroundResource(R.drawable.transparent);
                        aksam_box.setBackgroundResource(R.drawable.transparent);
                        yatsi_box.setBackgroundResource(R.drawable.transparent);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.beyaz));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        vakit_adi = "ÖĞLEN VAKTİNE";

                        tur = 0;
                        zaman = cr.getString(5) + ":00";
                    } else if ((normal_time.after(oglen_time)) && (normal_time.before(ikindi_time))) {
                        imsak_box.setBackgroundResource(R.drawable.transparent);
                        gunes_box.setBackgroundResource(R.drawable.transparent);
                        oglen_box.setBackgroundResource(R.drawable.aktif_namaz);
                        ikindi_box.setBackgroundResource(R.drawable.transparent);
                        aksam_box.setBackgroundResource(R.drawable.transparent);
                        yatsi_box.setBackgroundResource(R.drawable.transparent);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.beyaz));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        vakit_adi = "İKİNDİ VAKTİNE";

                        tur = 0;
                        zaman = cr.getString(6) + ":00";
                    } else if ((normal_time.after(ikindi_time)) && (normal_time.before(aksam_time))) {
                        imsak_box.setBackgroundResource(R.drawable.transparent);
                        gunes_box.setBackgroundResource(R.drawable.transparent);
                        oglen_box.setBackgroundResource(R.drawable.transparent);
                        ikindi_box.setBackgroundResource(R.drawable.aktif_namaz);
                        aksam_box.setBackgroundResource(R.drawable.transparent);
                        yatsi_box.setBackgroundResource(R.drawable.transparent);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.beyaz));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        vakit_adi = "AKŞAM VAKTİNE";

                        tur = 0;
                        zaman = cr.getString(7) + ":00";
                    } else if ((normal_time.after(aksam_time)) && (normal_time.before(yatsi_time))) {
                        imsak_box.setBackgroundResource(R.drawable.transparent);
                        gunes_box.setBackgroundResource(R.drawable.transparent);
                        oglen_box.setBackgroundResource(R.drawable.transparent);
                        ikindi_box.setBackgroundResource(R.drawable.transparent);
                        aksam_box.setBackgroundResource(R.drawable.aktif_namaz);
                        yatsi_box.setBackgroundResource(R.drawable.transparent);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.beyaz));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        vakit_adi = "YATSI VAKTİNE";
                        tur = 0;
                        zaman = cr.getString(8) + ":00";
                    } else {
                        imsak_box.setBackgroundResource(R.drawable.transparent);
                        gunes_box.setBackgroundResource(R.drawable.transparent);
                        oglen_box.setBackgroundResource(R.drawable.transparent);
                        ikindi_box.setBackgroundResource(R.drawable.transparent);
                        aksam_box.setBackgroundResource(R.drawable.transparent);
                        yatsi_box.setBackgroundResource(R.drawable.aktif_namaz);

                        tab2_imsak_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_txt.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_txt.setTextColor(mActivity.getColor(R.color.mavi));

                        tab2_imsak_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_gunes_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_oglen_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_ikindi_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_aksam_saat.setTextColor(mActivity.getColor(R.color.beyaz));
                        tab2_yatsi_saat.setTextColor(mActivity.getColor(R.color.mavi));
                        vakit_adi = "İMSAK VAKTİNE";

                        tur = 1;
                        zaman = cr.getString(3) + ":00";
                    }

                    islamic_takvim_txt.setText(cr.getString(9));
                    hicri_copy.setText(cr.getString(9));
                    imsak_saat.setText(cr.getString(3));
                    gunes_saat.setText(cr.getString(4));
                    oglen_saat.setText(cr.getString(5));
                    ikindi_saat.setText(cr.getString(6));
                    aksam_saat.setText(cr.getString(7));
                    yatsi_saat.setText(cr.getString(8));

                    tab2_imsak_saat.setText(cr.getString(3));
                    tab2_gunes_saat.setText(cr.getString(4));
                    tab2_oglen_saat.setText(cr.getString(5));
                    tab2_ikindi_saat.setText(cr.getString(6));
                    tab2_aksam_saat.setText(cr.getString(7));
                    tab2_yatsi_saat.setText(cr.getString(8));

                    vakit_txt.setText(vakit_adi);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        vakit_txt.setText(vakit_adi);
        vakit_txt_copy.setText(vakit_adi+":");

    }

    public void startTimer(){
        handler = new Handler();
        runnable  = new Runnable() {
            @Override
            public void run() {
                java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                int hours = 0;
                int min = 0;
                int days;
                int sec = 0;
                long difference;
                try {
                    Date reset_time = dateFormat.parse("00:00:00");
                    Date normal_time = dateFormat.parse(dateFormat.format(new Date()));
                    Date after_time = dateFormat.parse(zaman);
                    int x = normal_time.getHours();
                    int y = after_time.getHours();
                    int z = normal_time.getMinutes();
                    int q = after_time.getMinutes();
                    if(normal_time.getSeconds() == 0){
                        sec = 0;
                    } else {
                        sec = 60-normal_time.getSeconds();
                    }

                    if(tur == 0){
                        difference = after_time.getTime() - normal_time.getTime();
                        days = (int) (difference / (1000*60*60*24));
                        hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                        min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    } else {
                        if(x>y){
                            int hesapla1 = ((24-x)*60)-z;
                            int hesapla2 = (y*60)+q;
                            Date new_time = dateFormat.parse(minuteToTime(hesapla1+hesapla2,sec));
                            difference = new_time.getTime() - reset_time.getTime();
                            days = (int) (difference / (1000*60*60*24));
                            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                        } else {
                            difference = after_time.getTime() - normal_time.getTime();
                            days = (int) (difference / (1000*60*60*24));
                            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                        }
                    }

                    String saat   = "00";
                    String dakika = "00";
                    String saniye = "00";
                    if(hours < 10){
                        saat = "0"+String.valueOf(hours);
                    } else {
                        saat = String.valueOf(hours);
                    }
                    if(min < 10){
                        dakika = "0"+String.valueOf(min);
                    } else {
                        dakika = String.valueOf(min);
                    }
                    if(sec < 10){
                        if(sec == 0){
                            saniye = "00";
                        } else {
                            saniye = "0"+(60-normal_time.getSeconds());
                        }
                    } else {
                        saniye = String.valueOf((60-normal_time.getSeconds()));
                    }

                    sayac_txt.setText(saat+":"+dakika+":"+saniye);
                    sayac_copy_txt.setText(saat+":"+dakika+":"+saniye);
                    sayac_small.setText(saat+":"+dakika+":"+saniye);

                    if (sec == 59) {
                        NamazDb();
                    }

                   // Log.d("time",String.valueOf(hours)+" "+String.valueOf(min)+" "+String.valueOf(sec));

                    handler.postDelayed(this,1000);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable,1000);
    }


    public void PremiumControl(){
        String url = DefineUrl.Url+"?data=PremiumControl";
        StringRequest json = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    if(jsonResponse.getInt("state") == 1){
                        footer_reklam.setVisibility(View.GONE);
                    } else {
                        footer_reklam.setVisibility(View.VISIBLE);
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
                params.put("deviceid", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
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

    public static String minuteToTime(int minute,int sec) {
        int hour = minute / 60;
        minute %= 60;
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":"+String.valueOf(sec);
    }

    public void MainControl(){
        menu_statu = 0;
        tab1 = view.findViewById(R.id.tab1);
        tab2 = view.findViewById(R.id.tab2);

        tab2.setVisibility(View.GONE);
        bg_blur.setVisibility(View.GONE);
        bg.setVisibility(View.VISIBLE);
        tab1.startAnimation(op_on);
        tab1.setVisibility(View.VISIBLE);
        op_on.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
