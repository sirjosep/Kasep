package com.josepvictorr.kasep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josepvictorr.kasep.R;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.MyRecipeHolder> {
    Context mContext;

    public MyRecipeAdapter (Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_recipe, parent, false);
        return new MyRecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeHolder holder, int position) {
        holder.tvJudulRekomendasi.setText("Rekomendasi ");
        holder.tvJumlahResep.setText("Jumlah Resep");
        holder.tvWaktuRekomendasi.setText("Waktu Rekomendasi");
        holder.tvLihatDetailMyRecipe.setText("Lihat Detail");
        holder.btnLihatDetailMyRecipe.setBackgroundResource(R.drawable.btn_oranye);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyRecipeHolder extends RecyclerView.ViewHolder {
        TextView tvJudulRekomendasi;
        TextView tvJumlahResep;
        TextView tvWaktuRekomendasi;
        TextView tvLihatDetailMyRecipe;
        ImageButton btnLihatDetailMyRecipe;
        public MyRecipeHolder(@NonNull View itemView) {
            super(itemView);

            tvJudulRekomendasi = itemView.findViewById(R.id.tvJudulRekomendasi);
            tvJumlahResep = itemView.findViewById(R.id.tvJumlahResep);
            tvWaktuRekomendasi = itemView.findViewById(R.id.tvWaktuRekomendasi);
            tvLihatDetailMyRecipe = itemView.findViewById(R.id.tvLihatDetailMyRecipe);
            btnLihatDetailMyRecipe = itemView.findViewById(R.id.btnLihatDetailMyRecipe);
        }
    }
}
