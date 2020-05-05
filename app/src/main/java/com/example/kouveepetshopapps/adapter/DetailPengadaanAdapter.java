package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.databinding.AdapterDetailPengadaanBinding;
import com.example.kouveepetshopapps.model.DetailPengadaanDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.response.SearchProduk;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengadaanAdapter extends RecyclerView.Adapter<DetailPengadaanAdapter.MyViewHolder> {
    AdapterDetailPengadaanBinding adapterDetailPengadaanBinding;
    private Context context;
    private List<DetailPengadaanDAO> result;

    public DetailPengadaanAdapter(Context context, List<DetailPengadaanDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        adapterDetailPengadaanBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_detail_pengadaan, parent, false);

        final MyViewHolder holder = new MyViewHolder(adapterDetailPengadaanBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final DetailPengadaanDAO detail_pengadaan = result.get(position);

        holder.binding.setDetailPengadaan(detail_pengadaan);
        setProduk(holder, detail_pengadaan.getId_produk());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterDetailPengadaanBinding binding;

        public MyViewHolder(@NonNull AdapterDetailPengadaanBinding binding) {
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
