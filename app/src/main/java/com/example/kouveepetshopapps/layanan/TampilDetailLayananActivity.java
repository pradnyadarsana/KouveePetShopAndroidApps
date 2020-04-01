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
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDetailLayananActivity extends AppCompatActivity {
    private TextView nama_layanan, id_layanan, created_at, created_by, modified_at, modified_by,
            delete_at, delete_by;

    //recyclerview
    private List<HargaLayananDAO> ListHargaLayanan;
    private RecyclerView recyclerHargaLayanan;
    private HargaLayananAdapter adapterHargaLayanan;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_layanan);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("layanan");
        System.out.println(json);
        LayananDAO layanan = gson.fromJson(json, LayananDAO.class);

        nama_layanan = findViewById(R.id.viewNamaLayanan);
        id_layanan = findViewById(R.id.viewIdLayanan);
        created_at = findViewById(R.id.viewCreatedAtLayanan);
        created_by = findViewById(R.id.viewCreatedByLayanan);
        modified_at = findViewById(R.id.viewModifiedAtLayanan);
        modified_by = findViewById(R.id.viewModifiedByLayanan);
        delete_at = findViewById(R.id.viewDeleteAtLayanan);
        delete_by = findViewById(R.id.viewDeleteByLayanan);

        recyclerHargaLayanan = findViewById(R.id.recycler_view_harga_layanan);
        //addLayananBtn = findViewById(R.id.addLayananButton);

        ListHargaLayanan = new ArrayList<>();
        adapterHargaLayanan = new HargaLayananAdapter(TampilDetailLayananActivity.this, ListHargaLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerHargaLayanan.setLayoutManager(mLayoutManager);
        recyclerHargaLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerHargaLayanan.setAdapter(adapterHargaLayanan);
        setData(layanan);
        setRecycleView(layanan);

//        addLayananBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent add = new Intent(ListLayananActivity.this, TambahProdukActivity.class);
////                startActivity(add);
//            }
//        });


    }

    public void setData(LayananDAO layanan){
        id_layanan.setText(Integer.toString(layanan.getId_layanan()));
        nama_layanan.setText(layanan.getNama());
        created_at.setText(layanan.getCreated_at());
        created_by.setText(layanan.getCreated_by());
        modified_at.setText(layanan.getModified_at());
        modified_by.setText(layanan.getModified_by());
        delete_at.setText(layanan.getDelete_at());
        delete_by.setText(layanan.getDelete_by());
    }

    public void setRecycleView(LayananDAO layanan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargalayananDAOCall = apiService.searchHargaLayananByIdLayanan(Integer.toString(layanan.getId_layanan()));

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
