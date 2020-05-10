package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.produk.TampilDetailProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StokProdukAdapter extends RecyclerView.Adapter<StokProdukAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<ProdukDAO> result;
    private List<ProdukDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO admin;

    public StokProdukAdapter(Context context, List<ProdukDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_stok_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ProdukDAO produk = resultFiltered.get(position);
        System.out.println(resultFiltered.get(position).getNama()+" "+position);
        holder.nama.setText(produk.getNama());
        holder.id_produk.setText(Integer.toString(produk.getId_produk()));
        holder.satuan.setText(produk.getSatuan());
        holder.jumlah_stok.setText(Integer.toString(produk.getJumlah_stok()));
        holder.harga.setText(Integer.toString(produk.getHarga()));
        holder.min_stok.setText(Integer.toString(produk.getMin_stok()));

        String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
        System.out.println(photo_url);
        Glide.with(context).load(photo_url).into(holder.gambar);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(produk, TampilDetailProdukActivity.class);
            }
        });
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
        return resultFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resultFiltered = result;
                } else {
                    List<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || Integer.toString(row.getId_produk()).contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    resultFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = resultFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultFiltered = (ArrayList<ProdukDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_produk, nama, satuan, jumlah_stok, min_stok, harga;
        private ImageView gambar;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_produk = itemView.findViewById(R.id.tvIdStokProduk);
            nama = itemView.findViewById(R.id.tvNamaStokProduk);
            satuan = itemView.findViewById(R.id.tvSatuanStokProduk);
            jumlah_stok = itemView.findViewById(R.id.tvJumlahStokStokProduk);
            harga = itemView.findViewById(R.id.tvHargaStokProduk);
            min_stok = itemView.findViewById(R.id.tvMinStokStokProduk);
            gambar = itemView.findViewById(R.id.ivGambarStokProduk);
            parent = itemView.findViewById(R.id.ParentStokProduk);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
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
