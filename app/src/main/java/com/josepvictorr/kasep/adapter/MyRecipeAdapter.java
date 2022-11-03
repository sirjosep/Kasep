package com.josepvictorr.kasep.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.item.history.ResponseHistoryRekomendasiItem;
import com.josepvictorr.kasep.model.ResponseHistoryRekomendasi;
import com.josepvictorr.kasep.myrecipe.HasilRekomendasiActivity;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.MyRecipeHolder> {
    List<ResponseHistoryRekomendasiItem> responseHistoryRekomendasiItems;
    Context mContext;
    KasepApiService mKasepApiService;

    public MyRecipeAdapter (Context context, List<ResponseHistoryRekomendasiItem> historyRekomendasiItems){
        this.responseHistoryRekomendasiItems = historyRekomendasiItems;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_rekomendasi, parent, false);
        return new MyRecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeHolder holder, int position) {
        final ResponseHistoryRekomendasiItem responseHistoryRekomendasiItem =
                responseHistoryRekomendasiItems.get(position);

        mKasepApiService = UtilsApi.getKasepApiService();
        holder.tvHashtagHasilRekomendasi.setText("#HasilRekomendasi" + responseHistoryRekomendasiItems.get(position).getIdHasil());
        holder.tvNamaResepHistory.setText(responseHistoryRekomendasiItems.get(position).getDaftarHasil().getResult().get(0).getNamaResep());
        holder.tvBahanHistory.setText("Bahan yang dicari : " + responseHistoryRekomendasiItems.get(position).getDaftarHasil().getResult().get(0).getBahan());
        holder.tvTingkatKemiripanHistory.setText("Tingkat Kemiripan : " + responseHistoryRekomendasiItems.get(position).getDaftarHasil().getResult().get(0).getTingkatKemiripan());
        holder.tvTimestamp.setText(responseHistoryRekomendasiItems.get(position).getCreatedAt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailRekomendasiIntent = new Intent(mContext, HasilRekomendasiActivity.class);
                detailRekomendasiIntent.putExtra("id_hasil", responseHistoryRekomendasiItem.getIdHasil());
                mContext.startActivity(detailRekomendasiIntent);
            }
        });
        holder.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Hapus History");
                alert.setMessage("Apakah anda yakin akan menghapus history?");
                Log.d(TAG, responseHistoryRekomendasiItems.get(holder.getAdapterPosition()).getIdHasil());
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mKasepApiService.deleteHistory
                                (responseHistoryRekomendasiItems.get(holder.getAdapterPosition()).getIdHasil())
                                .enqueue(new Callback<ResponseHistoryRekomendasi>() {
                                    @Override
                                    public void onResponse(Call<ResponseHistoryRekomendasi> call, Response<ResponseHistoryRekomendasi> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseHistoryRekomendasi> call, Throwable t) {

                                    }
                                });
                        Toast.makeText(mContext, "Berhasil menghapus history", Toast.LENGTH_SHORT).show();
                        removeAt(holder.getAdapterPosition());
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    private void removeAt(int position) {
        responseHistoryRekomendasiItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, responseHistoryRekomendasiItems.size());
    }

    @Override
    public int getItemCount() {
        return responseHistoryRekomendasiItems.size();
    }

    public class MyRecipeHolder extends RecyclerView.ViewHolder {
        TextView tvHashtagHasilRekomendasi, tvNamaResepHistory, tvBahanHistory, tvTingkatKemiripanHistory, tvTimestamp;
        Button btnHistory;
        public MyRecipeHolder(@NonNull View itemView) {
            super(itemView);

            tvHashtagHasilRekomendasi = itemView.findViewById(R.id.tvHashtagHasilRekomendasi);
            tvNamaResepHistory = itemView.findViewById(R.id.tvNamaResepHistory);
            tvBahanHistory = itemView.findViewById(R.id.tvBahanHistory);
            tvTingkatKemiripanHistory = itemView.findViewById(R.id.tvTingkatKemiripanHistory);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnHistory = itemView.findViewById(R.id.btnHistory);
        }
    }
}
