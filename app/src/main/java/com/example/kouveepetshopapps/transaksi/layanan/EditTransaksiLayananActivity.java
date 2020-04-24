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
import com.example.kouveepetshopapps.adapter.EditTransaksiLayananAdapter;
import com.example.kouveepetshopapps.adapter.EditTransaksiProdukAdapter;
import com.example.kouveepetshopapps.adapter.TambahTransaksiLayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityEditTransaksiLayananBinding;
import com.example.kouveepetshopapps.databinding.ActivityEditTransaksiProdukBinding;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiLayananBinding;
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
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.GetDetailTransaksiLayanan;
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.example.kouveepetshopapps.transaksi.produk.EditTransaksiProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTransaksiLayananActivity extends AppCompatActivity {

    private AutoCompleteTextView nama_hewan;
    private EditText diskon;
    private Button btnTambahItemLayanan, btnEditTransaksiLayanan;

    private List<NamaHargaLayanan> ListNamaHargaLayanan;
    private List<HewanDAO> ListHewan;
    private List<HargaLayananDAO> ListHargaLayanan;
    private List<LayananDAO> ListLayanan;
    private List<UkuranHewanDAO> ListUkuranHewan;
    private List<DetailTransaksiLayananDAO> ListDetailTransaksiLayananTemp;
    private List<DetailTransaksiLayananDAO> ListDetailTransaksiLayanan;
    private List<HargaLayananDAO> ListHargaLayananPilihan;

    private RecyclerView recyclerEditLayanan;
    private EditTransaksiLayananAdapter adapterEditLayanan;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityEditTransaksiLayananBinding editTransaksiLayananBinding;
    private TransaksiLayananDAO editTransaksiLayananData;
    private HewanDAO nama_hewan_bind;
    private PegawaiDAO cust_service;

    private List<Integer> deleted_id_detail_transaksi;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    int statHargaLayanan=0, statUkuranHewan=0, statLayanan=0;

    public boolean edited_transaksi=false, added_detail=false, edited_detail=false, deleted_detail=false;
    public int countEditTransaksi=0, countEditDetail=0, countAddDetail=0, countDeleteDetail=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTransaksiLayananBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_transaksi_layanan);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //get list of hewan and layanan
        deleted_id_detail_transaksi = new ArrayList<>();
        ListHargaLayanan = new ArrayList<>();
        ListLayanan = new ArrayList<>();
        ListUkuranHewan = new ArrayList<>();
        ListNamaHargaLayanan = new ArrayList<>();
        ListHewan = new ArrayList<>();
        ListDetailTransaksiLayananTemp = new ArrayList<>();
        ListDetailTransaksiLayanan = new ArrayList<>();
        ListHargaLayananPilihan = new ArrayList<>();
        nama_hewan_bind = new HewanDAO();

        editTransaksiLayananData = gson.fromJson(getIntent().getStringExtra("transaksi_layanan"), TransaksiLayananDAO.class);
        searchCustService(editTransaksiLayananData.getId_customer_service());
        statHargaLayanan=0; statUkuranHewan=0; statLayanan=0;
        getListHewan();
        getListHargaLayanan();
        getListLayanan();
        getListUkuranHewan();

        //DATA BINDING SECTION
        editTransaksiLayananBinding.setTransaksiLayanan(editTransaksiLayananData);

        //create Autocompletetextview for nama hewan
        nama_hewan = findViewById(R.id.etNamaHewanEditTransaksiLayanan);
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
                editTransaksiLayananData.setId_hewan(id_hewan);
                if(id_hewan==-1){
                    editTransaksiLayananData.setDiskon(0);
                }
                editTransaksiLayananBinding.setTransaksiLayanan(editTransaksiLayananData);
            }
        });

        //SETUP TEXT WATCHER FOR DISKON FIELD
        diskon = findViewById(R.id.etDiskonEditTransaksiLayanan);
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

        //setup recyclerview
        recyclerEditLayanan = findViewById(R.id.recycler_view_edit_transaksi_layanan);
        adapterEditLayanan = new EditTransaksiLayananAdapter(EditTransaksiLayananActivity.this, editTransaksiLayananData,
                ListDetailTransaksiLayanan, ListHargaLayanan, ListNamaHargaLayanan, ListHargaLayananPilihan, editTransaksiLayananBinding, deleted_id_detail_transaksi);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerEditLayanan.setLayoutManager(mLayoutManager);
        recyclerEditLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerEditLayanan.setAdapter(adapterEditLayanan);

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemLayanan = findViewById(R.id.btnTambahItemEditTransaksiLayanan);
        btnTambahItemLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiLayananDAO detailtransaksilayanan = new DetailTransaksiLayananDAO();
                detailtransaksilayanan.setJumlah(1);
                detailtransaksilayanan.setCreated_by(pegawai.getUsername());
                HargaLayananDAO hargalayanan = new HargaLayananDAO();
                ListDetailTransaksiLayanan.add(detailtransaksilayanan);
                ListHargaLayananPilihan.add(hargalayanan);
                adapterEditLayanan.notifyItemInserted(ListDetailTransaksiLayanan.size());

                for(int i=0; i<ListDetailTransaksiLayanan.size();i++){
                    System.out.println("id layanan detail: "+ListDetailTransaksiLayanan.get(i).getId_harga_layanan()+", id layanan list: "+ListHargaLayananPilihan.get(i).getId_harga_layanan());
                }

                hitungTotal();
            }
        });

        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnEditTransaksiLayanan = findViewById(R.id.btnEditTransaksiLayanan);
        btnEditTransaksiLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data transaksi layanan ======");
                System.out.println("id_customer_service: "+editTransaksiLayananData.getId_customer_service());
                System.out.println("id_hewan: "+editTransaksiLayananData.getId_hewan());
                System.out.println("subtotal: "+editTransaksiLayananData.getSubtotal());
                System.out.println("diskon: "+editTransaksiLayananData.getDiskon());
                System.out.println("total: "+editTransaksiLayananData.getTotal());
                System.out.println("");
                System.out.println("====== Data detail transaksi layanan ======");
                for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayanan
                ) {
                    System.out.println("id harga layanan: "+detail.getId_harga_layanan());
                    System.out.println("jumlah: "+detail.getJumlah());
                    System.out.println("total harga: "+detail.getTotal_harga());
                }
                System.out.println("==========================");

                edited_transaksi=false; added_detail=false; edited_detail=false; deleted_detail=false;
                countEditTransaksi=0; countEditDetail=0; countAddDetail=0; countDeleteDetail=0;

                if(isEmptyCart()){
                    showStandardDialog("Tidak ada layanan yang terdaftar pada transaksi ini, mohon tambahkan layanan terlebih dahulu");
                }else{
                    if(editTransaksiLayananData.getId_hewan()==-1){
                        showDialogHewanNotFound();
                    }else if(isAnyWrongLayanan()){
                        showDialogAnyWrongHargaLayanan();
                    }else{
                        //fungsi tambah transaksi
                        System.out.println("TRANSAKSI DITAMBAHKAN");
                        editTransaksiLayanan();
                    }
                }
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(EditTransaksiLayananActivity.this, CsMainMenu.class);
        back.putExtra("from","transaksi");
        back.putExtra("firstView","layanan");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void getDetailTransaksiLayanan(TransaksiLayananDAO transaksi_layanan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetDetailTransaksiLayanan> detailtransaksilayananDAOCall = apiService.getDetailTransaksiLayananByIdTransaksi(
                transaksi_layanan.getId_transaksi_layanan());

        detailtransaksilayananDAOCall.enqueue(new Callback<GetDetailTransaksiLayanan>() {
            @Override
            public void onResponse(Call<GetDetailTransaksiLayanan> call, Response<GetDetailTransaksiLayanan> response) {
                if(!response.body().getListDataDetailTransaksiLayanan().isEmpty()){
                    ListDetailTransaksiLayanan.addAll(response.body().getListDataDetailTransaksiLayanan());
                    System.out.println(ListDetailTransaksiLayanan.get(0).getId_harga_layanan());
                    ListDetailTransaksiLayananTemp.clear();
                    for(int i=0;i<ListDetailTransaksiLayanan.size();i++){
                        DetailTransaksiLayananDAO temp = new DetailTransaksiLayananDAO(ListDetailTransaksiLayanan.get(i).getId_detail_transaksi_layanan(),
                                ListDetailTransaksiLayanan.get(i).getId_transaksi_layanan(),ListDetailTransaksiLayanan.get(i).getId_harga_layanan(),
                                ListDetailTransaksiLayanan.get(i).getJumlah(),ListDetailTransaksiLayanan.get(i).getTotal_harga(),
                                ListDetailTransaksiLayanan.get(i).getCreated_at(),ListDetailTransaksiLayanan.get(i).getCreated_by(),
                                ListDetailTransaksiLayanan.get(i).getModified_at(),ListDetailTransaksiLayanan.get(i).getModified_by());
                        ListDetailTransaksiLayananTemp.add(temp);
                        HargaLayananDAO harga = searchHargaLayanan(ListDetailTransaksiLayanan.get(i).getId_harga_layanan());
                        ListHargaLayananPilihan.add(harga);
                    }

                    adapterEditLayanan.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailTransaksiLayanan> call, Throwable t) {
                Toast.makeText(EditTransaksiLayananActivity.this, "Gagal menampilkan detail transaksi produk", Toast.LENGTH_SHORT).show();
            }
        });
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

                if(editTransaksiLayananData.getId_hewan()!=0){
                    HewanDAO hewan = findHewan(editTransaksiLayananData.getId_hewan());
                    if(hewan!=null){
                        nama_hewan_bind.setId_hewan(hewan.getId_hewan());
                        nama_hewan_bind.setAktif(hewan.getAktif());
                        nama_hewan_bind.setNama(hewan.getNama());
                        nama_hewan_bind.setId_pelanggan(hewan.getId_pelanggan());
                        nama_hewan_bind.setId_jenis_hewan(hewan.getId_jenis_hewan());
                        nama_hewan_bind.setTanggal_lahir(hewan.getTanggal_lahir());
                    }
                }else{
                    editTransaksiLayananData.setId_hewan(-1);
                }

                editTransaksiLayananBinding.setNamaHewanBind(nama_hewan_bind);
                System.out.println("id hewan edit transaksi data: "+editTransaksiLayananData.getId_hewan());

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
                getDetailTransaksiLayanan(editTransaksiLayananData);
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
        adapterEditLayanan.notifyDataSetChanged();
    }

    public void searchCustService(int id_customer_service){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPegawai> pegawaiDAOCall = apiService.searchPegawai(Integer.toString(id_customer_service));

        pegawaiDAOCall.enqueue(new Callback<SearchPegawai>() {
            @Override
            public void onResponse(Call<SearchPegawai> call, Response<SearchPegawai> response) {
                cust_service = response.body().getPegawai();
                editTransaksiLayananBinding.setPegawai(cust_service);
            }

            @Override
            public void onFailure(Call<SearchPegawai> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
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

    public HargaLayananDAO searchHargaLayanan(int id){
        for (HargaLayananDAO layanan: ListHargaLayanan
        ) {
            if(layanan.getId_harga_layanan()==id){
                return layanan;
            }
        }
        return null;
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
        editTransaksiLayananData.setSubtotal(hitungSubtotal(ListDetailTransaksiLayanan));
        int total_trans = editTransaksiLayananData.getSubtotal()-editTransaksiLayananData.getDiskon();
        if(total_trans<0){
            editTransaksiLayananData.setTotal(0);
        }else{
            editTransaksiLayananData.setTotal(total_trans);
        }
        System.out.println("subtotal: "+editTransaksiLayananData.getSubtotal());
        System.out.println("diskon: "+editTransaksiLayananData.getDiskon());
        System.out.println("total: "+editTransaksiLayananData.getTotal());
        editTransaksiLayananBinding.setTransaksiLayanan(editTransaksiLayananData);
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
                            editTransaksiLayanan();
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
                        editTransaksiLayanan();
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

    public void editTransaksiLayanan(){
        String id_hewan = String.valueOf(editTransaksiLayananData.getId_hewan());
        String diskon = editTransaksiLayananData.getStringDiskon();
        if(id_hewan.equalsIgnoreCase("-1")){
            id_hewan = null;
            diskon = null;
        }
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.ubahTransaksiLayanan(editTransaksiLayananData.getId_transaksi_layanan(),
                id_hewan,editTransaksiLayananData.getStringSubtotal(), diskon, editTransaksiLayananData.getStringTotal(),
                pegawai.getUsername());

        System.out.println("(isExecuted) Sebelum edit transaksi layanan: "+transaksiLayananDAOCall.isExecuted());
        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                countEditTransaksi=1;
                String id_transaksi_layanan = response.body().getMessage();
                System.out.println(id_transaksi_layanan);
                Toast.makeText(EditTransaksiLayananActivity.this, "Transaksi Layanan Berhasil Diubah", Toast.LENGTH_SHORT).show();
                tambahDetailTransaksiLayanan(editTransaksiLayananData.getId_transaksi_layanan());
                editDetailTransaksiLayanan(ListDetailTransaksiLayanan);
                hapusDetailTransaksiLayanan();
                if(countEditTransaksi==1){
                    System.out.println("==== after editTrans ====");
                    System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);
                    if(countAddDetail==1 && countEditDetail==1 && countDeleteDetail==1){
                        startIntent();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                countEditTransaksi=-1;
                Toast.makeText(EditTransaksiLayananActivity.this, "Transaksi Produk Gagal Diubah", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println("(isExecuted) Setelah edit transaksi produk: "+transaksiLayananDAOCall.isExecuted());
        if(countEditTransaksi==1 && transaksiLayananDAOCall.isExecuted()){
            System.out.println("==== after editTrans ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);
            if(countAddDetail==1 && countEditDetail==1 && countDeleteDetail==1){
                startIntent();
            }
        }
    }

    public void tambahDetailTransaksiLayanan(String id_transaksi_layanan){
        List<DetailTransaksiLayananDAO> added_list = new ArrayList<>();

        for (DetailTransaksiLayananDAO detail : ListDetailTransaksiLayanan) {
            if(detail.getId_detail_transaksi_layanan()==0){
                DetailTransaksiLayananDAO temp = detail;
                temp.setId_transaksi_layanan(id_transaksi_layanan);
                added_list.add(temp);
            }
        }

        if(added_list.isEmpty()){
            countAddDetail=1;
        }else{
            List<DetailTransaksiLayananDAO> detail_temp = new ArrayList<>();
            for (DetailTransaksiLayananDAO item: added_list) {
                if(item.getId_harga_layanan()!=0){
                    detail_temp.add(item);
                }
            }
//            for(int i=0;i<detail_temp.size();i++){
//                detail_temp.get(i).setId_transaksi_layanan(id_transaksi_layanan);
//            }
            Gson gson = new Gson();
            String detail_transaksi_layanan = gson.toJson(detail_temp);

            System.out.println("DETAIL TRANSAKSI DITAMBAH");
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.tambahDetailTransaksiLayananMultiple(detail_transaksi_layanan);

            transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    countAddDetail=1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Transaksi Layanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    if (countAddDetail==1){
                        System.out.println("==== after addDetail ====");
                        System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

                        if(countEditTransaksi==1 && countEditDetail==1 && countDeleteDetail==1){
                            startIntent();
                        }

                    }
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    //hapusTransaksiProduk(id_transaksi_produk);
                    countAddDetail=-1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Transaksi Layanan Gagal Ditambahkan", Toast.LENGTH_SHORT).show();

                }
            });
            added_detail=transaksiLayananDAOCall.isExecuted();
        }

        if (countAddDetail==1){
            System.out.println("==== after addDetail ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

            if(countEditTransaksi==1 && countEditDetail==1 && countDeleteDetail==1){
                startIntent();
            }
        }
    }

    public void editDetailTransaksiLayanan(List<DetailTransaksiLayananDAO> ListDetailTransaksiLayanan){
        List<DetailTransaksiLayananDAO> detail_edited = new ArrayList<>();

        System.out.println("Cek perbandingan List detail temp dan list detail edit");
        System.out.println("List Detail Temp");
        for (DetailTransaksiLayananDAO item: ListDetailTransaksiLayananTemp){
            System.out.println("id detail: "+item.getId_detail_transaksi_layanan()+", id transaksi: "+item.getId_transaksi_layanan()+
                    ", id produk: "+item.getId_harga_layanan()+", jumlah: "+item.getJumlah());
        }
        System.out.println("List Detail edit");
        for (DetailTransaksiLayananDAO item: ListDetailTransaksiLayanan){
            System.out.println("id detail: "+item.getId_detail_transaksi_layanan()+", id transaksi: "+item.getId_transaksi_layanan()+
                    ", id produk: "+item.getId_harga_layanan()+", jumlah: "+item.getJumlah());
        }

        for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayanan) {
//            System.out.println("Masuk FOR edit detail: id="+detail.getId_detail_transaksi_produk()+" id produk: "+detail.getId_produk()+
//                    " jumlah: "+detail.getJumlah());
            if(detail.getId_detail_transaksi_layanan()!=0){
                DetailTransaksiLayananDAO compare = findDetailTransaksiLayananTemp(detail.getId_detail_transaksi_layanan());
//                System.out.println("Compare variable: id="+compare.getId_detail_transaksi_produk()+" id produk: "+compare.getId_produk()+
//                        " jumlah: "+compare.getJumlah());
                if(detail.getId_harga_layanan()!=compare.getId_harga_layanan() || detail.getJumlah()!=compare.getJumlah()){
                    detail.setModified_by(pegawai.getUsername());
                    detail_edited.add(detail);
//                    System.out.println("edited item: "+detail.getId_detail_transaksi_produk());
                }
            }
        }

        if (detail_edited.isEmpty()){
            countEditDetail=1;
        }else{
            System.out.println("DETAIL TRANSAKSI DIUBAH");
            Gson gson = new Gson();
            String detail_transaksi_layanan = gson.toJson(detail_edited);

            System.out.println("Edited json: "+detail_transaksi_layanan);
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.ubahDetailTransaksiLayananMultiple(detail_transaksi_layanan);

            transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    countEditDetail=1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Detail Transaksi Layanan Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    if(countEditDetail==1){
                        System.out.println("==== after editDetail ====");
                        System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

                        if(countEditTransaksi==1 && countAddDetail==1 && countDeleteDetail==1){
                            startIntent();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    System.out.println(t.getMessage());
                    countEditDetail=-1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Detail Transaksi Layanan Gagal Diubah", Toast.LENGTH_SHORT).show();

                }
            });
        }
        if(countEditDetail==1){
            System.out.println("==== after editDetail ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

            if(countEditTransaksi==1 && countAddDetail==1 && countDeleteDetail==1){
                startIntent();
            }
        }
    }

    private void hapusDetailTransaksiLayanan(){
        if(deleted_id_detail_transaksi.isEmpty()){
            countDeleteDetail=1;

        }else{
            String[] deleted_id = new String[deleted_id_detail_transaksi.size()];
            int i = 0;
            for (Integer id_detail: deleted_id_detail_transaksi
            ) {
                deleted_id[i] = String.valueOf(id_detail);
                i++;
            }

            Gson gson = new Gson();
            String id_detail_transaksi_layanan = gson.toJson(deleted_id);

            System.out.println("deleted id detail transaksi layanan: "+id_detail_transaksi_layanan);
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> detailTransaksiLayananDAOCall = apiService.hapusDetailTransaksiLayananMultiple(id_detail_transaksi_layanan);

            detailTransaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    //reverse close
                    System.out.println(response.body().getMessage());
                    countDeleteDetail=1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Detail Transaksi Layanan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    if(countDeleteDetail==1){
                        System.out.println("==== after deleteDetail ====");
                        System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

                        if(countEditTransaksi==1 && countAddDetail==1 && countEditDetail==1){
                            startIntent();
                        }
                    }
                    //                delete(position);
                }
                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    System.out.println(t.getMessage());
                    countDeleteDetail=-1;
                    Toast.makeText(EditTransaksiLayananActivity.this, "Detail Transaksi Layanan Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Gagal menghapus data hewan", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(countDeleteDetail==1){
            System.out.println("==== after deleteDetail ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

            if(countEditTransaksi==1 && countAddDetail==1 && countEditDetail==1){
                startIntent();
            }
        }
    }

    public HewanDAO findHewan(int id_hewan){
        for (HewanDAO hewan:ListHewan
        ) {
            if(hewan.getId_hewan()==id_hewan){
                return hewan;
            }
        }
        return null;
    }

    public DetailTransaksiLayananDAO findDetailTransaksiLayananTemp(int id_detail_transaksi_layanan){
        for (DetailTransaksiLayananDAO detail: ListDetailTransaksiLayananTemp
        ) {
            if(detail.getId_detail_transaksi_layanan()==id_detail_transaksi_layanan){
                return detail;
            }
        }
        return null;
    }
}
