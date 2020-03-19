package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.LayananAdapter;
import com.example.kouveepetshopapps.adapter.ProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layanan);
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
//                Intent add = new Intent(ListLayananActivity.this, TambahProdukActivity.class);
//                startActivity(add);

                //get text from adapter
                TextView nama_layanan = recyclerLayanan.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.tvNamaLayanan);
                System.out.println(nama_layanan.getText());
            }
        });
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
}
