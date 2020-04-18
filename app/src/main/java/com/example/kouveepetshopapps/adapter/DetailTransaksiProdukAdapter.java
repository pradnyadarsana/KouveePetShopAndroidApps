package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.databinding.AdapterDetailTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;

import java.util.List;

public class DetailTransaksiProdukAdapter extends RecyclerView.Adapter<DetailTransaksiProdukAdapter.MyViewHolder> {
    AdapterDetailTransaksiProdukBinding adapterDetailTransaksiProdukBinding;
    private Context context;
    private List<DetailTransaksiProdukDAO> result;
    private List<ProdukDAO> listProduk;

    public DetailTransaksiProdukAdapter(Context context, List<DetailTransaksiProdukDAO> result, List<ProdukDAO> listProduk) {
        this.context = context;
        this.result = result;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        adapterDetailTransaksiProdukBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_detail_transaksi_produk, parent, false);

        //View v = LayoutInflater.from(context).inflate(R.layout.adapter_detail_transaksi_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(adapterDetailTransaksiProdukBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final DetailTransaksiProdukDAO detail_transaksi_produk = result.get(position);
        final ProdukDAO produk = findProduk(detail_transaksi_produk.getId_produk());

        holder.binding.setDetailTransaksiProduk(detail_transaksi_produk);
        holder.binding.setProduk(produk);
//        System.out.println(result.get(position).getHarga() + " " + position);
//
//        setNamaUkuran(holder, harga_layanan.getId_ukuran_hewan());
//        holder.harga.setText(Integer.toString(harga_layanan.getHarga()));
////        holder.parent.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                startIntent(layanan, TampilDetailLayananActivity.class);
////            }
////        });
//        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                showDialog(harga_layanan, position);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterDetailTransaksiProdukBinding binding;

//        private TextView nama_ukuran_hewan, harga;
//        private CardView parent;

        public MyViewHolder(@NonNull AdapterDetailTransaksiProdukBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
//            nama_ukuran_hewan = itemView.findViewById(R.id.tvNamaUkuranLayanan);
//            harga = itemView.findViewById(R.id.tvHargaLayanan);
//            parent = itemView.findViewById(R.id.ParentHargaLayanan);
        }

        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    public ProdukDAO findProduk(int id_produk){
        for (ProdukDAO produk: listProduk
             ) {
            if(produk.getId_produk()==id_produk){
                return produk;
            }
        }
        return null;
    }
}
