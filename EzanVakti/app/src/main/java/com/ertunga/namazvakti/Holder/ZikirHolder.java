package com.ertunga.namazvakti.Holder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class ZikirHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt,okunan,toplam;
    public ImageView icon;
    public Activity activity;
    public CircularProgressBar progressBar;

    @Override
    public void onClick(View v) {
        // Toast.makeText(v.getContext(),"LOG "+getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public ZikirHolder(final View view) {
        super(view);
        txt = view.findViewById(R.id.txt);
        icon = view.findViewById(R.id.icon);
        activity = (Activity) view.getContext();
        okunan = view.findViewById(R.id.okunan);
        toplam = view.findViewById(R.id.toplam);
        progressBar = view.findViewById(R.id.progress);
        view.setOnClickListener(this);
    }
}
