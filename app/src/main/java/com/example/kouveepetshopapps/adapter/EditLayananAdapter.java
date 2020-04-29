package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.SearchUkuranHewan;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLayananAdapter extends RecyclerView.Adapter<EditLayananAdapter.MyViewHolder> {
    private Context context;
    private List<HargaLayananDAO> result;

    public EditLayananAdapter(Context context, List<HargaLayananDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_edit_layanan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final HargaLayananDAO hargaLayanan = result.get(position);
        System.out.println(result.get(position).getId_ukuran_hewan()+" "+position);
        holder.id_harga_layanan.setText(Integer.toString(hargaLayanan.getId_harga_layanan()));
        holder.id_layanan.setText(String.valueOf(hargaLayanan.getId_layanan()));
        holder.id_ukuran.setText(String.valueOf(hargaLayanan.getId_ukuran_hewan()));
        setNamaUkuran(holder, hargaLayanan.getId_ukuran_hewan());
        holder.harga_layanan.setText(String.valueOf(hargaLayanan.getHarga()));
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, id_harga_layanan, id_ukuran, id_layanan;
        private TextInputEditText harga_layanan;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_harga_layanan = itemView.findViewById(R.id.tvIdEditLayanan);
            id_layanan = itemView.findViewById(R.id.tvIdLayananEditLayanan);
            id_ukuran = itemView.findViewById(R.id.tvIdUkuranEditLayanan);
            nama = itemView.findViewById(R.id.tvEditLayanan);
            harga_layanan = itemView.findViewById(R.id.etEditHargaLayanan);
            parent = itemView.findViewById(R.id.ParentEditLayanan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    public void setNamaUkuran(final MyViewHolder holder, final int id_ukuran_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchUkuranHewan> ukuranDAOCall = apiService.searchUkuran(Integer.toString(id_ukuran_hewan));

        ukuranDAOCall.enqueue(new Callback<SearchUkuranHewan>() {
            @Override
            public void onResponse(Call<SearchUkuranHewan> call, Response<SearchUkuranHewan> response) {
                UkuranHewanDAO ukuran = response.body().getUkuranhewan();
                if(ukuran.getNama()!=null){
                    holder.nama.setText(ukuran.getNama());
                }else{
                    holder.nama.setText(Integer.toString(id_ukuran_hewan));
                }

            }
            @Override
            public void onFailure(Call<SearchUkuranHewan> call, Throwable t) {
                holder.nama.setText(Integer.toString(id_ukuran_hewan));
            }
        });
    }
}
