package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.supplier.ListSupplierActivity;

public class AdminManagePageFragment extends Fragment {
    private Button produk, layanan, supplier, pengadaan, jenis_hewan, ukuran_hewan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_manage_page, container, false);

        produk = (Button) view.findViewById(R.id.btnKelolaProduk);
        layanan = (Button) view.findViewById(R.id.btnKelolaLayanan);
        supplier = (Button) view.findViewById(R.id.btnKelolaSupplier);
        pengadaan = (Button) view.findViewById(R.id.btnKelolaPengadaan);
        jenis_hewan = (Button) view.findViewById(R.id.btnKelolaJenisHewan);
        ukuran_hewan = (Button) view.findViewById(R.id.btnKelolaUkuranHewan);

        produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ListSupplierActivity.class);
            }
        });

        pengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        jenis_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ukuran_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void startIntent(Class nextView){
        Intent newActivity = new Intent(getContext(), nextView);
//        edit.putExtra("id", hasil.getId());
//        edit.putExtra("username", hasil.getUsername());
//        edit.putExtra("waktu", hasil.getDatetime());
//        edit.putExtra("alamat", hasil.getAddress());
//        edit.putExtra("deskripsi", hasil.getDescription());
//        edit.putExtra("kategori", hasil.getKategori());
        startActivity(newActivity);
    }
}
