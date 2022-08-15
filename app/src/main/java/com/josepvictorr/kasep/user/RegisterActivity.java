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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etRegisterNama, etRegisterEmail, etPwdRegister, etPwdConfirmRegister;
    ProgressDialog loading;
    Context mContext;
    KasepApiService mKasepApiService;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        mContext = this;
        mKasepApiService = UtilsApi.getKasepApiService();

        etRegisterNama = findViewById(R.id.etRegisterNama);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etPwdRegister = findViewById(R.id.etPwdRegister);
        etPwdConfirmRegister = findViewById(R.id.etPwdConfirmRegister);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                if (!etPwdRegister.getText().toString().equals(etPwdConfirmRegister.getText().toString())){
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Password tidak sesuai", Toast.LENGTH_SHORT).show();
                } else if (etRegisterNama.getText().toString().equals("")
                        || etRegisterEmail.getText().toString().equals("")
                        || etPwdRegister.getText().toString().equals("")
                        || etPwdConfirmRegister.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "pastikan semua field telah terisi", Toast.LENGTH_SHORT).show();
                } else {
                    requestRegister();
                }
            }
        });
    }

    private void requestRegister() {
        mKasepApiService.registerRequest(etRegisterNama.getText().toString(),
                etRegisterEmail.getText().toString().toLowerCase(), etPwdRegister.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject getResult = new JSONObject(response.body().string());
                            if (!getResult.getString("pesan").equals("email sudah terdaftar")){
                                loading.dismiss();
                                Toast.makeText(mContext, "Register berhasil, silahkan login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, LoginActivity.class));
                            } else {
                                loading.dismiss();
                                Toast.makeText(mContext, getResult.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
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