package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.EditLayananAdapter;
import com.example.kouveepetshopapps.adapter.TambahLayananAdapter;
import com.example.kouveepetshopapps.adapter.UkuranHewanAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHargaLayanan;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.ukuran_hewan.ListUkuranHewanActivity;
import com.example.kouveepetshopapps.ukuran_hewan.TambahUkuranHewanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class EditLayananActivity extends AppCompatActivity{

    private TextInputEditText namaUpdate;
    private Button btnUpdateLayanan;

    private List<HargaLayananDAO> ListHargaLayanan;
    private RecyclerView recyclerUkuranHewan;
    private EditLayananAdapter adapterUkuranHewan;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    LayananDAO layanan;

    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layanan);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        context = getApplicationContext();
        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("layanan");
        System.out.println(json);
        layanan = gson.fromJson(json, LayananDAO.class);

        setAtribut();
        setText(layanan);

        btnUpdateLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama layanan kosong");
                }else
                {
                    updateLayanan(layanan);
                }
            }
        });
    }

    private void sharedPreferences(){
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
    }

    private void setAtribut(){
        namaUpdate = findViewById(R.id.etNamaLayananUpdate);
        btnUpdateLayanan = findViewById(R.id.btnUpdateLayanan);

        //recyclerview
        recyclerUkuranHewan = findViewById(R.id.recycler_view_edit_layanan);
        ListHargaLayanan = new ArrayList<>();
        adapterUkuranHewan = new EditLayananAdapter(EditLayananActivity.this, ListHargaLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerUkuranHewan.setLayoutManager(mLayoutManager);
        recyclerUkuranHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerUkuranHewan.setAdapter(adapterUkuranHewan);
        setRecycleView();
    }

    private void setText(LayananDAO layanan){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
        namaUpdate.setText(layanan.getNama());
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargaLayananDAOCall = apiService.searchHargaLayananByIdLayanan(String.valueOf(layanan.getId_layanan()));

        hargaLayananDAOCall.enqueue(new Callback<GetHargaLayanan>() {
            @Override
            public void onResponse(Call<GetHargaLayanan> call, Response<GetHargaLayanan> response) {
                ListHargaLayanan.addAll(response.body().getListDataHargaLayanan());
                System.out.println(ListHargaLayanan.get(0).getHarga());
                adapterUkuranHewan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetHargaLayanan> call, Throwable t) {
                Toast.makeText(EditLayananActivity.this, "Gagal menampilkan Ukuran Hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(EditLayananActivity.this, ListLayananActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateLayanan(final LayananDAO hasil){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> layananDAOCall = apiService.ubahLayanan(Integer.toString(hasil.getId_layanan()),namaUpdate.getText().toString(),
                admin.getUsername());
        layananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                System.out.println("child count: "+recyclerUkuranHewan.getChildCount());

                //get text from adapter
                for(int i=0;i<recyclerUkuranHewan.getChildCount();i++)
                {
                    final TextView id_harga_layanan = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdEditLayanan);
                    final TextView id_layanan_edit = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdLayananEditLayanan);
                    final TextView id_ukuran = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdUkuranEditLayanan);
                    final EditText harga = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.etEditHargaLayanan);

                    ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
                    Call<PostUpdateDelete> hargalayananDAOCall = apiService.ubahHargaLayanan(id_harga_layanan.getText().toString(),
                            id_layanan_edit.getText().toString(), id_ukuran.getText().toString(), harga.getText().toString(), admin.getUsername());
                    hargalayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                        @Override
                        public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                            System.out.println("sukses update harga layanan id ukuran: "+
                                    id_harga_layanan.getText().toString()+" "+harga.getText().toString());
                        }

                        @Override
                        public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                            System.out.println("gagal update harga layanan id ukuran: "+
                                    id_harga_layanan.getText().toString()+" "+harga.getText().toString());
                        }
                    });
                }
                System.out.println("sukses update layanan dan harganya");
                Toast.makeText(EditLayananActivity.this, "Sukses update layanan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditLayananActivity.this, "Gagal update layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditLayananActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form dengan benar!");
        alertDialogBuilder.setMessage(rules);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setPositiveButton("SIAP!",new DialogInterface.OnClickListener() {
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
