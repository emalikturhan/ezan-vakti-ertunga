package com.ertunga.namazvakti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ertunga.namazvakti.Special.Functions;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ertunga.namazvakti.Db.DataBase;
import com.ertunga.namazvakti.Special.DefineUrl;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ayarlar extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    RequestQueue rg;
    Functions functions = new Functions();
    private ImageView back, tema1, tema2, tema1_select, tema2_select;
    private RelativeLayout loaderbox, zaman_content;
    private LinearLayout zaman_box;
    private TextView reload, imsak_zaman, gunes_zaman, oglen_zaman, ikindi_zaman, aksam_zaman, yatsi_zaman;
    private TextView imsak_melodi, gunes_melodi, oglen_melodi, ikindi_melodi, aksam_melodi, yatsi_melodi, onbesdk, otuzdk, kirkbesdk;
    private TextView melodi_txt_1, melodi_txt_2, melodi_txt_3, melodi_txt_4, melodi_txt_5, melodi_txt_6;
    private Switch imsak_once, gunes_once, oglen_once, ikindi_once, aksam_once, yatsi_once;
    private Switch bildirim_cubugu, sabah, oglen, ikindi, aksam, yatsi;
    private Animation op_on, op_off;
    int zaman = 0;
    int imsak_dk = 0;
    int gunes_dk = 0;
    int oglen_dk = 0;
    int ikindi_dk = 0;
    int aksam_dk = 0;
    int yatsi_dk = 0;
    private InterstitialAd mInterstitialAd;

    int willChangeTime;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        loadAd();
        addAutoStartupCheck();
        Init();
        Intents();
        Clicks();
        Animations();

    }

    private void Init() {
        back = findViewById(R.id.back);
        tema1 = findViewById(R.id.tema1);
        tema2 = findViewById(R.id.tema2);
        tema1_select = findViewById(R.id.tema1_select);
        tema2_select = findViewById(R.id.tema2_select);
        loaderbox = findViewById(R.id.loaderbox);
        reload = findViewById(R.id.reload);
        String reloadText = "Eklemiş olduğunuz şehirlerin namaz vakit bilgilerini güncellemek için "+"<b>" + "Tıklayınız" + "</b> ";
        reload.setText(Html.fromHtml(reloadText));
        bildirim_cubugu = findViewById(R.id.bildirim_cubugu);
        sabah = findViewById(R.id.sabah);
        oglen = findViewById(R.id.oglen);
        ikindi = findViewById(R.id.ikindi);
        aksam = findViewById(R.id.aksam);
        yatsi = findViewById(R.id.yatsi);

        imsak_zaman = findViewById(R.id.imsak_zaman);
        gunes_zaman = findViewById(R.id.gunes_zaman);
        oglen_zaman = findViewById(R.id.oglen_zaman);
        ikindi_zaman = findViewById(R.id.ikindi_zaman);
        aksam_zaman = findViewById(R.id.aksam_zaman);
        yatsi_zaman = findViewById(R.id.yatsi_zaman);

        imsak_melodi = findViewById(R.id.imsak_melodi);
        gunes_melodi = findViewById(R.id.gunes_melodi);
        oglen_melodi = findViewById(R.id.oglen_melodi);
        ikindi_melodi = findViewById(R.id.ikindi_melodi);
        aksam_melodi = findViewById(R.id.aksam_melodi);
        yatsi_melodi = findViewById(R.id.yatsi_melodi);

        imsak_once = findViewById(R.id.imsak_once);
        gunes_once = findViewById(R.id.gunes_once);
        oglen_once = findViewById(R.id.oglen_once);
        ikindi_once = findViewById(R.id.ikindi_once);
        aksam_once = findViewById(R.id.aksam_once);
        yatsi_once = findViewById(R.id.yatsi_once);

        zaman_content = findViewById(R.id.zaman_content);
        zaman_box = findViewById(R.id.zaman_box);
        onbesdk = findViewById(R.id.onbesdk);
        otuzdk = findViewById(R.id.otuzdk);
        kirkbesdk = findViewById(R.id.kirkbesdk);

        melodi_txt_1 = findViewById(R.id.melodi_txt_1);
        melodi_txt_2 = findViewById(R.id.melodi_txt_2);
        melodi_txt_3 = findViewById(R.id.melodi_txt_3);
        melodi_txt_4 = findViewById(R.id.melodi_txt_4);
        melodi_txt_5 = findViewById(R.id.melodi_txt_5);
        melodi_txt_6 = findViewById(R.id.melodi_txt_6);

    }

    private void Intents() {
        sharedPreferences = getSharedPreferences("com.ertunga.namazvakti", Context.MODE_PRIVATE);
        rg = Volley.newRequestQueue(this);
        if (sharedPreferences.getInt("bg", 0) == 0) {
            tema1_select.setVisibility(View.VISIBLE);
            tema2_select.setVisibility(View.GONE);
        } else {
            tema1_select.setVisibility(View.GONE);
            tema2_select.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getInt("bildirim_cubugu", 0) == 0) {
            bildirim_cubugu.setChecked(false);
        } else {
            bildirim_cubugu.setChecked(true);
        }

        if (sharedPreferences.getInt("sabah_bildirim", 0) == 0) {
            sabah.setChecked(false);
        } else {
            sabah.setChecked(true);
        }

        if (sharedPreferences.getInt("oglen_bildirim", 0) == 0) {
            oglen.setChecked(false);
        } else {
            oglen.setChecked(true);
        }

        if (sharedPreferences.getInt("ikindi_bildirim", 0) == 0) {
            ikindi.setChecked(false);
        } else {
            ikindi.setChecked(true);
        }

        if (sharedPreferences.getInt("aksam_bildirim", 0) == 0) {
            aksam.setChecked(false);
        } else {
            aksam.setChecked(true);
        }

        if (sharedPreferences.getInt("yatsi_bildirim", 0) == 0) {
            yatsi.setChecked(false);
        } else {
            yatsi.setChecked(true);
        }


        // SET MİNUTES ------------

        imsak_dk = sharedPreferences.getInt("imsak_dk", 0);
        imsak_zaman.setText(imsak_dk + "Dk. Önce");

        gunes_dk = sharedPreferences.getInt("gunes_dk", 0);
        gunes_zaman.setText(gunes_dk + "Dk. Önce");

        oglen_dk = sharedPreferences.getInt("oglen_dk", 0);
        oglen_zaman.setText(oglen_dk + "Dk. Önce");

        ikindi_dk = sharedPreferences.getInt("ikindi_dk", 0);
        ikindi_zaman.setText(ikindi_dk + "Dk. Önce");

        aksam_dk = sharedPreferences.getInt("aksam_dk", 0);
        aksam_zaman.setText(aksam_dk + "Dk. Önce");

        yatsi_dk = sharedPreferences.getInt("yatsi_dk", 0);
        yatsi_zaman.setText(yatsi_dk + "Dk. Önce");

        // SET MİNUTES ------------


        if (sharedPreferences.getInt("imsak_dk_bildirim", 0) == 0) {
            imsak_once.setChecked(false);
        } else {
            imsak_once.setChecked(true);
        }

        if (sharedPreferences.getInt("gunes_dk_bildirim", 0) == 0) {
            gunes_once.setChecked(false);
        } else {
            gunes_once.setChecked(true);
        }

        if (sharedPreferences.getInt("oglen_dk_bildirim", 0) == 0) {
            oglen_once.setChecked(false);
        } else {
            oglen_once.setChecked(true);
        }

        if (sharedPreferences.getInt("ikindi_dk_bildirim", 0) == 0) {
            ikindi_once.setChecked(false);
        } else {
            ikindi_once.setChecked(true);
        }

        if (sharedPreferences.getInt("aksam_dk_bildirim", 0) == 0) {
            aksam_once.setChecked(false);
        } else {
            aksam_once.setChecked(true);
        }

        if (sharedPreferences.getInt("yatsi_dk_bildirim", 0) == 0) {
            yatsi_once.setChecked(false);
        } else {
            yatsi_once.setChecked(true);
        }

    }

    private void Clicks() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tema1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tema1_select.setVisibility(View.VISIBLE);
                tema2_select.setVisibility(View.GONE);
                sharedPreferences.edit().putInt("bg", 0).apply();
            }
        });

        tema2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tema1_select.setVisibility(View.GONE);
                tema2_select.setVisibility(View.VISIBLE);
                sharedPreferences.edit().putInt("bg", 1).apply();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (functions.InternetControl(Ayarlar.this) == true) {
                    AylikNamaz();
                } else {
                    Toast.makeText(Ayarlar.this, "Internet bağlantınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bildirim_cubugu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        sharedPreferences.edit().putInt("bildirim_cubugu", 1).apply();

                        Intent serviceIntentx = new Intent(Ayarlar.this, NamazService.class);
                        stopService(serviceIntentx);
                        ContextCompat.startForegroundService(Ayarlar.this, serviceIntentx);

                    } else {
                        sharedPreferences.edit().putInt("bildirim_cubugu", 0).apply();

                        Intent serviceIntentx = new Intent(Ayarlar.this, NamazService.class);
                        stopService(serviceIntentx);

                    }
                } catch (Exception e) {
                    Toast.makeText(Ayarlar.this, "Error: "+e, Toast.LENGTH_LONG).show();
                    Toast.makeText(Ayarlar.this, "Error: "+e, Toast.LENGTH_LONG).show();
                    Toast.makeText(Ayarlar.this, "Error: "+e, Toast.LENGTH_LONG).show();
                }
            }
        });

        sabah.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("sabah_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("sabah_bildirim", 0).apply();
                }
            }
        });

        oglen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("oglen_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("oglen_bildirim", 0).apply();
                }
            }
        });

        ikindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("ikindi_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("ikindi_bildirim", 0).apply();
                }
            }
        });

        aksam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("aksam_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("aksam_bildirim", 0).apply();
                }
            }
        });

        yatsi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("yatsi_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("yatsi_bildirim", 0).apply();
                }
            }
        });


        final EditText zaman_edittext = findViewById(R.id.zaman_edittext);
        imsak_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 1;
                zaman_edittext.setText(imsak_dk + "");

            }
        });

        gunes_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 2;
                zaman_edittext.setText(gunes_dk + "");

            }
        });

        oglen_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 3;
                zaman_edittext.setText(oglen_dk + "");

            }
        });


        ikindi_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 4;
                zaman_edittext.setText(ikindi_dk + "");

            }
        });

        aksam_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 5;
                zaman_edittext.setText(aksam_dk + "");

            }
        });

        yatsi_zaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_content.setVisibility(View.VISIBLE);
                zaman_box.startAnimation(op_on);
                zaman = 6;
                zaman_edittext.setText(yatsi_dk + "");

            }
        });

        AppCompatButton zaman_kaydet = findViewById(R.id.zaman_kaydet);
        zaman_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dk = Integer.parseInt(zaman_edittext.getText().toString());
                if (dk < 61 && dk > 0) {
                    switch (zaman) {
                        case 1:
                            sharedPreferences.edit().putInt("imsak_dk", dk).apply();
                            break;
                        case 2:
                            sharedPreferences.edit().putInt("gunes_dk", dk).apply();
                            break;
                        case 3:
                            sharedPreferences.edit().putInt("oglen_dk", dk).apply();
                            break;
                        case 4:
                            sharedPreferences.edit().putInt("ikindi_dk", dk).apply();
                            break;
                        case 5:
                            sharedPreferences.edit().putInt("aksam_dk", dk).apply();
                            break;
                        case 6:
                            sharedPreferences.edit().putInt("yatsi_dk", dk).apply();
                            break;
                    }

                    Toast.makeText(Ayarlar.this, "Kaydedildi!", Toast.LENGTH_SHORT).show();
                    zaman_box.startAnimation(op_off);
                    op_off.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            zaman_content.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    finish();
                    startActivity(new Intent(getApplicationContext(), Ayarlar.class));
                } else {
                    Toast.makeText(Ayarlar.this, "Değer 0'dan büyük ve en fazla 60 olmalı!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        zaman_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zaman_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        zaman_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        onbesdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zaman == 1) {
                    imsak_dk = 1;
                    sharedPreferences.edit().putInt("imsak_dk", 1).apply();
                    imsak_zaman.setText("15dk. Önce");
                } else if (zaman == 2) {
                    gunes_dk = 1;
                    sharedPreferences.edit().putInt("gunes_dk", 1).apply();
                    gunes_zaman.setText("15dk. Önce");
                } else if (zaman == 3) {
                    oglen_dk = 1;
                    sharedPreferences.edit().putInt("oglen_dk", 1).apply();
                    oglen_zaman.setText("15dk. Önce");
                } else if (zaman == 4) {
                    ikindi_dk = 1;
                    sharedPreferences.edit().putInt("ikindi_dk", 1).apply();
                    ikindi_zaman.setText("15dk. Önce");
                } else if (zaman == 5) {
                    aksam_dk = 1;
                    sharedPreferences.edit().putInt("aksam_dk", 1).apply();
                    aksam_zaman.setText("15dk. Önce");
                } else if (zaman == 6) {
                    yatsi_dk = 1;
                    sharedPreferences.edit().putInt("yatsi_dk", 1).apply();
                    yatsi_zaman.setText("15dk. Önce");
                }

                onbesdk.setTextColor(getColor(R.color.yesil));
                otuzdk.setTextColor(getColor(R.color.siyah));
                kirkbesdk.setTextColor(getColor(R.color.siyah));

                zaman_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        zaman_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        otuzdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zaman == 1) {
                    imsak_dk = 2;
                    sharedPreferences.edit().putInt("imsak_dk", 2).apply();
                    imsak_zaman.setText("30dk. Önce");
                } else if (zaman == 2) {
                    gunes_dk = 2;
                    sharedPreferences.edit().putInt("gunes_dk", 2).apply();
                    gunes_zaman.setText("30dk. Önce");
                } else if (zaman == 3) {
                    oglen_dk = 2;
                    sharedPreferences.edit().putInt("oglen_dk", 2).apply();
                    oglen_zaman.setText("30dk. Önce");
                } else if (zaman == 4) {
                    ikindi_dk = 2;
                    sharedPreferences.edit().putInt("ikindi_dk", 2).apply();
                    ikindi_zaman.setText("30dk. Önce");
                } else if (zaman == 5) {
                    aksam_dk = 2;
                    sharedPreferences.edit().putInt("aksam_dk", 2).apply();
                    aksam_zaman.setText("30dk. Önce");
                } else if (zaman == 6) {
                    yatsi_dk = 2;
                    sharedPreferences.edit().putInt("yatsi_dk", 2).apply();
                    yatsi_zaman.setText("30dk. Önce");
                }
                onbesdk.setTextColor(getColor(R.color.siyah));
                otuzdk.setTextColor(getColor(R.color.yesil));
                kirkbesdk.setTextColor(getColor(R.color.siyah));

                zaman_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        zaman_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        kirkbesdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zaman == 1) {
                    imsak_dk = 3;
                    sharedPreferences.edit().putInt("imsak_dk", 3).apply();
                    imsak_zaman.setText("45dk. Önce");
                } else if (zaman == 2) {
                    gunes_dk = 3;
                    sharedPreferences.edit().putInt("gunes_dk", 3).apply();
                    gunes_zaman.setText("45dk. Önce");
                } else if (zaman == 3) {
                    oglen_dk = 3;
                    sharedPreferences.edit().putInt("oglen_dk", 3).apply();
                    oglen_zaman.setText("45dk. Önce");
                } else if (zaman == 4) {
                    ikindi_dk = 3;
                    sharedPreferences.edit().putInt("ikindi_dk", 3).apply();
                    ikindi_zaman.setText("45dk. Önce");
                } else if (zaman == 5) {
                    aksam_dk = 3;
                    sharedPreferences.edit().putInt("aksam_dk", 3).apply();
                    aksam_zaman.setText("45dk. Önce");
                } else if (zaman == 6) {
                    yatsi_dk = 3;
                    sharedPreferences.edit().putInt("yatsi_dk", 3).apply();
                    yatsi_zaman.setText("45dk. Önce");
                }
                onbesdk.setTextColor(getColor(R.color.siyah));
                otuzdk.setTextColor(getColor(R.color.siyah));
                kirkbesdk.setTextColor(getColor(R.color.yesil));

                zaman_box.startAnimation(op_off);
                op_off.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        zaman_content.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        imsak_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("imsak_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("imsak_dk_bildirim", 0).apply();
                }
            }
        });

        gunes_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("gunes_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("gunes_dk_bildirim", 0).apply();
                }
            }
        });

        oglen_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("oglen_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("oglen_dk_bildirim", 0).apply();
                }
            }
        });

        ikindi_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("ikindi_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("ikindi_dk_bildirim", 0).apply();
                }
            }
        });

        aksam_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("aksam_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("aksam_dk_bildirim", 0).apply();
                }
            }
        });

        yatsi_once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedPreferences.edit().putInt("yatsi_dk_bildirim", 1).apply();
                } else {
                    sharedPreferences.edit().putInt("yatsi_dk_bildirim", 0).apply();
                }
            }
        });


        imsak_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 1);
                i.putExtras(b);
                startActivity(i);
            }
        });

        gunes_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 2);
                i.putExtras(b);
                startActivity(i);
            }
        });

        oglen_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 3);
                i.putExtras(b);
                startActivity(i);
            }
        });

        ikindi_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 4);
                i.putExtras(b);
                startActivity(i);
            }
        });

        aksam_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 5);
                i.putExtras(b);
                startActivity(i);
            }
        });

        yatsi_melodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                Intent i = new Intent(Ayarlar.this, MelodiSec.class);
                b.putInt("type", 6);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void Animations() {
        op_on = AnimationUtils.loadAnimation(this, R.anim.opacity_on);
        op_off = AnimationUtils.loadAnimation(this, R.anim.opacity_off);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void AylikNamaz() {
        loaderbox.setVisibility(View.VISIBLE);
        String url = DefineUrl.Url + "?data=AylikNamaz&ilceid=" + sharedPreferences.getString("ilceid", null);
        Log.d("asdx", url);
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("data");
                    for (int i = 0; i <= ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        //  UList.add(new IlceList(obj.getString("ilce"),obj.getString("ilceid")));
                        String gun = obj.getString("gun");
                        String ay = obj.getString("ay");
                        String yil = obj.getString("yil");
                        String imsak = obj.getString("imsak");
                        String gunes = obj.getString("gunes");
                        String oglen = obj.getString("oglen");
                        String ikindi = obj.getString("ikindi");
                        String aksam = obj.getString("aksam");
                        String yatsi = obj.getString("yatsi");
                        String hicri = obj.getString("hicri");
                        String guntxt = obj.getString("gun_txt");
                        String kible = obj.getString("kible");
                        String hadis = obj.getString("hadis");
                        String hadis_adi = obj.getString("hadis_adi");
                        String dua = obj.getString("dua");
                        String dua_adi = obj.getString("dua_adi");
                        DataBase veritabanı = new DataBase(Ayarlar.this);
                        veritabanı.veriEkle(gun, ay, yil, imsak, gunes, oglen, ikindi, aksam, yatsi, hicri, guntxt, kible, hadis, hadis_adi, dua, dua_adi);

                        sharedPreferences.edit().putInt("kible", obj.getInt("kible")).apply();
                        loaderbox.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    loaderbox.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rg.add(json);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPreferences.getInt("imsak_melodi", 0) == 1) {
            melodi_txt_1.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 2) {
            melodi_txt_1.setText("Essalat");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 3) {
            melodi_txt_1.setText("Ezan-1");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 4) {
            melodi_txt_1.setText("Ezan-2");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 5) {
            melodi_txt_1.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 6) {
            melodi_txt_1.setText("Halaka-1");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 7) {
            melodi_txt_1.setText("Halaka-2");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 8) {
            melodi_txt_1.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 9) {
            melodi_txt_1.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 10) {
            melodi_txt_1.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 11) {
            melodi_txt_1.setText("Melodi-1");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 12) {
            melodi_txt_1.setText("Melodi-2");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 13) {
            melodi_txt_1.setText("Sela");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 14) {
            melodi_txt_1.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 15) {
            melodi_txt_1.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 16) {
            melodi_txt_1.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("imsak_melodi", 0) == 17) {
            melodi_txt_1.setText("Kısa Bildirim Sesi-2");
        }


        if (sharedPreferences.getInt("gunes_melodi", 0) == 1) {
            melodi_txt_2.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 2) {
            melodi_txt_2.setText("Essalat");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 3) {
            melodi_txt_2.setText("Ezan-1");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 4) {
            melodi_txt_2.setText("Ezan-2");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 5) {
            melodi_txt_2.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 6) {
            melodi_txt_2.setText("Halaka-1");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 7) {
            melodi_txt_2.setText("Halaka-2");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 8) {
            melodi_txt_2.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 9) {
            melodi_txt_2.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 10) {
            melodi_txt_2.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 11) {
            melodi_txt_2.setText("Melodi-1");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 12) {
            melodi_txt_2.setText("Melodi-2");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 13) {
            melodi_txt_2.setText("Sela");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 14) {
            melodi_txt_2.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 15) {
            melodi_txt_2.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 16) {
            melodi_txt_2.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("gunes_melodi", 0) == 17) {
            melodi_txt_2.setText("Kısa Bildirim Sesi-2");
        }

        if (sharedPreferences.getInt("oglen_melodi", 0) == 1) {
            melodi_txt_3.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 2) {
            melodi_txt_3.setText("Essalat");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 3) {
            melodi_txt_3.setText("Ezan-1");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 4) {
            melodi_txt_3.setText("Ezan-2");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 5) {
            melodi_txt_3.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 6) {
            melodi_txt_3.setText("Halaka-1");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 7) {
            melodi_txt_3.setText("Halaka-2");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 8) {
            melodi_txt_3.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 9) {
            melodi_txt_3.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 10) {
            melodi_txt_3.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 11) {
            melodi_txt_3.setText("Melodi-1");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 12) {
            melodi_txt_3.setText("Melodi-2");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 13) {
            melodi_txt_3.setText("Sela");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 14) {
            melodi_txt_3.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 15) {
            melodi_txt_3.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 16) {
            melodi_txt_3.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("oglen_melodi", 0) == 17) {
            melodi_txt_3.setText("Kısa Bildirim Sesi-2");
        }

        if (sharedPreferences.getInt("ikindi_melodi", 0) == 1) {
            melodi_txt_4.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 2) {
            melodi_txt_4.setText("Essalat");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 3) {
            melodi_txt_4.setText("Ezan-1");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 4) {
            melodi_txt_4.setText("Ezan-2");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 5) {
            melodi_txt_4.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 6) {
            melodi_txt_4.setText("Halaka-1");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 7) {
            melodi_txt_4.setText("Halaka-2");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 8) {
            melodi_txt_4.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 9) {
            melodi_txt_4.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 10) {
            melodi_txt_4.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 11) {
            melodi_txt_4.setText("Melodi-1");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 12) {
            melodi_txt_4.setText("Melodi-2");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 13) {
            melodi_txt_4.setText("Sela");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 14) {
            melodi_txt_4.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 15) {
            melodi_txt_4.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 16) {
            melodi_txt_4.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("ikindi_melodi", 0) == 17) {
            melodi_txt_4.setText("Kısa Bildirim Sesi-2");
        }

        if (sharedPreferences.getInt("aksam_melodi", 0) == 1) {
            melodi_txt_5.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 2) {
            melodi_txt_5.setText("Essalat");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 3) {
            melodi_txt_5.setText("Ezan-1");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 4) {
            melodi_txt_5.setText("Ezan-2");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 5) {
            melodi_txt_5.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 6) {
            melodi_txt_5.setText("Halaka-1");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 7) {
            melodi_txt_5.setText("Halaka-2");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 8) {
            melodi_txt_5.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 9) {
            melodi_txt_5.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 10) {
            melodi_txt_5.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 11) {
            melodi_txt_5.setText("Melodi-1");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 12) {
            melodi_txt_5.setText("Melodi-2");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 13) {
            melodi_txt_5.setText("Sela");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 14) {
            melodi_txt_5.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 15) {
            melodi_txt_5.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 16) {
            melodi_txt_5.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("aksam_melodi", 0) == 17) {
            melodi_txt_5.setText("Kısa Bildirim Sesi-2");
        }

        if (sharedPreferences.getInt("yatsi_melodi", 0) == 1) {
            melodi_txt_6.setText("Davul Sesi");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 2) {
            melodi_txt_6.setText("Essalat");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 3) {
            melodi_txt_6.setText("Ezan-1");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 4) {
            melodi_txt_6.setText("Ezan-2");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 5) {
            melodi_txt_6.setText("Ezan Duası");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 6) {
            melodi_txt_6.setText("Halaka-1");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 7) {
            melodi_txt_6.setText("Halaka-2");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 8) {
            melodi_txt_6.setText("Kısa Ezan-1");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 9) {
            melodi_txt_6.setText("Kısa Ezan-2");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 10) {
            melodi_txt_6.setText("Mekke Ezanı");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 11) {
            melodi_txt_6.setText("Melodi-1");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 12) {
            melodi_txt_6.setText("Melodi-2");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 13) {
            melodi_txt_6.setText("Sela");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 14) {
            melodi_txt_6.setText("Uyandırma Sesi-1");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 15) {
            melodi_txt_6.setText("Uyandırma Sesi-2");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 16) {
            melodi_txt_6.setText("Kısa Bildirim Sesi");
        } else if (sharedPreferences.getInt("yatsi_melodi", 0) == 17) {
            melodi_txt_6.setText("Kısa Bildirim Sesi-2");
        }

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
                        Ayarlar.this.mInterstitialAd = interstitialAd;
                        showInterstitial();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Ayarlar.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Ayarlar.this.mInterstitialAd = null;
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

    private void addAutoStartupCheck() {

        LinearLayout alertbox = findViewById(R.id.alertbox);

        try {
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                alertbox.setVisibility(View.VISIBLE);
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                alertbox.setVisibility(View.VISIBLE);
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                alertbox.setVisibility(View.VISIBLE);
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                alertbox.setVisibility(View.VISIBLE);
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                alertbox.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }


    public void addAutoStartup(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(getApplicationContext())) {
            RequestPermission();
        }


        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }

    public void RequestPermission() {
        // Check if Android M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    // PermissionDenied();
                    Toast.makeText(this, "İzin verilmedi!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Zaten izin verilmiş.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}