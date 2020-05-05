package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kouveepetshopapps.LoginActivity;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.jenishewan.ListJenisHewanActivity;
import com.example.kouveepetshopapps.layanan.ListLayananActivity;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.pengadaan.PengadaanActivity;
import com.example.kouveepetshopapps.produk.ListProdukActivity;
import com.example.kouveepetshopapps.supplier.ListSupplierActivity;
import com.example.kouveepetshopapps.ukuran_hewan.ListUkuranHewanActivity;
import com.google.gson.Gson;

public class AdminManagePageFragment extends Fragment {
    private CardView produk, layanan, supplier, pengadaan, jenis_hewan, ukuran_hewan;
    private TextView nama_admin;
    private ImageView btnLogoutAdmin;
    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_manage_page, container, false);

        loggedUser = getActivity().getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        nama_admin = view.findViewById(R.id.viewNamaAdmin);
        nama_admin.setText(admin.getNama());

        btnLogoutAdmin = view.findViewById(R.id.btnLogoutAdmin);
        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        produk = (CardView) view.findViewById(R.id.btnKelolaProduk);
        layanan = (CardView) view.findViewById(R.id.btnKelolaLayanan);
        supplier = (CardView) view.findViewById(R.id.btnKelolaSupplier);
        pengadaan = (CardView) view.findViewById(R.id.btnKelolaPengadaan);
        jenis_hewan = (CardView) view.findViewById(R.id.btnKelolaJenisHewan);
        ukuran_hewan = (CardView) view.findViewById(R.id.btnKelolaUkuranHewan);

        produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ListProdukActivity.class);
            }
        });

        layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ListLayananActivity.class);
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
                startIntent(PengadaanActivity.class);
            }
        });

        jenis_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ListJenisHewanActivity.class);
            }
        });

        ukuran_hewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(ListUkuranHewanActivity.class);
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

    private void showLogoutDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin akan keluar?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_logout)
                .setCancelable(false)
                .setPositiveButton("Keluar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //delete loggedUser data
                        SharedPreferences loggedUser = getActivity().getSharedPreferences("logged_user", Context.MODE_PRIVATE);
                        loggedUser.edit().clear().apply();
                        //start intent to login page
                        Intent logout = new Intent(getActivity(), LoginActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logout);
                        getActivity().finish();
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
