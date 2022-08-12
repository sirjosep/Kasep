package com.josepvictorr.kasep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.item.ResponseHistoryBahanItem;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.List;


public class BahanAdapter extends RecyclerView.Adapter <BahanAdapter.BahanHolder> {
    Context mContext;
    List<ResponseHistoryBahanItem> responseHistoryBahanItems;

    public BahanAdapter(Context context, List<ResponseHistoryBahanItem> bahanItemList){
        this.mContext = context;
        responseHistoryBahanItems = bahanItemList;
    }

    @NonNull
    @Override
    public BahanAdapter.BahanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bahan, parent, false);
        return new BahanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BahanAdapter.BahanHolder holder, int position) {
        final ResponseHistoryBahanItem responseHistoryBahanItem = responseHistoryBahanItems.get(position);
        RequestOptions myOptions = new RequestOptions()
                .override(600, 200);

        Glide.with(mContext)
                .asBitmap()
                .apply(myOptions)
                .load("http://kasep-api.my.id/api/storage/" + responseHistoryBahanItem.getFotoBahan())
                .into(holder.ivBahan);
        holder.tvNamaBahan.setText(responseHistoryBahanItem.getNamaBahan());
        holder.tvWaktuTerdeteksi.setText("Waktu Terdeteksi : " + responseHistoryBahanItem.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return responseHistoryBahanItems.size();
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
