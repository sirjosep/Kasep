package com.josepvictorr.kasep.myrecipe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.adapter.BahanResepAdapter;
import com.josepvictorr.kasep.adapter.OtherRecommendationAdapter;
import com.josepvictorr.kasep.detect.DetectActivity;
import com.josepvictorr.kasep.item.ResponseDetailItem;
import com.josepvictorr.kasep.item.ResponseResepItem;
import com.josepvictorr.kasep.model.ResponseDetailResep;
import com.josepvictorr.kasep.model.ResponseHistoryRekomendasi;
import com.josepvictorr.kasep.model.ResponseResep;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.MasakApaApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekomendasiActivity extends AppCompatActivity {
    ProgressDialog loading;
    List<ResponseDetailItem> responseDetailItems;
    int positionMax;
    TextView tvJudulRekomendasi, tvSkorJaccard, tvDaftarBahanUser;
    ImageView ivHasilRekomendasi;
    String imageFilePath;

    KasepApiService mKasepApiService;
    MasakApaApiService mApiService;
    RecyclerView rvBahanResepRekomendasi, rvOtherRecommendation;
    BahanResepAdapter bahanResepAdapter;
    OtherRecommendationAdapter otherRecommendationAdapter;
    Button btnRecommendMe, btnSimpanRekomendasi, btnDetailHasilRekomendasi, btnTambahBahanRekomendasi;
    RelativeLayout rlBackgroundResult, rlBackgroundOther, rlRekomendasi;
    LinearLayout llMasukkanBahan;
    ImageButton btnTambahMasukkanResep, btnHapusMasukkanResep, btnTambahMasukkanResep2;
    EditText etBahan;
    Context mContext;
    List<Float> listHasilJaccardSorted;
    List<String> bahanUser, listKey, listNamaResep, listThumbnail,
            listKeySorted, listNamaResepSorted, listThumbnailSorted;
    PrefManager prefManager;
    CheckBox checkBox;
    List<CheckBox> list;
    int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);

        getSupportActionBar().hide();

        mContext = this;
        responseDetailItems = new ArrayList<>();
        prefManager = new PrefManager(this);

        listKeySorted = new ArrayList<>();
        listKey = new ArrayList<>();
        listNamaResep = new ArrayList<>();
        listThumbnail = new ArrayList<>();
        listNamaResepSorted = new ArrayList<>();
        listThumbnailSorted = new ArrayList<>();
        listHasilJaccardSorted = new ArrayList<>();

        mApiService = UtilsApi.getApiService();
        mKasepApiService = UtilsApi.getKasepApiService();

        btnRecommendMe = findViewById(R.id.btnRecommendMe);
        btnSimpanRekomendasi = findViewById(R.id.btnSimpanRekomendasi);
        btnDetailHasilRekomendasi = findViewById(R.id.btnDetailHasilRekomendasi);

        rvBahanResepRekomendasi = findViewById(R.id.rvBahanResepRekomendasi);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        rvBahanResepRekomendasi.setLayoutManager(mLayoutManger);
        rvBahanResepRekomendasi.setItemAnimator(new DefaultItemAnimator());
        rvBahanResepRekomendasi.setNestedScrollingEnabled(false);

        rvOtherRecommendation = findViewById(R.id.rvOtherRecommendation);
        RecyclerView.LayoutManager otherLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvOtherRecommendation.setLayoutManager(otherLayoutManager);
        rvOtherRecommendation.setItemAnimator(new DefaultItemAnimator());
        rvOtherRecommendation.setNestedScrollingEnabled(false);

        tvJudulRekomendasi = findViewById(R.id.tvJudulRekomendasi);
        tvSkorJaccard = findViewById(R.id.tvJudulJaccard);
        tvDaftarBahanUser = findViewById(R.id.tvDaftarBahanUser);

        ivHasilRekomendasi = findViewById(R.id.ivHasilRekomendasi);

        rlBackgroundResult = findViewById(R.id.rlBackgroundResult);
        rlBackgroundOther = findViewById(R.id.rlBackgroundOther);

        rlRekomendasi = findViewById(R.id.rlRekomendasi);
        llMasukkanBahan = findViewById(R.id.llMasukkanBahan);

        etBahan = findViewById(R.id.etBahan);

        bahanUser = new ArrayList<>();

        btnTambahMasukkanResep = findViewById(R.id.btnTambahMasukkanResep);
        btnTambahBahanRekomendasi = findViewById(R.id.btnTambahBahanRekomendasi);

        LinearLayout check_add_layout = findViewById(R.id.check_add_layout);
        list = new LinkedList<>();
        if (getIntent().hasExtra("hasilDeteksi")) {
            for (int i = 0; i < getIntent().getStringArrayListExtra("hasilDeteksi").size(); i ++){
                checkBox = new CheckBox(this);
                checkBox.setId(i);
                checkBox.setText(getIntent().getStringArrayListExtra("hasilDeteksi").get(i)
                        + "(" + getIntent().getStringArrayListExtra("hasilValue").get(i) + ")" );
                LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                checkParams.setMargins(5, 5, 5, 5);
                list.add(checkBox);
                check_add_layout.addView(checkBox, checkParams);
            }
        } else {
            tvDaftarBahanUser.setVisibility(View.GONE);
            llMasukkanBahan.setVisibility(View.VISIBLE);
            btnTambahBahanRekomendasi.setVisibility(View.GONE);
        }

        btnRecommendMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiService.getResponseItem().enqueue(new Callback<ResponseResep>() {
                    @Override
                    public void onResponse(Call<ResponseResep> call, Response<ResponseResep> response) {
                        if (getIntent().hasExtra("hasilDeteksi")){
                            recommendMe(response.body().getResults());
                        } else {
                            for (int i = 0; i < llMasukkanBahan.getChildCount(); i++){
                                if (llMasukkanBahan.getChildAt(i) instanceof LinearLayoutCompat) {
                                    LinearLayoutCompat ll = (LinearLayoutCompat) llMasukkanBahan.getChildAt(i);
                                    for (int j = 0; j < ll.getChildCount(); j++){
                                        if (ll.getChildAt(j) instanceof EditText){
                                            EditText et = (EditText) ll.getChildAt(j);
                                            if (et.getId() == R.id.etBahan) {
                                                bahanUser.add(et.getText().toString());
                                            }
                                        }
                                    }
                                }
                            }
                            List<Boolean> checkInput = new ArrayList<>();
                            for (int i = 0; i < bahanUser.size(); i++){
                                if (!bahanUser.get(i).equals("")){
                                    checkInput.add(true);
                                } else {
                                    checkInput.add(false);
                                }
                            }
                            if (checkInput.contains(false)) {
                                Toast.makeText(mContext, "Pastikan semua field terisi", Toast.LENGTH_SHORT).show();
                            } else if (checkInput.contains(true)) {
                                recommendMe(response.body().getResults());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseResep> call, Throwable t) {

                    }
                });
            }
        });

        btnSimpanRekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanHasilRekomendasi();
            }
        });

        btnTambahBahanRekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahBahanLagi();
            }
        });

        btnTambahMasukkanResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahInputBahan();
            }
        });
    }
    private void tambahBahanLagi() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putStringArrayListExtra("hasilDeteksi", (ArrayList<String>) bahanUser);
        File photoFile = null;
        try {
            photoFile = createImagePath();
            Uri photoUri = FileProvider.getUriForFile(this, "com.josepvictorr.kasep.provider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImagePath() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> userBahan = getIntent().getStringArrayListExtra("hasilDeteksi");
                Intent detectImageIntent = new Intent(this, DetectActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                detectImageIntent.putExtra("path", imageFilePath);
                detectImageIntent.putStringArrayListExtra("hasilDeteksi", (ArrayList<String>) userBahan);
                startActivity(detectImageIntent);
            }
            else if(resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
    private void simpanHasilRekomendasi() {
        loading = ProgressDialog.show(this, null, "Menyimpan...", true, false);

        JSONObject jResult = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

        for (int i = 0; i < listKeySorted.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object

            try {
                jGroup.put("key_resep", listKeySorted.get(i));
                jGroup.put("thumbnail", listThumbnailSorted.get(i).replaceAll("\\\\",""));
                jGroup.put("nama_resep", listNamaResepSorted.get(i));
                jGroup.put("tingkat_kemiripan", listHasilJaccardSorted.get(i));
                jGroup.put("bahan", bahanUser.toString());

                jArray.put(jGroup);

                // /itemDetail Name is JsonArray Name
                jResult.put("result", jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            Log.d("Sorted", jResult.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mKasepApiService.simpanHistory(prefManager.getSP_IdUser(), jResult.toString(4))
                    .enqueue(new Callback<ResponseHistoryRekomendasi>() {
                @Override
                public void onResponse(Call<ResponseHistoryRekomendasi> call, Response<ResponseHistoryRekomendasi> response) {
                    loading.dismiss();
                    Toast.makeText(mContext, "berhasil menyimpan hasil rekomendasi", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseHistoryRekomendasi> call, Throwable t) {
                    loading.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void tambahInputBahan() {
        final View layTambahEdit = getLayoutInflater().inflate(R.layout.layout_input_bahan, null, false);
        btnHapusMasukkanResep = layTambahEdit.findViewById(R.id.btnHapusMasukkanResep);
        btnHapusMasukkanResep.setVisibility(View.VISIBLE);
        btnTambahMasukkanResep2 = layTambahEdit.findViewById(R.id.btnTambahMasukkanResep);

        btnTambahMasukkanResep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahInputBahan();
            }
        });

        btnHapusMasukkanResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(layTambahEdit);
            }
        });
        llMasukkanBahan.addView(layTambahEdit);
    }

    private void removeView(View view) {
        llMasukkanBahan.removeView(view);
    }

    private void recommendMe(List<ResponseResepItem> results) {
        loading = ProgressDialog.show(RekomendasiActivity.this, null, "Mencari Rekomendasi...", true, false);
        if (results != null && results.size() > 1) {
            //bersihkan terlebih dahulu list
            responseDetailItems.clear();
            listKey.clear();
            listKeySorted.clear();
            listNamaResep.clear();
            listNamaResepSorted.clear();
            listThumbnail.clear();
            listThumbnailSorted.clear();
            listHasilJaccardSorted.clear();
            for (CheckBox cb : list){
                if (cb.isChecked()){
                    bahanUser.add(getIntent().getStringArrayListExtra("hasilDeteksi").get(cb.getId()));
                }
            }
            System.out.println(bahanUser);
            //manggil tiap key resep
            for (ResponseResepItem responseResepItem : results) {
                mApiService.getResponseDetailItem(responseResepItem.getKey()).enqueue(new Callback<ResponseDetailResep>() {
                    @Override
                    public void onResponse(Call<ResponseDetailResep> call, Response<ResponseDetailResep> response) {
                        ResponseDetailResep responseDetailResep = response.body();
                        if (responseDetailResep != null) {
                            ResponseDetailItem responseDetailItem = responseDetailResep.getResults();
                            if (responseDetailItem != null) {
                                // dapetin detail resep
                                listKey.add(responseResepItem.getKey());
                                listNamaResep.add(responseResepItem.getTitle());
                                listThumbnail.add(responseResepItem.getThumb());
                                responseDetailItems.add(responseDetailItem);
                            }
                        }
                        // jika semua detail sudah diambil
                        if (results.size() == responseDetailItems.size()) {
                            //sample
//                          String test = "bawang merah";
//                          List<String> bahanUser = Arrays.asList("bawang merah", "bawang putih", "cabai merah", "ayam");

                            // insialisasi integer untuk perhitungan
                            int hasilBahanSama = 0;
                            int hasilBahanTidakSama = 0;
                            int hasilBahanTidakSamaDariUser = 0;
                            float hasilJaccardSimilarityResep;

                            // list buat nampung daftar bahan dari suatu resep dan nama bahan
                            List<String> bahanResep;
                            List<String> cekNamaSudahTrue = new ArrayList<>();

                            // penampung sementara hasil pengecekan true false
                            List<Boolean> hasilTrue = new ArrayList<>();
                            List<Boolean> hasilFalse = new ArrayList<>();

                            // menghitung jumlah kemiripan atau tidak kemiripan
                            List<Integer> jumlahTrue = new ArrayList<>();
                            List<Integer> jumlahFalse = new ArrayList<>();

                            // menampung hasil perhitungan jaccard
                            List<Float> listHasilJaccard = new ArrayList<>();
                            List<Float> listHasilJaccardRemoved = new ArrayList<>();

                            // menampung bahan hasil rekomendasi
                            List<String> bahanHasilRekomendasi;
                            List<Integer> listPosisiBahanSama;
                            List<Integer> posisiBahanSama;

                            if (bahanUser.size() == 1) {
                                hasilTrue.clear();
                                // dapetin bahan resep
                                for (int i = 0; i <= 9; i++){
                                    bahanResep = responseDetailItems.get(i).getIngredient();
                                    //perbandingan bahan dari api dan bahan dari user
                                    for (int j= 0; j < bahanResep.size(); j++){
                                        String bahanResepCheck = bahanResep.get(j);
                                        // cek true false
                                        for (int k = 0; k <= bahanUser.size() - 1; k++){
                                            if (bahanResepCheck.toLowerCase().contains(bahanUser.get(k).toLowerCase())) {
                                                hasilTrue.add(true);
                                            }
                                        }
                                    }

                                    hasilBahanSama = hasilTrue.size();
                                    hasilBahanTidakSama = bahanResep.size() - hasilBahanSama;
                                    hasilBahanTidakSamaDariUser = bahanUser.size() - hasilBahanSama;
                                    hasilJaccardSimilarityResep = Math.round(hasilBahanSama * 100 / (hasilBahanSama + hasilBahanTidakSama + hasilBahanTidakSamaDariUser));

                                    // masukkan hasil perhitungan ke penampungan
                                    listHasilJaccard.add(hasilJaccardSimilarityResep);
                                    listHasilJaccardSorted.add(hasilJaccardSimilarityResep);
                                    listHasilJaccardRemoved.add(hasilJaccardSimilarityResep);
                                    jumlahTrue.add(hasilBahanSama);
                                    jumlahFalse.add(hasilBahanTidakSama);

                                    // clear penampungan sementara untuk perulangan berikutnya
                                    hasilTrue.clear();
                                    hasilFalse.clear();
                                }
                            } else if (bahanUser.size() > 1) {
                                for (int i = 0; i <= 9; i++){
                                    bahanResep = responseDetailItems.get(i).getIngredient();
                                    for (int j= 0; j < bahanResep.size(); j++){
                                        String bahanResepCheck = bahanResep.get(j);
                                        // cek true false
                                        for (int k = 0; k <= bahanUser.size() - 1; k++){
                                            if (bahanResepCheck.toLowerCase().contains(bahanUser.get(k).toLowerCase())) {
                                                hasilTrue.add(true);
                                            }
                                        }
                                    }
                                    hasilBahanSama = hasilTrue.size();
                                    hasilBahanTidakSama = bahanResep.size() - hasilBahanSama;
                                    hasilBahanTidakSamaDariUser = bahanUser.size() - hasilBahanSama;
                                    hasilJaccardSimilarityResep = Math.round(hasilBahanSama * 100 / (hasilBahanSama + hasilBahanTidakSama + hasilBahanTidakSamaDariUser));

                                    // masukkan hasil perhitungan ke penampungan
                                    listHasilJaccard.add(hasilJaccardSimilarityResep);
                                    listHasilJaccardSorted.add(hasilJaccardSimilarityResep);
                                    listHasilJaccardRemoved.add(hasilJaccardSimilarityResep);
                                    jumlahTrue.add(hasilBahanSama);
                                    jumlahFalse.add(hasilBahanTidakSama);

                                    // clear penampungan sementara untuk perulangan berikutnya
                                    hasilTrue.clear();
                                    hasilFalse.clear();
                                }
                            }

                            // cari nilai tertinggi dan posisi resepnya di list untuk direkomendasikan
                            Float nilaiJaccardTertinggi = Collections.max(listHasilJaccard);

                            // mengurutkan hasil nilai jaccard
                            Collections.sort(listHasilJaccardSorted);

                            // mencari posisi dari hasil list jaccard yang sudah diurutkan
                            List <Integer> positionSortedList = new ArrayList<>();
                            for (int i = 0; i <= listHasilJaccardSorted.size() - 1; i++){
                                positionSortedList.add(listHasilJaccardRemoved.indexOf(listHasilJaccardSorted.get(i)));
                                listHasilJaccardRemoved.set(listHasilJaccardRemoved.indexOf(listHasilJaccardSorted.get(i)), -1.0F);
                            }

                            //posisi nilai tertinggi
                            positionMax = listHasilJaccard.indexOf(nilaiJaccardTertinggi);

                            // mengurutkan nilai key, nama resep, thumbnail sesuai dengan nilai hasil jaccard yang sudah diurutkan
                            for (int i = 0; i <= positionSortedList.size() - 1; i++){
                                listKeySorted.add(listKey.get(positionSortedList.get(i)));
                                listNamaResepSorted.add(listNamaResep.get(positionSortedList.get(i)));
                                listThumbnailSorted.add(String.valueOf(listThumbnail.get(positionSortedList.get(i))));
                            }

                            // mengurutkan dari yang terbesar
                            Collections.reverse(listKeySorted);
                            Collections.reverse(listThumbnailSorted);
                            Collections.reverse(listNamaResepSorted);
                            Collections.reverse(listHasilJaccardSorted);

                            otherRecommendationAdapter =
                                    new OtherRecommendationAdapter(mContext, listKeySorted, listNamaResepSorted,
                                            listThumbnailSorted, listHasilJaccardSorted);
                            rvOtherRecommendation.setAdapter(otherRecommendationAdapter);
                            otherRecommendationAdapter.notifyDataSetChanged();


                            String namaResepRekomendasi = responseDetailItems.get(positionMax).getTitle();
                            bahanHasilRekomendasi = responseDetailItems.get(positionMax).getIngredient();

                            tvJudulRekomendasi.setText(namaResepRekomendasi);
                            tvSkorJaccard.setText("Tingkat kemiripan :" + nilaiJaccardTertinggi);
                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.drawable.ic_baseline_image_24)
                                    .error(R.mipmap.ic_launcher_round);
                            Glide.with(mContext)
                                    .load(listThumbnailSorted.get(0))
                                    .apply(options)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(ivHasilRekomendasi);
                            bahanResepAdapter = new BahanResepAdapter(bahanHasilRekomendasi);
                            rvBahanResepRekomendasi.setAdapter(bahanResepAdapter);
                            bahanResepAdapter.notifyDataSetChanged();

                            btnDetailHasilRekomendasi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent detailResepIntent = new Intent(mContext, DetailRecipeActivity.class);
                                    detailResepIntent.putExtra("key", listKeySorted.get(0));
                                    detailResepIntent.putExtra("judul", listNamaResepSorted.get(0));
                                    detailResepIntent.putExtra("thumbnail", listThumbnailSorted.get(0));
                                    startActivity(detailResepIntent);
                                }
                            });

                            rlBackgroundResult.setVisibility(View.VISIBLE);
                            rlBackgroundOther.setVisibility(View.VISIBLE);
                            btnSimpanRekomendasi.setVisibility(View.VISIBLE);
                            Log.d("Rekomendasi Resep :", responseDetailItems.get(positionMax).getTitle());
                            loading.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDetailResep> call, Throwable t) {

                    }
                });
            }
        }
    }
}