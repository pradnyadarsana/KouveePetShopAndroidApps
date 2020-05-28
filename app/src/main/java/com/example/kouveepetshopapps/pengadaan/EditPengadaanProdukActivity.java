package com.example.kouveepetshopapps.pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.util.Supplier;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
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
import com.example.kouveepetshopapps.adapter.EditPengadaanProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityEditPengadaanProdukBinding;
import com.example.kouveepetshopapps.model.DetailPengadaanDAO;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.navigation.AdminManagePageFragment;
import com.example.kouveepetshopapps.response.GetDetailPengadaan;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.google.gson.Gson;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class EditPengadaanProdukActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    private AutoCompleteTextView nama_supplier;
    private Button btnTambahItemProduk, btnEditTransaksiProduk;

    private List<SupplierDAO> ListSupplier;
    private List<ProdukDAO> ListProduk;
    private List<DetailPengadaanDAO> ListDetailPengadaanTemp;
    private List<DetailPengadaanDAO> ListDetailPengadaan;
    private List<ProdukDAO> ListProdukPilihan;

    private RecyclerView recyclerEditProduk;
    private EditPengadaanProdukAdapter adapterEditProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityEditPengadaanProdukBinding editPengadaanProdukBinding;
    private PengadaanProdukDAO editPengadaanProdukData;
    private SupplierDAO supplierData;

    private List<Integer> deleted_id_detail_pengadaan;

    private String[] arrayMonths = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
            "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] arrayDayOfWeek = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private String hari;
    private String tanggal;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    public boolean edited_transaksi=false, added_detail=false, edited_detail=false, deleted_detail=false;
    public int countEditTransaksi=0, countEditDetail=0, countAddDetail=0, countDeleteDetail=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editPengadaanProdukBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_pengadaan_produk);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        context = getApplicationContext();
        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //get all data
        deleted_id_detail_pengadaan = new ArrayList<>();
        ListProduk = new ArrayList<>();
        ListSupplier = new ArrayList<>();
        ListDetailPengadaanTemp = new ArrayList<>();
        ListDetailPengadaan = new ArrayList<>();
        ListProdukPilihan = new ArrayList<>();
        supplierData = new SupplierDAO();

        editPengadaanProdukData = gson.fromJson(getIntent().getStringExtra("pengadaan_produk"), PengadaanProdukDAO.class);
        getListSupplier();

        //DATA BINDING SECTION
        LocalDateTime date = LocalDateTime.now();
        int dayValue = DayOfWeek.from(date).getValue();
        hari = arrayDayOfWeek[dayValue-1];
        int dayOfMonth = date.getDayOfMonth();
        String month = arrayMonths[date.getMonth().getValue()-1];
        int year = date.getYear();
        tanggal = dayOfMonth + " " + month + " " + year;
        editPengadaanProdukBinding.setHari(hari);
        editPengadaanProdukBinding.setTanggal(tanggal);
        editPengadaanProdukBinding.setPengadaanProduk(editPengadaanProdukData);

        //create Autocompletetextview for nama hewan
        nama_supplier = findViewById(R.id.etNamaSupplierEditPengadaanProduk);
        nama_supplier.setThreshold(1);//will start working from first character

        //monitoring the input from nama hewan
        nama_supplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("after text changed: "+s.toString());
                int id_supplier = getIdSupplier(s.toString());
                System.out.println("id supplier : "+id_supplier);
                editPengadaanProdukData.setId_supplier(id_supplier);
                editPengadaanProdukBinding.setPengadaanProduk(editPengadaanProdukData);
            }
        });

        //setup recyclerview
        recyclerEditProduk = findViewById(R.id.recycler_view_edit_produk_pengadaan);
        adapterEditProduk = new EditPengadaanProdukAdapter(EditPengadaanProdukActivity.this, editPengadaanProdukData,
                ListDetailPengadaan, ListProduk, ListProdukPilihan, editPengadaanProdukBinding, deleted_id_detail_pengadaan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerEditProduk.setLayoutManager(mLayoutManager);
        recyclerEditProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerEditProduk.setAdapter(adapterEditProduk);

        getListProduk();

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemProduk = findViewById(R.id.btnTambahItemEditPengadaanProduk);
        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPengadaanDAO detailpengadaan = new DetailPengadaanDAO();
                detailpengadaan.setJumlah(1);
                detailpengadaan.setHarga(0);
                detailpengadaan.setCreated_by(pegawai.getUsername());
                ProdukDAO produk = new ProdukDAO();
                ListDetailPengadaan.add(detailpengadaan);
                ListProdukPilihan.add(produk);
                adapterEditProduk.notifyItemInserted(ListDetailPengadaan.size());

                for(int i=0; i<ListDetailPengadaan.size();i++){
                    System.out.println("id produk detail: "+ListDetailPengadaan.get(i).getId_produk()+", id produk produk: "+ListProdukPilihan.get(i).getId_produk());
                }

                hitungTotal();
            }
        });

        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnEditTransaksiProduk = findViewById(R.id.btnEditPengadaanProduk);
        btnEditTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data transaksi produk ======");
                System.out.println("id_supplier: "+editPengadaanProdukData.getId_supplier());
                System.out.println("total: "+editPengadaanProdukData.getTotal());
                System.out.println("");
                System.out.println("====== Data detail transaksi produk ======");
                for (DetailPengadaanDAO detail: ListDetailPengadaan
                ) {
                    System.out.println("id produk: "+detail.getId_produk());
                    System.out.println("jumlah: "+detail.getJumlah());
                    System.out.println("Harga: "+detail.getHarga());
                    System.out.println("total harga: "+detail.getTotal_harga());
                }
                System.out.println("==========================");

                edited_transaksi=false; added_detail=false; edited_detail=false; deleted_detail=false;
                countEditTransaksi=0; countEditDetail=0; countAddDetail=0; countDeleteDetail=0;

                if(editPengadaanProdukData.getId_supplier()==-1){
                    if(nama_supplier.getText().toString().length()!=0){
                        showStandardDialog("Nama supplier tidak terdaftar pada database, " +
                                "mohon isi nama supplier yang telah terdaftar atau daftarkan supplier terlebih dahulu.");
                    }else{
                        showStandardDialog("Mohon masukan nama supplier terlebih dahulu.");
                    }
                }else if(isEmptyCart()) {
                    showStandardDialog("Tidak ada produk yang terdaftar pada pengadaan ini, " +
                            "mohon tambahkan produk terlebih dahulu");
                }else{
                    if(isAnyWrongProduct()){
                        showDialogAnyWrongProduct();
                    }else{
                        //fungsi tambah transaksi
                        System.out.println("PENGADAAN DITAMBAHKAN");
                        editPengadaanProduk();
                    }
                }

            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(EditPengadaanProdukActivity.this, PengadaanActivity.class);
