package com.josepvictorr.kasep.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.item.bahan.ResponseHistoryHasilDeteksiItem;
import com.josepvictorr.kasep.model.ResponseHistoryBahan;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BahanAdapter extends RecyclerView.Adapter <BahanAdapter.BahanHolder> {
    Context mContext;
    List<ResponseHistoryHasilDeteksiItem> responseHistoryBahanItems;
    KasepApiService mKasepApiService;

    public BahanAdapter(Context context, List<ResponseHistoryHasilDeteksiItem> bahanItemList){
        this.mContext = context;
        this.responseHistoryBahanItems = bahanItemList;
    }

    @NonNull
    @Override
    public BahanAdapter.BahanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bahan, parent, false);
        return new BahanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BahanAdapter.BahanHolder holder, int position) {
        final ResponseHistoryHasilDeteksiItem responseHistoryBahanItem = responseHistoryBahanItems.get(position);
        mKasepApiService = UtilsApi.getKasepApiService();

        RequestOptions myOptions = new RequestOptions()
                .override(600, 200);
        Glide.with(mContext)
                .asBitmap()
                .apply(myOptions)
                .load("http://kasep-api.my.id/api/storage/" + responseHistoryBahanItem.getFotoBahan())
                .into(holder.ivBahan);
        holder.tvNamaBahanUtama.setText(responseHistoryBahanItem.getDaftarHasil().getResult().get(0).getHasilUtama());
        holder.tvNamaBahanLain
                .setText("Beberapa hasil lain : " +
                        responseHistoryBahanItem.getDaftarHasil().getResult().get(0).getHasilLain());
        String dateString = responseHistoryBahanItem.getCreatedAt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        PrettyTime p  = new PrettyTime();
        String timestamp = p.format(convertedDate);
        holder.tvWaktuTerdeteksi.setText("Waktu Terdeteksi : " + timestamp);
        holder.btnDeleteBahan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle("Hapus Bahan");
                    alert.setMessage("Apakah anda yakin akan menghapus bahan?");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mKasepApiService.deleteBahan(
                                    responseHistoryBahanItems.get(holder.getAdapterPosition()).getIdBahan())
                                    .enqueue(new Callback<ResponseHistoryBahan>() {
                                @Override
                                public void onResponse(Call<ResponseHistoryBahan> call, Response<ResponseHistoryBahan> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseHistoryBahan> call, Throwable t) {

                                }
                            });
                            Toast.makeText(mContext, "Berhasil menghapus bahan", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return responseHistoryBahanItems.size();
    }

    public class BahanHolder extends RecyclerView.ViewHolder {
        ImageView ivBahan;
        TextView tvNamaBahanUtama, tvNamaBahanLain, tvWaktuTerdeteksi;
        Button btnDeleteBahan;

        public BahanHolder(@NonNull View itemView) {
            super(itemView);
            ivBahan = itemView.findViewById(R.id.ivBahan);
            tvNamaBahanUtama = itemView.findViewById(R.id.tvNamaBahanUtama);
            tvNamaBahanLain = itemView.findViewById(R.id.tvNamaBahanLain);
            tvWaktuTerdeteksi = itemView.findViewById(R.id.tvWaktuTerdeteksi);
            btnDeleteBahan = itemView.findViewById(R.id.btnDeleteBahan);
        }
    }
    public void removeAt(int position) {
        responseHistoryBahanItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, responseHistoryBahanItems.size());
    }
}
