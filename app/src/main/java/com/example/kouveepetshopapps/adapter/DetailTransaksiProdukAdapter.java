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
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.databinding.AdapterDetailTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchProduk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransaksiProdukAdapter extends RecyclerView.Adapter<DetailTransaksiProdukAdapter.MyViewHolder> {
    AdapterDetailTransaksiProdukBinding adapterDetailTransaksiProdukBinding;
    private Context context;
    private List<DetailTransaksiProdukDAO> result;

    public DetailTransaksiProdukAdapter(Context context, List<DetailTransaksiProdukDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        adapterDetailTransaksiProdukBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_detail_transaksi_produk, parent, false);

        final MyViewHolder holder = new MyViewHolder(adapterDetailTransaksiProdukBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final DetailTransaksiProdukDAO detail_transaksi_produk = result.get(position);

        holder.binding.setDetailTransaksiProduk(detail_transaksi_produk);
        setProduk(holder, detail_transaksi_produk.getId_produk());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterDetailTransaksiProdukBinding binding;

        public MyViewHolder(@NonNull AdapterDetailTransaksiProdukBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    public void setProduk(final MyViewHolder holder, int id_produk){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchProduk> produkDAOCall = apiService.searchProduk(Integer.toString(id_produk));
        produkDAOCall.enqueue(new Callback<SearchProduk>() {
            @Override
            public void onResponse(Call<SearchProduk> call, Response<SearchProduk> response) {
                ProdukDAO produk = response.body().getProduk();
                holder.binding.setProduk(produk);
            }

            @Override
            public void onFailure(Call<SearchProduk> call, Throwable t) {
            }
        });

    }
}
