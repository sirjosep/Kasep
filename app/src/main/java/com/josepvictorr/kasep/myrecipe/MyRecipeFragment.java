package com.josepvictorr.kasep.myrecipe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.MyRecipeAdapter;
import com.josepvictorr.kasep.item.history.ResponseHistoryRekomendasiItem;
import com.josepvictorr.kasep.model.ResponseHistoryRekomendasi;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyRecipeFragment extends Fragment {
    Context mContext;
    KasepApiService mKasepApiService;
    TextView tvEmptyMyRecipe;
    Button btnCariResep;
    ProgressDialog loading;
    PrefManager prefManager;
    RecyclerView rvHistory;
    MyRecipeAdapter myRecipeAdapter;

    List<ResponseHistoryRekomendasiItem> responseHistoryRekomendasiItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_recipe, container, false);
        mContext = rootView.getContext();

        mKasepApiService = UtilsApi.getKasepApiService();
        prefManager = new PrefManager(mContext);

        rvHistory = rootView.findViewById(R.id.rvHistory);

        btnCariResep = rootView.findViewById(R.id.btnCariResep);
        tvEmptyMyRecipe = rootView.findViewById(R.id.tvEmptyMyRecipe);

        btnCariResep.setOnClickListener(view -> {
            Intent myRecipeRecommendIntent = new Intent(mContext, RekomendasiActivity.class);
            mContext.startActivity(myRecipeRecommendIntent);
        });

        myRecipeAdapter = new MyRecipeAdapter(getContext(), responseHistoryRekomendasiItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvHistory.setLayoutManager(mLayoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        requestTampilResep();
        return rootView;
    }

    private void requestTampilResep() {
        loading = ProgressDialog.show(getContext(), null, "Menampilkan History...", true, true);
        mKasepApiService.history(prefManager.getSP_IdUser()).enqueue(new Callback<ResponseHistoryRekomendasi>() {
            @Override
            public void onResponse(Call<ResponseHistoryRekomendasi> call, Response<ResponseHistoryRekomendasi> response) {
                loading.dismiss();
                final List<ResponseHistoryRekomendasiItem> responseHistoryRekomendasiItems = response.body().getHistoryResep();
                if (response.body().getJumlahHistory() > 0){
                    tvEmptyMyRecipe.setVisibility(View.GONE);
                    rvHistory.setAdapter(new MyRecipeAdapter(mContext, responseHistoryRekomendasiItems));
                    myRecipeAdapter.notifyDataSetChanged();
                }
//                tvEmptyMyRecipe.setText
//                        (response.body().getHistoryResep().get(0).getDaftarHasil().getResult().get(0).getKeyResep());
            }

            @Override
            public void onFailure(Call<ResponseHistoryRekomendasi> call, Throwable t) {

            }
        });
    }
}