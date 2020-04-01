package com.example.kouveepetshopapps.produk;

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
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.ProdukAdapter;
import com.example.kouveepetshopapps.adapter.SupplierAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.navigation.AdminManagePageFragment;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.supplier.TambahSupplierActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProdukActivity extends AppCompatActivity {
    private List<ProdukDAO> ListProduk;
    private RecyclerView recyclerProduk;
    private ProdukAdapter adapterProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addProdukBtn;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        Toolbar toolbar = findViewById(R.id.searchProdukToolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Produk");
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

        recyclerProduk = findViewById(R.id.recycler_view_produk);
        addProdukBtn = findViewById(R.id.addProdukButton);

        ListProduk = new ArrayList<>();
        adapterProduk = new ProdukAdapter(ListProdukActivity.this, ListProduk);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerProduk.setLayoutManager(mLayoutManager);
        recyclerProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerProduk.setAdapter(adapterProduk);
        setRecycleView();

        addProdukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(ListProdukActivity.this, TambahProdukActivity.class);
                startActivity(add);
            }
        });
    }

    private void startIntent(Class nextClass){
        Intent back = new Intent(getApplicationContext(), nextClass);
        back.putExtra("from", "kelola_data");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetProduk> produkDAOCall = apiService.getAllProdukAktif();

        produkDAOCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                ListProduk.addAll(response.body().getListDataProduk());
                System.out.println(ListProduk.get(0).getNama());
                adapterProduk.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                Toast.makeText(ListProdukActivity.this, "Gagal menampilkan Produk", Toast.LENGTH_SHORT).show();
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
        searchView.setQueryHint("ID / Nama Produk");

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterProduk.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterProduk.getFilter().filter(query);
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
