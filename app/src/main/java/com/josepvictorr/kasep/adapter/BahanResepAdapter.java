package com.josepvictorr.kasep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josepvictorr.kasep.R;

import java.util.List;

public class BahanResepAdapter extends RecyclerView.Adapter <BahanResepAdapter.BahanResepHolder> {
    List<String> listBahanResep;

    public BahanResepAdapter(List<String> listBahan){
        this.listBahanResep = listBahan;
    }

    @NonNull
    @Override
    public BahanResepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bahan_resep, parent, false);
        return new BahanResepHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BahanResepHolder holder, int position) {
        holder.tvNamaBahanResep.setText(listBahanResep.get(position));
    }

    @Override
    public int getItemCount() {
        return listBahanResep.size();
    }


    public class BahanResepHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBahanResep;
        public BahanResepHolder(View itemView) {
            super(itemView);

            tvNamaBahanResep = itemView.findViewById(R.id.tvNamaBahanResep);
        }
    }
}

