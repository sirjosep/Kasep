package com.josepvictorr.kasep.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.MyRecipeAdapter;
import com.josepvictorr.kasep.adapter.RecipeAdapter;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;


public class MyRecipeFragment extends Fragment {
    Context mContext;
	RecyclerView rvMyRecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_recipe, container, false);
        mContext = rootView.getContext();
        Button btnCariResep = rootView.findViewById(R.id.btnCariResep);
        btnCariResep.setOnClickListener(view -> {
            Intent myRecipeRecommendIntent = new Intent(mContext, RekomendasiActivity.class);
            mContext.startActivity(myRecipeRecommendIntent);
        });
        return rootView;
    }
}