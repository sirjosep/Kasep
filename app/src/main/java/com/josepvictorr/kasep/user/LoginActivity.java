package com.josepvictorr.kasep.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.josepvictorr.kasep.HomeActivity;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText etLoginEmail, etPwdLogin;
    ProgressDialog loading;
    Context mContext;
    KasepApiService mKasepApiService;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        mContext = this;
        mKasepApiService = UtilsApi.getKasepApiService();
        prefManager = new PrefManager(this);
        TextView tvDaftarSekarang = findViewById(R.id.tvDaftarSekarang);
        Button btnLogin = findViewById(R.id.btnLogin);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etPwdLogin = findViewById(R.id.etPwdLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                if (!etLoginEmail.getText().toString().equals("") && !etLoginEmail.getText().toString().equals("")){
                    requestLogin();
                } else {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvDaftarSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void requestLogin() {
        mKasepApiService.loginRequest(etLoginEmail.getText().toString(),
                etPwdLogin.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject getResult = new JSONObject(response.body().string());
                                if (getResult.getString("pesan").equals("email tidak terdaftar atau tidak sesuai")) {
                                    Toast.makeText(mContext, getResult.getString("pesan"), Toast.LENGTH_SHORT).show();
                                } else if (getResult.getString("pesan").equals("password salah")) {
                                    Toast.makeText(mContext, getResult.getString("pesan"), Toast.LENGTH_SHORT).show();
                                } else if (getResult.getString("pesan").equals("Berhasil login")) {
                                    Toast.makeText(mContext, "Login berhasil", Toast.LENGTH_SHORT).show();
                                    int id_user = getResult.getInt("id_user");
                                    prefManager.saveSPBoolean(prefManager.SP_LoginCheck, true);
                                    prefManager.saveSPInt(prefManager.SP_IdUser, id_user);
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                }
                            } catch (JSONException e) {
                                loading.dismiss();
                                e.printStackTrace();
                            } catch (IOException e) {
                                loading.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}