package com.josepvictorr.kasep.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.adapter.StepResepAdapter;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.util.apihelper.BaseApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRecipeActivity extends AppCompatActivity {
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    TextView tvJudulDetailResep;
    TextView tvWaktuMemasakDetail;
    TextView tvPorsiDetail;
    TextView tvKesulitanDetail;
    TextView tvDeskripsiResep;
    ImageView ivThumbnailDetail;
    BahanResepAdapter bahanResepAdapter;
    StepResepAdapter stepResepAdapter;
    RecyclerView rvBahanResep;
    RecyclerView rvStepResep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();

        tvJudulDetailResep = findViewById(R.id.tvJudulDetailResep);
        tvJudulDetailResep.setText(getIntent().getStringExtra("judul"));

        tvWaktuMemasakDetail = findViewById(R.id.tvWaktuMemasakDetail);
        tvWaktuMemasakDetail.setText(getIntent().getStringExtra("waktu_memasak"));

        tvPorsiDetail = findViewById(R.id.tvPorsiDetail);
        tvPorsiDetail.setText(getIntent().getStringExtra("porsi"));

        tvKesulitanDetail = findViewById(R.id.tvKesulitanDetail);
        String cekKesulitan = getIntent().getStringExtra("kesulitan");
        if (cekKesulitan.equals("Mudah")){
            tvKesulitanDetail.setBackground(getResources().getDrawable(R.drawable.hijau_mudah));
            tvKesulitanDetail.setText(cekKesulitan);
        } else if (cekKesulitan.equals("Cukup Rumit")){
            tvKesulitanDetail.setBackground(getResources().getDrawable(R.drawable.kuning_cukup_rumit));
            tvKesulitanDetail.setText(cekKesulitan);
        } else if (cekKesulitan.equals("Level Chef Panji")){
            tvKesulitanDetail.setBackground(getResources().getDrawable(R.drawable.merah_chef_panji));
            tvKesulitanDetail.setText(cekKesulitan);
        } else {
            tvKesulitanDetail.setText(cekKesulitan);
        }

        ivThumbnailDetail = findViewById(R.id.ivThumbnailDetail);
        Glide.with(this)
                .load(getIntent().getStringExtra("thumbnail"))
                .into(ivThumbnailDetail);

        rvBahanResep = findViewById(R.id.rvBahanResep);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvBahanResep.setLayoutManager(mLayoutManger);
        rvBahanResep.setItemAnimator(new DefaultItemAnimator());

        rvStepResep = findViewById(R.id.rvStepResep);
        RecyclerView.LayoutManager stepLayoutManger = new LinearLayoutManager(this);
        rvStepResep.setLayoutManager(stepLayoutManger);
        rvStepResep.setItemAnimator(new DefaultItemAnimator());
        getDetailResult();
    }

    private void getDetailResult() {
        String key = getIntent().getStringExtra("key");
        loading = ProgressDialog.show(this, null, "Menampilkan detail...", true, false);
        mApiService.getResponseDetailItem(key).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                    loading.dismiss();
                    tvDeskripsiResep = findViewById(R.id.tvDeskripsiResep);
                    tvDeskripsiResep.setText(response.body().getResults().getDesc());
                    bahanResepAdapter = new BahanResepAdapter(response.body().getResults().getIngredient());
                    stepResepAdapter = new StepResepAdapter(response.body().getResults().getStep());
                    rvBahanResep.setAdapter(bahanResepAdapter);
                    rvStepResep.setAdapter(stepResepAdapter);
                    stepResepAdapter.notifyDataSetChanged();
                    bahanResepAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
    }
}