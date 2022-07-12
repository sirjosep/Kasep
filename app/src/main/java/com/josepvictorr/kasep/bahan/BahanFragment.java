package com.josepvictorr.kasep.bahan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanAdapter;
import com.josepvictorr.kasep.adapter.RecipeAdapter;

public class BahanFragment extends Fragment {

    Context mContext;
    RecyclerView rvBahan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bahan, container, false);
        rvBahan = rootView.findViewById(R.id.rvBahan);
        mContext = rootView.getContext();
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getContext());
        rvBahan.setLayoutManager(mLayoutManger);
        rvBahan.setAdapter(new BahanAdapter(mContext));
        rvBahan.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }
}