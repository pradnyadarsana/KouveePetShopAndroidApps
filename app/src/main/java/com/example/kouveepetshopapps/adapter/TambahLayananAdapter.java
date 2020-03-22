package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.ukuran_hewan.TampilDetailUkuranHewanActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahLayananAdapter extends RecyclerView.Adapter<TambahLayananAdapter.MyViewHolder> {
    private Context context;
    private List<UkuranHewanDAO> result;

    public TambahLayananAdapter(Context context, List<UkuranHewanDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_tambah_layanan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final UkuranHewanDAO ukuranHewan = result.get(position);
        System.out.println(result.get(position).getId_ukuran_hewan()+" "+position);
        holder.id_ukuran_hewan.setText(Integer.toString(ukuranHewan.getId_ukuran_hewan()));
        holder.nama.setText(ukuranHewan.getNama());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, id_ukuran_hewan;
        private EditText harga_layanan;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_ukuran_hewan = itemView.findViewById(R.id.tvIdTambahLayanan);
            nama = itemView.findViewById(R.id.tvTambahLayanan);
            harga_layanan = itemView.findViewById(R.id.etHargaLayanan);
            parent = itemView.findViewById(R.id.ParentTambahLayanan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }
}
