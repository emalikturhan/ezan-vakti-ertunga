package com.ertunga.namazvakti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.ads.AdsControl;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.ertunga.namazvakti.Fragments.Vakitler;
import com.ertunga.namazvakti.InterFace.MenuInterFace;
import com.ertunga.namazvakti.Special.BlurTransform;
import com.ertunga.namazvakti.Special.Functions;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuInterFace {

    Functions functions = new Functions();
    RequestQueue rg;
    private DrawerLayout drawer;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    private LinearLayout namaz_vakitleri, namaz_imsakiye, namaz_sureleri, namaz_dualari, pusula, kurani_kerim, imsakiye, tesbihat, kaza_takibi, zikirler, dualar, destek, premium, radio, ayarlar, dini_gunler;
    Boolean isServiceRunning = false;
    private ImageView facebook, twitter, instagram;

     ReviewManager manager;
     ReviewInfo reviewInfo;

    String deviceid;
    int vip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Init();
        Intents();
        Clicks();
        loadAd();
        Content(new Vakitler());

        rg = Volley.newRequestQueue(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }



        if (sharedPreferences.getInt("firstlogin",1) != 0) {


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Bilgilendirme");
            builder.setMessage("Uygulamanın stabil çalışması için 'diğer uygulamaların üzerinde göster' seçeneği aktifleştirilmelidir.");
            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 0);
                }
            });
            builder.show();


            sharedPreferences.edit().putInt("firstlogin", 0).apply();
        }

        AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());


     /*

        isServiceRunning = ServiceTools.isServiceRunning(MainActivity.this.getApplicationContext(), NamazService.class);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("bildirim_cubugu", 0) == 1) {
            Intent serviceIntent = new Intent(this, NamazService.class);
            if (!isServiceRunning) {
                stopService(serviceIntent);
                ContextCompat.startForegroundService(this, serviceIntent);
            }
        }

      */
        activateReviewInfo();
        AdsControl.getInstance().adsGecisLoading(this);
    }



    @Override
    protected void onStart() {
        super.onStart();

        /*
        try {
           Intent serviceIntent = new Intent(MainActivity.this, NamazService.class);
           serviceIntent.putExtra("name","deneme");
           startService(serviceIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
        */
    }

    private void Init() {
        namaz_sureleri = findViewById(R.id.namaz_sureleri);
        namaz_imsakiye = findViewById(R.id.namaz_imsakiye);
        namaz_dualari = findViewById(R.id.namaz_dualari);
        pusula = findViewById(R.id.pusula);
        kurani_kerim = findViewById(R.id.kurani_kerim);
        imsakiye = findViewById(R.id.imsakiye);
        tesbihat = findViewById(R.id.tesbihat);
        kaza_takibi = findViewById(R.id.kaza_takibi);
        zikirler = findViewById(R.id.zikirler);
        dualar = findViewById(R.id.dualar);
        destek = findViewById(R.id.destek);
        radio = findViewById(R.id.radio);
        ayarlar = findViewById(R.id.ayarlar);
        namaz_vakitleri = findViewById(R.id.namaz_vakitleri);
        dini_gunler = findViewById(R.id.dini_gunler);
    }

    private void Intents() {
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rg = Volley.newRequestQueue(this);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            ImageView nav_bg = findViewById(R.id.nav_bg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Picasso.get().load(R.drawable.mosquesplash_1).transform(new BlurTransform(getApplicationContext())).fit().centerInside().into(nav_bg);
            }
        }

        // OneSignal Initialization
        try {
         /*   OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void activateReviewInfo(){

        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
            } else {

            }
        });
    }

    private void startReviewInfo(){
        if(reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
            flow.addOnCompleteListener(task -> {
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown. Thus, no
                // matter the result, we continue our app flow.
            });
        }
    }

    private void Clicks() {
        namaz_vakitleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        namaz_sureleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startActivity(new Intent(MainActivity.this, NamazSureleri.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        namaz_imsakiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startReviewInfo();
                    startActivity(new Intent(MainActivity.this, Imsakiye.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });


        namaz_dualari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startActivity(new Intent(MainActivity.this, NamazDualari.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pusula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startActivity(new Intent(MainActivity.this, Pusula.class));
            }
        });

        imsakiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startReviewInfo();
                startActivity(new Intent(MainActivity.this, Imsakiye.class));
            }
        });

        tesbihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startReviewInfo();
                    startActivity(new Intent(MainActivity.this, DiniBilgiler.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        kaza_takibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startReviewInfo();
                startActivity(new Intent(MainActivity.this, Kazalar.class));
            }
        });

        zikirler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startReviewInfo();
                    startActivity(new Intent(MainActivity.this, Zikirler.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dualar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startActivity(new Intent(MainActivity.this, Dualar.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });


        destek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startActivity(new Intent(MainActivity.this, Destek.class));
            }
        });

        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.InternetControl(MainActivity.this) == true) {
                    Statu(false);
                    startActivity(new Intent(MainActivity.this, Radio.class));
                } else {
                    Toast.makeText(MainActivity.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startActivity(new Intent(MainActivity.this, Ayarlar.class));
            }
        });

        dini_gunler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Statu(false);
                startActivity(new Intent(MainActivity.this, DiniGunler.class));
            }
        });


    }


    public void Content(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void Statu(boolean statu) {
        if (statu == true) {
            drawer.openDrawer(Gravity.LEFT);
        } else {
            drawer.closeDrawer(Gravity.LEFT);
        }
    }

    public static class ServiceTools {
        private String LOG_TAG = ServiceTools.class.getName();

        public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

            for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    /*
    public void GetSosyalMedya(){
        String url = DefineUrl.Url+"?data=GetSosyalMedya";
        JsonObjectRequest json = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("state");
                    for(int i=0; i<=ar.length(); i++){
                        JSONObject obj = ar.getJSONObject(i);
                        fc  = obj.getString("facebook");
                        tw   = obj.getString("twitter");
                        in = obj.getString("instagram");

                        if(fc.length() > 0){
                            facebook.setVisibility(View.VISIBLE);
                        } else {
                            facebook.setVisibility(View.GONE);
                        }

                        if(tw.length() > 0){
                            twitter.setVisibility(View.VISIBLE);
                        } else {
                            twitter.setVisibility(View.GONE);
                        }

                        if(in.length() > 0){
                            instagram.setVisibility(View.VISIBLE);
                        } else {
                            instagram.setVisibility(View.GONE);
                        }
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



    public void GetSosyalMedya() {
        String url = DefineUrl.Url + "?data=GetSosyalMedya";
        StringRequest json = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray ar = jsonResponse.getJSONArray("state");
                    for (int i = 0; i <= ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        fc = obj.getString("facebook");
                        tw = obj.getString("twitter");
                        in = obj.getString("instagram");

                        if (fc.length() > 0) {
                            facebook.setVisibility(View.VISIBLE);
                        } else {
                            facebook.setVisibility(View.GONE);
                        }

                        if (tw.length() > 0) {
                            twitter.setVisibility(View.VISIBLE);
                        } else {
                            twitter.setVisibility(View.GONE);
                        }

                        if (in.length() > 0) {
                            instagram.setVisibility(View.VISIBLE);
                        } else {
                            instagram.setVisibility(View.GONE);
                        }

                        vip = obj.getInt("vip");
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
                params.put("deviceid", deviceid);
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
 */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-8620269889999726/1571357884",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.mInterstitialAd = interstitialAd;
                        showInterstitial();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }


    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }

}
