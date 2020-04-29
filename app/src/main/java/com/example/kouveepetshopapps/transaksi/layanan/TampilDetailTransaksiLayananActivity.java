package com.example.kouveepetshopapps.transaksi.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.DetailTransaksiLayananAdapter;
import com.example.kouveepetshopapps.adapter.DetailTransaksiProdukAdapter;
import com.example.kouveepetshopapps.adapter.EditTransaksiLayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTampilDetailTransaksiLayananBinding;
import com.example.kouveepetshopapps.databinding.ActivityTampilDetailTransaksiLayananBindingImpl;
import com.example.kouveepetshopapps.databinding.ActivityTampilDetailTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiLayananDAO;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.GetDetailTransaksiLayanan;
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchJenisHewan;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.example.kouveepetshopapps.response.SearchPelanggan;
import com.example.kouveepetshopapps.transaksi.produk.EditTransaksiProdukActivity;
import com.example.kouveepetshopapps.transaksi.produk.TambahTransaksiProdukActivity;
import com.example.kouveepetshopapps.transaksi.produk.TampilDetailTransaksiProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDetailTransaksiLayananActivity extends AppCompatActivity {
    ActivityTampilDetailTransaksiLayananBinding detailTransaksiBinding;
    private Button btnEditTransaksi, btnDeleteTransaksi, btnUpdateProgressTransaksi, btnTambahTransaksi;

    private TransaksiLayananDAO transaksi_layanan;
    private HewanDAO hewan;
    private JenisHewanDAO jenis_hewan;
    private PelangganDAO pelanggan;
    private PegawaiDAO cust_service;
    private PegawaiDAO kasir;
    private String[] tempVar = {"-", "Guest"};
    private String[] textHintStatus = {"Tunggu sampai layanan selesai diproses.", "Lakukan segera pembayaran melalui kasir."};

    //RECYCLERVIEW DETAIL TRANSAKSI LAYANAN
    private List<DetailTransaksiLayananDAO> ListDetailTransaksiLayanan;
    private RecyclerView recyclerDetailTransaksiLayanan;
    private DetailTransaksiLayananAdapter adapterDetailTransaksiLayanan;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailTransaksiBinding = DataBindingUtil.setContentView(this, R.layout.activity_tampil_detail_transaksi_layanan);
        btnEditTransaksi = findViewById(R.id.btnEditTransaksiLayananFromDetail);
        btnDeleteTransaksi = findViewById(R.id.btnDeleteTransaksiLayananFromDetail);
        btnTambahTransaksi = findViewById(R.id.btnAddTransaksiLayananFromDetail);
        btnUpdateProgressTransaksi = findViewById(R.id.btnUpdateProgressTransaksiLayananFromDetail);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //Gson gson = new Gson();
        transaksi_layanan = gson.fromJson(getIntent().getStringExtra("transaksi_layanan"), TransaksiLayananDAO.class);

        detailTransaksiBinding.setTempVar(tempVar);
        detailTransaksiBinding.setTextHintStatus(textHintStatus);
        detailTransaksiBinding.setTransaksiLayanan(transaksi_layanan);

        if(transaksi_layanan.getId_hewan()!=0){
            searchHewan(transaksi_layanan.getId_hewan());
        }
        searchCustService(transaksi_layanan.getId_customer_service());
        searchKasir(transaksi_layanan.getId_kasir());

        recyclerDetailTransaksiLayanan = findViewById(R.id.recycler_view_tampil_detail_transaksi_layanan);
        ListDetailTransaksiLayanan = new ArrayList<>();
        adapterDetailTransaksiLayanan = new DetailTransaksiLayananAdapter(TampilDetailTransaksiLayananActivity.this,
                ListDetailTransaksiLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDetailTransaksiLayanan.setLayoutManager(mLayoutManager);
        recyclerDetailTransaksiLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerDetailTransaksiLayanan.setAdapter(adapterDetailTransaksiLayanan);
        setRecycleView(transaksi_layanan);

        btnEditTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(TampilDetailTransaksiLayananActivity.this, EditTransaksiLayananActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(transaksi_layanan);
                edit.putExtra("transaksi_layanan",data);
                startActivity(edit);
            }
        });

        btnDeleteTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaksiLayanan(transaksi_layanan);
            }
        });

        btnUpdateProgressTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProgressTransaksi(transaksi_layanan, pegawai.getUsername());
            }
        });

        btnTambahTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(TampilDetailTransaksiLayananActivity.this, TambahTransaksiLayananActivity.class);
                //tambah.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tambah);
            }
        });
    }

    public void setRecycleView(TransaksiLayananDAO transaksi_layanan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetDetailTransaksiLayanan> detailtransaksilayananDAOCall = apiService.getDetailTransaksiLayananByIdTransaksi(
                transaksi_layanan.getId_transaksi_layanan());

        detailtransaksilayananDAOCall.enqueue(new Callback<GetDetailTransaksiLayanan>() {
            @Override
            public void onResponse(Call<GetDetailTransaksiLayanan> call, Response<GetDetailTransaksiLayanan> response) {
                if(!response.body().getListDataDetailTransaksiLayanan().isEmpty()){
                    ListDetailTransaksiLayanan.addAll(response.body().getListDataDetailTransaksiLayanan());
                    System.out.println(ListDetailTransaksiLayanan.get(0).getId_harga_layanan());
                    adapterDetailTransaksiLayanan.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailTransaksiLayanan> call, Throwable t) {
                Toast.makeText(TampilDetailTransaksiLayananActivity.this, "Gagal menampilkan detail transaksi produk", Toast.LENGTH_SHORT).show();
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

    private void updateProgressTransaksi(TransaksiLayananDAO transaksi_layanan, String username_pegawai){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.ubahProgressTransaksiLayanan(
                transaksi_layanan.getId_transaksi_layanan(),username_pegawai);

        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailTransaksiLayananActivity.this, "Progress berhasil diperbaharui", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(TampilDetailTransaksiLayananActivity.this, CsMainMenu.class);
                back.putExtra("from","transaksi");
                back.putExtra("firstView","layanan");
                back.putExtra("tab_layanan","selesai");
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                t.printStackTrace();
                System.out.println("gagal update");
            }
        });
    }

    private void deleteTransaksiLayanan(final TransaksiLayananDAO transaksi_layanan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.hapusTransaksiLayanan(transaksi_layanan.getId_transaksi_layanan());

        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailTransaksiLayananActivity.this, "Sukses menghapus data transaksi", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(TampilDetailTransaksiLayananActivity.this, CsMainMenu.class);
                back.putExtra("from","transaksi");
                back.putExtra("firstView","layanan");
                if(transaksi_layanan.isLayananSelesai()){
                    back.putExtra("tab_layanan","selesai");
                }else{
                    back.putExtra("tab_layanan","diproses");
                }
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(TampilDetailTransaksiLayananActivity.this, "Gagal menghapus data transaksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(TampilDetailTransaksiLayananActivity.this, CsMainMenu.class);
        back.putExtra("from","transaksi");
        back.putExtra("firstView","layanan");
        if(transaksi_layanan.isLayananSelesai()){
            back.putExtra("tab_layanan","selesai");
        }else{
            back.putExtra("tab_layanan","diproses");
        }
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }
}
