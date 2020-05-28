package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.HargaLayananAdapter;
import com.example.kouveepetshopapps.adapter.LayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class TampilDetailLayananActivity extends AppCompatActivity {
    private TextView nama_layanan, id_layanan, created_at, created_by, modified_at, modified_by,
            delete_at, delete_by;

    //recyclerview
    private List<HargaLayananDAO> ListHargaLayanan;
    private RecyclerView recyclerHargaLayanan;
    private HargaLayananAdapter adapterHargaLayanan;
    private RecyclerView.LayoutManager mLayoutManager;

    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_layanan);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        context = getApplicationContext();
        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("layanan");
        System.out.println(json);
        LayananDAO layanan = gson.fromJson(json, LayananDAO.class);

        nama_layanan = findViewById(R.id.viewNamaLayanan);
        id_layanan = findViewById(R.id.viewIdLayanan);
        created_at = findViewById(R.id.viewCreatedAtLayanan);
        created_by = findViewById(R.id.viewCreatedByLayanan);
        modified_at = findViewById(R.id.viewModifiedAtLayanan);
        modified_by = findViewById(R.id.viewModifiedByLayanan);
        delete_at = findViewById(R.id.viewDeleteAtLayanan);
        delete_by = findViewById(R.id.viewDeleteByLayanan);

        recyclerHargaLayanan = findViewById(R.id.recycler_view_harga_layanan);
        //addLayananBtn = findViewById(R.id.addLayananButton);

        ListHargaLayanan = new ArrayList<>();
        adapterHargaLayanan = new HargaLayananAdapter(TampilDetailLayananActivity.this, ListHargaLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerHargaLayanan.setLayoutManager(mLayoutManager);
        recyclerHargaLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerHargaLayanan.setAdapter(adapterHargaLayanan);
        setData(layanan);
        setRecycleView(layanan);

//        addLayananBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent add = new Intent(ListLayananActivity.this, TambahProdukActivity.class);
////                startActivity(add);
//            }
//        });


    }

    public void setData(LayananDAO layanan){
        id_layanan.setText(Integer.toString(layanan.getId_layanan()));
        nama_layanan.setText(layanan.getNama());
        created_at.setText(layanan.getCreated_at());
        created_by.setText(layanan.getCreated_by());
        modified_at.setText(layanan.getModified_at());
        modified_by.setText(layanan.getModified_by());
        delete_at.setText(layanan.getDelete_at());
        delete_by.setText(layanan.getDelete_by());
    }

    public void setRecycleView(LayananDAO layanan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargalayananDAOCall = apiService.searchHargaLayananByIdLayanan(Integer.toString(layanan.getId_layanan()));

        hargalayananDAOCall.enqueue(new Callback<GetHargaLayanan>() {
            @Override
            public void onResponse(Call<GetHargaLayanan> call, Response<GetHargaLayanan> response) {
                if(!response.body().getListDataHargaLayanan().isEmpty()){
                    ListHargaLayanan.addAll(response.body().getListDataHargaLayanan());
                    System.out.println(ListHargaLayanan.get(0).getHarga());
                    adapterHargaLayanan.notifyDataSetChanged();
//                  Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetHargaLayanan> call, Throwable t) {
                Toast.makeText(TampilDetailLayananActivity.this, "Gagal menampilkan harga layanan", Toast.LENGTH_SHORT).show();
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
