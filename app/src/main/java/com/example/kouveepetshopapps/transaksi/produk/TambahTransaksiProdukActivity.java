package com.example.kouveepetshopapps.transaksi.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.kouveepetshopapps.adapter.TambahTransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiProdukBinding;
import com.example.kouveepetshopapps.hewan.TambahHewanActivity;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksiProdukActivity extends AppCompatActivity {
    private AutoCompleteTextView nama_hewan;
    private EditText diskon;
    private Button btnTambahItemProduk, btnTambahTransaksiProduk;

    private List<HewanDAO> ListHewan;
    private List<ProdukDAO> ListProduk;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;
    private List<ProdukDAO> ListProdukPilihan;

    private RecyclerView recyclerTambahProduk;
    private TambahTransaksiProdukAdapter adapterTambahProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityTambahTransaksiProdukBinding transaksiProdukBinding;
    private TransaksiProdukDAO transaksiProdukData;
    private HewanDAO nama_hewan_bind;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    int formCheckCount;

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
                if(id_hewan==-1){
                    transaksiProdukData.setDiskon(0);
                }
                transaksiProdukBinding.setTransaksiProduk(transaksiProdukData);
            }
        });

        //SETUP TEXT WATCHER FOR DISKON FIELD
        diskon = findViewById(R.id.etDiskonTransaksiProduk);
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
        transaksiProdukData.setId_hewan(-1);
        transaksiProdukBinding.setPegawai(pegawai);
        transaksiProdukBinding.setNamaHewanBind(nama_hewan_bind);
        transaksiProdukBinding.setTransaksiProduk(transaksiProdukData);

        //setup recyclerview
        recyclerTambahProduk = findViewById(R.id.recycler_view_tambah_produk_transaksi);
        adapterTambahProduk = new TambahTransaksiProdukAdapter(TambahTransaksiProdukActivity.this, transaksiProdukData, ListDetailTransaksiProduk, ListProduk, ListProdukPilihan, transaksiProdukBinding);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTambahProduk.setLayoutManager(mLayoutManager);
        recyclerTambahProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerTambahProduk.setAdapter(adapterTambahProduk);
        //setRecyclerView();

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemProduk = findViewById(R.id.btnTambahItemTransaksiProduk);
        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiProdukDAO detailtransaksiproduk = new DetailTransaksiProdukDAO();
                detailtransaksiproduk.setJumlah(1);
                detailtransaksiproduk.setCreated_by(pegawai.getUsername());
                ProdukDAO produk = new ProdukDAO();
                ListDetailTransaksiProduk.add(detailtransaksiproduk);
                ListProdukPilihan.add(produk);
                adapterTambahProduk.notifyItemInserted(ListDetailTransaksiProduk.size());

                for(int i=0; i<ListDetailTransaksiProduk.size();i++){
                    System.out.println("id produk detail: "+ListDetailTransaksiProduk.get(i).getId_produk()+", id produk produk: "+ListProdukPilihan.get(i).getId_produk());
                }

                hitungTotal();
            }
        });


        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnTambahTransaksiProduk = findViewById(R.id.btnTambahTransaksiProduk);
        btnTambahTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data transaksi produk ======");
                System.out.println("id_customer_service: "+transaksiProdukData.getId_customer_service());
                System.out.println("id_hewan: "+transaksiProdukData.getId_hewan());
                System.out.println("subtotal: "+transaksiProdukData.getSubtotal());
                System.out.println("diskon: "+transaksiProdukData.getDiskon());
                System.out.println("total: "+transaksiProdukData.getTotal());
                System.out.println("");
                System.out.println("====== Data detail transaksi produk ======");
                for (DetailTransaksiProdukDAO detail: ListDetailTransaksiProduk
                     ) {
                    System.out.println("id produk: "+detail.getId_produk());
                    System.out.println("jumlah: "+detail.getJumlah());
                    System.out.println("total harga: "+detail.getTotal_harga());
                }
                System.out.println("==========================");

                if(isEmptyCart()){
                    showStandardDialog("Tidak ada produk yang terdaftar pada transaksi ini, mohon tambahkan produk terlebih dahulu");
                }else{
                    if(transaksiProdukData.getId_hewan()==-1){
                        showDialogHewanNotFound();
                    }else if(isAnyWrongProduct()){
                        showDialogAnyWrongProduct();
                    }else{
                        //fungsi tambah transaksi
                        System.out.println("TRANSAKSI DITAMBAHKAN");
                        tambahTransaksiProduk();
                    }
                }

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

    public int hitungSubtotal(List<DetailTransaksiProdukDAO> detailtransaksiproduk){
        int subtotal=0;
        for (DetailTransaksiProdukDAO detail: detailtransaksiproduk
        ) {
            subtotal = subtotal+detail.getTotal_harga();
        }
        return subtotal;
    }

    public void hitungTotal(){
        transaksiProdukData.setSubtotal(hitungSubtotal(ListDetailTransaksiProduk));
        int total_trans = transaksiProdukData.getSubtotal()-transaksiProdukData.getDiskon();
        if(total_trans<0){
            transaksiProdukData.setTotal(0);
        }else{
            transaksiProdukData.setTotal(total_trans);
        }
        System.out.println("subtotal: "+transaksiProdukData.getSubtotal());
        System.out.println("diskon: "+transaksiProdukData.getDiskon());
        System.out.println("total: "+transaksiProdukData.getTotal());
        transaksiProdukBinding.setTransaksiProduk(transaksiProdukData);
    }

    private void showStandardDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahTransaksiProdukActivity.this);

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahTransaksiProdukActivity.this);

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
                        if(isAnyWrongProduct()) {
                            showDialogAnyWrongProduct();
                        }else {
                            //fungsi tambah transaksi
                            System.out.println("TRANSAKSI DITAMBAHKAN");
                            tambahTransaksiProduk();
                        }
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void showDialogAnyWrongProduct(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahTransaksiProdukActivity.this);

        // set title dialog
        //alertDialogBuilder.setTitle("Isi form transaksi dengan benar!");
        alertDialogBuilder.setMessage("Ada produk yang belum terdaftar di database, tetap lanjutkan dengan menghapus produk tersebut?");

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
                        tambahTransaksiProduk();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private boolean isAnyWrongProduct(){
        for (DetailTransaksiProdukDAO detail: ListDetailTransaksiProduk
        ) {
            if(detail.getId_produk()==0){
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyCart(){
        int count = 0;
        for (DetailTransaksiProdukDAO detail: ListDetailTransaksiProduk
        ) {
            if(detail.getId_produk()!=0){
                count++;
            }
        }
        if(count==0){
            return true;
        }
        return false;
    }

    public void tambahTransaksiProduk(){
        String id_hewan = String.valueOf(transaksiProdukData.getId_hewan());
        String diskon = transaksiProdukData.getStringDiskon();
        if(id_hewan.equalsIgnoreCase("-1")){
            id_hewan = null;
            diskon = null;
        }
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.tambahTransaksiProduk(String.valueOf(pegawai.getId_pegawai()),
                id_hewan,transaksiProdukData.getStringSubtotal(), diskon, transaksiProdukData.getStringTotal(),
                pegawai.getUsername());

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                String id_transaksi_produk = response.body().getMessage();
                System.out.println(id_transaksi_produk);
                tambahDetailTransaksiProduk(id_transaksi_produk);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(TambahTransaksiProdukActivity.this, "Transaksi Produk Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahDetailTransaksiProduk(final String id_transaksi_produk){
        for(int i=0;i<ListDetailTransaksiProduk.size();i++){
            ListDetailTransaksiProduk.get(i).setId_transaksi_produk(id_transaksi_produk);
        }

        Gson gson = new Gson();
        String detail_transaksi_produk = gson.toJson(ListDetailTransaksiProduk);

        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.tambahDetailTransaksiProdukMultiple(detail_transaksi_produk);

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println(response.body().getMessage());
                Toast.makeText(TambahTransaksiProdukActivity.this, "Transaksi Produk Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                hapusTransaksiProduk(id_transaksi_produk);
                Toast.makeText(TambahTransaksiProdukActivity.this, "Transaksi Produk Gagal", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void hapusTransaksiProduk(String id_transaksi_produk){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.hapusTransaksiProduk(id_transaksi_produk);

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println("transaksi produk terhapus");
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println("gagal menghapus transaksi produk");
            }
        });
    }
}
