package com.josepvictorr.kasep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josepvictorr.kasep.bahan.BahanFragment;
import com.josepvictorr.kasep.detect.DetectActivity;
import com.josepvictorr.kasep.myrecipe.MyRecipeFragment;
import com.josepvictorr.kasep.recipe.RecipeFragment;
import com.josepvictorr.kasep.user.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

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
        FloatingActionButton floatingActionButton = findViewById(R.id.fabHome);
        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, DetectActivity.class));
        });
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