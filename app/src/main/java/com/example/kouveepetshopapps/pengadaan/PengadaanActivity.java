package com.example.kouveepetshopapps.pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.pengadaan.menunggu_konfirmasi.ListPengadaanMenungguKonfirmasiFragment;
import com.example.kouveepetshopapps.pengadaan.pesanan_diproses.ListPengadaanPesananDiprosesFragment;
import com.example.kouveepetshopapps.pengadaan.pesanan_selesai.ListPengadaanPesananSelesaiFragment;

public class PengadaanActivity extends AppCompatActivity {

    public SearchView searchView;
    public Toolbar toolbar;
    public int ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengadaan);

        toolbar = findViewById(R.id.searchPengadaanToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        setSupportActionBar(toolbar);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Pengadaan Produk");
        getSupportActionBar().setSubtitle("Menunggu Konfirmasi");

        String firstView = null;
        if (getIntent().getStringExtra("firstView") != null) {
            firstView = getIntent().getStringExtra("firstView");
        }

        if (firstView!=null){
            if(firstView.equalsIgnoreCase("menunggu konfirmasi"))
            {
                loadFragment(new ListPengadaanMenungguKonfirmasiFragment());
                getSupportActionBar().setSubtitle("Menunggu Konfirmasi");
            }else if(firstView.equalsIgnoreCase("pesanan diproses"))
            {
                loadFragment(new ListPengadaanPesananDiprosesFragment());
                getSupportActionBar().setSubtitle("Pesanan Diproses");
            }else if(firstView.equalsIgnoreCase("pesanan selesai")){
                loadFragment(new ListPengadaanPesananSelesaiFragment());
                getSupportActionBar().setSubtitle("Pesanan Selesai");
            }
        }else{
            loadFragment(new ListPengadaanMenungguKonfirmasiFragment());
            toolbar.setSubtitle("Menunggu Konfirmasi");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pengadaan_option_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("ID Pengadaan Produk");
//        if(ID==0){
//            searchView.setQueryHint("ID Transaksi Produk");
//        }else if(ID==1){
//            searchView.setQueryHint("ID Transaksi Layanan Dalam Proses");
//        }else if(ID==2){
//            searchView.setQueryHint("ID Transaksi Layanan Selesai Diproses");
//        }

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //adapterJenisHewan.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //adapterJenisHewan.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menunggu_konfirmasi){
            toolbar.setSubtitle("Menunggu Konfirmasi");
            ID = 0;
            loadFragment(new ListPengadaanMenungguKonfirmasiFragment());
            return true;
        } else if (item.getItemId() == R.id.pesanan_diproses) {
            toolbar.setSubtitle("Pesanan Diproses");
            loadFragment(new ListPengadaanPesananDiprosesFragment());
            return true;
        }else if (item.getItemId() == R.id.pesanan_selesai) {
            toolbar.setSubtitle("Pesanan Selesai");
            loadFragment(new ListPengadaanPesananSelesaiFragment());
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);

//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pengadaan_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
