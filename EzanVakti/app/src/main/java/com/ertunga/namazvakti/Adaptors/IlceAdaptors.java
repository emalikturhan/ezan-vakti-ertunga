package com.ertunga.namazvakti.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.InterFace.IlceInterFace;
import com.ertunga.namazvakti.Holder.IlceHolder;
import com.ertunga.namazvakti.Lists.IlceList;
import com.ertunga.namazvakti.R;

import java.util.List;

public class IlceAdaptors extends RecyclerView.Adapter<IlceHolder>{

    private List<IlceList> ilceList;
    int index = -1;

    public IlceAdaptors(List<IlceList> ilceList){ this.ilceList = ilceList; }

    @Override
    public IlceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sehir_list, parent, false);
        return new IlceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final IlceHolder holder, final int i) {
        final IlceList data = ilceList.get(i);
        holder.name.setText(String.valueOf(data.getIlce_adi()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = i;
                notifyDataSetChanged();
                IlceInterFace ilceInterFace = (IlceInterFace) holder.activity;
                ilceInterFace.Ilce(data.getIlce_adi(),data.getIlce_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ilceList.size();
    }
}
