package com.ertunga.namazvakti.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.InterFace.ListInterface;
import com.ertunga.namazvakti.Holder.OneHolder;
import com.ertunga.namazvakti.Lists.OneList;
import com.ertunga.namazvakti.R;

import java.util.List;

public class SureAdaptor extends RecyclerView.Adapter<OneHolder>{

    private List<OneList> oneList;
    int index = -1;

    public SureAdaptor(List<OneList> oneList){ this.oneList = oneList; }

    @Override
    public OneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_list, parent, false);
        return new OneHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OneHolder holder, final int i) {
        final OneList data = oneList.get(i);
        if(data.getBaslik().length() > 30){
            holder.txt.setText(String.valueOf(data.getBaslik().substring(0,30)+"..."));
        } else {
            holder.txt.setText(String.valueOf(data.getBaslik()));
        }

        holder.icon.setImageResource(data.getIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                ListInterface listInterface = (ListInterface) holder.activity;
                listInterface.Data(data.getBaslik(),data.getData());
            }
        });
    }

    @Override
    public int getItemCount() {
        return oneList.size();
    }
}
