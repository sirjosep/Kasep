package com.josepvictorr.kasep.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.util.apihelper.BaseApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog loading;

    String namaResep1;
    String namaResep2;
    String namaResep3;
    String namaResep4;
    String namaResep5;
    String namaResep6;
    String namaResep7;
    String namaResep8;
    String namaResep9;
    String namaResep10;

    String key1 = "resep-sate-kerang-bumbu-kecap-pedas";
    String key2 = "resep-sop-buntut-asam-pedas";
    String key3 = "resep-ayam-goreng-sayur-godog";
    String key4 = "resep-sukiyaki-praktis";
    String key5 = "resep-nasi-goreng-kambing-kebuli";
    String key6 = "resep-ayam-goreng-serundeng-kelapa";
    String key7 = "resep-kalio-ayam-kacang-merah";
    String key8 = "resep-misoa-kuah-jamur-udang";
    String key9 = "resep-sambal-korek-daun-jeruk";
    String key10 = "tumis-kangkung-spesial";

    List<String> bahanResep1;
    List<String> bahanResep2;
    List<String> bahanResep3;
    List<String> bahanResep4;
    List<String> bahanResep5;
    List<String> bahanResep6;
    List<String> bahanResep7;
    List<String> bahanResep8;
    List<String> bahanResep9;
    List<String> bahanResep10;
    List<Float> listHasilJaccard = new ArrayList<>();

    int hasilBahanSamaResep1;
    int hasilBahanTidakSamaDariResep1;
    int hasilBahanTidakSamaDariUserResep1;
    float hasilJaccardSimilarityResep1;

    int hasilBahanSamaResep2;
    int hasilBahanTidakSamaDariResep2;
    int hasilBahanTidakSamaDariUserResep2;
    float hasilJaccardSimilarityResep2;

    int hasilBahanSamaResep3;
    int hasilBahanTidakSamaDariResep3;
    int hasilBahanTidakSamaDariUserResep3;
    float hasilJaccardSimilarityResep3;

    int hasilBahanSamaResep4;
    int hasilBahanTidakSamaDariResep4;
    int hasilBahanTidakSamaDariUserResep4;
    float hasilJaccardSimilarityResep4;

    int hasilBahanSamaResep5;
    int hasilBahanTidakSamaDariResep5;
    int hasilBahanTidakSamaDariUserResep5;
    float hasilJaccardSimilarityResep5;

    int hasilBahanSamaResep6;
    int hasilBahanTidakSamaDariResep6;
    int hasilBahanTidakSamaDariUserResep6;
    float hasilJaccardSimilarityResep6;

    int hasilBahanSamaResep7;
    int hasilBahanTidakSamaDariResep7;
    int hasilBahanTidakSamaDariUserResep7;
    float hasilJaccardSimilarityResep7;

    int hasilBahanSamaResep8;
    int hasilBahanTidakSamaDariResep8;
    int hasilBahanTidakSamaDariUserResep8;
    float hasilJaccardSimilarityResep8;

    int hasilBahanSamaResep9;
    int hasilBahanTidakSamaDariResep9;
    int hasilBahanTidakSamaDariUserResep9;
    float hasilJaccardSimilarityResep9;

    int hasilBahanSamaResep10;
    int hasilBahanTidakSamaDariResep10;
    int hasilBahanTidakSamaDariUserResep10;
    float hasilJaccardSimilarityResep10;

    EditText etBahan1;
    EditText etBahan2;
    EditText etBahan3;
    EditText etBahan4;

    String bahan1;
    String bahan2;
    String bahan3;
    String bahan4;

    BaseApiService mApiService;
    RecyclerView rvBahanResepRekomendasi;
    BahanResepAdapter bahanResepAdapter;
    Button btnRecommendMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        mApiService = UtilsApi.getApiService();
        btnRecommendMe = findViewById(R.id.btnRecommendMe);
        rvBahanResepRekomendasi = findViewById(R.id.rvBahanResepRekomendasi);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvBahanResepRekomendasi.setLayoutManager(mLayoutManger);
        rvBahanResepRekomendasi.setItemAnimator(new DefaultItemAnimator());

        getBahan();
        btnRecommendMe.setOnClickListener(this);
    }

    private void getBahan() {
        loading = ProgressDialog.show(this, null, "Loading...", true, true);
        mApiService.getResponseDetailItem(key1).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep1 = response.body().getResults().getIngredient();
                namaResep1 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key2).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep2 = response.body().getResults().getIngredient();
                namaResep2 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key3).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep3 = response.body().getResults().getIngredient();
                namaResep3 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key4).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep4 = response.body().getResults().getIngredient();
                namaResep4 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key5).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep5 = response.body().getResults().getIngredient();
                namaResep5 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key6).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep6 = response.body().getResults().getIngredient();
                namaResep6 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key7).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep7 = response.body().getResults().getIngredient();
                namaResep7 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key8).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep8 = response.body().getResults().getIngredient();
                namaResep8 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key9).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                bahanResep9 = response.body().getResults().getIngredient();
                namaResep9 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
        mApiService.getResponseDetailItem(key10).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                loading.dismiss();
                bahanResep10 = response.body().getResults().getIngredient();
                namaResep10 = response.body().getResults().getTitle();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        TextView tvJudulRekomendasi = findViewById(R.id.tvJudulRekomendasi);
        TextView tvSkorJaccard = findViewById(R.id.tvJudulJaccard);
        etBahan1 = findViewById(R.id.etBahan1);
        etBahan2 = findViewById(R.id.etBahan2);
        etBahan3 = findViewById(R.id.etBahan3);
        etBahan4 = findViewById(R.id.etBahan4);

        listHasilJaccard.clear();
        tvJudulRekomendasi.setText("");
        tvSkorJaccard.setText("");

        bahan1 = etBahan1.getText().toString();
        bahan2 = etBahan2.getText().toString();
        bahan3 = etBahan3.getText().toString();
        bahan4 = etBahan4.getText().toString();

        hitungResep1();
        hitungResep2();
        hitungResep3();
        hitungResep4();
        hitungResep5();
        hitungResep6();
        hitungResep7();
        hitungResep8();
        hitungResep9();
        hitungResep10();

        String hasilTertinggi = String.valueOf(Collections.max(listHasilJaccard));

        if (hasilTertinggi.equals("0.0")){
            tvJudulRekomendasi.setText("Tidak ada Resep yang dapat direkomendasikan");
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep1))) {
            tvJudulRekomendasi.setText(namaResep1);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep1);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if(hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep2))) {
            tvJudulRekomendasi.setText(namaResep2);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep2);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep3))) {
            tvJudulRekomendasi.setText(namaResep3);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep3);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep4))) {
            tvJudulRekomendasi.setText(namaResep4);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep4);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep5))) {
            tvJudulRekomendasi.setText(namaResep5);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep5);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep6))) {
            tvJudulRekomendasi.setText(namaResep6);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep6);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep7))) {
            tvJudulRekomendasi.setText(namaResep7);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep7);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep8))) {
            tvJudulRekomendasi.setText(namaResep8);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep8);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep9))) {
            tvJudulRekomendasi.setText(namaResep9);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep9);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        } else if (hasilTertinggi.equals(String.valueOf(hasilJaccardSimilarityResep10))) {
            tvJudulRekomendasi.setText(namaResep10);
            tvSkorJaccard.setText("Tingkat Kemiripan : " + hasilTertinggi);
            bahanResepAdapter = new BahanResepAdapter(bahanResep10);
            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
            bahanResepAdapter.notifyDataSetChanged();
        }
    }

    private void hitungResep1() {
        List<Boolean> hasilTrueBahan1 = new ArrayList<>();
        List<Boolean> hasilFalseBahan1 = new ArrayList<>();
        for (String bahanResep1Check : bahanResep1) {
            if (bahanResep1Check.contains(bahan1) || bahanResep1Check.contains(bahan2) ||
                    bahanResep1Check.contains(bahan3) || bahanResep1Check.contains(bahan4)) {
                hasilTrueBahan1.add(bahanResep1Check.contains(bahan1)
                        || bahanResep1Check.contains(bahan2)
                        || bahanResep1Check.contains(bahan3) || bahanResep1Check.contains(bahan4));
            } else {
                hasilFalseBahan1.add(bahanResep1Check.contains(bahan1)
                        || bahanResep1Check.contains(bahan2)
                        || bahanResep1Check.contains(bahan3) || bahanResep1Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep1 = hasilTrueBahan1.size();
        hasilBahanTidakSamaDariResep1 = hasilFalseBahan1.size();
        hasilBahanTidakSamaDariUserResep1 = 4 - hasilBahanSamaResep1;
        hasilJaccardSimilarityResep1 = Math.round(hasilBahanSamaResep1 * 100 / (hasilBahanSamaResep1 + hasilBahanTidakSamaDariResep1 + hasilBahanTidakSamaDariUserResep1));
        listHasilJaccard.add(hasilJaccardSimilarityResep1);
    }

    private void hitungResep2() {
        List<Boolean> hasilTrueBahan2 = new ArrayList<>();
        List<Boolean> hasilFalseBahan2 = new ArrayList<>();
        for (String bahanResep2Check : bahanResep2) {
            if (bahanResep2Check.contains(bahan1) || bahanResep2Check.contains(bahan2) ||
                    bahanResep2Check.contains(bahan3) || bahanResep2Check.contains(bahan4)) {
                hasilTrueBahan2.add(bahanResep2Check.contains(bahan1)
                        || bahanResep2Check.contains(bahan2)
                        || bahanResep2Check.contains(bahan3) || bahanResep2Check.contains(bahan4));
            } else {
                hasilFalseBahan2.add(bahanResep2Check.contains(bahan1)
                        || bahanResep2Check.contains(bahan2)
                        || bahanResep2Check.contains(bahan3) || bahanResep2Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep2 = hasilTrueBahan2.size();
        hasilBahanTidakSamaDariResep2 = hasilFalseBahan2.size();
        hasilBahanTidakSamaDariUserResep2 = 4 - hasilBahanSamaResep2;
        hasilJaccardSimilarityResep2 = hasilBahanSamaResep2 * 100 / (hasilBahanSamaResep2 + hasilBahanTidakSamaDariResep2 + hasilBahanTidakSamaDariUserResep2);
        listHasilJaccard.add(hasilJaccardSimilarityResep2);
    }
    private void hitungResep3() {
        List<Boolean> hasilTrueBahan3 = new ArrayList<>();
        List<Boolean> hasilFalseBahan3 = new ArrayList<>();
        for (String bahanResep3Check : bahanResep3) {
            if (bahanResep3Check.contains(bahan1) || bahanResep3Check.contains(bahan2) ||
                    bahanResep3Check.contains(bahan3) || bahanResep3Check.contains(bahan4)) {
                hasilTrueBahan3.add(bahanResep3Check.contains(bahan1)
                        || bahanResep3Check.contains(bahan2)
                        || bahanResep3Check.contains(bahan3) || bahanResep3Check.contains(bahan4));
            } else {
                hasilFalseBahan3.add(bahanResep3Check.contains(bahan1)
                        || bahanResep3Check.contains(bahan2)
                        || bahanResep3Check.contains(bahan3) || bahanResep3Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep3 = hasilTrueBahan3.size();
        hasilBahanTidakSamaDariResep3 = hasilFalseBahan3.size();
        hasilBahanTidakSamaDariUserResep3 = 4 - hasilBahanSamaResep3;
        hasilJaccardSimilarityResep3 = hasilBahanSamaResep3 * 100 / (hasilBahanSamaResep3 + hasilBahanTidakSamaDariResep3 + hasilBahanTidakSamaDariUserResep3);
        listHasilJaccard.add(hasilJaccardSimilarityResep3);
    }

    private void hitungResep4() {
        List<Boolean> hasilTrueBahan4 = new ArrayList<>();
        List<Boolean> hasilFalseBahan4 = new ArrayList<>();
        for (String bahanResep4Check : bahanResep4) {
            if (bahanResep4Check.contains(bahan1) || bahanResep4Check.contains(bahan2) ||
                    bahanResep4Check.contains(bahan3) || bahanResep4Check.contains(bahan4)) {
                hasilTrueBahan4.add(bahanResep4Check.contains(bahan1)
                        || bahanResep4Check.contains(bahan2)
                        || bahanResep4Check.contains(bahan3) || bahanResep4Check.contains(bahan4));
            } else {
                hasilFalseBahan4.add(bahanResep4Check.contains(bahan1)
                        || bahanResep4Check.contains(bahan2)
                        || bahanResep4Check.contains(bahan3) || bahanResep4Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep4 = hasilTrueBahan4.size();
        hasilBahanTidakSamaDariResep4 = hasilFalseBahan4.size();
        hasilBahanTidakSamaDariUserResep4 = 4 - hasilBahanSamaResep4;
        hasilJaccardSimilarityResep4 = hasilBahanSamaResep4 * 100 / (hasilBahanSamaResep4 + hasilBahanTidakSamaDariResep4 + hasilBahanTidakSamaDariUserResep4);
        listHasilJaccard.add(hasilJaccardSimilarityResep4);
    }

    private void hitungResep5() {
        List<Boolean> hasilTrueBahan5 = new ArrayList<>();
        List<Boolean> hasilFalseBahan5 = new ArrayList<>();
        for (String bahanResep5Check : bahanResep5) {
            if (bahanResep5Check.contains(bahan1) || bahanResep5Check.contains(bahan2) ||
                    bahanResep5Check.contains(bahan3) || bahanResep5Check.contains(bahan4)) {
                hasilTrueBahan5.add(bahanResep5Check.contains(bahan1)
                        || bahanResep5Check.contains(bahan2)
                        || bahanResep5Check.contains(bahan3) || bahanResep5Check.contains(bahan4));
            } else {
                hasilFalseBahan5.add(bahanResep5Check.contains(bahan1)
                        || bahanResep5Check.contains(bahan2)
                        || bahanResep5Check.contains(bahan3) || bahanResep5Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep5 = hasilTrueBahan5.size();
        hasilBahanTidakSamaDariResep5 = hasilFalseBahan5.size();
        hasilBahanTidakSamaDariUserResep5 = 4 - hasilBahanSamaResep5;
        hasilJaccardSimilarityResep5 = hasilBahanSamaResep5 * 100 / (hasilBahanSamaResep5 + hasilBahanTidakSamaDariResep5 + hasilBahanTidakSamaDariUserResep5);
        listHasilJaccard.add(hasilJaccardSimilarityResep5);
    }
    private void hitungResep6() {
        List<Boolean> hasilTrueBahan6 = new ArrayList<>();
        List<Boolean> hasilFalseBahan6 = new ArrayList<>();
        for (String bahanResep6Check : bahanResep6) {
            if (bahanResep6Check.contains(bahan1) || bahanResep6Check.contains(bahan2) ||
                    bahanResep6Check.contains(bahan3) || bahanResep6Check.contains(bahan4)) {
                hasilTrueBahan6.add(bahanResep6Check.contains(bahan1)
                        || bahanResep6Check.contains(bahan2)
                        || bahanResep6Check.contains(bahan3) || bahanResep6Check.contains(bahan4));
            } else {
                hasilFalseBahan6.add(bahanResep6Check.contains(bahan1)
                        || bahanResep6Check.contains(bahan2)
                        || bahanResep6Check.contains(bahan3) || bahanResep6Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep6 = hasilTrueBahan6.size();
        hasilBahanTidakSamaDariResep6 = hasilFalseBahan6.size();
        hasilBahanTidakSamaDariUserResep6 = 4 - hasilBahanSamaResep6;
        hasilJaccardSimilarityResep6 = Math.round(hasilBahanSamaResep6 * 100 / (hasilBahanSamaResep6 + hasilBahanTidakSamaDariResep6 + hasilBahanTidakSamaDariUserResep6));
        listHasilJaccard.add(hasilJaccardSimilarityResep6);
    }
    private void hitungResep7() {
        List<Boolean> hasilTrueBahan7 = new ArrayList<>();
        List<Boolean> hasilFalseBahan7 = new ArrayList<>();
        for (String bahanResep7Check : bahanResep7) {
            if (bahanResep7Check.contains(bahan1) || bahanResep7Check.contains(bahan2) ||
                    bahanResep7Check.contains(bahan3) || bahanResep7Check.contains(bahan4)) {
                hasilTrueBahan7.add(bahanResep7Check.contains(bahan1)
                        || bahanResep7Check.contains(bahan2)
                        || bahanResep7Check.contains(bahan3) || bahanResep7Check.contains(bahan4));
            } else {
                hasilFalseBahan7.add(bahanResep7Check.contains(bahan1)
                        || bahanResep7Check.contains(bahan2)
                        || bahanResep7Check.contains(bahan3) || bahanResep7Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep7 = hasilTrueBahan7.size();
        hasilBahanTidakSamaDariResep7 = hasilFalseBahan7.size();
        hasilBahanTidakSamaDariUserResep7 = 4 - hasilBahanSamaResep7;
        hasilJaccardSimilarityResep7 = Math.round(hasilBahanSamaResep7 * 100 / (hasilBahanSamaResep7 + hasilBahanTidakSamaDariResep7 + hasilBahanTidakSamaDariUserResep7));
        listHasilJaccard.add(hasilJaccardSimilarityResep7);
    }
    private void hitungResep8() {
        List<Boolean> hasilTrueBahan8 = new ArrayList<>();
        List<Boolean> hasilFalseBahan8 = new ArrayList<>();
        for (String bahanResep8Check : bahanResep8) {
            if (bahanResep8Check.contains(bahan1) || bahanResep8Check.contains(bahan2) ||
                    bahanResep8Check.contains(bahan3) || bahanResep8Check.contains(bahan4)) {
                hasilTrueBahan8.add(bahanResep8Check.contains(bahan1)
                        || bahanResep8Check.contains(bahan2)
                        || bahanResep8Check.contains(bahan3) || bahanResep8Check.contains(bahan4));
            } else {
                hasilFalseBahan8.add(bahanResep8Check.contains(bahan1)
                        || bahanResep8Check.contains(bahan2)
                        || bahanResep8Check.contains(bahan3) || bahanResep8Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep8 = hasilTrueBahan8.size();
        hasilBahanTidakSamaDariResep8 = hasilFalseBahan8.size();
        hasilBahanTidakSamaDariUserResep8 = 4 - hasilBahanSamaResep8;
        hasilJaccardSimilarityResep8 = Math.round(hasilBahanSamaResep8 * 100 / (hasilBahanSamaResep8 + hasilBahanTidakSamaDariResep8 + hasilBahanTidakSamaDariUserResep8));
        listHasilJaccard.add(hasilJaccardSimilarityResep8);
    }
    private void hitungResep9() {
        List<Boolean> hasilTrueBahan9 = new ArrayList<>();
        List<Boolean> hasilFalseBahan9 = new ArrayList<>();
        for (String bahanResep9Check : bahanResep9) {
            if (bahanResep9Check.contains(bahan1) || bahanResep9Check.contains(bahan2) ||
                    bahanResep9Check.contains(bahan3) || bahanResep9Check.contains(bahan4)) {
                hasilTrueBahan9.add(bahanResep9Check.contains(bahan1)
                        || bahanResep9Check.contains(bahan2)
                        || bahanResep9Check.contains(bahan3) || bahanResep9Check.contains(bahan4));
            } else {
                hasilFalseBahan9.add(bahanResep9Check.contains(bahan1)
                        || bahanResep9Check.contains(bahan2)
                        || bahanResep9Check.contains(bahan3) || bahanResep9Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep9 = hasilTrueBahan9.size();
        hasilBahanTidakSamaDariResep9 = hasilFalseBahan9.size();
        hasilBahanTidakSamaDariUserResep9 = 4 - hasilBahanSamaResep9;
        hasilJaccardSimilarityResep9 = Math.round(hasilBahanSamaResep9 * 100 / (hasilBahanSamaResep9 + hasilBahanTidakSamaDariResep9 + hasilBahanTidakSamaDariUserResep9));
        listHasilJaccard.add(hasilJaccardSimilarityResep9);
    }
    private void hitungResep10() {
        List<Boolean> hasilTrueBahan10 = new ArrayList<>();
        List<Boolean> hasilFalseBahan10 = new ArrayList<>();
        for (String bahanResep10Check : bahanResep10) {
            if (bahanResep10Check.contains(bahan1) || bahanResep10Check.contains(bahan2) ||
                    bahanResep10Check.contains(bahan3) || bahanResep10Check.contains(bahan4)) {
                hasilTrueBahan10.add(bahanResep10Check.contains(bahan1)
                        || bahanResep10Check.contains(bahan2)
                        || bahanResep10Check.contains(bahan3) || bahanResep10Check.contains(bahan4));
            } else {
                hasilFalseBahan10.add(bahanResep10Check.contains(bahan1)
                        || bahanResep10Check.contains(bahan2)
                        || bahanResep10Check.contains(bahan3) || bahanResep10Check.contains(bahan4));
            }
        }
        hasilBahanSamaResep10 = hasilTrueBahan10.size();
        hasilBahanTidakSamaDariResep10 = hasilFalseBahan10.size();
        hasilBahanTidakSamaDariUserResep10 = 4 - hasilBahanSamaResep10;
        hasilJaccardSimilarityResep10 = Math.round(hasilBahanSamaResep10 * 100 / (hasilBahanSamaResep10 + hasilBahanTidakSamaDariResep10 + hasilBahanTidakSamaDariUserResep10));
        listHasilJaccard.add(hasilJaccardSimilarityResep10);
    }
}