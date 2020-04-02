package com.example.kouveepetshopapps.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kouveepetshopapps.LoginActivity;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.google.gson.Gson;

public class ProfilCsFragment extends Fragment {
    private TextView id_pegawai, nama, username, alamat, tanggal_lahir, telp, role, created_at, created_by,
            modified_at, modified_by, delete_at, delete_by;
    private RelativeLayout btnLogout;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_cs, container, false);

        loggedUser = getActivity().getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        id_pegawai = view.findViewById(R.id.viewIdPegawai);
        nama = view.findViewById(R.id.viewNamaPegawai);
        username = view.findViewById(R.id.viewUsernamePegawai);
        alamat = view.findViewById(R.id.viewAlamatPegawai);
        tanggal_lahir = view.findViewById(R.id.viewTanggalLahirPegawai);
        telp = view.findViewById(R.id.viewTelpPegawai);
        role = view.findViewById(R.id.viewRolePegawai);
        created_at = view.findViewById(R.id.viewCreatedAtPegawai);
        created_by = view.findViewById(R.id.viewCreatedByPegawai);
        modified_at = view.findViewById(R.id.viewModifiedAtPegawai);
        modified_by = view.findViewById(R.id.viewModifiedByPegawai);
        delete_at = view.findViewById(R.id.viewDeleteAtPegawai);
        delete_by = view.findViewById(R.id.viewDeleteByPegawai);

        btnLogout = view.findViewById(R.id.btnLogoutPegawai);

        setField(pegawai);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
        return view;
    }

    public void setField(PegawaiDAO pegawai){
        id_pegawai.setText(Integer.toString(pegawai.getId_pegawai()));
        nama.setText(pegawai.getNama());
        username.setText(pegawai.getUsername());
        alamat.setText(pegawai.getAlamat());
        tanggal_lahir.setText(pegawai.getTanggal_lahir());
        telp.setText(pegawai.getTelp());
        role.setText(pegawai.getRole());
        created_at.setText(pegawai.getCreated_at());
        created_by.setText(pegawai.getCreated_by());
        modified_at.setText(pegawai.getModified_at());
        modified_by.setText(pegawai.getModified_by());
        delete_at.setText(pegawai.getDelete_at());
        delete_by.setText(pegawai.getDelete_by());
    }

    private void showLogoutDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin akan keluar?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
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
