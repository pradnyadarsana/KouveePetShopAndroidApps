package com.example.kouveepetshopapps.pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.DetailPengadaanAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.ActivityTampilDetailPengadaanBinding;
import com.example.kouveepetshopapps.model.DetailPengadaanDAO;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.response.GetDetailPengadaan;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.response.SearchSupplier;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class TampilDetailPengadaanActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    ActivityTampilDetailPengadaanBinding tampilDetailPengadaanBinding;
    private Button btnEditPengadaan, btnDeletePengadaan, btnUpdateProgressPengadaan, btnCetakStrukPengadaan, btnTambahPengadaan;

    private PengadaanProdukDAO pengadaan_produk;
    private SupplierDAO supplier;

    //RECYCLERVIEW DETAIL PENGADAAN
    private List<DetailPengadaanDAO> ListDetailPengadaan;
    private RecyclerView recyclerDetailPengadaan;
    private DetailPengadaanAdapter adapterDetailPengadaan;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tampilDetailPengadaanBinding = DataBindingUtil.setContentView(this, R.layout.activity_tampil_detail_pengadaan);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        context = getApplicationContext();
        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        btnEditPengadaan = findViewById(R.id.btnEditPengadaanProdukFromDetail);
        btnDeletePengadaan = findViewById(R.id.btnDeletePengadaanProdukFromDetail);
        btnUpdateProgressPengadaan = findViewById(R.id.btnUbahStatusPengadaanProdukFromDetail);
        btnCetakStrukPengadaan = findViewById(R.id.btnCetakPengadaanProdukFromDetail);
        btnTambahPengadaan = findViewById(R.id.btnAddPengadaanProdukFromDetail);

        //get logged user
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        //Gson gson = new Gson();
        pengadaan_produk = gson.fromJson(getIntent().getStringExtra("pengadaan_produk"), PengadaanProdukDAO.class);

        tampilDetailPengadaanBinding.setPengadaanProduk(pengadaan_produk);

        searchSupplier(pengadaan_produk.getId_supplier());

        recyclerDetailPengadaan = findViewById(R.id.recycler_view_tampil_detail_pengadaan_produk);
        ListDetailPengadaan = new ArrayList<>();
        adapterDetailPengadaan = new DetailPengadaanAdapter(TampilDetailPengadaanActivity.this,
                ListDetailPengadaan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDetailPengadaan.setLayoutManager(mLayoutManager);
        recyclerDetailPengadaan.setItemAnimator(new DefaultItemAnimator());
        recyclerDetailPengadaan.setAdapter(adapterDetailPengadaan);
        setRecycleView(pengadaan_produk);

        btnEditPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(TampilDetailPengadaanActivity.this, EditPengadaanProdukActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(pengadaan_produk);
                edit.putExtra("pengadaan_produk",data);
                startActivity(edit);
            }
        });

        btnDeletePengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(pengadaan_produk.getId_pengadaan_produk());
            }
        });

        btnUpdateProgressPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pengadaan_produk.getStatus().equalsIgnoreCase("Menunggu Konfirmasi")){
                    updateStatusToProsesPengadaanProduk(pengadaan_produk.getId_pengadaan_produk());
                }else if(pengadaan_produk.getStatus().equalsIgnoreCase("Pesanan Diproses")){
                    updateStatusToSelesaiPengadaanProduk(pengadaan_produk.getId_pengadaan_produk());
                }
            }
        });

        btnCetakStrukPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetakPengadaanProduk(pengadaan_produk);
            }
        });

        btnTambahPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(TampilDetailPengadaanActivity.this, TambahPengadaanProdukActivity.class);
                //tambah.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tambah);
            }
        });
    }

    public void setRecycleView(PengadaanProdukDAO pengadaan_produk){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetDetailPengadaan> detailpengadaanDAOCall = apiService.getDetailPengadaanByIdPengadaan(
                pengadaan_produk.getId_pengadaan_produk());

        detailpengadaanDAOCall.enqueue(new Callback<GetDetailPengadaan>() {
            @Override
            public void onResponse(Call<GetDetailPengadaan> call, Response<GetDetailPengadaan> response) {
                if(!response.body().getListDataDetailPengadaan().isEmpty()){
                    ListDetailPengadaan.addAll(response.body().getListDataDetailPengadaan());
                    System.out.println(ListDetailPengadaan.get(0).getId_produk());
                    adapterDetailPengadaan.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetDetailPengadaan> call, Throwable t) {
                Toast.makeText(TampilDetailPengadaanActivity.this, "Gagal menampilkan detail pengadaan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchSupplier(int id_supplier){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchSupplier> supplierDAOCall = apiService.searchSupplier(Integer.toString(id_supplier));
        supplierDAOCall.enqueue(new Callback<SearchSupplier>() {
            @Override
            public void onResponse(Call<SearchSupplier> call, Response<SearchSupplier> response) {
                if(response.body().getSupplier()!=null)
                supplier = response.body().getSupplier();
                tampilDetailPengadaanBinding.setSupplier(supplier);
            }

            @Override
            public void onFailure(Call<SearchSupplier> call, Throwable t) {
                //holder.nama_hewan.setText(Integer.toString(id_hewan));
            }
        });

    }

    private void updateStatusToProsesPengadaanProduk(final String id){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.ubahStatusPengadaanToProses(id, pegawai.getUsername());

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Sukses memperbaharui status pengadaan", Toast.LENGTH_SHORT).show();
                cetakPengadaanProduk(pengadaan_produk);

            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Gagal memperbaharui status pengadaan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatusToSelesaiPengadaanProduk(String id){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.ubahStatusPengadaanToSelesai(id, pegawai.getUsername());

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Sukses memperbaharui status pengadaan", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(TampilDetailPengadaanActivity.this, PengadaanActivity.class);
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                back.putExtra("firstView", "pesanan selesai");
                startActivity(back);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Gagal memperbaharui status pengadaan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(final String id_pengadaan_produk){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TampilDetailPengadaanActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus pengadaan produk?");
        alertDialogBuilder.setMessage(id_pengadaan_produk);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deletePengadaanProduk(id_pengadaan_produk);
                    }
                }).setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void deletePengadaanProduk(String id){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.hapusPengadaanProduk(id);

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Sukses menghapus data pengadaan produk", Toast.LENGTH_SHORT).show();
                Intent back = new Intent(TampilDetailPengadaanActivity.this, PengadaanActivity.class);
                back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(TampilDetailPengadaanActivity.this, "Gagal menghapus data pengadaan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cetakPengadaanProduk(final PengadaanProdukDAO pengadaan_produk){
        Intent struk = new Intent(TampilDetailPengadaanActivity.this, StrukPengadaanWebView.class);
        struk.putExtra("id_pengadaan_produk",pengadaan_produk.getId_pengadaan_produk());
        struk.putExtra("status",pengadaan_produk.getStatus());
        startActivity(struk);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(TampilDetailPengadaanActivity.this, PengadaanActivity.class);
        back.putExtra("from","pengadaan");
        back.putExtra("firstView",pengadaan_produk.getStatus());
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }
}
