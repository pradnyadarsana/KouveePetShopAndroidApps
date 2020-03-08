package com.example.kouveepetshopapps.supplier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.PelangganAdapter;
import com.example.kouveepetshopapps.adapter.SupplierAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSupplierActivity extends AppCompatActivity {

    private List<SupplierDAO> ListSupplier;
    private RecyclerView recyclerSupplier;
    private SupplierAdapter adapterSupplier;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addSupplierBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_supplier);

        recyclerSupplier = findViewById(R.id.recycler_view_supplier);
        addSupplierBtn = findViewById(R.id.addSupplierButton);

        ListSupplier = new ArrayList<>();
        adapterSupplier = new SupplierAdapter(ListSupplierActivity.this, ListSupplier);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSupplier.setLayoutManager(mLayoutManager);
        recyclerSupplier.setItemAnimator(new DefaultItemAnimator());
        recyclerSupplier.setAdapter(adapterSupplier);
        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetSupplier> supplierDAOCall = apiService.getAllSupplierAktif();

        supplierDAOCall.enqueue(new Callback<GetSupplier>() {
            @Override
            public void onResponse(Call<GetSupplier> call, Response<GetSupplier> response) {
                ListSupplier.addAll(response.body().getListDataSupplier());
                System.out.println(ListSupplier.get(0).getNama());
                adapterSupplier.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetSupplier> call, Throwable t) {
                Toast.makeText(ListSupplierActivity.this, "Gagal menampilkan Supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
