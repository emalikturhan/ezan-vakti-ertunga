package com.ertunga.namazvakti.Adaptors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ertunga.namazvakti.InterFace.ZikirInterFace;
import com.ertunga.namazvakti.Db.ZikirDb;
import com.ertunga.namazvakti.Holder.ZikirHolder;
import com.ertunga.namazvakti.Lists.ZikirList;
import com.ertunga.namazvakti.R;

import java.util.List;

public class ZikirAdaptors extends RecyclerView.Adapter<ZikirHolder>{

    private List<ZikirList> zikirList;
    int index = -1;

    public ZikirAdaptors(List<ZikirList> zikirList){ this.zikirList = zikirList; }

    @Override
    public ZikirHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zikir_list, parent, false);
        return new ZikirHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ZikirHolder holder, final int i) {
        final ZikirList data = zikirList.get(i);
        holder.txt.setText(String.valueOf(data.getBaslik()));
        holder.icon.setImageResource(data.getIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                ZikirInterFace zikirInterFace = (ZikirInterFace) holder.activity;
                zikirInterFace.Data(data.getId(),data.getBaslik(),data.getOkunus(),data.getMeali(),data.getArapca(),data.getAdet());
            }
        });

        ZikirDb veritabanı = new ZikirDb(holder.activity);
        SQLiteDatabase db = veritabanı.getWritableDatabase();
        String[] sutunlar = {"Okunan", "Adet", "Toplam", "ZikirId"};
        Cursor cr = db.query("Zikirler", sutunlar, null, null, null, null, null);
        while (cr.moveToNext()) {
            if(data.getId() == cr.getInt(3)){
                holder.okunan.setText(String.valueOf(cr.getInt(0)));
                holder.toplam.setText(String.valueOf(cr.getInt(1)));
                int hesap = (cr.getInt(1)/100);
                int bar   =  (cr.getInt(0)/hesap);
                holder.progressBar.setProgress(bar);


            }
        }

    }

    @Override
    public int getItemCount() {
        return zikirList.size();
    }
}
