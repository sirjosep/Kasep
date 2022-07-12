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


public class BahanAdapter extends RecyclerView.Adapter <BahanAdapter.BahanHolder> {
    Context mContext;
    public BahanAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public BahanAdapter.BahanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bahan, parent, false);
        return new BahanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BahanAdapter.BahanHolder holder, int position) {
        holder.ivBahan.setImageResource(R.drawable.ic_baseline_image_24);
        holder.tvNamaBahan.setText("Nama Bahan");
        holder.tvWaktuTerdeteksi.setText("Waktu Terdeteksi");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class BahanHolder extends RecyclerView.ViewHolder {
        ImageView ivBahan;
        TextView tvNamaBahan;
        TextView tvWaktuTerdeteksi;
        public BahanHolder(@NonNull View itemView) {
            super(itemView);
            ivBahan = itemView.findViewById(R.id.ivBahan);
            tvNamaBahan = itemView.findViewById(R.id.tvNamaBahan);
            tvWaktuTerdeteksi = itemView.findViewById(R.id.tvWaktuTerdeteksi);
        }
    }
}
