package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.produk.TampilDetailProdukActivity;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.MyViewHolder>{
    private NotificationManagerCompat notificationManager;
    private Context context;
    private List<NotifikasiDAO> result;
    SharedPreferences loggedUser;
    PegawaiDAO admin;

    private String[] arrayMonths = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
            "Agustus", "September", "Oktober", "November", "Desember"};
    private String[] arrayDayOfWeek = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

    public NotifikasiAdapter(Context context, List<NotifikasiDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_notifikasi, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        notificationManager = NotificationManagerCompat.from(context);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NotifikasiDAO notifikasi = result.get(position);
        System.out.println(result.get(position).getId_notifikasi()+" "+position);
        setProdukNotifikasi(holder, notifikasi);
        if(notifikasi.getStatus()==0){
            //holder.parent.setCardBackgroundColor(R.color.colorPrimaryBeige);
            holder.parent.setBackgroundResource(R.color.colorPrimaryBeige);
        }else{
            //holder.parent.setCardBackgroundColor(R.color.colorPrimaryWhite);
            holder.parent.setBackgroundResource(R.color.colorPrimaryWhite);
        }
//        holder.nama.setText(produk.getNama());
//        holder.id_produk.setText(Integer.toString(produk.getId_produk()));
//        holder.satuan.setText(produk.getSatuan());
//        holder.jumlah_stok.setText(Integer.toString(produk.getJumlah_stok()));
//        holder.harga.setText(Integer.toString(produk.getHarga()));
//        holder.min_stok.setText(Integer.toString(produk.getMin_stok()));
//
//        String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
//        System.out.println(photo_url);
//        Glide.with(context).load(photo_url).into(holder.gambar);
//

//        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                showDialog(produk, position);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView deskripsi, created_at;
        private ImageView gambar;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            deskripsi = itemView.findViewById(R.id.tvDeskripsiNotifikasi);
            created_at = itemView.findViewById(R.id.tvCreatedAtNotifikasi);
            gambar = itemView.findViewById(R.id.ivGambarProdukNotifikasi);
            parent = itemView.findViewById(R.id.ParentNotifikasi);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTvCreatedAt(MyViewHolder holder, String createdAt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime created_at = LocalDateTime.parse(createdAt, formatter);

        String hari = arrayDayOfWeek[created_at.getDayOfWeek().getValue()-1];
        int tanggal = created_at.getDayOfMonth();
        String month = arrayMonths[created_at.getMonth().getValue()-1];
        int year = created_at.getYear();

        int jam = created_at.getHour();
        int min = created_at.getMinute();
        String menit = "00";
        if(min<10){
            menit = "0"+min;
        }else{
            menit = String.valueOf(min);
        }


        String dateText = hari+", "+tanggal+" "+month+" "+year+" "+jam+"."+menit;
        holder.created_at.setText(dateText);
    }

    public void setProdukNotifikasi(final MyViewHolder holder, final NotifikasiDAO notifikasi){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchProduk> produkDAOCall = apiService.searchProduk(Integer.toString(notifikasi.getId_produk()));

        produkDAOCall.enqueue(new Callback<SearchProduk>() {
            @Override
            public void onResponse(Call<SearchProduk> call, Response<SearchProduk> response) {
                final ProdukDAO produk = response.body().getProduk();
                if(produk!=null){
                    String desc = "Jumlah stok "+produk.getNama()+" sudah kurang dari minimum stoknya. "+
                            "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                    Spannable sb = new SpannableString(desc);
                    sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), desc.indexOf(produk.getNama()),
                            desc.indexOf(produk.getNama())+produk.getNama().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
                    holder.deskripsi.setText(sb, TextView.BufferType.SPANNABLE);
                    String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
                    Glide.with(context).load(photo_url).into(holder.gambar);
                    holder.parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            readNotification(holder, notifikasi.getId_notifikasi());
                            startIntent(produk, TampilDetailProdukActivity.class);
                        }
                    });
                }else{
                    String desc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" sudah kurang dari minimum stoknya. "+
                            "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                    Spannable sb = new SpannableString(desc);
                    sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), desc.indexOf(String.valueOf(notifikasi.getId_produk())),
                            desc.indexOf(String.valueOf(notifikasi.getId_produk()))+String.valueOf(notifikasi.getId_produk()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
                    holder.deskripsi.setText(sb, TextView.BufferType.SPANNABLE);
                }

                setTvCreatedAt(holder, notifikasi.getCreated_at());

            }

            @Override
            public void onFailure(Call<SearchProduk> call, Throwable t) {
                String desc = "Jumlah stok produk dengan ID "+notifikasi.getId_produk()+" sudah kurang dari minimum stoknya. "+
                        "Segera lakukan pengadaan produk untuk menambahkan stok yang tersedia.";
                Spannable sb = new SpannableString(desc);
                sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), desc.indexOf(String.valueOf(notifikasi.getId_produk())),
                        desc.indexOf(String.valueOf(notifikasi.getId_produk()))+String.valueOf(notifikasi.getId_produk()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
                holder.deskripsi.setText(sb, TextView.BufferType.SPANNABLE);

                setTvCreatedAt(holder, notifikasi.getCreated_at());
            }
        });
    }

    public void readNotification(final MyViewHolder holder, final int id_notifikasi){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> notifDAOCall = apiService.ubahStatusNotifTerbaca(Integer.toString(id_notifikasi));

        notifDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                holder.parent.setBackgroundResource(R.color.colorPrimaryWhite);
                notificationManager.cancel(id_notifikasi);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
            }
        });
    }

    private void startIntent(ProdukDAO hasil, Class nextView){
        Intent view = new Intent(context, nextView);
        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        view.putExtra("produk", json);
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }
}
