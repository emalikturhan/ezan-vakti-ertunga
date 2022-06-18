package com.ertunga.namazvakti.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.InterFace.SehirInterFace;
import com.ertunga.namazvakti.Holder.SehirHolder;
import com.ertunga.namazvakti.Lists.SehirList;
import com.ertunga.namazvakti.R;

import java.util.List;

public class SehirAdaptors extends RecyclerView.Adapter<SehirHolder>{

    private List<SehirList> sehirList;
    int index = -1;

    public SehirAdaptors(List<SehirList> sehirList){ this.sehirList = sehirList; }

    @Override
    public SehirHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sehir_list, parent, false);
        return new SehirHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SehirHolder holder, final int i) {
        final SehirList data = sehirList.get(i);
        holder.name.setText(String.valueOf(data.getSehir_adi()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = i;
                notifyDataSetChanged();
                SehirInterFace sehirInterFace = (SehirInterFace) holder.activity;
                sehirInterFace.Sehir(data.getSehir_adi(),data.getSehir_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sehirList.size();
    }
}
