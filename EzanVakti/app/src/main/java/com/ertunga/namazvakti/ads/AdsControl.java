package com.ertunga.namazvakti.ads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ertunga.namazvakti.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class AdsControl {
    private InterstitialAd mInterstitialAd;
    private String TAG = "AdsControl";
    private static AdsControl uniqueInstance;
    private InterstitialAd interstitialAd;
    public static boolean adsIsReady = true;

    public static AdsControl getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new AdsControl();
        }
        return uniqueInstance;
    }

    private AdsControl() {
    }

    public InterstitialAd adsGecisLoading(Context mContext) {


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(mContext,mContext.getResources().getString(R.string.interstitial_key), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });



        return interstitialAd;

    }



}
