package com.josepvictorr.kasep.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.model.ResponseResep;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    List<ResponseResepItem> responseResepItems;
    Context mContext;

    public RecipeAdapter(Context context, List<ResponseResepItem> recipeList){
        this.mContext = context;
        responseResepItems = recipeList;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resep, parent, false);
        return new RecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        final ResponseResepItem responseResepItem = responseResepItems.get(position);
        Glide.with(mContext)
                .load(responseResepItem.getThumb())
                .into(holder.ivPreviewResep);
        holder.tvNamaResep.setText(responseResepItem.getTitle());
        holder.tvWaktuMemasak.setText(responseResepItem.getTimes());
        holder.tvPorsi.setText(responseResepItem.getPortion());
        String cekKesulitan = responseResepItem.getDificulty();
        if (cekKesulitan.equals("Mudah")){
            holder.tvKesulitan.setBackground(mContext.getResources().getDrawable(R.drawable.hijau_mudah));
            holder.tvKesulitan.setText(responseResepItem.getDificulty());
        } else if (cekKesulitan.equals("Cukup Rumit")){
            holder.tvKesulitan.setBackground(mContext.getResources().getDrawable(R.drawable.kuning_cukup_rumit));
            holder.tvKesulitan.setText(responseResepItem.getDificulty());
        } else if (cekKesulitan.equals("Level Chef Panji")){
            holder.tvKesulitan.setBackground(mContext.getResources().getDrawable(R.drawable.merah_chef_panji));
            holder.tvKesulitan.setText(responseResepItem.getDificulty());
        } else {
            holder.tvKesulitan.setText(responseResepItem.getDificulty());
        }

        holder.itemView.setOnClickListener(view -> {
            Intent detailResepIntent = new Intent(mContext, DetailRecipeActivity.class);
            detailResepIntent.putExtra("key", responseResepItem.getKey());
            detailResepIntent.putExtra("judul", responseResepItem.getTitle());
            detailResepIntent.putExtra("thumbnail", responseResepItem.getThumb());
            detailResepIntent.putExtra("waktu_memasak", responseResepItem.getTimes());
            detailResepIntent.putExtra("porsi", responseResepItem.getPortion());
            detailResepIntent.putExtra("kesulitan", responseResepItem.getDificulty());
            mContext.startActivity(detailResepIntent);
        });
    }

    @Override
    public int getItemCount() {
        return responseResepItems.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder {
        ImageView ivPreviewResep;
        TextView tvNamaResep;
        TextView tvWaktuMemasak;
        TextView tvPorsi;
        TextView tvKesulitan;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);
            ivPreviewResep = itemView.findViewById(R.id.ivPreviewResep);
            tvNamaResep = itemView.findViewById(R.id.tvNamaResep);
            tvWaktuMemasak = itemView.findViewById(R.id.tvWaktuMemasak);
            tvPorsi = itemView.findViewById(R.id.tvPorsi);
            tvKesulitan = itemView.findViewById(R.id.tvKesulitan);
        }
    }
}
