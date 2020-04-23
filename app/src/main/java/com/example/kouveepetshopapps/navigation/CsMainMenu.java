package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.transaksi.TransaksiFragment;
import com.example.kouveepetshopapps.hewan.ListHewanFragment;
import com.example.kouveepetshopapps.pelanggan.ListPelangganFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CsMainMenu extends AppCompatActivity {
    private BottomNavigationView csBottomNavigationView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cs_main);

        csBottomNavigationView = (BottomNavigationView) findViewById(R.id.bn_main_cs);

        if (getIntent().getStringExtra("from")!=null){
            if(getIntent().getStringExtra("from").equalsIgnoreCase("transaksi"))
            {
                Fragment fragment = new TransaksiFragment();
                Bundle bundle = new Bundle();

                csBottomNavigationView.setSelectedItemId(R.id.cs_transaksi);
                if(getIntent().getStringExtra("firstView").equalsIgnoreCase("produk")){
                    bundle.putString("firstView", "produk");
                }else if(getIntent().getStringExtra("firstView").equalsIgnoreCase("layanan")){
                    bundle.putString("firstView", "layanan");
                    bundle.putString("tab_layanan",getIntent().getStringExtra("tab_layanan"));
                }
                fragment.setArguments(bundle);
                loadFragment(fragment);

            }else if(getIntent().getStringExtra("from").equalsIgnoreCase("hewan"))
            {
                loadFragment(new ListHewanFragment());
                csBottomNavigationView.setSelectedItemId(R.id.cs_hewan);
            } else if(getIntent().getStringExtra("from").equalsIgnoreCase("pelanggan"))
            {
                loadFragment(new ListPelangganFragment());
                csBottomNavigationView.setSelectedItemId(R.id.cs_pelanggan);
            } else if(getIntent().getStringExtra("from").equalsIgnoreCase("profil"))
            {
                loadFragment(new ProfilCsFragment());
                csBottomNavigationView.setSelectedItemId(R.id.cs_profil);
            }
        }else{
            loadFragment(new TransaksiFragment());
        }

        csBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.cs_transaksi:
                        fragment = new TransaksiFragment();
                        break;
                    case R.id.cs_hewan:
                        fragment = new ListHewanFragment();
                        break;
                    case R.id.cs_pelanggan:
                        fragment = new ListPelangganFragment();
                        break;
                    case R.id.cs_profil:
                        fragment = new ProfilCsFragment();
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
