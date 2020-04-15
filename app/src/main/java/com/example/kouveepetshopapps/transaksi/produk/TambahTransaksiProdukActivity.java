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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiProdukBinding;
import com.example.kouveepetshopapps.hewan.TambahHewanActivity;
import com.example.kouveepetshopapps.layanan.ListLayananActivity;
import com.example.kouveepetshopapps.layanan.TambahLayananActivity;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksiProdukActivity extends AppCompatActivity {
    private AutoCompleteTextView nama_hewan;
    private Button btnTambahItemProduk, btnTambahTransaksiProduk;

    private List<HewanDAO> ListHewan;
    private List<ProdukDAO> ListProduk;

    private RecyclerView recyclerTambahProduk;
    private TambahTransaksiProdukAdapter adapterTambahProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    //data binding
    ActivityTambahTransaksiProdukBinding transaksiProdukBinding;
    private TransaksiProdukDAO transaksiProdukData;
    private HewanDAO nama_hewan_bind;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;
    private List<ProdukDAO> ListProdukPilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transaksiProdukBinding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_transaksi_produk);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //create Autocompletetextview for nama hewan
        nama_hewan = (AutoCompleteTextView) findViewById(R.id.etNamaHewanTransaksiProduk);
        nama_hewan.setThreshold(1);//will start working from first character

        //monitoring the input from nama hewan
        nama_hewan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("after text changed: "+s.toString());
                int id_hewan = getIdHewan(s.toString());
                System.out.println("id hewan : "+id_hewan);
                transaksiProdukData.setId_hewan(id_hewan);

                transaksiProdukBinding.setTransaksiProduk(transaksiProdukData);
            }
        });

        //get list of hewan and produk
        ListProduk = new ArrayList<>();
        ListHewan = new ArrayList<>();
        ListDetailTransaksiProduk = new ArrayList<>();
        ListProdukPilihan = new ArrayList<>();
        getListHewan();
        getListProduk();

        //DATA BINDING SECTION
        transaksiProdukData = new TransaksiProdukDAO();
        transaksiProdukData.setId_customer_service(pegawai.getId_pegawai());

        transaksiProdukBinding.setPegawai(pegawai);
        transaksiProdukBinding.setNamaHewanBind(nama_hewan_bind);
        transaksiProdukBinding.setTransaksiProduk(transaksiProdukData);

        btnTambahItemProduk = findViewById(R.id.btnTambahItemTransaksiProduk);
        btnTambahTransaksiProduk = findViewById(R.id.btnTambahTransaksiProduk);

        //setup recyclerview
        recyclerTambahProduk = findViewById(R.id.recycler_view_tambah_produk_transaksi);
        adapterTambahProduk = new TambahTransaksiProdukAdapter(TambahTransaksiProdukActivity.this, ListDetailTransaksiProduk, ListProduk, ListProdukPilihan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTambahProduk.setLayoutManager(mLayoutManager);
        recyclerTambahProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerTambahProduk.setAdapter(adapterTambahProduk);
        //setRecyclerView();

        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiProdukDAO detailtransaksiproduk = new DetailTransaksiProdukDAO();
                detailtransaksiproduk.setJumlah(1);
                ProdukDAO produk = new ProdukDAO();
                ListDetailTransaksiProduk.add(detailtransaksiproduk);
                ListProdukPilihan.add(produk);
                adapterTambahProduk.notifyItemInserted(ListDetailTransaksiProduk.size());
                for(int i=0; i<ListDetailTransaksiProduk.size();i++){
                    System.out.println("id produk detail: "+ListDetailTransaksiProduk.get(i).getId_produk()+", id produk produk: "+ListProdukPilihan.get(i).getId_produk());
                }

            }
        });

        //adapterTambahProduk.

        btnTambahTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tambahLayanan();
                System.out.println("id_customer_service: "+transaksiProdukData.getId_customer_service());
                System.out.println("id_hewan: "+transaksiProdukData.getId_hewan());
                System.out.println("subtotal: "+transaksiProdukData.getSubtotal());
                System.out.println("diskon: "+transaksiProdukData.getDiskon());
                System.out.println("total: "+transaksiProdukData.getTotal());
            }
        });
    }

//    private void startIntent(){
//        Intent back = new Intent(TambahLayananActivity.this, ListLayananActivity.class);
//        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(back);
//    }

    public void getListHewan(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetHewan> hewanDAOCall = apiService.getAllHewanAktif();

        hewanDAOCall.enqueue(new Callback<GetHewan>() {
            @Override
            public void onResponse(Call<GetHewan> call, Response<GetHewan> response) {
                ListHewan.addAll(response.body().getListDataHewan());
                System.out.println(ListHewan.get(0).getNama());
                String[] arrName = new String[ListHewan.size()];
                int i = 0;
                for (HewanDAO hewan: ListHewan
                ) {
                    arrName[i] = hewan.getNama();
                    i++;
                }

                //Creating the instance of ArrayAdapter containing list of fruit names
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.select_dialog_item, arrName);

                nama_hewan.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetHewan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListProduk(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetProduk> produkDAOCall = apiService.getAllProdukAktif();

        produkDAOCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                ListProduk.addAll(response.body().getListDataProduk());
                System.out.println(ListProduk.get(0).getNama());
//                String[] arrName = new String[ListProduk.size()];
//                int i = 0;
//                for (ProdukDAO produk: ListProduk
//                ) {
//                    arrName[i] = produk.getNama();
//                    i++;
//                }

                //Creating the instance of ArrayAdapter containing list of fruit names
//                ArrayAdapter<String> adapter = new ArrayAdapter<>
//                        (getApplicationContext(), android.R.layout.select_dialog_item, arrName);
//
//                nama_hewan.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getIdHewan(String nama)
    {
        for (HewanDAO hewan:ListHewan
        ) {
            if(hewan.getNama().equalsIgnoreCase(nama)){
                return hewan.getId_hewan();
            }
        }
        return -1;
    }

}
