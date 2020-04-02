package com.example.kouveepetshopapps.supplier;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSupplierActivity extends AppCompatActivity {

    private EditText nama, telp, alamat;
    private Button btnTambah;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_supplier);

        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        nama = (EditText) findViewById(R.id.etNamaSupplier);
        telp = (EditText) findViewById(R.id.etTelpSupplier);
        alamat = (EditText) findViewById(R.id.etAlamatSupplier);
        btnTambah = (Button) findViewById(R.id.btnTambahSupplier);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama kosong");
                }else if (telp.getText().toString().isEmpty()){
                    showDialog("Kolom nomor telepon kosong");
                }else if (alamat.getText().toString().isEmpty()){
                    showDialog("Kolom alamat kosong");
                }else
                {
                    tambahSupplier();
                }
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(TambahSupplierActivity.this, ListSupplierActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void tambahSupplier(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> supplierDAOCall = apiService.tambahSupplier(nama.getText().toString(),
                alamat.getText().toString(), telp.getText().toString(), admin.getUsername());

        supplierDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(TambahSupplierActivity.this, "Sukses menambahkan supplier", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(TambahSupplierActivity.this, "Gagal menambahkan supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahSupplierActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form dengan benar!");
        alertDialogBuilder.setMessage(rules);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setPositiveButton("SIAP!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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
