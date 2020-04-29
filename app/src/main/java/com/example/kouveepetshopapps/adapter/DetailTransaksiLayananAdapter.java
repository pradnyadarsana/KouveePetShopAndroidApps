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
import com.example.kouveepetshopapps.databinding.AdapterDetailTransaksiLayananBinding;
import com.example.kouveepetshopapps.databinding.AdapterDetailTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiLayananDAO;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.SearchHargaLayanan;
import com.example.kouveepetshopapps.response.SearchLayanan;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.response.SearchUkuranHewan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransaksiLayananAdapter extends RecyclerView.Adapter<DetailTransaksiLayananAdapter.MyViewHolder> {
    AdapterDetailTransaksiLayananBinding adapterDetailTransaksiLayananBinding;
    private Context context;
    private List<DetailTransaksiLayananDAO> result;

    public DetailTransaksiLayananAdapter(Context context, List<DetailTransaksiLayananDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        adapterDetailTransaksiLayananBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.adapter_detail_transaksi_layanan, parent, false);

        final MyViewHolder holder = new MyViewHolder(adapterDetailTransaksiLayananBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final DetailTransaksiLayananDAO detail_transaksi_layanan = result.get(position);

        holder.binding.setDetailTransaksiLayanan(detail_transaksi_layanan);
        setNamaLayanan(holder, detail_transaksi_layanan.getId_harga_layanan());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterDetailTransaksiLayananBinding binding;

        public MyViewHolder(@NonNull AdapterDetailTransaksiLayananBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    public void setNamaLayanan(final MyViewHolder holder, int id_harga_layanan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchHargaLayanan> hargalayananDAOCall = apiService.searchHargaLayanan(Integer.toString(id_harga_layanan));
        hargalayananDAOCall.enqueue(new Callback<SearchHargaLayanan>() {
            @Override
            public void onResponse(Call<SearchHargaLayanan> call, Response<SearchHargaLayanan> response) {
                HargaLayananDAO harga_layanan = response.body().getHarga_layanan();
                if(harga_layanan!=null){
                    searchLayanan(holder,harga_layanan.getId_layanan());
                    searchUkuranHewan(holder,harga_layanan.getId_ukuran_hewan());
                }
                holder.binding.setHargaLayanan(harga_layanan);
            }
            @Override
            public void onFailure(Call<SearchHargaLayanan> call, Throwable t) {
            }
        });
    }

    public void searchLayanan(final MyViewHolder holder, int id_layanan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchLayanan> layananDAOCall = apiService.searchLayanan(Integer.toString(id_layanan));
        layananDAOCall.enqueue(new Callback<SearchLayanan>() {
            @Override
            public void onResponse(Call<SearchLayanan> call, Response<SearchLayanan> response) {
                LayananDAO layanan = response.body().getLayanan();
                holder.binding.setLayanan(layanan);
            }

            @Override
            public void onFailure(Call<SearchLayanan> call, Throwable t) {
            }
        });
    }

    public void searchUkuranHewan(final MyViewHolder holder, int id_ukuran_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchUkuranHewan> ukuranhewanDAOCall = apiService.searchUkuran(Integer.toString(id_ukuran_hewan));
        ukuranhewanDAOCall.enqueue(new Callback<SearchUkuranHewan>() {
            @Override
            public void onResponse(Call<SearchUkuranHewan> call, Response<SearchUkuranHewan> response) {
                UkuranHewanDAO ukuran_hewan = response.body().getUkuranhewan();
                holder.binding.setUkuran(ukuran_hewan);
            }
            @Override
            public void onFailure(Call<SearchUkuranHewan> call, Throwable t) {
            }
        });
    }
}
