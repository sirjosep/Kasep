package com.josepvictorr.kasep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.josepvictorr.kasep.intro.IntroFragment;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

public class MainActivity extends AppCompatActivity {
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeOpen()){
            getFragmentPage(new IntroFragment());
        } else {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }

    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}