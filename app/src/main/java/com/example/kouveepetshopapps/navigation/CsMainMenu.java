package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.kouveepetshopapps.PetActivity;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.TransactionActivity;
import com.example.kouveepetshopapps.pelanggan.PelangganFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CsMainMenu extends AppCompatActivity {
    private BottomNavigationView csBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cs_main);

        csBottomNavigationView = (BottomNavigationView) findViewById(R.id.bn_main_cs);

        loadFragment(new TransactionActivity());

        csBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.cs_transaksi:
                        fragment = new TransactionActivity();
                        break;
                    case R.id.cs_hewan:
                        fragment = new PetActivity();
                        break;
                    case R.id.cs_pelanggan:
                        fragment = new PelangganFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
