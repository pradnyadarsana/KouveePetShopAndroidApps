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
import com.example.kouveepetshopapps.adapter.TambahPengadaanProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTambahPengadaanProdukBinding;
import com.example.kouveepetshopapps.model.DetailPengadaanDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchPengadaanProduk;
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

public class TambahPengadaanProdukActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    private AutoCompleteTextView nama_supplier;
    private Button btnTambahItemProduk, btnTambahPengadaanProduk;

    private List<SupplierDAO> ListSupplier;
    private List<ProdukDAO> ListProduk;
    private List<DetailPengadaanDAO> ListDetailPengadaan;
    private List<ProdukDAO> ListProdukPilihan;

    private RecyclerView recyclerTambahProduk;
    private TambahPengadaanProdukAdapter adapterTambahProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    //data binding
    ActivityTambahPengadaanProdukBinding pengadaanProdukBinding;
    private PengadaanProdukDAO pengadaanProdukData;
    private SupplierDAO supplierData;

    private String[] arrayMonths = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
            "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] arrayDayOfWeek = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private String hari;
    private String tanggal;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengadaanProdukBinding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_pengadaan_produk);

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

        //create Autocompletetextview for nama hewan
        nama_supplier = (AutoCompleteTextView) findViewById(R.id.etNamaSupplierPengadaanProduk);
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
                pengadaanProdukData.setId_supplier(id_supplier);
                pengadaanProdukBinding.setPengadaanProduk(pengadaanProdukData);
            }
        });

        //get list of hewan and produk
        ListProduk = new ArrayList<>();
        ListSupplier = new ArrayList<>();
        ListDetailPengadaan = new ArrayList<>();
        ListProdukPilihan = new ArrayList<>();
        getListSupplier();
        getListProduk();

        //DATA BINDING SECTION
        LocalDateTime date = LocalDateTime.now();
        int dayValue = DayOfWeek.from(date).getValue();
        hari = arrayDayOfWeek[dayValue-1];
        int dayOfMonth = date.getDayOfMonth();
        String month = arrayMonths[date.getMonth().getValue()-1];
        int year = date.getYear();
        tanggal = dayOfMonth + " " + month + " " + year;
        pengadaanProdukData = new PengadaanProdukDAO();
        pengadaanProdukData.setId_supplier(-1);
        pengadaanProdukBinding.setHari(hari);
        pengadaanProdukBinding.setTanggal(tanggal);
        pengadaanProdukBinding.setSupplier(supplierData);
        pengadaanProdukBinding.setPengadaanProduk(pengadaanProdukData);

        //setup recyclerview
        recyclerTambahProduk = findViewById(R.id.recycler_view_tambah_produk_pengadaan);
        adapterTambahProduk = new TambahPengadaanProdukAdapter(TambahPengadaanProdukActivity.this,
                pengadaanProdukData, ListDetailPengadaan, ListProduk, ListProdukPilihan, pengadaanProdukBinding);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTambahProduk.setLayoutManager(mLayoutManager);
        recyclerTambahProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerTambahProduk.setAdapter(adapterTambahProduk);

        //SETUP BUTTON TAMBAH ITEM PRODUK
        btnTambahItemProduk = findViewById(R.id.btnTambahItemPengadaanProduk);
        btnTambahItemProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPengadaanDAO detailpengadaanproduk = new DetailPengadaanDAO();
                detailpengadaanproduk.setJumlah(1);
                detailpengadaanproduk.setHarga(0);
                detailpengadaanproduk.setCreated_by(pegawai.getUsername());
                ProdukDAO produk = new ProdukDAO();
                ListDetailPengadaan.add(detailpengadaanproduk);
                ListProdukPilihan.add(produk);
                adapterTambahProduk.notifyItemInserted(ListDetailPengadaan.size());

                for(int i=0; i<ListDetailPengadaan.size();i++){
                    System.out.println("id produk detail: "+ListDetailPengadaan.get(i).getId_produk()+", id produk produk: "+ListProdukPilihan.get(i).getId_produk());
                }

                hitungTotal();
            }
        });

        //SETUP BUTTON TAMBAH TRANSAKSI PRODUK
        btnTambahPengadaanProduk = findViewById(R.id.btnTambahPengadaanProduk);
        btnTambahPengadaanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungTotal();
                //tambahLayanan();
                System.out.println("====== Data pengadaan produk ======");
                System.out.println("id_supplier: "+pengadaanProdukData.getId_supplier());
                System.out.println("total: "+pengadaanProdukData.getTotal());
                System.out.println("");
                System.out.println("====== Data detail pengadaan produk ======");
                for (DetailPengadaanDAO detail: ListDetailPengadaan
                ) {
                    System.out.println("id produk: "+detail.getId_produk());
                    System.out.println("jumlah: "+detail.getJumlah());
                    System.out.println("harga: "+detail.getHarga());
                    System.out.println("total harga: "+detail.getTotal_harga());
                }
                System.out.println("==========================");

                if(pengadaanProdukData.getId_supplier()==-1){
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
                        tambahPengadaanProduk();
                    }
                }

            }
        });
    }

    private void startIntent(PengadaanProdukDAO pengadaan){
        Intent viewDetail = new Intent(TambahPengadaanProdukActivity.this, TampilDetailPengadaanActivity.class);

        Gson gson = new Gson();
        String pengadaan_produk = gson.toJson(pengadaan);
        viewDetail.putExtra("pengadaan_produk",pengadaan_produk);
        viewDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(viewDetail);
    }

    public void getListSupplier(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetSupplier> supplierDAOCall = apiService.getAllSupplierAktif();

        supplierDAOCall.enqueue(new Callback<GetSupplier>() {
            @Override
            public void onResponse(Call<GetSupplier> call, Response<GetSupplier> response) {
                ListSupplier.addAll(response.body().getListDataSupplier());
                //System.out.println(ListHewan.get(0).getNama());
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

                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetSupplier> call, Throwable t) {
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
                //System.out.println(ListProduk.get(0).getNama());
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getIdSupplier(String nama)
    {
        for (SupplierDAO supplier:ListSupplier
        ) {
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
        pengadaanProdukData.setTotal(total);
        System.out.println("total: "+pengadaanProdukData.getTotal());
        pengadaanProdukBinding.setPengadaanProduk(pengadaanProdukData);
    }

    private void showStandardDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahPengadaanProdukActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form pengadaan dengan benar!");
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahPengadaanProdukActivity.this);

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
                        System.out.println("PENGADAAN DITAMBAHKAN");
                        tambahPengadaanProduk();
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

    public void tambahPengadaanProduk(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchPengadaanProduk> pengadaanProdukDAOCall = apiService.tambahPengadaanProduk(
                String.valueOf(pengadaanProdukData.getId_supplier()), pengadaanProdukData.getStringTotal(),
                pegawai.getUsername());

        pengadaanProdukDAOCall.enqueue(new Callback<SearchPengadaanProduk>() {
            @Override
            public void onResponse(Call<SearchPengadaanProduk> call, Response<SearchPengadaanProduk> response) {
                PengadaanProdukDAO pengadaan_produk = response.body().getPengadaan_produk();
                System.out.println(pengadaan_produk.getId_pengadaan_produk());
                tambahDetailPengadaanProduk(pengadaan_produk);
            }

            @Override
            public void onFailure(Call<SearchPengadaanProduk> call, Throwable t) {
                Toast.makeText(TambahPengadaanProdukActivity.this, "Pengadaan Produk Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahDetailPengadaanProduk(final PengadaanProdukDAO pengadaan_produk){
        List<DetailPengadaanDAO> detail_temp = new ArrayList<>();

        if(isAnyWrongProduct()){
            for (DetailPengadaanDAO item: ListDetailPengadaan) {
                if(item.getId_produk()!=0){
                    detail_temp.add(item);
                }
            }
        }else{
            detail_temp = ListDetailPengadaan;
        }

        for(int i=0;i<detail_temp.size();i++){
            detail_temp.get(i).setId_pengadaan_produk(pengadaan_produk.getId_pengadaan_produk());
        }

        Gson gson = new Gson();
        String detail_pengadaan = gson.toJson(detail_temp);

        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.tambahDetailPengadaanMultiple(detail_pengadaan);

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println(response.body().getMessage());
                Toast.makeText(TambahPengadaanProdukActivity.this, "Pengadaan Produk Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                startIntent(pengadaan_produk);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                hapusPengadaanProduk(pengadaan_produk.getId_pengadaan_produk());
                Toast.makeText(TambahPengadaanProdukActivity.this, "Pengadaan Produk Gagal Ditambahkan", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void hapusPengadaanProduk(String id_pengadaan_produk){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.hapusPengadaanProduk(id_pengadaan_produk);

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
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
