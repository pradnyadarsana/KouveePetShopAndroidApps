package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.LayananAdapter;
import com.example.kouveepetshopapps.adapter.ProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.produk.ListProdukActivity;
import com.example.kouveepetshopapps.produk.TambahProdukActivity;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLayananActivity extends AppCompatActivity {
    private List<LayananDAO> ListLayanan;
    private RecyclerView recyclerLayanan;
    private LayananAdapter adapterLayanan;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addLayananBtn;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layanan);

        Toolbar toolbar = findViewById(R.id.searchLayananToolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Layanan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    return;
                }else{
                    startIntent(AdminMainMenu.class);
                }
            }
        });

        recyclerLayanan = findViewById(R.id.recycler_view_layanan);
        addLayananBtn = findViewById(R.id.addLayananButton);

        ListLayanan = new ArrayList<>();
        adapterLayanan = new LayananAdapter(ListLayananActivity.this, ListLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerLayanan.setLayoutManager(mLayoutManager);
        recyclerLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerLayanan.setAdapter(adapterLayanan);
        setRecycleView();

        addLayananBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(ListLayananActivity.this, TambahLayananActivity.class);
                startActivity(add);
            }
        });
    }

    private void startIntent(Class nextClass){
        Intent back = new Intent(ListLayananActivity.this, nextClass);
        back.putExtra("from", "kelola_data");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetLayanan> layananDAOCall = apiService.getAllLayananAktif();

        layananDAOCall.enqueue(new Callback<GetLayanan>() {
            @Override
            public void onResponse(Call<GetLayanan> call, Response<GetLayanan> response) {
                ListLayanan.addAll(response.body().getListDataLayanan());
                System.out.println(ListLayanan.get(0).getNama());
                adapterLayanan.notifyDataSetChanged();

//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetLayanan> call, Throwable t) {
                Toast.makeText(ListLayananActivity.this, "Gagal menampilkan layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("ID / Nama Layanan");

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterLayanan.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterLayanan.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
