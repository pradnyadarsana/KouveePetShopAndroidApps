package com.example.kouveepetshopapps.transaksi.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.TambahLayananAdapter;
import com.example.kouveepetshopapps.adapter.TambahTransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.layanan.ListLayananActivity;
import com.example.kouveepetshopapps.layanan.TambahLayananActivity;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksiProdukActivity extends AppCompatActivity {
    private AutoCompleteTextView nama_hewan;
    private TextView customer_service;
    private EditText diskon;
    private Button btnTambahItemProduk, btnTambahTransaksiProduk;

    private List<HewanDAO> ListHewan;
    private List<ProdukDAO> ListProduk;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;

    private RecyclerView recyclerTambahProduk;
    private TambahTransaksiProdukAdapter adapterTambahProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_transaksi_produk);

        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        customer_service = findViewById(R.id.tvNamaPegawaiTransaksiProduk);
        customer_service.setText(pegawai.getNama());

        nama_hewan = findViewById(R.id.etNamaHewanTransaksiProduk);
        diskon = findViewById(R.id.etDiskonTransaksiProduk);

        btnTambahItemProduk = findViewById(R.id.btnTambahItemTransaksiProduk);
        btnTambahTransaksiProduk = findViewById(R.id.btnTambahTransaksiProduk);

        ListProduk = new ArrayList<>();
        ListHewan = new ArrayList<>();
        ListDetailTransaksiProduk = new ArrayList<>();

        //recyclerview
        recyclerTambahProduk = findViewById(R.id.recycler_view_tambah_produk_transaksi);
        adapterTambahProduk = new TambahTransaksiProdukAdapter(TambahTransaksiProdukActivity.this, ListDetailTransaksiProduk, ListProduk);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTambahProduk.setLayoutManager(mLayoutManager);
        recyclerTambahProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerTambahProduk.setAdapter(adapterTambahProduk);
        //setRecyclerView();

        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiProdukDAO detailtransaksiproduk = new DetailTransaksiProdukDAO();
                ListDetailTransaksiProduk.add(detailtransaksiproduk);
                adapterTambahProduk.notifyDataSetChanged();
            }
        });

        btnTambahTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tambahLayanan();
            }
        });
    }

//    public void setRecyclerView(){
//        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
//        Call<GetUkuranHewan> ukuranHewanDAOCall = apiService.getAllUkuranHewanAktif();
//
//        ukuranHewanDAOCall.enqueue(new Callback<GetUkuranHewan>() {
//            @Override
//            public void onResponse(Call<GetUkuranHewan> call, Response<GetUkuranHewan> response) {
//                ListUkuranHewan.addAll(response.body().getListDataUkuranHewan());
//                System.out.println(ListUkuranHewan.get(0).getNama());
//                adapterUkuranHewan.notifyDataSetChanged();
////                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<GetUkuranHewan> call, Throwable t) {
//                Toast.makeText(TambahLayananActivity.this, "Gagal menampilkan Ukuran Hewan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void startIntent(){
//        Intent back = new Intent(TambahLayananActivity.this, ListLayananActivity.class);
//        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(back);
//    }
}
