package com.josepvictorr.kasep.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.model.ResponseHistoryBahan;
import com.josepvictorr.kasep.model.ResponseProfile;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    Button btnEditProfile, btnLogout;
    PrefManager prefManager;
    Context mContext;
    TextView tvNamaUser, tvEmailUser, tvTanggalBergabung, tvJumlahBahanTerdeteksi;
    KasepApiService mKasepApiService;
    ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        btnLogout = rootView.findViewById(R.id.btnLogout);

        mContext = rootView.getContext();
        prefManager = new PrefManager(mContext);

        tvNamaUser = rootView.findViewById(R.id.tvNamaUser);
        tvEmailUser = rootView.findViewById(R.id.tvEmailUser);
        tvTanggalBergabung = rootView.findViewById(R.id.tvTanggalBergabung);
        tvJumlahBahanTerdeteksi = rootView.findViewById(R.id.tvJumlahBahanTerdeteksi);

        mKasepApiService = UtilsApi.getKasepApiService();

        showProfile();

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

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, EditProfileActivity.class)
                        .putExtra("nama", tvNamaUser.getText().toString())
                        .putExtra("email", tvEmailUser.getText().toString())
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return rootView;
    }

    private void showProfile() {
        loading = ProgressDialog.show(getContext(), null, "Menampilkan Profile...", true, true);
        mKasepApiService.tampilProfile(prefManager.getSP_IdUser()).enqueue(new Callback<ResponseProfile>() {
            @Override
            public void onResponse(Call<ResponseProfile> call, Response<ResponseProfile> response) {
                tvNamaUser.setText(response.body().getProfile().get(0).getNama());
                tvEmailUser.setText(response.body().getProfile().get(0).getEmail());
                tvTanggalBergabung.setText("Tanggal bergabung : " + response.body().getProfile().get(0).getCreatedAt());
            }

            @Override
            public void onFailure(Call<ResponseProfile> call, Throwable t) {

            }
        });
        mKasepApiService.getDetailBahan(String.valueOf(prefManager.getSP_IdUser())).enqueue(new Callback<ResponseHistoryBahan>() {
            @Override
            public void onResponse(Call<ResponseHistoryBahan> call, Response<ResponseHistoryBahan> response) {
                loading.dismiss();
                tvJumlahBahanTerdeteksi.setText("Jumlah bahan terdeteksi : " + response.body().getJumlahBahan());
            }

            @Override
            public void onFailure(Call<ResponseHistoryBahan> call, Throwable t) {

            }
        });
    }
}