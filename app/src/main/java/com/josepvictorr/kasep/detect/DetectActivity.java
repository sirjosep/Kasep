package com.josepvictorr.kasep.detect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.Concept;
import com.clarifai.grpc.api.Data;
import com.clarifai.grpc.api.Image;
import com.clarifai.grpc.api.Input;
import com.clarifai.grpc.api.MultiOutputResponse;
import com.clarifai.grpc.api.Output;
import com.clarifai.grpc.api.PostModelOutputsRequest;
import com.clarifai.grpc.api.V2Grpc;
import com.clarifai.grpc.api.status.StatusCode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.protobuf.ByteString;
import com.josepvictorr.kasep.R;
import com.josepvictorr.kasep.model.ResponseHistoryBahan;
import com.josepvictorr.kasep.myrecipe.RekomendasiActivity;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;
import com.josepvictorr.kasep.util.apihelper.KasepApiService;
import com.josepvictorr.kasep.util.apihelper.UtilsApi;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetectActivity extends AppCompatActivity {
    ImageView ivBahanDeteksi;
    ProgressDialog loading;
    TranslatorOptions options = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.INDONESIAN)
            .build();
    final Translator englishIndonesiaTranslator = Translation.getClient(options);

    static final String MODEL_ID = "food-item-recognition";

    Button btnSimpan, btnDeteksiLagi;
    String hasil, hasilTranslated, imageFilePath;
    TextView tvHasilDeteksi, tvWarning;
    KasepApiService mKasepApiService;
    PrefManager prefManager;
    Context mContext;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        getSupportActionBar().hide();
        mKasepApiService = UtilsApi.getKasepApiService();
        prefManager = new PrefManager(this);
        mContext = this;
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        ivBahanDeteksi = findViewById(R.id.ivBahanDeteksi);
        tvHasilDeteksi = findViewById(R.id.tvHasilDeteksi);
        tvWarning = findViewById(R.id.tvWarning);
        requestPostImage();
        Glide.with(this).load(getIntent().getStringExtra("path")).into(ivBahanDeteksi);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        btnDeteksiLagi = findViewById(R.id.btnDeteksiLagi);
        btnDeteksiLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deteksiLagi();
            }
        });
    }

    private void deteksiLagi() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                Intent detectImageIntent = new Intent(this, DetectActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                detectImageIntent.putExtra("path", imageFilePath);
                startActivity(detectImageIntent);
            }
            else if(resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void requestPostImage() {
        loading = ProgressDialog.show(this, null, "Melakukan Deteksi...", true, false);
        Thread mThread = new Thread(){
            @Override
            public void run() {
                V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getJsonChannel())
                        .withCallCredentials(new ClarifaiCallCredentials("cefe649a35374b3190cb7968272afe99"));

                MultiOutputResponse postModelOutputsResponse = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    try {
                        postModelOutputsResponse = stub.postModelOutputs(
                                PostModelOutputsRequest.newBuilder()
                                        .setModelId(MODEL_ID)
                                        .addInputs(
                                                Input.newBuilder().setData(
                                                        Data.newBuilder().setImage(
                                                                Image.newBuilder()
                                                                        .setBase64(ByteString.copyFrom(Files.readAllBytes(
                                                                                new File(getIntent().getStringExtra("path")).toPath()
                                                                        )))
                                                        )
                                                )
                                        )
                                        .build()
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (postModelOutputsResponse.getStatus().getCode() != StatusCode.SUCCESS) {
                    Toast.makeText(DetectActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                Output output = postModelOutputsResponse.getOutputs(0);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        RelativeLayout rlHasilDeteksi = findViewById(R.id.rlHasilDeteksi);
                        rlHasilDeteksi.setVisibility(View.VISIBLE);
                        hasil = output.getData().getConcepts(0).getName();
                        englishIndonesiaTranslator.translate(hasil)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String successTranslate) {
                                        hasilTranslated = successTranslate;
                                        tvHasilDeteksi.setText(successTranslate);
                                        if (output.getData().getConcepts(0).getValue() < 0.90){
                                            tvWarning.setText("*Hasil yang ditampilkan dapat kemungkinan tidak sesuai, " +
                                                    "hal ini dapat disebabkan karena beberapa hal berikut : " + getText(R.string.penyebab) +
                                                    "\n jika hasil dirasa tidak sesuai, silahkan foto ulang dengan menekan tombol deteksi");
                                            tvWarning.setVisibility(View.VISIBLE);
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        tvHasilDeteksi.setText(e.toString());
                                    }
                                });

                        tvHasilDeteksi.setTextColor(getResources().getColor(R.color.black));
                        Log.d("CLARIFAI","Predicted concepts:");
                        for (Concept concept : output.getData().getConceptsList()) {
                            System.out.printf("%s %.2f%n", concept.getName(), concept.getValue());
                            Log.d("CLARIFAI",concept.getName() + " " + concept.getValue());
                        }
                    }
                });
            }
        };
        mThread.start();
    }

    private void uploadFile() {
        loading = ProgressDialog.show(this, null, "Menyimpan...", true, false);

        //pass it like this
        File file = new File(getIntent().getStringExtra("path"));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part foto_bahan =
                MultipartBody.Part.createFormData("foto_bahan", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody id_user =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(prefManager.getSP_IdUser()));

        RequestBody nama_bahan =
                RequestBody.create(MediaType.parse("multipart/form-data"), tvHasilDeteksi.getText().toString());

        Call<ResponseHistoryBahan> call = mKasepApiService.simpanGambar(id_user, foto_bahan, nama_bahan);
        call.enqueue(new Callback<ResponseHistoryBahan>() {
            @Override
            public void onResponse(Call<ResponseHistoryBahan> call, Response<ResponseHistoryBahan> response) {
                loading.dismiss();
                Toast.makeText(mContext, "berhasil menyimpan hasil deteksi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseHistoryBahan> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "terjadi kesalahan, silahkan tekan kembali tombol simpan", Toast.LENGTH_SHORT).show();
                Log.d("TAG", t.getMessage());
            }
        });
    }
}