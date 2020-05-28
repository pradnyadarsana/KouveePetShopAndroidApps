package com.example.kouveepetshopapps.pengadaan.menunggu_konfirmasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.PengadaanProdukAdapter;
import com.example.kouveepetshopapps.adapter.TransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.pengadaan.TambahPengadaanProdukActivity;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetPengadaanProduk;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.transaksi.produk.TambahTransaksiProdukActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class ListPengadaanMenungguKonfirmasiFragment extends Fragment {
    private List<PengadaanProdukDAO> ListPengadaanProduk;
    private RecyclerView recyclerPengadaanProduk;
    public PengadaanProdukAdapter adapterPengadaanProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addPengadaanProdukBtn;

    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_pengadaan_menunggu_konfirmasi, container, false);

        notificationManager = NotificationManagerCompat.from(getContext());

        recyclerPengadaanProduk = view.findViewById(R.id.recycler_view_pengadaan_menunggu_konfirmasi);
        addPengadaanProdukBtn = view.findViewById(R.id.addPengadaanButton);
        addPengadaanProdukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TambahPengadaanProdukActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        ListPengadaanProduk = new ArrayList<>();
        adapterPengadaanProduk = new PengadaanProdukAdapter(getContext(), ListPengadaanProduk);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerPengadaanProduk.setLayoutManager(mLayoutManager);
        recyclerPengadaanProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerPengadaanProduk.setAdapter(adapterPengadaanProduk);

        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetPengadaanProduk> pengadaanProdukDAOCall = apiService.getPengadaanProdukMenungguKonfirmasi();

        pengadaanProdukDAOCall.enqueue(new Callback<GetPengadaanProduk>() {
            @Override
            public void onResponse(Call<GetPengadaanProduk> call, Response<GetPengadaanProduk> response) {
                ListPengadaanProduk.addAll(response.body().getListDataPengadaanProduk());
                if(!ListPengadaanProduk.isEmpty()){
                    //System.out.println(ListTransaksiProduk.get(0).getId_transaksi_produk());
                    adapterPengadaanProduk.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Tidak ada pengadaan produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetPengadaanProduk> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal menampilkan pengadaan produk", Toast.LENGTH_SHORT).show();
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
