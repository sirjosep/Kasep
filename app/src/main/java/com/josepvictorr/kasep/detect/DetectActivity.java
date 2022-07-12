package com.josepvictorr.kasep.detect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetectActivity extends AppCompatActivity {
    ImageView ivSampleBahanDeteksi;
    ProgressDialog loading;
    TranslatorOptions options = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.INDONESIAN)
            .build();
    final Translator englishIndonesiaTranslator = Translation.getClient(options);

    static final String MODEL_ID = "food-item-recognition";

    static final String IMAGE_URL = "https://kiranadude.com/wp-content/uploads/2022/03/Cabbage.jpg";

    private static final int REQUEST_CAPTURE_IMAGE = 100;

    String imageFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        ivSampleBahanDeteksi = findViewById(R.id.ivSampleBahanDeteksi);
        openCameraIntent();

//        ivSampleBahanDeteksi = findViewById(R.id.ivSampleBahanDeteksi);
//        Glide.with(this)
//                .load(IMAGE_URL)
//                .into(ivSampleBahanDeteksi);
//
//        Button btnDetect = findViewById(R.id.btnDetect);
//        btnDetect.setOnClickListener(view -> {
//            requestPostImage();
//        });

    }

    private void openCameraIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Toast.makeText(this, "Foto bahan makanan yang ingin diketahui", Toast.LENGTH_SHORT).show();
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImagePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, "com.josepvictorr.kasep.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                requestPostImage();
                Glide.with(this).load(imageFilePath).into(ivSampleBahanDeteksi);
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                // User Cancelled the action
            }
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
    private void requestPostImage() {
        loading = ProgressDialog.show(this, null, "Melakukan Deteksi...", true, false);
        Thread mThread = new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
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
                                                                                        new File(imageFilePath).toPath()
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
                            throw new RuntimeException("Post model outputs failed, status: " + postModelOutputsResponse.getStatus());
                        }
                        loading.dismiss();
                        Output output = postModelOutputsResponse.getOutputs(0);
                        TextView tvHasilDeteksi1 = findViewById(R.id.tvHasilDeteksi1);
                        TextView tvHasilDeteksi2 = findViewById(R.id.tvHasilDeteksi2);
                        TextView tvHasilDeteksi3 = findViewById(R.id.tvHasilDeteksi3);
                        TextView tvHasilDeteksi4 = findViewById(R.id.tvHasilDeteksi4);
                        TextView tvHasilDeteksi5 = findViewById(R.id.tvHasilDeteksi5);

                        String hasil1 = output.getData().getConcepts(0).getName();
                        String hasil2 = output.getData().getConcepts(1).getName();
                        englishIndonesiaTranslator.translate(hasil1)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String successTranslate) {
                                        tvHasilDeteksi2.setText("Hasil 1 (indonesia) = " + successTranslate);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        tvHasilDeteksi2.setText(e.toString());
                                    }
                                });

                        englishIndonesiaTranslator.translate(hasil2)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String successTranslate) {
                                        tvHasilDeteksi4.setText("Hasil 2  (indonesia) = " + successTranslate);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        tvHasilDeteksi4.setText(e.toString());
                                    }
                                });
                        tvHasilDeteksi1.setText("Hasil 1 (Inggris) : " + hasil1);
                        tvHasilDeteksi3.setText("Hasil 2 (Inggris) : " + hasil2);

                        tvHasilDeteksi1.setTextColor(getResources().getColor(R.color.black));
                        tvHasilDeteksi2.setTextColor(getResources().getColor(R.color.black));
                        tvHasilDeteksi3.setTextColor(getResources().getColor(R.color.black));
                        tvHasilDeteksi4.setTextColor(getResources().getColor(R.color.black));
                        tvHasilDeteksi5.setTextColor(getResources().getColor(R.color.black));
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
}