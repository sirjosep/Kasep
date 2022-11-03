package com.josepvictorr.kasep.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.adapter.StepResepAdapter;
import com.josepvictorr.kasep.adapter.YoutubeAdapter;
import com.josepvictorr.kasep.item.youtube.ResponseYoutubeItem;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.model.ResponseYoutube;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.apihelper.YoutubeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRecipeActivity extends AppCompatActivity {
    ProgressDialog loading;
    Context mContext;
    MasakApaApiService mApiService;
    YoutubeApiService mYoutubeApiService;
    TextView tvJudulDetailResep, tvWaktuMemasakDetail, tvPorsiDetail, tvKesulitanDetail, tvDeskripsiResep;
    ImageView ivThumbnailDetail;
    BahanResepAdapter bahanResepAdapter;
    StepResepAdapter stepResepAdapter;
    YoutubeAdapter youtubeAdapter;
    RecyclerView rvBahanResep, rvStepResep, rvYoutubePlayer;
    RelativeLayout rlTagsDetail, rlDeskripsi, rlBahan, rlStep, rlYoutube;
    FrameLayout flThumbnailDetail;
    List<ResponseYoutubeItem> responseYoutubeItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        mYoutubeApiService = UtilsApi.getYoutubeApiService();

        flThumbnailDetail = findViewById(R.id.flThumbnailDetail);
        rlTagsDetail = findViewById(R.id.rlTagsDetail);
        rlDeskripsi = findViewById(R.id.rlDeskripsi);
        rlBahan = findViewById(R.id.rlBahan);
        rlStep = findViewById(R.id.rlStep);
        rlYoutube = findViewById(R.id.rlYoutube);

        tvJudulDetailResep = findViewById(R.id.tvJudulDetailResep);
        if (getIntent().hasExtra("judul")){
            tvJudulDetailResep.setText(getIntent().getStringExtra("judul"));
        }

        tvWaktuMemasakDetail = findViewById(R.id.tvWaktuMemasakDetail);
        if (getIntent().hasExtra("waktu_memasak")){
            tvWaktuMemasakDetail.setText(getIntent().getStringExtra("waktu_memasak"));
        }

        tvPorsiDetail = findViewById(R.id.tvPorsiDetail);
        if (getIntent().hasExtra("porsi")){
            tvPorsiDetail.setText(getIntent().getStringExtra("porsi"));
        }

        tvKesulitanDetail = findViewById(R.id.tvKesulitanDetail);
        if (getIntent().hasExtra("kesulitan")){
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
        }

        ivThumbnailDetail = findViewById(R.id.ivThumbnailDetail);
        Glide.with(this)
                .load(getIntent().getStringExtra("thumbnail"))
                .into(ivThumbnailDetail);

        rvBahanResep = findViewById(R.id.rvBahanResep);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvBahanResep.setLayoutManager(mLayoutManger);
        rvBahanResep.setItemAnimator(new DefaultItemAnimator());
        rvBahanResep.setNestedScrollingEnabled(false);

        rvStepResep = findViewById(R.id.rvStepResep);
        RecyclerView.LayoutManager stepLayoutManger = new LinearLayoutManager(this);
        rvStepResep.setLayoutManager(stepLayoutManger);
        rvStepResep.setItemAnimator(new DefaultItemAnimator());
        rvStepResep.setNestedScrollingEnabled(false);

        rvYoutubePlayer = findViewById(R.id.rvYoutubePlayer);
        RecyclerView.LayoutManager youtubeLayoutManager = new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false);
        rvYoutubePlayer.setLayoutManager(youtubeLayoutManager);
        rvYoutubePlayer.setItemAnimator(new DefaultItemAnimator());
        rvYoutubePlayer.setNestedScrollingEnabled(false);

        youtubeAdapter = new YoutubeAdapter(mContext, responseYoutubeItemList);

        loading = ProgressDialog.show(this, null, "Menampilkan detail...", true, false);

        getDetailResult();
        getYoutubeResult();

    }

    private void getYoutubeResult() {
        mYoutubeApiService.getYoutubeViedos(
                "snippet", "10", getIntent().getStringExtra("judul"),
                "video", "AIzaSyCm7zR5RSRbvQ2w-FAT6coyHyi3jnNiSFI")
                .enqueue(new Callback<ResponseYoutube>() {
                    @Override
                    public void onResponse(Call<ResponseYoutube> call, Response<ResponseYoutube> response) {
                        loading.dismiss();
                        final List<ResponseYoutubeItem> responseYoutubeItems = response.body().getItems();

                        rvYoutubePlayer.setAdapter(new YoutubeAdapter(mContext, responseYoutubeItems));
                        youtubeAdapter.notifyDataSetChanged();
                        rlYoutube.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseYoutube> call, Throwable t) {

                    }
                });
    }

    private void getDetailResult() {
        String key = getIntent().getStringExtra("key");
        mApiService.getResponseDetailItem(key).enqueue(new Callback<ResponseDetailResep>() {
            @Override
            public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                    tvDeskripsiResep = findViewById(R.id.tvDeskripsiResep);
                    tvDeskripsiResep.setText(response.body().getResults().getDesc());
                    bahanResepAdapter = new BahanResepAdapter(response.body().getResults().getIngredient());
                    stepResepAdapter = new StepResepAdapter(response.body().getResults().getStep());

                    rvBahanResep.setAdapter(bahanResepAdapter);
                    rvStepResep.setAdapter(stepResepAdapter);

                    stepResepAdapter.notifyDataSetChanged();
                    bahanResepAdapter.notifyDataSetChanged();

                    flThumbnailDetail.setVisibility(View.VISIBLE);
                    rlTagsDetail.setVisibility(View.VISIBLE);
                    rlDeskripsi.setVisibility(View.VISIBLE);
                    rlBahan.setVisibility(View.VISIBLE);
                    rlStep.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}