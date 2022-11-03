package com.josepvictorr.kasep.myrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.adapter.OtherRecommendationAdapter;
import com.josepvictorr.kasep.model.ResponseDetailHistoryRekomendasi;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasilRekomendasiActivity extends AppCompatActivity {
    ProgressDialog loading;
    Context mContext;
    KasepApiService mKasepApiService;
    MasakApaApiService mApiService;
    BahanResepAdapter bahanResepAdapter;
    OtherRecommendationAdapter otherRecommendationAdapter;
    TextView tvDetailJudulRekomendasi, tvDetailJudulJaccard;
    ImageView ivDetailHasilRekomendasi;
    RecyclerView rvDetailBahanResepRekomendasi, rvDetailOtherRecommendation;
    RelativeLayout rlDetailBackgroundResult, rlDetailBackgroundOther;
    Button btnLihatDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_rekomendasi);

        getSupportActionBar().hide();
        mContext = this;
        mKasepApiService = UtilsApi.getKasepApiService();

        mApiService = UtilsApi.getApiService();

        tvDetailJudulRekomendasi = findViewById(R.id.tvDetailJudulRekomendasi);
        tvDetailJudulJaccard = findViewById(R.id.tvDetailJudulJaccard);
        btnLihatDetail = findViewById(R.id.btnLihatDetail);

        rlDetailBackgroundResult = findViewById(R.id.rlDetailBackgroundResult);
        rlDetailBackgroundOther = findViewById(R.id.rlDetailBackgroundOther);

        ivDetailHasilRekomendasi = findViewById(R.id.ivDetailHasilRekomendasi);
        rvDetailBahanResepRekomendasi = findViewById(R.id.rvDetailBahanResepRekomendasi);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvDetailBahanResepRekomendasi.setLayoutManager(mLayoutManger);
        rvDetailBahanResepRekomendasi.setItemAnimator(new DefaultItemAnimator());
        rvDetailBahanResepRekomendasi.setNestedScrollingEnabled(false);

        rvDetailOtherRecommendation = findViewById(R.id.rvDetailOtherRecommendation);
        RecyclerView.LayoutManager otherLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvDetailOtherRecommendation.setLayoutManager(otherLayoutManager);
        rvDetailOtherRecommendation.setItemAnimator(new DefaultItemAnimator());
        rvDetailOtherRecommendation.setNestedScrollingEnabled(false);

        tampilDetailRekomendasi();
    }

    private void tampilDetailRekomendasi() {
        loading = ProgressDialog.show(this, null, "Menampilkan detail...", true, false);
        mKasepApiService.detailHistory(getIntent().getStringExtra("id_hasil")).enqueue(new Callback<ResponseDetailHistoryRekomendasi>() {
            @Override
            public void onResponse(Call<ResponseDetailHistoryRekomendasi> call, Response<ResponseDetailHistoryRekomendasi> response) {
                loading.dismiss();
                tvDetailJudulRekomendasi.setText(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getNamaResep());
                tvDetailJudulJaccard.setText(
                        "Tingkat Kemiripan : " +
                                        response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getTingkatKemiripan());
                Glide.with(mContext)
                        .load(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getThumbnail())
                        .into(ivDetailHasilRekomendasi);
                mApiService.getResponseDetailItem(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getKeyResep())
                        .enqueue(new Callback<ResponseDetailResep>() {
                            @Override
                            public void onResponse(Call<ResponseDetailResep> call1, Response<ResponseDetailResep> response1) {
                                loading.dismiss();
                                bahanResepAdapter = new BahanResepAdapter(response1.body().getResults().getIngredient());
                                rvDetailBahanResepRekomendasi.setAdapter(bahanResepAdapter);
                                bahanResepAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<ResponseDetailResep> call1, Throwable tt) {

                            }
                        });
                List<String> listKey, listNama, listThumbnail;
                List<Float> listHasilJaccard;

                listKey = new ArrayList<>();
                listNama = new ArrayList<>();
                listThumbnail = new ArrayList<>();
                listHasilJaccard = new ArrayList<>();

                for (int i = 0; i < response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().size(); i++){
                    listKey.add(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(i).getKeyResep());
                    listNama.add(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(i).getNamaResep());
                    listThumbnail.add(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(i).getThumbnail());
                    listHasilJaccard.add(Float.valueOf(response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(i).getTingkatKemiripan()));
                }
                otherRecommendationAdapter = new OtherRecommendationAdapter(
                        mContext, listKey, listNama, listThumbnail, listHasilJaccard
                );
                rvDetailOtherRecommendation.setAdapter(otherRecommendationAdapter);
                otherRecommendationAdapter.notifyDataSetChanged();

                btnLihatDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent detailResepIntent = new Intent(mContext, DetailRecipeActivity.class);
                        detailResepIntent.putExtra("key", response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getKeyResep());
                        detailResepIntent.putExtra("judul", response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getNamaResep());
                        detailResepIntent.putExtra("thumbnail", response.body().getDetailRekomendasi().get(0).getDaftarHasil().getResult().get(0).getThumbnail());
                        startActivity(detailResepIntent);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseDetailHistoryRekomendasi> call, Throwable t) {

            }
        });
    }
}