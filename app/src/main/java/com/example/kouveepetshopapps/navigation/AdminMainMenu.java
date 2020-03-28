package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.kouveepetshopapps.NotificationFragment;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.StockUpdateFragment;
import com.example.kouveepetshopapps.TransactionActivity;
import com.example.kouveepetshopapps.hewan.ListHewanFragment;
import com.example.kouveepetshopapps.pelanggan.ListPelangganFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainMenu extends AppCompatActivity {

    private BottomNavigationView adminBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_admin_main);

        adminBottomNavigationView = (BottomNavigationView) findViewById(R.id.bn_main_admin);

        loadFragment(new StockUpdateFragment());
        if (getIntent().getStringExtra("from")!=null){
            if(getIntent().getStringExtra("from").equalsIgnoreCase("stok"))
            {
                loadFragment(new StockUpdateFragment());
                adminBottomNavigationView.setSelectedItemId(R.id.admin_stock);
            }else if(getIntent().getStringExtra("from").equalsIgnoreCase("notifikasi"))
            {
                loadFragment(new NotificationFragment());
                adminBottomNavigationView.setSelectedItemId(R.id.admin_notification);
            } else if(getIntent().getStringExtra("from").equalsIgnoreCase("kelola_data"))
            {
                loadFragment(new AdminManagePageFragment());
                adminBottomNavigationView.setSelectedItemId(R.id.admin_manage);
            }
        }

        adminBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.admin_stock:
                        fragment = new StockUpdateFragment();
                        break;
                    case R.id.admin_notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.admin_manage:
                        fragment = new AdminManagePageFragment();
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
