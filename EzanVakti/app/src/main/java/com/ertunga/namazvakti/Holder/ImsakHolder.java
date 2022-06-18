package com.ertunga.namazvakti.Holder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.R;

public class ImsakHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView gun,gun_txt,ay,hicri,aksam,imsak,sabah,oglen;
    public Activity activity;

    @Override
    public void onClick(View v) {
        // Toast.makeText(v.getContext(),"LOG "+getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public ImsakHolder(final View view) {
        super(view);
        gun = view.findViewById(R.id.gun);
        gun_txt = view.findViewById(R.id.gun_txt);
        ay = view.findViewById(R.id.ay);
        hicri = view.findViewById(R.id.hicri);
        aksam = view.findViewById(R.id.aksam);
        imsak = view.findViewById(R.id.imsak);
        sabah = view.findViewById(R.id.sabah);
        oglen = view.findViewById(R.id.oglen);
        activity = (Activity) view.getContext();
        view.setOnClickListener(this);
    }
}
