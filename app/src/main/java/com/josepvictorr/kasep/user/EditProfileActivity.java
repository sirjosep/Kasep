package com.josepvictorr.kasep.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText etEditNama, etEditEmail, etPwdEdit, etPwdConfirmEdit;
    ProgressDialog loading;
    Context mContext;
    KasepApiService mKasepApiService;
    Button btnEdit;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().hide();

        mContext = this;
        mKasepApiService = UtilsApi.getKasepApiService();

        etEditNama = findViewById(R.id.etEditNama);
        etEditEmail = findViewById(R.id.etEditEmail);
        etPwdEdit = findViewById(R.id.etPwdEdit);
        etPwdConfirmEdit = findViewById(R.id.etPwdConfirmEdit);
        btnEdit = findViewById(R.id.btnEdit);
        prefManager = new PrefManager(this);

        etEditNama.setText(getIntent().getStringExtra("nama"));
        etEditEmail.setText(getIntent().getStringExtra("email"));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanEdit();
            }
        });
    }

    private void simpanEdit() {
        loading = ProgressDialog.show(mContext, null, "Merubah Profile...", true, true);
        mKasepApiService.editProfile(prefManager.getSP_IdUser(), etEditNama.getText().toString(),
                etEditEmail.getText().toString(), etPwdEdit.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    loading.dismiss();
                    Toast.makeText(mContext, "Berhasil menyimpan perubahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}