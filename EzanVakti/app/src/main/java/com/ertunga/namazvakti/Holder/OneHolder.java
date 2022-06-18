package com.ertunga.namazvakti.Holder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.R;

public class OneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt;
    public ImageView icon;
    public Activity activity;

    @Override
    public void onClick(View v) {
        // Toast.makeText(v.getContext(),"LOG "+getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public OneHolder(final View view) {
        super(view);
        txt = view.findViewById(R.id.txt);
        icon = view.findViewById(R.id.icon);
        activity = (Activity) view.getContext();
        view.setOnClickListener(this);
    }
}
