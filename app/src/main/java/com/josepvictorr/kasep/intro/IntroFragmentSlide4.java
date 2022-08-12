package com.josepvictorr.kasep.intro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.josepvictorr.kasep.HomeActivity;
import com.josepvictorr.kasep.user.LoginActivity;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;
import com.josepvictorr.kasep.R;

public class IntroFragmentSlide4 extends Fragment {
    PrefManager prefManager;
    ImageButton btnGetStarted;
    ProgressDialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_intro_slide4, container, false);
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.INDONESIAN)
                .build();
        final Translator englishIndonesiaTranslator = Translation.getClient(options);
        btnGetStarted = rootView.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(view -> {
            new AlertDialog.Builder(rootView.getContext())
                    .setTitle("Download")
                    .setMessage("Kurang lebih 40MB data akan di download, pastikan anda terhubung ke internet")
                    .setIcon(R.drawable.ic_baseline_cloud_download_24)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            loading = ProgressDialog.show(rootView.getContext(), null,"Downloading...", true,false);
                            DownloadConditions downloadConditions = new DownloadConditions.Builder()
                                    .build();

                            englishIndonesiaTranslator.downloadModelIfNeeded(downloadConditions)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            loading.dismiss();
                                            prefManager = new PrefManager(getActivity());
                                            prefManager.setFirstTimeOpen(false);
                                            Toast.makeText(loading.getContext(), "Download berhasil, silahkan login / register", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(loading.getContext(), "Download gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }})
                    .show();
        });
        return rootView;
    }
}