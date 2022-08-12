package com.josepvictorr.kasep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.josepvictorr.kasep.intro.IntroFragment;
import com.josepvictorr.kasep.user.LoginActivity;
import com.josepvictorr.kasep.util.sharedpref.PrefManager;

public class MainActivity extends AppCompatActivity {
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeOpen() && !prefManager.getSP_LoginCheck()){
            getFragmentPage(new IntroFragment());
        } else if (!prefManager.isFirstTimeOpen() && !prefManager.getSP_LoginCheck()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (!prefManager.isFirstTimeOpen() && prefManager.getSP_LoginCheck()) {
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