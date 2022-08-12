package com.josepvictorr.kasep.recipe;

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
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.RecipeAdapter;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.model.ResponseResep;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment {
    SwipeRefreshLayout refreshResep;
    ProgressDialog loading;
    RecipeAdapter recipeAdapter;
    MasakApaApiService mApiService;
    Context mContext;
    RecyclerView rvRecipe;

    List<ResponseResepItem> resepItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_recipe, container, false);

        rvRecipe = rootView.findViewById(R.id.rvRecipe);
        mContext = rootView.getContext();
        mApiService = UtilsApi.getApiService();

        refreshResep = rootView.findViewById(R.id.refreshResep);
        refreshResep.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
            private void refreshItem(){
                getResultRecipeList();
                onItemLoad();
            }
            private void onItemLoad() {
                refreshResep.setRefreshing(false);
            }
        });
        recipeAdapter = new RecipeAdapter(getContext(), resepItemList);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        rvRecipe.setLayoutManager(mLayoutManger);
        rvRecipe.setItemAnimator(new DefaultItemAnimator());
        getResultRecipeList();
        return rootView;
    }

    private void getResultRecipeList(){
        loading = ProgressDialog.show(getContext(), null, "Menampilkan Resep...", true, true);
        mApiService.getResponseItem().enqueue(new Callback<ResponseResep>() {
            @Override
            public void onResponse(Call<ResponseResep> call, Response<ResponseResep> response) {
                loading.dismiss();
                final List<ResponseResepItem> responseResepItems = response.body().getResults();

                rvRecipe.setAdapter(new RecipeAdapter(mContext, responseResepItems));
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseResep> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}