package com.example.kouveepetshopapps.jenishewan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJenisHewanActivity extends AppCompatActivity {

    EditText namaUpdate;
    Button btnUpdate;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jenishewan);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("jenis_hewan");
        System.out.println(json);
        final JenisHewanDAO jenis_hewan = gson.fromJson(json, JenisHewanDAO.class);

        setAtribut();
        setText();

        btnUpdate = (Button) findViewById(R.id.btnUpdateJenisHewan);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama jenis hewan kosong");
                }else
                {
                    updateJenisHewan(jenis_hewan);
                }
            }
        });
    }

    public void sharedPreferences(){
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
    }

    public void setAtribut(){
        namaUpdate = (EditText) findViewById(R.id.etNamaJenisHewanUpdate);
    }

    public void setText(){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
        //id = getIntent().getStringExtra("id_jenis_hewan");
        //id = getIntent().getExtras().getInt("id_jenis_hewan");
        //idj = String.valueOf(id);
    }

    private void startIntent(){
        Intent back = new Intent(EditJenisHewanActivity.this, ListJenisHewanActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateJenisHewan(JenisHewanDAO hasil){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> jenisHewanDAOCall = apiService.ubahJenisHewan(Integer.toString(hasil.getId_jenis_hewan()), namaUpdate.getText().toString(), admin.getUsername());

        jenisHewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(EditJenisHewanActivity.this, "Sukses update jenis hewan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditJenisHewanActivity.this, "Gagal update jenis hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditJenisHewanActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form dengan benar!");
        alertDialogBuilder.setMessage(rules);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("SIAP!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
