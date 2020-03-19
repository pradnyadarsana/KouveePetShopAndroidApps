package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.HargaLayananAdapter;
import com.example.kouveepetshopapps.adapter.LayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDetailLayananActivity extends AppCompatActivity {
    private TextView nama_layanan, id_layanan, created_at, created_by;

    //recyclerview
    private List<HargaLayananDAO> ListHargaLayanan;
    private RecyclerView recyclerHargaLayanan;
    private HargaLayananAdapter adapterHargaLayanan;
    private RecyclerView.LayoutManager mLayoutManager;
    //private FloatingActionButton addLayananBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_layanan);

        nama_layanan = findViewById(R.id.viewNamaLayanan);
        id_layanan = findViewById(R.id.viewIdLayanan);
        created_at = findViewById(R.id.viewCreatedAtLayanan);
        created_by = findViewById(R.id.viewCreatedByLayanan);

        recyclerHargaLayanan = findViewById(R.id.recycler_view_harga_layanan);
        //addLayananBtn = findViewById(R.id.addLayananButton);

        ListHargaLayanan = new ArrayList<>();
        adapterHargaLayanan = new HargaLayananAdapter(TampilDetailLayananActivity.this, ListHargaLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerHargaLayanan.setLayoutManager(mLayoutManager);
        recyclerHargaLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerHargaLayanan.setAdapter(adapterHargaLayanan);
        setData();
        setRecycleView();

//        addLayananBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent add = new Intent(ListLayananActivity.this, TambahProdukActivity.class);
////                startActivity(add);
//            }
//        });


    }

    public void setData(){
        id_layanan.setText(getIntent().getStringExtra("id_layanan"));
        nama_layanan.setText(getIntent().getStringExtra("nama_layanan"));
        created_at.setText(getIntent().getStringExtra("created_at"));
        created_by.setText(getIntent().getStringExtra("created_by"));
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargalayananDAOCall = apiService.searchHargaLayananByIdLayanan(getIntent().getStringExtra("id_layanan"));

        hargalayananDAOCall.enqueue(new Callback<GetHargaLayanan>() {
            @Override
            public void onResponse(Call<GetHargaLayanan> call, Response<GetHargaLayanan> response) {
                if(!response.body().getListDataHargaLayanan().isEmpty()){
                    ListHargaLayanan.addAll(response.body().getListDataHargaLayanan());
                    System.out.println(ListHargaLayanan.get(0).getHarga());
                    adapterHargaLayanan.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetHargaLayanan> call, Throwable t) {
                Toast.makeText(TampilDetailLayananActivity.this, "Gagal menampilkan harga layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
