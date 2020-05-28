package com.example.kouveepetshopapps.ukuran_hewan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.SupplierAdapter;
import com.example.kouveepetshopapps.adapter.UkuranHewanAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.supplier.TambahSupplierActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kouveepetshopapps.App.CHANNEL_ID;

public class ListUkuranHewanActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    Context context;
    private List<NotifikasiDAO> ListNotifikasi;

    private List<UkuranHewanDAO> ListUkuranHewan;
    private RecyclerView recyclerUkuranHewan;
    private UkuranHewanAdapter adapterUkuranHewan;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addUkuranHewanBtn;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ukuran_hewan);

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        context = getApplicationContext();
        ListNotifikasi = new ArrayList<>();
        getNewNotifications();

        Toolbar toolbar = findViewById(R.id.searchUkuranHewanToolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ukuran Hewan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    return;
                }else{
                    startIntent(AdminMainMenu.class);
                }
            }
        });

        recyclerUkuranHewan = findViewById(R.id.recycler_view_ukuran_hewan);
        addUkuranHewanBtn = findViewById(R.id.addUkuranHewanButton);

        ListUkuranHewan = new ArrayList<>();
        adapterUkuranHewan = new UkuranHewanAdapter(ListUkuranHewanActivity.this, ListUkuranHewan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerUkuranHewan.setLayoutManager(mLayoutManager);
        recyclerUkuranHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerUkuranHewan.setAdapter(adapterUkuranHewan);
        setRecycleView();

        addUkuranHewanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(ListUkuranHewanActivity.this, TambahUkuranHewanActivity.class);
                startActivity(add);
            }
        });
    }

    private void startIntent(Class nextClass){
        Intent back = new Intent(getApplicationContext(), nextClass);
        back.putExtra("from", "kelola_data");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetUkuranHewan> ukuranHewanDAOCall = apiService.getAllUkuranHewanAktif();

        ukuranHewanDAOCall.enqueue(new Callback<GetUkuranHewan>() {
            @Override
            public void onResponse(Call<GetUkuranHewan> call, Response<GetUkuranHewan> response) {
                ListUkuranHewan.addAll(response.body().getListDataUkuranHewan());
                //System.out.println(ListUkuranHewan.get(0).getNama());
                adapterUkuranHewan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUkuranHewan> call, Throwable t) {
                Toast.makeText(ListUkuranHewanActivity.this, "Gagal menampilkan ukuran hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("ID / Nama Ukuran Hewan");

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterUkuranHewan.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterUkuranHewan.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
