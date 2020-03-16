package com.example.kouveepetshopapps.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.ProdukAdapter;
import com.example.kouveepetshopapps.adapter.SupplierAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

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
                Toast.makeText(ListProdukActivity.this, "Gagal menampilkan Supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
