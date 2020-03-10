package com.example.kouveepetshopapps.jenishewan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.response.PostUpdateDelete;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahJenisHewanActivity extends AppCompatActivity {

    private EditText nama;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jenishewan);
        nama = (EditText) findViewById(R.id.etNamaJenisHewan);
        btnTambah = (Button) findViewById(R.id.btnTambahJenisHewan);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama kosong");
                }else
                {
                    tambahJenisHewan();
                }
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(TambahJenisHewanActivity.this, ListJenisHewanActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void tambahJenisHewan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> jenisHewanDAOCall = apiService.tambahJenisHewan(nama.getText().toString(),
                "admin");

        jenisHewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(TambahJenisHewanActivity.this, "Sukses menambahkan jenis hewan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(TambahJenisHewanActivity.this, "Gagal menambahkan jenis hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahJenisHewanActivity.this);

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
