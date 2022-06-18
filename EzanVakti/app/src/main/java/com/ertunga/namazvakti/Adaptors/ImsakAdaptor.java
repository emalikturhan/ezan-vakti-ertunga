package com.ertunga.namazvakti.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.Holder.ImsakHolder;
import com.ertunga.namazvakti.Lists.ImsakList;
import com.ertunga.namazvakti.R;

import java.util.List;

public class ImsakAdaptor extends RecyclerView.Adapter<ImsakHolder>{

    private List<ImsakList> imsakList;
    int index = -1;

    public ImsakAdaptor(List<ImsakList> imsakList){ this.imsakList = imsakList; }

    @Override
    public ImsakHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.imsak_list, parent, false);
        return new ImsakHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImsakHolder holder, final int i) {
        final ImsakList data = imsakList.get(i);
        holder.gun.setText(String.valueOf(data.getGun()));
        holder.gun_txt.setText(" / "+String.valueOf(data.getGun_txt()));
        holder.ay.setText(String.valueOf(data.getAy()));
        holder.hicri.setText(String.valueOf(data.getHicri()));
        holder.imsak.setText(String.valueOf(data.getImsak()));
        holder.sabah.setText(String.valueOf(data.getSabah()));
        holder.oglen.setText(String.valueOf(data.getOglen()));
        holder.aksam.setText(String.valueOf(data.getAksam()));
    }

    @Override
    public int getItemCount() {
        return imsakList.size();
    }
}
