package com.ertunga.namazvakti.Holder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.R;

public class IlceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public Activity activity;

    @Override
    public void onClick(View v) {
        // Toast.makeText(v.getContext(),"LOG "+getAdapterPosition(), Toast.LENGTH_LONG).show();
    }

    public IlceHolder(final View view) {
        super(view);
        name = view.findViewById(R.id.name);
        activity = (Activity) view.getContext();
        view.setOnClickListener(this);
    }
}
