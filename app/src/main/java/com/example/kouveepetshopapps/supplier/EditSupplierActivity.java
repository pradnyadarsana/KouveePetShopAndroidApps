package com.example.kouveepetshopapps.supplier;

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
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSupplierActivity extends AppCompatActivity {

    EditText namaUpdate;
    EditText tlpUpdate;
    EditText alamatUpdate;

    Button btnUpdate;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("supplier");
        System.out.println(json);
        final SupplierDAO supplier = gson.fromJson(json, SupplierDAO.class);

        setAtribut();
        setText();

        btnUpdate = (Button) findViewById(R.id.btnUpdateSupplier);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama supplier kosong");
                }else if(tlpUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom no telepon kosong");
                }else if(alamatUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom alamat kosong");
                }else
                {
                    updateSupplier(supplier);
                }
            }
        });
    }

    private void sharedPreferences(){
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
    }

    private void setAtribut(){
        namaUpdate = (EditText) findViewById(R.id.etNamaSupplierUpdate);
        tlpUpdate = (EditText) findViewById(R.id.etTelpSupplierUpdate);
        alamatUpdate = (EditText) findViewById(R.id.etAlamatSupplierUpdate);
    }

    private void setText(){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
        tlpUpdate.setText(getIntent().getStringExtra("telp"));
        alamatUpdate.setText(getIntent().getStringExtra("alamat"));
    }

    private void startIntent(){
        Intent back = new Intent(EditSupplierActivity.this, ListSupplierActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateSupplier(SupplierDAO hasil){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> supplierDAOCall = apiService.ubahSupplier(Integer.toString(hasil.getId_supplier()), namaUpdate.getText().toString(),
                alamatUpdate.getText().toString(), tlpUpdate.getText().toString(), admin.getUsername());

        supplierDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(EditSupplierActivity.this, "Sukses update supplier", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditSupplierActivity.this, "Gagal update supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditSupplierActivity.this);

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
