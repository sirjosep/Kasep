package com.josepvictorr.kasep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josepvictorr.kasep.bahan.BahanFragment;
import com.josepvictorr.kasep.detect.DetectActivity;
import com.josepvictorr.kasep.myrecipe.MyRecipeFragment;
import com.josepvictorr.kasep.recipe.DetailRecipeActivity;
import com.josepvictorr.kasep.recipe.RecipeFragment;
import com.josepvictorr.kasep.user.ProfileFragment;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    String imageFilePath;
    PrefManager prefManager;
    Boolean checkBoxResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        TextView tvTitleFragment = findViewById(R.id.tvTitleFragment);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case
                            R.id.home:
                            tvTitleFragment.setText("Home");
                            fragment = new RecipeFragment();
                            break;
                    case
                            R.id.bahan:
                            tvTitleFragment.setText("Daftar Bahan");
                            fragment = new BahanFragment();
                            break;
                    case
                            R.id.deteksi_bahan:
                            break;
                    case
                            R.id.my_recipe:
                            tvTitleFragment.setText("My Recipe");
                            fragment = new MyRecipeFragment();
                            break;
                    case
                            R.id.profile:
                            tvTitleFragment.setText("Profile");
                            fragment = new ProfileFragment();
                            break;
                }
                return getFragmentPage(fragment);
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        FloatingActionButton fabDetect = findViewById(R.id.fabDetect);
        fabDetect.setOnClickListener(view -> {
            prefManager = new PrefManager(this);
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"},
                        0);
            } else if(ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && !prefManager.getPetunjukCheck()) {
                DialogPlus dialog = DialogPlus.newDialog(this)
                        .setExpanded(true)
                        .setContentHolder(new ViewHolder(R.layout.item_tutorial_dialog))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .create();
                dialog.show();
                CheckBox cbJanganTunjukkanLagi;
                cbJanganTunjukkanLagi = (CheckBox) findViewById(R.id.cbJanganTunjukkanLagi);
                checkBoxResult = false;
                Button btnConfirm = findViewById(R.id.btnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbJanganTunjukkanLagi.isChecked()) {
                            prefManager.saveSPBoolean(PrefManager.SP_PetunjukCheck, true);
                            dialog.dismiss();
                            openCameraIntent();
                        } else if (!cbJanganTunjukkanLagi.isChecked()){
                            prefManager.saveSPBoolean(PrefManager.SP_PetunjukCheck, false);
                            dialog.dismiss();
                            openCameraIntent();
                        }
                    }
                });
            } else if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && prefManager.getPetunjukCheck()) {
                openCameraIntent();
            }
        });
    }

    private void openCameraIntent(){
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
                Intent detectImageIntent = new Intent(this, DetectActivity.class);
                detectImageIntent.putExtra("path", imageFilePath);
                startActivity(detectImageIntent);
            }
            else if(resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container_home, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}