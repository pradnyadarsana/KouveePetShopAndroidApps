package com.example.kouveepetshopapps.transaksi.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.TambahTransaksiLayananAdapter;
import com.example.kouveepetshopapps.adapter.TambahTransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiLayananBinding;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiLayananDAO;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.NamaHargaLayanan;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchTransaksiLayanan;
import com.example.kouveepetshopapps.response.SearchTransaksiProduk;
import com.example.kouveepetshopapps.transaksi.produk.TambahTransaksiProdukActivity;
import com.example.kouveepetshopapps.transaksi.produk.TampilDetailTransaksiProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksiLayananActivity extends AppCompatActivity {
    private AutoCompleteTextView nama_hewan;
    private EditText diskon;
    private Button btnTambahItemLayanan, btnTambahTransaksiLayanan;

    private List<NamaHargaLayanan> ListNamaHargaLayanan;
    private List<HewanDAO> ListHewan;
    private List<HargaLayananDAO> ListHargaLayanan;
    private List<LayananDAO> ListLayanan;
    private List<UkuranHewanDAO> ListUkuranHewan;
    private List<DetailTransaksiLayananDAO> ListDetailTransaksiLayanan;
    private List<HargaLayananDAO> ListHargaLayananPilihan;

    private RecyclerView recyclerTambahLayanan;
    private TambahTransaksiLayananAdapter adapterTambahLayanan;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityTambahTransaksiLayananBinding transaksiLayananBinding;
    private TransaksiLayananDAO transaksiLayananData;
    private HewanDAO nama_hewan_bind;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    int statHargaLayanan=0, statUkuranHewan=0, statLayanan=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transaksiLayananBinding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_transaksi_layanan);

        //get list of hewan and produk
        ListHargaLayanan = new ArrayList<>();
        ListLayanan = new ArrayList<>();
        ListUkuranHewan = new ArrayList<>();
        ListNamaHargaLayanan = new ArrayList<>();
        ListHewan = new ArrayList<>();
        ListDetailTransaksiLayanan = new ArrayList<>();
        ListHargaLayananPilihan = new ArrayList<>();

        statHargaLayanan=0; statUkuranHewan=0; statLayanan=0;
        getListHewan();
        getListHargaLayanan();
        getListLayanan();
        getListUkuranHewan();

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //create Autocompletetextview for nama hewan
        nama_hewan = (AutoCompleteTextView) findViewById(R.id.etNamaHewanTransaksiLayanan);
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
                transaksiLayananData.setId_hewan(id_hewan);
                if(id_hewan==-1){
                    transaksiLayananData.setDiskon(0);
                }
                transaksiLayananBinding.setTransaksiLayanan(transaksiLayananData);
            }
        });

        //SETUP TEXT WATCHER FOR DISKON FIELD
        diskon = findViewById(R.id.etDiskonTransaksiLayanan);
        diskon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hitungTotal();
            }
        });

        //DATA BINDING SECTION
        transaksiLayananData = new TransaksiLayananDAO();
        transaksiLayananData.setId_customer_service(pegawai.getId_pegawai());
        transaksiLayananData.setId_hewan(-1);
        transaksiLayananBinding.setPegawai(pegawai);
        transaksiLayananBinding.setNamaHewanBind(nama_hewan_bind);
        transaksiLayananBinding.setTransaksiLayanan(transaksiLayananData);

        //setup recyclerview
        recyclerTambahLayanan = findViewById(R.id.recycler_view_tambah_layanan_transaksi);
        adapterTambahLayanan = new TambahTransaksiLayananAdapter(TambahTransaksiLayananActivity.this,
                transaksiLayananData, ListDetailTransaksiLayanan, ListHargaLayanan, ListNamaHargaLayanan,
                ListHargaLayananPilihan, transaksiLayananBinding);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTambahLayanan.setLayoutManager(mLayoutManager);
        recyclerTambahLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerTambahLayanan.setAdapter(adapterTambahLayanan);

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemLayanan = findViewById(R.id.btnTambahItemTransaksiLayanan);
        btnTambahItemLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiLayananDAO detailtransaksilayanan = new DetailTransaksiLayananDAO();
                detailtransaksilayanan.setJumlah(1);
                detailtransaksilayanan.setCreated_by(pegawai.getUsername());
                HargaLayananDAO hargalayanan = new HargaLayananDAO();
                ListDetailTransaksiLayanan.add(detailtransaksilayanan);
                ListHargaLayananPilihan.add(hargalayanan);
                adapterTambahLayanan.notifyItemInserted(ListDetailTransaksiLayanan.size());

                for(int i=0; i<ListDetailTransaksiLayanan.size();i++){
                    System.out.println("id produk detail: "+ListDetailTransaksiLayanan.get(i).getId_harga_layanan()+", id produk produk: "+ListHargaLayananPilihan.get(i).getId_harga_layanan());
                }

                hitungTotal();
            }
        });

        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnTambahTransaksiLayanan = findViewById(R.id.btnTambahTransaksiLayanan);
        btnTambahTransaksiLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data transaksi produk ======");
                System.out.println("id_customer_service: "+transaksiLayananData.getId_customer_service());
                System.out.println("id_hewan: "+transaksiLayananData.getId_hewan());
                System.out.println("subtotal: "+transaksiLayananData.getSubtotal());
                System.out.println("diskon: "+transaksiLayananData.getDiskon());
                System.out.println("total: "+transaksiLayananData.getTotal());
                System.out.println("");
                System.out.println("====== Data detail transaksi produk ======");
                for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayanan
                ) {
                    System.out.println("id harga layanan: "+detail.getId_harga_layanan());
                    System.out.println("jumlah: "+detail.getJumlah());
                    System.out.println("total harga: "+detail.getTotal_harga());
                }
                System.out.println("==========================");

                if(isEmptyCart()){
                    showStandardDialog("Tidak ada produk yang terdaftar pada transaksi ini, mohon tambahkan produk terlebih dahulu");
                }else{
                    if(transaksiLayananData.getId_hewan()==-1){
                        showDialogHewanNotFound();
                    }else if(isAnyWrongLayanan()){
                        showDialogAnyWrongHargaLayanan();
                    }else{
                        //fungsi tambah transaksi
                        System.out.println("TRANSAKSI DITAMBAHKAN");
                        tambahTransaksiLayanan();
                    }
                }
            }
        });
    }

    private void startIntent(TransaksiLayananDAO transaksi){
        Intent viewDetail = new Intent(this, TampilDetailTransaksiLayananActivity.class);

        Gson gson = new Gson();
        String transaksi_layanan = gson.toJson(transaksi);
        viewDetail.putExtra("transaksi_layanan",transaksi_layanan);
        viewDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewDetail);
    }

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

    public void getListHargaLayanan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargaLayananDAOCall = apiService.getAllHargaLayananAktif();

        hargaLayananDAOCall.enqueue(new Callback<GetHargaLayanan>() {
            @Override
            public void onResponse(Call<GetHargaLayanan> call, Response<GetHargaLayanan> response) {
                ListHargaLayanan.addAll(response.body().getListDataHargaLayanan());
                System.out.println(ListHargaLayanan.get(0).getId_harga_layanan());
                statHargaLayanan=1;
                System.out.println("==== After get Harga Layanan =====");
                System.out.println("statHargaLayanan: "+statHargaLayanan+", statLayanan: "+statLayanan+", statUkuranHewan: "+statUkuranHewan);
                if(statHargaLayanan==1 && statLayanan==1 && statUkuranHewan==1){
                    setListNamaHargaLayanan();
                }
            }

            @Override
            public void onFailure(Call<GetHargaLayanan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListLayanan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetLayanan> layananDAOCall = apiService.getAllLayanan();

        layananDAOCall.enqueue(new Callback<GetLayanan>() {
            @Override
            public void onResponse(Call<GetLayanan> call, Response<GetLayanan> response) {
                ListLayanan.addAll(response.body().getListDataLayanan());
                System.out.println(ListLayanan.get(0).getNama());
                statLayanan=1;
                System.out.println("==== After get Layanan =====");
                System.out.println("statHargaLayanan: "+statHargaLayanan+", statLayanan: "+statLayanan+", statUkuranHewan: "+statUkuranHewan);
                if(statHargaLayanan==1 && statLayanan==1 && statUkuranHewan==1){
                    setListNamaHargaLayanan();
                }
            }

            @Override
            public void onFailure(Call<GetLayanan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getListUkuranHewan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetUkuranHewan> ukuranDAOCall = apiService.getAllUkuranHewan();

        ukuranDAOCall.enqueue(new Callback<GetUkuranHewan>() {
            @Override
            public void onResponse(Call<GetUkuranHewan> call, Response<GetUkuranHewan> response) {
                ListUkuranHewan.addAll(response.body().getListDataUkuranHewan());
                System.out.println(ListUkuranHewan.get(0).getNama());
                statUkuranHewan=1;

                System.out.println("==== After get Ukuran hewan =====");
                System.out.println("statHargaLayanan: "+statHargaLayanan+", statLayanan: "+statLayanan+", statUkuranHewan: "+statUkuranHewan);
                if(statHargaLayanan==1 && statLayanan==1 && statUkuranHewan==1){
                    setListNamaHargaLayanan();
                }
            }

            @Override
            public void onFailure(Call<GetUkuranHewan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setListNamaHargaLayanan(){
        ListNamaHargaLayanan.clear();
        for (HargaLayananDAO item: ListHargaLayanan) {
            String nama = searchLayanan(item.getId_layanan()).getNama()+" "+searchUkuranHewan(item.getId_ukuran_hewan()).getNama();
            NamaHargaLayanan nama_harga = new NamaHargaLayanan(item.getId_harga_layanan(),nama);
            System.out.println("NAMA HARGA LAYANAN: id = "+nama_harga.getId_harga_layanan()+", nama = "+nama_harga.getNama_harga_layanan());
            ListNamaHargaLayanan.add(nama_harga);
        }
        System.out.println("LIST NAMA HARGA LAYANAN SET COMPLETED");
        adapterTambahLayanan.notifyDataSetChanged();
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

    public LayananDAO searchLayanan(int id)
    {
        for (LayananDAO layanan: ListLayanan
             ) {
            if(layanan.getId_layanan()==id){
                return layanan;
            }
        }
        return null;
    }

    public UkuranHewanDAO searchUkuranHewan(int id)
    {
        for (UkuranHewanDAO ukuran: ListUkuranHewan
        ) {
            if(ukuran.getId_ukuran_hewan()==id){
                return ukuran;
            }
        }
        return null;
    }

    public int hitungSubtotal(List<DetailTransaksiLayananDAO> detailtransaksilayanan){
        int subtotal=0;
        for (DetailTransaksiLayananDAO detail: detailtransaksilayanan
        ) {
            subtotal = subtotal+detail.getTotal_harga();
        }
        return subtotal;
    }

    public void hitungTotal(){
        transaksiLayananData.setSubtotal(hitungSubtotal(ListDetailTransaksiLayanan));
        int total_trans = transaksiLayananData.getSubtotal()-transaksiLayananData.getDiskon();
        if(total_trans<0){
            transaksiLayananData.setTotal(0);
        }else{
            transaksiLayananData.setTotal(total_trans);
        }
        System.out.println("subtotal: "+transaksiLayananData.getSubtotal());
        System.out.println("diskon: "+transaksiLayananData.getDiskon());
        System.out.println("total: "+transaksiLayananData.getTotal());
        transaksiLayananBinding.setTransaksiLayanan(transaksiLayananData);
    }

    private void showStandardDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form transaksi dengan benar!");
        alertDialogBuilder.setMessage(rules);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Siap!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // close dialog
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void showDialogHewanNotFound(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        //alertDialogBuilder.setTitle("Isi form transaksi dengan benar!");
        alertDialogBuilder.setMessage("Data hewan pelanggan tidak ditemukan, tetap lanjutkan bukan sebagai member?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Lanjut",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // close dialog
                        dialog.cancel();
                        if(isAnyWrongLayanan()) {
                            showDialogAnyWrongHargaLayanan();
                        }else {
                            //fungsi tambah transaksi
                            System.out.println("TRANSAKSI DITAMBAHKAN");
                            tambahTransaksiLayanan();
                        }
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void showDialogAnyWrongHargaLayanan(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        //alertDialogBuilder.setTitle("Isi form transaksi dengan benar!");
        alertDialogBuilder.setMessage("Ada layanan yang belum terdaftar di database, tetap lanjutkan dengan menghapus layanan tersebut?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Lanjut",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // close dialog
                        dialog.cancel();
                        //fungsi tambah transaksi
                        System.out.println("TRANSAKSI DITAMBAHKAN");
                        tambahTransaksiLayanan();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private boolean isAnyWrongLayanan(){
        for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayanan
        ) {
            if(detail.getId_harga_layanan()==0){
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyCart(){
        int count = 0;
        for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayanan
        ) {
            if(detail.getId_harga_layanan()!=0){
                count++;
            }
        }
        if(count==0){
            return true;
        }
        return false;
    }

    public void tambahTransaksiLayanan(){
        String id_hewan = String.valueOf(transaksiLayananData.getId_hewan());
        String diskon = transaksiLayananData.getStringDiskon();
        if(id_hewan.equalsIgnoreCase("-1")){
            id_hewan = null;
            diskon = null;
        }
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchTransaksiLayanan> transaksiLayananDAOCall = apiService.tambahTransaksiLayanan(String.valueOf(pegawai.getId_pegawai()),
                id_hewan,transaksiLayananData.getStringSubtotal(), diskon, transaksiLayananData.getStringTotal(),
                pegawai.getUsername());

        transaksiLayananDAOCall.enqueue(new Callback<SearchTransaksiLayanan>() {
            @Override
            public void onResponse(Call<SearchTransaksiLayanan> call, Response<SearchTransaksiLayanan> response) {
                TransaksiLayananDAO transaksi_layanan = response.body().getTransaksi_layanan();
                System.out.println(transaksi_layanan.getId_transaksi_layanan());
                tambahDetailTransaksiLayanan(transaksi_layanan);
            }

            @Override
            public void onFailure(Call<SearchTransaksiLayanan> call, Throwable t) {
                Toast.makeText(TambahTransaksiLayananActivity.this, "Gagal Membuat Transaksi Layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchTransaksiLayanan(String id_transaksi_layanan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchTransaksiLayanan> transaksiLayananDAOCall = apiService.searchTransaksiLayanan(id_transaksi_layanan);

        transaksiLayananDAOCall.enqueue(new Callback<SearchTransaksiLayanan>() {
            @Override
            public void onResponse(Call<SearchTransaksiLayanan> call, Response<SearchTransaksiLayanan> response) {
                TransaksiLayananDAO transaksi_layanan = response.body().getTransaksi_layanan();
                System.out.println(transaksi_layanan.getId_transaksi_layanan());
                startIntent(transaksi_layanan);
            }

            @Override
            public void onFailure(Call<SearchTransaksiLayanan> call, Throwable t) {
                //Toast.makeText(TambahTransaksiLayananActivity.this, "Gagal Membuat Transaksi Layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahDetailTransaksiLayanan(final TransaksiLayananDAO transaksi_layanan){
        List<DetailTransaksiLayananDAO> detail_temp = new ArrayList<>();

        if(isAnyWrongLayanan()){
            for (DetailTransaksiLayananDAO item: ListDetailTransaksiLayanan) {
                if(item.getId_harga_layanan()!=0){
                    detail_temp.add(item);
                }
            }
        }else{
            detail_temp = ListDetailTransaksiLayanan;
        }

        for(int i=0;i<detail_temp.size();i++){
            detail_temp.get(i).setId_transaksi_layanan(transaksi_layanan.getId_transaksi_layanan());
        }

        Gson gson = new Gson();
        String detail_transaksi_layanan = gson.toJson(detail_temp);

        System.out.println("DETAIL TRANSAKSI LAYANAN");
        System.out.println(detail_transaksi_layanan);
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.tambahDetailTransaksiLayananMultiple(detail_transaksi_layanan);

        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println(response.body().getMessage());
                Toast.makeText(TambahTransaksiLayananActivity.this, "Transaksi Layanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                searchTransaksiLayanan(transaksi_layanan.getId_transaksi_layanan());
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                hapusTransaksiLayanan(transaksi_layanan.getId_transaksi_layanan());
                Toast.makeText(TambahTransaksiLayananActivity.this, "Gagal Membuat Detail Transaksi Layanan", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void hapusTransaksiLayanan(String id_transaksi_layanan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.hapusTransaksiLayanan(id_transaksi_layanan);

        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println("transaksi layanan terhapus");
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println("gagal menghapus transaksi layanan");
            }
        });
    }


}
