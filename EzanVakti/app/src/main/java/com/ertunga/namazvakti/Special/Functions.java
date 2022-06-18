package com.ertunga.namazvakti.Special;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class Functions {

    public boolean InternetControl(Activity activity){
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity.getActiveNetworkInfo()!=null){
            if(connectivity.getActiveNetworkInfo().isConnected ()) return true;
        }
        return false;
    }

    public void requestPermission(Activity activity, int PRC){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
        },PRC);
    }

    public boolean checkPermissionFromDevice(Context c){
        boolean status = false;
        int read_external_storage_result = ContextCompat.checkSelfPermission(c, Manifest.permission.READ_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(c,Manifest.permission.RECORD_AUDIO);
        int camera_result = ContextCompat.checkSelfPermission(c,Manifest.permission.CAMERA);
        int access_fine_location_result = ContextCompat.checkSelfPermission(c,Manifest.permission.ACCESS_FINE_LOCATION);
        int access_coarse_location_result = ContextCompat.checkSelfPermission(c,Manifest.permission.ACCESS_COARSE_LOCATION);
        int access_network_state_result = ContextCompat.checkSelfPermission(c,Manifest.permission.ACCESS_NETWORK_STATE);

        if((read_external_storage_result == PackageManager.PERMISSION_GRANTED) && (record_audio_result == PackageManager.PERMISSION_GRANTED) &&
                (camera_result == PackageManager.PERMISSION_GRANTED) && (access_fine_location_result == PackageManager.PERMISSION_GRANTED) &&
                (access_coarse_location_result == PackageManager.PERMISSION_GRANTED) && (access_network_state_result == PackageManager.PERMISSION_GRANTED)) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }

    @Deprecated
    public void ChangeLanguage(Activity activity,String language){
        try {
            Locale myLocale = new Locale(language);
            Resources res = activity.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void  CloseKeyboard(Activity activity){
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SlideUpL(Animation animation, final LinearLayout linearLayout){
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
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

    public void SlideDownL(Animation animation, final LinearLayout linearLayout){
        linearLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void SlideUpR(Animation animation, final RelativeLayout relativeLayout){
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
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

    public void SlideDownR(Animation animation, final RelativeLayout relativeLayout){
        try {
            relativeLayout.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relativeLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void MyRiadRegular(Activity activity, TextView textview){
        Typeface face=Typeface.createFromAsset(activity.getAssets(),"fonts/myriad.otf");
        textview.setTypeface(face);
    }

    public String AyKontrol(String ay){
        String num = "";
        if(ay.equals("Ocak")){
            num = "01" ;
        } else if(ay.equals("Şubat")){
            num = "02" ;
        } else if(ay.equals("Mart")){
            num = "03" ;
        }  else if(ay.equals("Nisan")){
            num = "04" ;
        } else if(ay.equals("Mayıs")){
            num = "05" ;
        } else if(ay.equals("Haziran")){
            num = "06" ;
        } else if(ay.equals("Temmuz")){
            num = "07" ;
        } else if(ay.equals("Ağustos")){
            num = "08" ;
        } else if(ay.equals("Eylül")){
            num = "09" ;
        } else if(ay.equals("Ekim")){
            num = "10" ;
        } else if(ay.equals("Kasım")){
            num = "11" ;
        } else if(ay.equals("Aralık")){
            num = "12" ;
        }
        return num;
    }

    public String AyConvert(String ay){
        String num = "";
        if(ay.equals("01")){
            num = "Ock" ;
        } else if(ay.equals("02")){
            num = "Şub" ;
        } else if(ay.equals("03")){
            num = "Mar" ;
        }  else if(ay.equals("04")){
            num = "Nis" ;
        } else if(ay.equals("05")){
            num = "May" ;
        } else if(ay.equals("06")){
            num = "Haz" ;
        } else if(ay.equals("07")){
            num = "Tem" ;
        } else if(ay.equals("08")){
            num = "Ağu" ;
        } else if(ay.equals("09")){
            num = "Eyl" ;
        } else if(ay.equals("10")){
            num = "Eki" ;
        } else if(ay.equals("11")){
            num = "Kas" ;
        } else if(ay.equals("12")){
            num = "Ara" ;
        }
        return num;
    }



}
