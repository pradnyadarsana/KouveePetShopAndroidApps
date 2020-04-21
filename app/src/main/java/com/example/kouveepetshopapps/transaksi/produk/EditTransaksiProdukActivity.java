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
import com.example.kouveepetshopapps.adapter.EditTransaksiProdukAdapter;
import com.example.kouveepetshopapps.adapter.TambahTransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityEditTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTransaksiProdukActivity extends AppCompatActivity {
    private AutoCompleteTextView nama_hewan;
    private EditText diskon;
    private Button btnTambahItemProduk, btnEditTransaksiProduk;

    private List<HewanDAO> ListHewan;
    private List<ProdukDAO> ListProduk;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProdukTemp;
    private List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk;
    private List<ProdukDAO> ListProdukPilihan;

    private RecyclerView recyclerEditProduk;
    private EditTransaksiProdukAdapter adapterEditProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityEditTransaksiProdukBinding editTransaksiProdukBinding;
    private TransaksiProdukDAO editTransaksiProdukData;
    private HewanDAO nama_hewan_bind;
    private PegawaiDAO cust_service;

    private List<Integer> deleted_id_detail_transaksi;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTransaksiProdukBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_transaksi_produk);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //get all data
        deleted_id_detail_transaksi = new ArrayList<>();
        ListProduk = new ArrayList<>();
        ListHewan = new ArrayList<>();
        ListDetailTransaksiProdukTemp = new ArrayList<>();
        ListDetailTransaksiProduk = new ArrayList<>();
        ListProdukPilihan = new ArrayList<>();
        nama_hewan_bind = new HewanDAO();

        editTransaksiProdukData = gson.fromJson(getIntent().getStringExtra("transaksi_produk"), TransaksiProdukDAO.class);
        searchCustService(editTransaksiProdukData.getId_customer_service());
        getListHewan();

        //DATA BINDING SECTION
        editTransaksiProdukBinding.setTransaksiProduk(editTransaksiProdukData);

        //create Autocompletetextview for nama hewan
        nama_hewan = findViewById(R.id.etNamaHewanEditTransaksiProduk);
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
                editTransaksiProdukData.setId_hewan(id_hewan);
                if(id_hewan==-1){
                    editTransaksiProdukData.setDiskon(0);
                }
                editTransaksiProdukBinding.setTransaksiProduk(editTransaksiProdukData);
            }
        });

        //SETUP TEXT WATCHER FOR DISKON FIELD
        diskon = findViewById(R.id.etDiskonEditTransaksiProduk);
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
        recyclerEditProduk = findViewById(R.id.recycler_view_edit_transaksi_produk);
        adapterEditProduk = new EditTransaksiProdukAdapter(EditTransaksiProdukActivity.this, editTransaksiProdukData,
                ListDetailTransaksiProduk, ListProduk, ListProdukPilihan, editTransaksiProdukBinding, deleted_id_detail_transaksi);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerEditProduk.setLayoutManager(mLayoutManager);
        recyclerEditProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerEditProduk.setAdapter(adapterEditProduk);

        getListProduk();

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemProduk = findViewById(R.id.btnTambahItemEditTransaksiProduk);
        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTransaksiProdukDAO detailtransaksiproduk = new DetailTransaksiProdukDAO();
                detailtransaksiproduk.setJumlah(1);
                detailtransaksiproduk.setCreated_by(pegawai.getUsername());
                ProdukDAO produk = new ProdukDAO();
                ListDetailTransaksiProduk.add(detailtransaksiproduk);
                ListProdukPilihan.add(produk);
                adapterEditProduk.notifyItemInserted(ListDetailTransaksiProduk.size());

                for(int i=0; i<ListDetailTransaksiProduk.size();i++){
                    System.out.println("id produk detail: "+ListDetailTransaksiProduk.get(i).getId_produk()+", id produk produk: "+ListProdukPilihan.get(i).getId_produk());
                }

                hitungTotal();
            }
        });

        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnEditTransaksiProduk = findViewById(R.id.btnEditTransaksiProduk);
        btnEditTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data transaksi produk ======");
                System.out.println("id_customer_service: "+editTransaksiProdukData.getId_customer_service());
                System.out.println("id_hewan: "+editTransaksiProdukData.getId_hewan());
                System.out.println("subtotal: "+editTransaksiProdukData.getSubtotal());
                System.out.println("diskon: "+editTransaksiProdukData.getDiskon());
                System.out.println("total: "+editTransaksiProdukData.getTotal());
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
                    if(editTransaksiProdukData.getId_hewan()==-1){
                        showDialogHewanNotFound();
                    }else if(isAnyWrongProduct()){
                        showDialogAnyWrongProduct();
                    }else{
                        //fungsi tambah transaksi
                        System.out.println("TRANSAKSI DIUBAH");
                        editTransaksiProduk();
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

    public void getListProduk(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetProduk> produkDAOCall = apiService.getAllProdukAktif();

        produkDAOCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                ListProduk.addAll(response.body().getListDataProduk());
                System.out.println(ListProduk.get(0).getNama());
                getDetailTransaksiProduk(editTransaksiProdukData);
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetailTransaksiProduk(TransaksiProdukDAO transaksi_produk){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetDetailTransaksiProduk> detailtransaksiprodukDAOCall = apiService.getDetailTransaksiProdukByIdTransaksi(
                transaksi_produk.getId_transaksi_produk());

        detailtransaksiprodukDAOCall.enqueue(new Callback<GetDetailTransaksiProduk>() {
            @Override
            public void onResponse(Call<GetDetailTransaksiProduk> call, Response<GetDetailTransaksiProduk> response) {
                if(!response.body().getListDataDetailTransaksiProduk().isEmpty()){
                    ListDetailTransaksiProduk.addAll(response.body().getListDataDetailTransaksiProduk());
                    System.out.println(ListDetailTransaksiProduk.get(0).getId_produk());

                    for(int i=0;i<ListDetailTransaksiProduk.size();i++){
                        DetailTransaksiProdukDAO temp = new DetailTransaksiProdukDAO(ListDetailTransaksiProduk.get(i).getId_detail_transaksi_produk(),
                                ListDetailTransaksiProduk.get(i).getId_transaksi_produk(),ListDetailTransaksiProduk.get(i).getId_produk(),
                                ListDetailTransaksiProduk.get(i).getJumlah(),ListDetailTransaksiProduk.get(i).getTotal_harga(),
                                ListDetailTransaksiProduk.get(i).getCreated_at(),ListDetailTransaksiProduk.get(i).getCreated_by(),
                                ListDetailTransaksiProduk.get(i).getModified_at(),ListDetailTransaksiProduk.get(i).getModified_by());
                        ListDetailTransaksiProdukTemp.add(temp);
                        //ListDetailTransaksiProdukTemp.set(i,ListDetailTransaksiProduk.get(i));
                        ProdukDAO produk = findProduk(ListDetailTransaksiProduk.get(i).getId_produk());
                        ListProdukPilihan.add(produk);
                    }

                    adapterEditProduk.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailTransaksiProduk> call, Throwable t) {
                Toast.makeText(EditTransaksiProdukActivity.this, "Gagal menampilkan detail transaksi produk", Toast.LENGTH_SHORT).show();
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

                if(editTransaksiProdukData.getId_hewan()!=0){
                    HewanDAO hewan = findHewan(editTransaksiProdukData.getId_hewan());
                    if(hewan!=null){
                        nama_hewan_bind.setId_hewan(hewan.getId_hewan());
                        nama_hewan_bind.setAktif(hewan.getAktif());
                        nama_hewan_bind.setNama(hewan.getNama());
                        nama_hewan_bind.setId_pelanggan(hewan.getId_pelanggan());
                        nama_hewan_bind.setId_jenis_hewan(hewan.getId_jenis_hewan());
                        nama_hewan_bind.setTanggal_lahir(hewan.getTanggal_lahir());
                    }
                }else{
                    editTransaksiProdukData.setId_hewan(-1);
                }

                editTransaksiProdukBinding.setNamaHewanBind(nama_hewan_bind);
                System.out.println("id hewan edit transaksi data: "+editTransaksiProdukData.getId_hewan());
                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetHewan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
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
                editTransaksiProdukBinding.setPegawai(cust_service);
            }

            @Override
            public void onFailure(Call<SearchPegawai> call, Throwable t) {
                //holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    public int getIdHewan(String nama)
    {
        for (HewanDAO hewan:ListHewan) {
            System.out.println("nama hewan: "+hewan.getNama()+" parameter nama: "+nama);
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
        editTransaksiProdukData.setSubtotal(hitungSubtotal(ListDetailTransaksiProduk));
        int total_trans = editTransaksiProdukData.getSubtotal()-editTransaksiProdukData.getDiskon();
        if(total_trans<0){
            editTransaksiProdukData.setTotal(0);
        }else{
            editTransaksiProdukData.setTotal(total_trans);
        }
        System.out.println("subtotal: "+editTransaksiProdukData.getSubtotal());
        System.out.println("diskon: "+editTransaksiProdukData.getDiskon());
        System.out.println("total: "+editTransaksiProdukData.getTotal());
        editTransaksiProdukBinding.setTransaksiProduk(editTransaksiProdukData);
    }

    private void showStandardDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTransaksiProdukActivity.this);

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTransaksiProdukActivity.this);

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
                            System.out.println("TRANSAKSI DIUBAH");
                            editTransaksiProduk();
                        }
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void showDialogAnyWrongProduct(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTransaksiProdukActivity.this);

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
                        System.out.println("TRANSAKSI DIUBAH");
                        editTransaksiProduk();
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

    public void editTransaksiProduk(){
        String id_hewan = String.valueOf(editTransaksiProdukData.getId_hewan());
        String diskon = editTransaksiProdukData.getStringDiskon();
        if(id_hewan.equalsIgnoreCase("-1")){
            id_hewan = null;
            diskon = null;
        }
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.ubahTransaksiProduk(editTransaksiProdukData.getId_transaksi_produk(),
                id_hewan,editTransaksiProdukData.getStringSubtotal(), diskon, editTransaksiProdukData.getStringTotal(),
                pegawai.getUsername());

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                String id_transaksi_produk = response.body().getMessage();
                System.out.println(id_transaksi_produk);
                Toast.makeText(EditTransaksiProdukActivity.this, "Transaksi Produk Berhasil Diubah", Toast.LENGTH_SHORT).show();
                tambahDetailTransaksiProduk(editTransaksiProdukData.getId_transaksi_produk());
                editDetailTransaksiProduk(ListDetailTransaksiProduk);
                hapusDetailTransaksiProduk();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditTransaksiProdukActivity.this, "Transaksi Produk Gagal Diubah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahDetailTransaksiProduk(String id_transaksi_produk){
        List<DetailTransaksiProdukDAO> added_list = new ArrayList<>();

        for (DetailTransaksiProdukDAO detail : ListDetailTransaksiProduk) {
            if(detail.getId_detail_transaksi_produk()==0){
                DetailTransaksiProdukDAO temp = detail;
                temp.setId_transaksi_produk(id_transaksi_produk);
                added_list.add(temp);
            }
        }

        if(!added_list.isEmpty()){
            List<DetailTransaksiProdukDAO> detail_temp = new ArrayList<>();
            for (DetailTransaksiProdukDAO item: added_list) {
                if(item.getId_produk()!=0){
                    detail_temp.add(item);
                }
            }
            for(int i=0;i<detail_temp.size();i++){
                detail_temp.get(i).setId_transaksi_produk(id_transaksi_produk);
            }
            Gson gson = new Gson();
            String detail_transaksi_produk = gson.toJson(detail_temp);

            System.out.println("DETAIL TRANSAKSI DITAMBAH");
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.tambahDetailTransaksiProdukMultiple(detail_transaksi_produk);

            transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    Toast.makeText(EditTransaksiProdukActivity.this, "Transaksi Produk Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    //hapusTransaksiProduk(id_transaksi_produk);
                    Toast.makeText(EditTransaksiProdukActivity.this, "Transaksi Produk Gagal Ditambahkan", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void editDetailTransaksiProduk(List<DetailTransaksiProdukDAO> ListDetailTransaksiProduk){
        List<DetailTransaksiProdukDAO> detail_edited = new ArrayList<>();

        System.out.println("Cek perbandingan List detail temp dan list detail edit");
        System.out.println("List Detail Temp");
        for (DetailTransaksiProdukDAO item: ListDetailTransaksiProdukTemp){
            System.out.println("id detail: "+item.getId_detail_transaksi_produk()+", id transaksi: "+item.getId_transaksi_produk()+
                    ", id produk: "+item.getId_produk()+", jumlah: "+item.getJumlah());
        }
        System.out.println("List Detail edit");
        for (DetailTransaksiProdukDAO item: ListDetailTransaksiProduk){
            System.out.println("id detail: "+item.getId_detail_transaksi_produk()+", id transaksi: "+item.getId_transaksi_produk()+
                    ", id produk: "+item.getId_produk()+", jumlah: "+item.getJumlah());
        }

        for (DetailTransaksiProdukDAO detail: ListDetailTransaksiProduk) {
//            System.out.println("Masuk FOR edit detail: id="+detail.getId_detail_transaksi_produk()+" id produk: "+detail.getId_produk()+
//                    " jumlah: "+detail.getJumlah());
            if(detail.getId_detail_transaksi_produk()!=0){
                DetailTransaksiProdukDAO compare = findDetailTransaksiProdukTemp(detail.getId_detail_transaksi_produk());
//                System.out.println("Compare variable: id="+compare.getId_detail_transaksi_produk()+" id produk: "+compare.getId_produk()+
//                        " jumlah: "+compare.getJumlah());
                if(detail.getId_produk()!=compare.getId_produk() || detail.getJumlah()!=compare.getJumlah()){
                    detail.setModified_by(pegawai.getUsername());
                    detail_edited.add(detail);
//                    System.out.println("edited item: "+detail.getId_detail_transaksi_produk());
                }
            }
        }

        if (!detail_edited.isEmpty()){
            System.out.println("DETAIL TRANSAKSI DIUBAH");
            Gson gson = new Gson();
            String detail_transaksi_produk = gson.toJson(detail_edited);

            System.out.println("Edited json: "+detail_transaksi_produk);
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.ubahDetailTransaksiProdukMultiple(detail_transaksi_produk);

            transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    Toast.makeText(EditTransaksiProdukActivity.this, "Detail Transaksi Produk Berhasil Diubah", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(EditTransaksiProdukActivity.this, "Detail Transaksi Produk Gagal Diubah", Toast.LENGTH_SHORT).show();

                }
            });
        }

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



    private void hapusDetailTransaksiProduk(){
        if(!deleted_id_detail_transaksi.isEmpty()){
            String[] deleted_id = new String[deleted_id_detail_transaksi.size()];
            int i = 0;
            for (Integer id_detail: deleted_id_detail_transaksi
            ) {
                deleted_id[i] = String.valueOf(id_detail);
                i++;
            }

            Gson gson = new Gson();
            String id_detail_transaksi_produk = gson.toJson(deleted_id);

            System.out.println("deleted id detail transaksi produk: "+id_detail_transaksi_produk);
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> detailTransaksiProdukDAOCall = apiService.hapusDetailTransaksiProdukMultiple(id_detail_transaksi_produk);

            detailTransaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    //reverse close
                    System.out.println(response.body().getMessage());
                    Toast.makeText(EditTransaksiProdukActivity.this, "Detail Transaksi Produk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
//                delete(position);
                }
                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Toast.makeText(EditTransaksiProdukActivity.this, "Detail Transaksi Produk Gagal Dihapus", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Gagal menghapus data hewan", Toast.LENGTH_SHORT).show();
                }
            });
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

    public ProdukDAO findProduk(int id_produk){
        for (ProdukDAO produk: ListProduk
        ) {
            if(produk.getId_produk()==id_produk){
                return produk;
            }
        }
        return null;
    }

    public DetailTransaksiProdukDAO findDetailTransaksiProdukTemp(int id_detail_transaksi_produk){
        for (DetailTransaksiProdukDAO detail: ListDetailTransaksiProdukTemp
        ) {
            if(detail.getId_detail_transaksi_produk()==id_detail_transaksi_produk){
                return detail;
            }
        }
        return null;
    }
}
