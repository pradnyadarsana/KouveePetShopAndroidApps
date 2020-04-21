package com.example.kouveepetshopapps.transaksi.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.DetailTransaksiProdukAdapter;
import com.example.kouveepetshopapps.adapter.HargaLayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTampilDetailTransaksiProdukBinding;
import com.example.kouveepetshopapps.layanan.TampilDetailLayananActivity;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchJenisHewan;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.example.kouveepetshopapps.response.SearchPelanggan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDetailTransaksiProdukActivity extends AppCompatActivity {
    ActivityTampilDetailTransaksiProdukBinding detailTransaksiBinding;
    private Button btnEditTransaksi, btnDeleteTransaksi, btnTambahTransaksi;

    private TransaksiProdukDAO transaksi_produk;
    private HewanDAO hewan;
    private JenisHewanDAO jenis_hewan;
    private PelangganDAO pelanggan;
    private PegawaiDAO cust_service;
    private PegawaiDAO kasir;
    private String[] tempVar = {"-", "Guest"};

    //RECYCLERVIEW DETAIL TRANSAKSI PRODUK
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;
    private RecyclerView recyclerDetailTransaksiProduk;
    private DetailTransaksiProdukAdapter adapterDetailTransaksiProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailTransaksiBinding = DataBindingUtil.setContentView(this, R.layout.activity_tampil_detail_transaksi_produk);
        btnEditTransaksi = findViewById(R.id.btnEditTransaksiProdukFromDetail);
        btnDeleteTransaksi = findViewById(R.id.btnDeleteTransaksiProdukFromDetail);
        btnTambahTransaksi = findViewById(R.id.btnAddTransaksiProdukFromDetail);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //Gson gson = new Gson();
        transaksi_produk = gson.fromJson(getIntent().getStringExtra("transaksi_produk"), TransaksiProdukDAO.class);

        detailTransaksiBinding.setTempVar(tempVar);
        detailTransaksiBinding.setTransaksiProduk(transaksi_produk);

        if(transaksi_produk.getId_hewan()!=0){
            searchHewan(transaksi_produk.getId_hewan());
        }
        searchCustService(transaksi_produk.getId_customer_service());
        searchKasir(transaksi_produk.getId_kasir());

        recyclerDetailTransaksiProduk = findViewById(R.id.recycler_view_tampil_detail_transaksi_produk);
        ListDetailTransaksiProduk = new ArrayList<>();
        adapterDetailTransaksiProduk = new DetailTransaksiProdukAdapter(TampilDetailTransaksiProdukActivity.this,
                ListDetailTransaksiProduk);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDetailTransaksiProduk.setLayoutManager(mLayoutManager);
        recyclerDetailTransaksiProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerDetailTransaksiProduk.setAdapter(adapterDetailTransaksiProduk);
        setRecycleView(transaksi_produk);

        btnEditTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(TampilDetailTransaksiProdukActivity.this, EditTransaksiProdukActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(transaksi_produk);
                edit.putExtra("transaksi_produk",data);
                startActivity(edit);
            }
        });

        btnDeleteTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaksiProduk(transaksi_produk.getId_transaksi_produk(), pegawai.getUsername());
            }
        });

        btnTambahTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(TampilDetailTransaksiProdukActivity.this, TambahTransaksiProdukActivity.class);
                //tambah.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tambah);
            }
        });
    }

    public void setRecycleView(TransaksiProdukDAO transaksi_produk){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetDetailTransaksiProduk> detailtransaksiprodukDAOCall = apiService.getDetailTransaksiProdukByIdTransaksi(
                transaksi_produk.getId_transaksi_produk());

        detailtransaksiprodukDAOCall.enqueue(new Callback<GetDetailTransaksiProduk>() {
            @Override
            public void onResponse(Call<GetDetailTransaksiProduk> call, Response<GetDetailTransaksiProduk> response) {
                if(!response.body().getListDataDetailTransaksiProduk().isEmpty()){
                    ListDetailTransaksiProduk.addAll(response.body().getListDataDetailTransaksiProduk());
                    System.out.println(ListDetailTransaksiProduk.get(0).getId_produk());
                    adapterDetailTransaksiProduk.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailTransaksiProduk> call, Throwable t) {
                Toast.makeText(TampilDetailTransaksiProdukActivity.this, "Gagal menampilkan detail transaksi produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchHewan(int id_hewan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchHewan> hewanDAOCall = apiService.searchHewan(Integer.toString(id_hewan));
        hewanDAOCall.enqueue(new Callback<SearchHewan>() {
            @Override
            public void onResponse(Call<SearchHewan> call, Response<SearchHewan> response) {
                hewan = response.body().getHewan();
                detailTransaksiBinding.setHewan(hewan);
                searchPelanggan(hewan.getId_pelanggan());
                searchJenisHewan(hewan.getId_jenis_hewan());
            }

            @Override
            public void onFailure(Call<SearchHewan> call, Throwable t) {
                //holder.nama_hewan.setText(Integer.toString(id_hewan));
            }
        });

    }

    public void searchPelanggan(int id_pelanggan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPelanggan> pelangganDAOCall = apiService.searchPelanggan(Integer.toString(id_pelanggan));

        pelangganDAOCall.enqueue(new Callback<SearchPelanggan>() {
            @Override
            public void onResponse(Call<SearchPelanggan> call, Response<SearchPelanggan> response) {
                pelanggan = response.body().getPelanggan();
                detailTransaksiBinding.setPelanggan(pelanggan);
            }

            @Override
            public void onFailure(Call<SearchPelanggan> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    public void searchCustService(int id_customer_service){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPegawai> pegawaiDAOCall = apiService.searchPegawai(Integer.toString(id_customer_service));

        pegawaiDAOCall.enqueue(new Callback<SearchPegawai>() {
            @Override
            public void onResponse(Call<SearchPegawai> call, Response<SearchPegawai> response) {
                cust_service = response.body().getPegawai();
                detailTransaksiBinding.setCustService(cust_service);
            }

            @Override
            public void onFailure(Call<SearchPegawai> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    public void searchKasir(int id_kasir){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPegawai> pegawaiDAOCall = apiService.searchPegawai(Integer.toString(id_kasir));

        pegawaiDAOCall.enqueue(new Callback<SearchPegawai>() {
            @Override
            public void onResponse(Call<SearchPegawai> call, Response<SearchPegawai> response) {
                kasir = response.body().getPegawai();
                detailTransaksiBinding.setKasir(kasir);
            }

            @Override
            public void onFailure(Call<SearchPegawai> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    public void searchJenisHewan(int id_jenis_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchJenisHewan> jenishewanDAOCall = apiService.searchJenisHewan(Integer.toString(id_jenis_hewan));

        jenishewanDAOCall.enqueue(new Callback<SearchJenisHewan>() {
            @Override
            public void onResponse(Call<SearchJenisHewan> call, Response<SearchJenisHewan> response) {
                jenis_hewan = response.body().getJenishewan();
                detailTransaksiBinding.setJenisHewan(jenis_hewan);
            }

            @Override
            public void onFailure(Call<SearchJenisHewan> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    private void deleteTransaksiProduk(String id, String delete_by){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.hapusTransaksiProduk(id);

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailTransaksiProdukActivity.this, "Sukses menghapus data transaksi", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(TampilDetailTransaksiProdukActivity.this, CsMainMenu.class);
                back.putExtra("from","transaksi");
                back.putExtra("firstView","produk");
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(TampilDetailTransaksiProdukActivity.this, "Gagal menghapus data transaksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(TampilDetailTransaksiProdukActivity.this, CsMainMenu.class);
        back.putExtra("from","transaksi");
        back.putExtra("firstView","produk");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }
}
