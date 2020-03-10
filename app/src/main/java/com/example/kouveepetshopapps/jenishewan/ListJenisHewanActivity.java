package com.example.kouveepetshopapps.jenishewan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.JenisHewanAdapter;
import com.example.kouveepetshopapps.adapter.SupplierAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.supplier.ListSupplierActivity;
import com.example.kouveepetshopapps.supplier.TambahSupplierActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListJenisHewanActivity extends AppCompatActivity {

    private List<JenisHewanDAO> ListJenisHewan;
    private RecyclerView recyclerJenisHewan;
    private JenisHewanAdapter adapterJenisHewan;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addJenisHewanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jenishewan);

        recyclerJenisHewan = findViewById(R.id.recycler_view_jenishewan);
        addJenisHewanBtn = findViewById(R.id.addJenisHewanButton);

        ListJenisHewan = new ArrayList<>();
        adapterJenisHewan = new JenisHewanAdapter(ListJenisHewanActivity.this, ListJenisHewan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerJenisHewan.setLayoutManager(mLayoutManager);
        recyclerJenisHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerJenisHewan.setAdapter(adapterJenisHewan);
        setRecycleView();

        addJenisHewanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(ListJenisHewanActivity.this, TambahJenisHewanActivity.class);
                startActivity(add);
            }
        });
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetJenisHewan> jenisHewanDAOCall = apiService.getAllJenisHewanAktif();

        jenisHewanDAOCall.enqueue(new Callback<GetJenisHewan>() {
            @Override
            public void onResponse(Call<GetJenisHewan> call, Response<GetJenisHewan> response) {
                ListJenisHewan.addAll(response.body().getListDataJenisHewan());
                System.out.println(ListJenisHewan.get(0).getNama());
                adapterJenisHewan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetJenisHewan> call, Throwable t) {
                Toast.makeText(ListJenisHewanActivity.this, "Gagal menampilkan Jenis Hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
