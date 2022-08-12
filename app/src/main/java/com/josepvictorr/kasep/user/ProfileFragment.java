package com.josepvictorr.kasep.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;


public class ProfileFragment extends Fragment {
    Button btnEditProfile, btnLogout;
    PrefManager prefManager;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        btnLogout = rootView.findViewById(R.id.btnLogout);

        mContext = rootView.getContext();
        prefManager = new PrefManager(mContext);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Logout berhasil", Toast.LENGTH_SHORT).show();
                prefManager.saveSPBoolean(prefManager.SP_LoginCheck, false);
                startActivity(new Intent(mContext, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
        return rootView;
    }
}