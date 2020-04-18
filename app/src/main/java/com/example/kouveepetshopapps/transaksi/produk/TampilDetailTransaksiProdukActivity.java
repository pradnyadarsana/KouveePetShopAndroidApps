package com.example.kouveepetshopapps.transaksi.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TampilDetailTransaksiProdukActivity extends AppCompatActivity {
    Type produkListType = new TypeToken<ArrayList<ProdukDAO>>(){}.getType();

    ActivityTampilDetailTransaksiProdukBinding detailTransaksiBinding;

    private TransaksiProdukDAO transaksi_produk;
    private HewanDAO hewan;
    private JenisHewanDAO jenis_hewan;
    private PelangganDAO pelanggan;
    private PegawaiDAO cust_service;
    private PegawaiDAO kasir;
    private String[] tempVar = {"-", "Guest"};

    //RECYCLERVIEW DETAIL TRANSAKSI PRODUK
    private List<ProdukDAO> ListProduk;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;
    private RecyclerView recyclerDetailTransaksiProduk;
    private DetailTransaksiProdukAdapter adapterDetailTransaksiProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailTransaksiBinding = DataBindingUtil.setContentView(this, R.layout.activity_tampil_detail_transaksi_produk);

        Gson gson = new Gson();
        transaksi_produk = gson.fromJson(getIntent().getStringExtra("transaksi_produk"), TransaksiProdukDAO.class);
        hewan = gson.fromJson(getIntent().getStringExtra("hewan"), HewanDAO.class);
        jenis_hewan = gson.fromJson(getIntent().getStringExtra("jenis_hewan"), JenisHewanDAO.class);
        pelanggan = gson.fromJson(getIntent().getStringExtra("pelanggan"), PelangganDAO.class);
        cust_service = gson.fromJson(getIntent().getStringExtra("cust_service"), PegawaiDAO.class);
        kasir = gson.fromJson(getIntent().getStringExtra("kasir"), PegawaiDAO.class);
        ListProduk = gson.fromJson(getIntent().getStringExtra("produk"), produkListType);

        System.out.println("cust service : "+cust_service.getId_pegawai());
        detailTransaksiBinding.setTempVar(tempVar);
        detailTransaksiBinding.setTransaksiProduk(transaksi_produk);
        detailTransaksiBinding.setPelanggan(pelanggan);
        detailTransaksiBinding.setHewan(hewan);
        detailTransaksiBinding.setJenisHewan(jenis_hewan);
        detailTransaksiBinding.setCustService(cust_service);
        detailTransaksiBinding.setKasir(kasir);

        recyclerDetailTransaksiProduk = findViewById(R.id.recycler_view_tampil_detail_transaksi_produk);
        ListDetailTransaksiProduk = new ArrayList<>();
        adapterDetailTransaksiProduk = new DetailTransaksiProdukAdapter(TampilDetailTransaksiProdukActivity.this,
                ListDetailTransaksiProduk, ListProduk);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDetailTransaksiProduk.setLayoutManager(mLayoutManager);
        recyclerDetailTransaksiProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerDetailTransaksiProduk.setAdapter(adapterDetailTransaksiProduk);
        setRecycleView(transaksi_produk);
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
}