//        back.putExtra("from","manage");
//        back.putExtra("firstView","pengadaan_produk");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void getListProduk(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetProduk> produkDAOCall = apiService.getAllProdukAktif();

        produkDAOCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                ListProduk.addAll(response.body().getListDataProduk());
                System.out.println(ListProduk.get(0).getNama());
                getDetailPengadaanProduk(editPengadaanProdukData);
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetailPengadaanProduk(PengadaanProdukDAO pengadaan_produk){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetDetailPengadaan> detailpengadaanDAOCall = apiService.getDetailPengadaanByIdPengadaan(
                pengadaan_produk.getId_pengadaan_produk());

        detailpengadaanDAOCall.enqueue(new Callback<GetDetailPengadaan>() {
            @Override
            public void onResponse(Call<GetDetailPengadaan> call, Response<GetDetailPengadaan> response) {
                if(!response.body().getListDataDetailPengadaan().isEmpty()){
                    ListDetailPengadaan.addAll(response.body().getListDataDetailPengadaan());
                    System.out.println(ListDetailPengadaan.get(0).getId_produk());

                    for(int i=0;i<ListDetailPengadaan.size();i++){
                        DetailPengadaanDAO temp = new DetailPengadaanDAO(ListDetailPengadaan.get(i).getId_detail_pengadaan(),
                                ListDetailPengadaan.get(i).getId_pengadaan_produk(), ListDetailPengadaan.get(i).getId_produk(),
                                ListDetailPengadaan.get(i).getJumlah(), ListDetailPengadaan.get(i).getHarga(),
                                ListDetailPengadaan.get(i).getTotal_harga(), ListDetailPengadaan.get(i).getCreated_at(),
                                ListDetailPengadaan.get(i).getCreated_by(), ListDetailPengadaan.get(i).getModified_at(),
                                ListDetailPengadaan.get(i).getModified_by());
                        ListDetailPengadaanTemp.add(temp);
                        //ListDetailTransaksiProdukTemp.set(i,ListDetailTransaksiProduk.get(i));
                        ProdukDAO produk = findProduk(ListDetailPengadaan.get(i).getId_produk());
                        ListProdukPilihan.add(produk);
                    }

                    adapterEditProduk.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailPengadaan> call, Throwable t) {
                Toast.makeText(EditPengadaanProdukActivity.this, "Gagal menampilkan detail pengadaan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListSupplier(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetSupplier> supplierDAOCall = apiService.getAllSupplierAktif();

        supplierDAOCall.enqueue(new Callback<GetSupplier>() {
            @Override
            public void onResponse(Call<GetSupplier> call, Response<GetSupplier> response) {
                ListSupplier.addAll(response.body().getListDataSupplier());
                System.out.println(ListSupplier.get(0).getNama());
                String[] arrName = new String[ListSupplier.size()];
                int i = 0;
                for (SupplierDAO supplier: ListSupplier
                ) {
                    arrName[i] = supplier.getNama();
                    i++;
                }

                //Creating the instance of ArrayAdapter containing list of fruit names
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.select_dialog_item, arrName);

                nama_supplier.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                if(editPengadaanProdukData.getId_supplier()!=0){
                    SupplierDAO supplier = findSupplier(editPengadaanProdukData.getId_supplier());
                    if(supplier!=null){
                        supplierData.setId_supplier(supplier.getId_supplier());
                        supplierData.setAktif(supplier.getAktif());
                        supplierData.setNama(supplier.getNama());
                        supplierData.setAlamat(supplier.getAlamat());
                        supplierData.setTelp(supplier.getTelp());
                        supplierData.setCreated_at(supplier.getCreated_at());
                        supplierData.setCreated_by(supplier.getCreated_by());
                        supplierData.setModified_at(supplier.getModified_at());
                        supplierData.setModified_by(supplier.getModified_by());
                    }
                }else{
                    editPengadaanProdukData.setId_supplier(-1);
                }

                editPengadaanProdukBinding.setSupplier(supplierData);
                System.out.println("id supplier edit pengadaan data: "+editPengadaanProdukData.getId_supplier());
                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetSupplier> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getIdSupplier(String nama)
    {
        for (SupplierDAO supplier:ListSupplier) {
            System.out.println("nama suppplier: "+supplier.getNama()+" parameter nama: "+nama);
            if(supplier.getNama().equalsIgnoreCase(nama)){
                return supplier.getId_supplier();
            }
        }
        return -1;
    }

    public void hitungTotal(){
        int total=0;
        for (DetailPengadaanDAO detail: ListDetailPengadaan
        ) {
            total = total+detail.getTotal_harga();
        }
        editPengadaanProdukData.setTotal(total);
        System.out.println("total: "+editPengadaanProdukData.getTotal());
        editPengadaanProdukBinding.setPengadaanProduk(editPengadaanProdukData);
    }

    private void showStandardDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditPengadaanProdukActivity.this);

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

    private void showDialogAnyWrongProduct(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditPengadaanProdukActivity.this);

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
                        editPengadaanProduk();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private boolean isAnyWrongProduct(){
        for (DetailPengadaanDAO detail: ListDetailPengadaan
        ) {
            if(detail.getId_produk()==0){
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyCart(){
        int count = 0;
        for (DetailPengadaanDAO detail: ListDetailPengadaan
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

    public void editPengadaanProduk(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanDAOCall = apiService.ubahPengadaanProduk(editPengadaanProdukData.getId_pengadaan_produk(),
                String.valueOf(editPengadaanProdukData.getId_supplier()), editPengadaanProdukData.getStringTotal(), pegawai.getUsername());

        System.out.println("(isExecuted) Sebelum edit pengadaan produk: "+pengadaanDAOCall.isExecuted());
        pengadaanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                countEditTransaksi=1;
                String id_pengadaan_produk = response.body().getMessage();
                System.out.println(id_pengadaan_produk);
                Toast.makeText(EditPengadaanProdukActivity.this, "Pengadaan Produk Berhasil Diubah", Toast.LENGTH_SHORT).show();
                tambahDetailPengadaanProduk(editPengadaanProdukData.getId_pengadaan_produk());
                editDetailPengadaanProduk(ListDetailPengadaan);
                hapusDetailPengadaanProduk();
                if(countEditTransaksi==1){
                    System.out.println("==== after editPengadaan ====");
                    System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);
                    if(countAddDetail==1 && countEditDetail==1 && countDeleteDetail==1){
                        startIntent();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                countEditTransaksi=-1;
                Toast.makeText(EditPengadaanProdukActivity.this, "Pengadaan Produk Gagal Diubah", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println("(isExecuted) Setelah edit pengadaan produk: "+pengadaanDAOCall.isExecuted());
        if(countEditTransaksi==1 && pengadaanDAOCall.isExecuted()){
            System.out.println("==== after editpengadaan ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);
            if(countAddDetail==1 && countEditDetail==1 && countDeleteDetail==1){
                startIntent();
            }
        }
    }

    public void tambahDetailPengadaanProduk(String id_pengadaan_produk){
        List<DetailPengadaanDAO> added_list = new ArrayList<>();

        for (DetailPengadaanDAO detail : ListDetailPengadaan) {
            if(detail.getId_detail_pengadaan()==0){
                DetailPengadaanDAO temp = detail;
                temp.setId_pengadaan_produk(id_pengadaan_produk);
                added_list.add(temp);
            }
        }

        if(added_list.isEmpty()){
            countAddDetail=1;
        }else{
            List<DetailPengadaanDAO> detail_temp = new ArrayList<>();
            for (DetailPengadaanDAO item: added_list) {
                if(item.getId_produk()!=0){
                    detail_temp.add(item);
                }
            }
            for(int i=0;i<detail_temp.size();i++){
                detail_temp.get(i).setId_pengadaan_produk(id_pengadaan_produk);
            }
            Gson gson = new Gson();
            String detail_pengadaan = gson.toJson(detail_temp);

            System.out.println("DETAIL PENGADAAN DITAMBAH");
            ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
            Call<PostUpdateDelete> pengadaanDAOCall = apiService.tambahDetailPengadaanMultiple(detail_pengadaan);

            pengadaanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    countAddDetail=1;
                    Toast.makeText(EditPengadaanProdukActivity.this, "Pengadaan Produk Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditPengadaanProdukActivity.this, "Transaksi Produk Gagal Ditambahkan", Toast.LENGTH_SHORT).show();

                }
            });
            added_detail=pengadaanDAOCall.isExecuted();
        }

        if (countAddDetail==1){
            System.out.println("==== after addDetail ====");
            System.out.println("editTrans: "+countEditTransaksi+", addDetail: "+countAddDetail+", editDetail: "+countEditDetail+", deleteDetail: "+countDeleteDetail);

            if(countEditTransaksi==1 && countEditDetail==1 && countDeleteDetail==1){
                startIntent();
            }

        }

    }

    public void editDetailPengadaanProduk(List<DetailPengadaanDAO> ListDetailPengadaan){
        List<DetailPengadaanDAO> detail_edited = new ArrayList<>();

        System.out.println("Cek perbandingan List detail temp dan list detail edit");
        System.out.println("List Detail Temp");
        for (DetailPengadaanDAO item: ListDetailPengadaanTemp){
            System.out.println("id detail: "+item.getId_detail_pengadaan()+", id pengadaan: "+item.getId_pengadaan_produk()+
                    ", id produk: "+item.getId_produk()+", jumlah: "+item.getJumlah()+", harga: "+item.getHarga());
        }
        System.out.println("List Detail edit");
        for (DetailPengadaanDAO item: ListDetailPengadaan){
            System.out.println("id detail: "+item.getId_detail_pengadaan()+", id pengadaan: "+item.getId_pengadaan_produk()+
                    ", id produk: "+item.getId_produk()+", jumlah: "+item.getJumlah()+", harga: "+item.getHarga());
        }

        for (DetailPengadaanDAO detail: ListDetailPengadaan) {
//            System.out.println("Masuk FOR edit detail: id="+detail.getId_detail_transaksi_produk()+" id produk: "+detail.getId_produk()+
//                    " jumlah: "+detail.getJumlah());
            if(detail.getId_detail_pengadaan()!=0){
                DetailPengadaanDAO compare = findDetailPengadaanProdukTemp(detail.getId_detail_pengadaan());
//                System.out.println("Compare variable: id="+compare.getId_detail_transaksi_produk()+" id produk: "+compare.getId_produk()+
//                        " jumlah: "+compare.getJumlah());
                if(detail.getId_produk()!=compare.getId_produk() || detail.getJumlah()!=compare.getJumlah() || detail.getHarga()!=compare.getHarga()){
                    detail.setModified_by(pegawai.getUsername());
                    detail_edited.add(detail);
//                    System.out.println("edited item: "+detail.getId_detail_transaksi_produk());
                }
            }
        }

        if (detail_edited.isEmpty()){
            countEditDetail=1;
        }else{
            System.out.println("DETAIL PENGADAAN DIUBAH");
            Gson gson = new Gson();
            String detail_pengadaan = gson.toJson(detail_edited);

            System.out.println("Edited json: "+detail_pengadaan);
            ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
            Call<PostUpdateDelete> pengadaanDAOCall = apiService.ubahDetailPengadaanMultiple(detail_pengadaan);

            pengadaanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    System.out.println(response.body().getMessage());
                    countEditDetail=1;
                    Toast.makeText(EditPengadaanProdukActivity.this, "Detail Pengadaan Produk Berhasil Diubah", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditPengadaanProdukActivity.this, "Detail Pengadaan Produk Gagal Diubah", Toast.LENGTH_SHORT).show();

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

    public void hapusPengadaanProduk(String id_pengadaan_produk){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanDAOCall = apiService.hapusPengadaanProduk(id_pengadaan_produk);

        pengadaanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println("pengadaan produk terhapus");
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println("gagal menghapus pengadaan produk");
            }
        });
    }

    private void hapusDetailPengadaanProduk(){
        if(deleted_id_detail_pengadaan.isEmpty()){
            countDeleteDetail=1;

        }else{
            String[] deleted_id = new String[deleted_id_detail_pengadaan.size()];
            int i = 0;
            for (Integer id_detail: deleted_id_detail_pengadaan
            ) {
                deleted_id[i] = String.valueOf(id_detail);
                i++;
            }

            Gson gson = new Gson();
            String id_detail_pengadaan = gson.toJson(deleted_id);

            System.out.println("deleted id detail pengadaan produk: "+id_detail_pengadaan);
            ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
            Call<PostUpdateDelete> pengadaanDAOCall = apiService.hapusDetailPengadaanMultiple(id_detail_pengadaan);

            pengadaanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    //reverse close
                    System.out.println(response.body().getMessage());
                    countDeleteDetail=1;
                    Toast.makeText(EditPengadaanProdukActivity.this, "Detail Pengadaan Produk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditPengadaanProdukActivity.this, "Detail Pengadaan Produk Gagal Dihapus", Toast.LENGTH_SHORT).show();
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

    public SupplierDAO findSupplier(int id_supplier){
        for (SupplierDAO supplier:ListSupplier
        ) {
            if(supplier.getId_supplier()==id_supplier){
                return supplier;
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

    public DetailPengadaanDAO findDetailPengadaanProdukTemp(int id_detail_pengadaan){
        for (DetailPengadaanDAO detail: ListDetailPengadaanTemp
        ) {
            if(detail.getId_detail_pengadaan()==id_detail_pengadaan){
                return detail;
            }
        }
        return null;
    }

    public void getNewNotifications(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetNotifikasi> notifDAOCall = apiService.getAllNotifikasiBelumTerbacaAsc();

        notifDAOCall.enqueue(new Callback<GetNotifikasi>() {
            @Override
            public void onResponse(Call<GetNotifikasi> call, Response<GetNotifikasi> response) {
                if(!response.body().getListDataNotifikasi().isEmpty()){
                    ListNotifikasi.addAll(response.body().getListDataNotifikasi());
                    System.out.println(ListNotifikasi.get(0).getId_notifikasi());
                    for (NotifikasiDAO notif: ListNotifikasi) {
                        searchProdukNotifikasi(notif);
                    }

                }
            }

            @Override
            public void onFailure(Call<GetNotifikasi> call, Throwable t) {
            }
        });
    }

    public void searchProdukNotifikasi(final NotifikasiDAO notifikasi){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchProduk> produkDAOCall = apiService.searchProduk(Integer.toString(notifikasi.getId_produk()));

        produkDAOCall.enqueue(new Callback<SearchProduk>() {
            @Override
            public void onResponse(Call<SearchProduk> call, Response<SearchProduk> response) {
                final ProdukDAO produk = response.body().getProduk();
                if(produk!=null){
                    String shortdesc = "Jumlah stok "+produk.getNama()+" kurang.";
                    String desc = "Jumlah stok "+produk.getNama()+" kurang dari minimum stok. "+
                            "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                    sendOnNotification(notifikasi.getId_notifikasi(), produk.getNama(), shortdesc, desc);
                }else{
                    String shortdesc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" kurang.";
                    String desc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" kurang dari minimum stok. "+
                            "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                    sendOnNotification(notifikasi.getId_notifikasi(), "ID Produk "+notifikasi.getId_produk(), shortdesc,  desc);
                }

            }

            @Override
            public void onFailure(Call<SearchProduk> call, Throwable t) {
                String shortdesc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" kurang.";
                String desc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" kurang dari minimum stok. "+
                        "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                sendOnNotification(notifikasi.getId_notifikasi(), "ID Produk "+notifikasi.getId_produk(), shortdesc,  desc);
            }
        });
    }

    public void sendOnNotification(int notification_id, String produkname, String shortMessage, String bigMessage) {
        String shortTitle = "Stok Kurang";

        Intent activityIntent = new Intent(context, AdminMainMenu.class);
        activityIntent.putExtra("from", "notifikasi");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.kouveepetshoplogo)
                .setContentTitle(shortTitle)
                .setContentText(shortMessage)
                //.setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigMessage)
                        .setBigContentTitle(produkname)
                        .setSummaryText(produkname))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(notification_id, notification);
    }
}
