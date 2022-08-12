package com.josepvictorr.kasep.bahan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanAdapter;
import com.josepvictorr.kasep.adapter.RecipeAdapter;
import com.josepvictorr.kasep.item.ResponseHistoryBahanItem;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.model.ResponseHistoryBahan;
import com.josepvictorr.kasep.model.ResponseResep;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BahanFragment extends Fragment {

    Context mContext;
    RecyclerView rvBahan;
    SwipeRefreshLayout refreshResep;
    ProgressDialog loading;
    BahanAdapter bahanAdapter;
    KasepApiService mKasepApiService;
    PrefManager prefManager;
    TextView tvHistoryNull;
    ImageView ivArrowDown;

    List<ResponseHistoryBahanItem> responseHistoryBahanItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bahan, container, false);
        rvBahan = rootView.findViewById(R.id.rvBahan);
        mContext = rootView.getContext();
        mKasepApiService = UtilsApi.getKasepApiService();
        prefManager = new PrefManager(mContext);
        tvHistoryNull = rootView.findViewById(R.id.tvHistoryNull);
        ivArrowDown = rootView.findViewById(R.id.ivArrowDown);
        bahanAdapter = new BahanAdapter(getContext(), responseHistoryBahanItemList);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        rvBahan.setLayoutManager(mLayoutManger);
        rvBahan.setItemAnimator(new DefaultItemAnimator());
        requestTampilBahan();
        return rootView;

//        refreshResep = rootView.findViewById(R.id.refreshResep);
//        refreshResep.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshItem();
//            }
//            private void refreshItem(){
//                getResultRecipeList();
//                onItemLoad();
//            }
//            private void onItemLoad() {
//                refreshResep.setRefreshing(false);
//            }
//        });
    }

    private void requestTampilBahan() {
        loading = ProgressDialog.show(getContext(), null, "Menampilkan Bahan...", true, true);
        mKasepApiService.getDetailBahan(String.valueOf(prefManager.getSP_IdUser()))
                .enqueue(new Callback<ResponseHistoryBahan>() {
            @Override
            public void onResponse(Call<ResponseHistoryBahan> call, Response<ResponseHistoryBahan> response) {
                loading.dismiss();
                final List<ResponseHistoryBahanItem> responseHistoryBahanItems = response.body().getListBahan();

                if (response.body().getJumlahBahan() > 0) {
                    rvBahan.setVisibility(View.VISIBLE);
                    rvBahan.setAdapter(new BahanAdapter(mContext, responseHistoryBahanItems));
                    bahanAdapter.notifyDataSetChanged();
                } else if (response.body().getJumlahBahan() == 0) {
                    tvHistoryNull.setVisibility(View.VISIBLE);
                    ivArrowDown.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryBahan> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}