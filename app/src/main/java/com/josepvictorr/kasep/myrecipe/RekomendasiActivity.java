package com.josepvictorr.kasep.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.item.ResponseDetailItem;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.model.ResponseResep;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiActivity extends AppCompatActivity {
    ProgressDialog loading;
    List<ResponseDetailItem> responseDetailItems;
    int position;
    TextView tvJudulRekomendasi;
    TextView tvSkorJaccard;

    MasakApaApiService mApiService;
    RecyclerView rvBahanResepRekomendasi;
    BahanResepAdapter bahanResepAdapter;
    Button btnRecommendMe;
    RelativeLayout rlBackgroundResult;
    LinearLayout llMasukkanBahan;
    ImageButton btnTambahMasukkanResep, btnHapusMasukkanResep, btnTambahMasukkanResep2;
    EditText etBahan;
    Context mContext;
    List<String> bahanUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        getSupportActionBar().hide();

        mContext = this;
        responseDetailItems = new ArrayList<>();
        mApiService = UtilsApi.getApiService();
        btnRecommendMe = findViewById(R.id.btnRecommendMe);
        rvBahanResepRekomendasi = findViewById(R.id.rvBahanResepRekomendasi);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvBahanResepRekomendasi.setLayoutManager(mLayoutManger);
        rvBahanResepRekomendasi.setItemAnimator(new DefaultItemAnimator());

        tvJudulRekomendasi = findViewById(R.id.tvJudulRekomendasi);
        tvSkorJaccard = findViewById(R.id.tvJudulJaccard);
        rlBackgroundResult = findViewById(R.id.rlBackgroundResult);
        llMasukkanBahan = findViewById(R.id.llMasukkanBahan);
        etBahan = findViewById(R.id.etBahan);
        btnTambahMasukkanResep = findViewById(R.id.btnTambahMasukkanResep);

        btnTambahMasukkanResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahBahan();
            }
        });

        btnRecommendMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(RekomendasiActivity.this, null, "Loading...", true, false);
                mApiService.getResponseItem().enqueue(new Callback<ResponseResep>() {
                    @Override
                    public void onResponse(Call<ResponseResep> call, Response<ResponseResep> response) {
                        recommendMe(response.body().getResults());
                        for (int i = 0; i < llMasukkanBahan.getChildCount(); i++){
                            if (llMasukkanBahan.getChildAt(i) instanceof LinearLayoutCompat) {
                                LinearLayoutCompat ll = (LinearLayoutCompat) llMasukkanBahan.getChildAt(i);
                                for (int j = 0; j < ll.getChildCount(); j++){
                                    if (ll.getChildAt(j) instanceof EditText){
                                        EditText et = (EditText) ll.getChildAt(j);
                                        if (et.getId() == R.id.etBahan) {
                                            Toast.makeText(mContext, ""+et.getText().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseResep> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void tambahBahan() {
        final View layTambahEdit = getLayoutInflater().inflate(R.layout.layout_input_bahan, null, false);
        btnHapusMasukkanResep = layTambahEdit.findViewById(R.id.btnHapusMasukkanResep);
        btnHapusMasukkanResep.setVisibility(View.VISIBLE);
        btnTambahMasukkanResep2 = layTambahEdit.findViewById(R.id.btnTambahMasukkanResep);

        btnTambahMasukkanResep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahBahan();
            }
        });

        btnHapusMasukkanResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(layTambahEdit);
            }
        });
        llMasukkanBahan.addView(layTambahEdit);
    }

    private void removeView(View view) {
        llMasukkanBahan.removeView(view);
    }

    private void recommendMe(List<ResponseResepItem> results) {
        if (results != null && results.size() > 1) {
            responseDetailItems.clear();
            //manggil tiap key resep
            for (ResponseResepItem responseResepItem : results) {
                mApiService.getResponseDetailItem(responseResepItem.getKey()).enqueue(new Callback<ResponseDetailResep>() {
                    @Override
                    public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                        ResponseDetailResep responseDetailResep = response.body();
                        if (responseDetailResep != null) {
                            ResponseDetailItem responseDetailItem = responseDetailResep.getResults();
                            if (responseDetailItem != null) {
                                // dapetin detail resep
                                responseDetailItems.add(responseDetailItem);
                            }
                        }
                        // jika semua detail sudah diambil
                        if (results.size() == responseDetailItems.size()) {
                            String test = "bawang merah";

                            List<String> percobaan = Arrays.asList("bawang merah", "bawang putih", "cabai merah", "ayam");
                            // insialisasi integer untuk perhitungan
                            int hasilBahanSama = 0;
                            int hasilBahanTidakSama = 0;
                            int hasilBahanTidakSamaDariTest = 0;
                            float hasilJaccardSimilarityResep;

                            // list buat nampung daftar bahan dari suatu resep dan nama bahan
                            List<String> bahanResep;
                            List<String> cekNamaSudahTrue = new ArrayList<>();

                            // penampung sementara hasil pengecekan true false
                            List<Boolean> hasilTrue = new ArrayList<>();
                            List<Boolean> hasilFalse = new ArrayList<>();

                            // menghitung jumlah kemiripan atau tidak kemiripan
                            List<Integer> jumlahTrue = new ArrayList<>();
                            List<Integer> jumlahFalse = new ArrayList<>();

                            // menampung hasil perhitungan jaccard
                            List<Float> listHasilJaccard = new ArrayList<>();

                            // menampung bahan hasil rekomendasi
                            List<String> bahanHasilRekomendasi;

                            // dapetin bahan resep
                            for (int i = 0; i <= 9; i++){
                                bahanResep = responseDetailItems.get(i).getIngredient();
                                for (String bahanResepCheck : bahanResep){
                                    // cek true false
                                    for (int j = 0; j <= percobaan.size() - 1; j++){
                                        //jika bahan yang dibanding dengan user belum ada yang sama
                                        if (bahanResepCheck.contains(percobaan.get(j)) && hasilTrue.size() == 0
                                                && cekNamaSudahTrue.size() == 0){
                                            hasilTrue.add(true);
                                            cekNamaSudahTrue.add(percobaan.get(j));
                                        }
                                        //jika bahan user yang dibandingin sudah ada yang sama
                                        else if(bahanResepCheck.contains(percobaan.get(j))
                                                && hasilTrue.size() > 0 && hasilTrue.size() <= percobaan.size()
                                                && cekNamaSudahTrue.size() > 0 && cekNamaSudahTrue.size() <= percobaan.size()
                                                && !cekNamaSudahTrue.contains(percobaan.get(j))) {
                                            hasilTrue.add(true);
                                            cekNamaSudahTrue.add(percobaan.get(j));
                                        }
                                        //jika bahan user yang terakhir tidak memiliki kesamaan
                                        else if (j == percobaan.size() - 1 && !bahanResepCheck.contains(percobaan.get(j))
                                                && !cekNamaSudahTrue.contains(percobaan.get(j))) {
                                            hasilFalse.add(false);
                                        } else if (j == percobaan.size() - 1 && !bahanResepCheck.contains(percobaan.get(j))
                                                && cekNamaSudahTrue.contains(percobaan.get(j))) {
                                            hasilFalse.add(false);
                                        }
                                    }
                                }
                                // hitung hasil true false

                                //jika hasiltrue lebih banyak dari bahan yang diinput user
                                if (hasilTrue.size() > percobaan.size()) {
                                    hasilBahanSama = percobaan.size();
                                } else if (hasilTrue.size() <= percobaan.size()) {
                                    hasilBahanSama = hasilTrue.size();
                                }
                                hasilBahanTidakSama = hasilFalse.size();
                                hasilBahanTidakSamaDariTest = percobaan.size() - hasilBahanSama;
                                hasilJaccardSimilarityResep = Math.round(hasilBahanSama * 100 / (hasilBahanSama + hasilBahanTidakSama + hasilBahanTidakSamaDariTest));

                                // masukkan hasil perhitungan ke penampungan
                                listHasilJaccard.add(hasilJaccardSimilarityResep);
                                jumlahTrue.add(hasilBahanSama);
                                jumlahFalse.add(hasilBahanTidakSama);

                                // clear penampungan sementara untuk perulangan berikutnya
                                hasilTrue.clear();
                                hasilFalse.clear();
                            }

                            // cari nilai tertinggi dan posisi resepnya di list untuk direkomendasikan
                            Float nilaiJaccardTertinggi = Collections.max(listHasilJaccard);
                            position = listHasilJaccard.indexOf(nilaiJaccardTertinggi);

                            String namaResepRekomendasi = responseDetailItems.get(position).getTitle();
                            bahanHasilRekomendasi = responseDetailItems.get(position).getIngredient();

                            tvJudulRekomendasi.setText(namaResepRekomendasi);
                            tvSkorJaccard.setText("Tingkat kemiripan :" + nilaiJaccardTertinggi.toString());
                            bahanResepAdapter = new BahanResepAdapter(bahanHasilRekomendasi);
                            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
                            bahanResepAdapter.notifyDataSetChanged();

                            rlBackgroundResult.setVisibility(View.VISIBLE);
                            loading.dismiss();

                            Log.d("Rekomendasi Resep :", results.get(2).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

                    }
                });
            }
        }
    }

}