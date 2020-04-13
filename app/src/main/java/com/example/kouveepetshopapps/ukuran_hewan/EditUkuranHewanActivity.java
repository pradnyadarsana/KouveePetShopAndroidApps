package com.example.kouveepetshopapps.ukuran_hewan;

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
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUkuranHewanActivity extends AppCompatActivity {

    EditText ukuranUpdate;
    Button btnUpdate;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ukuran_hewan);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("ukuran_hewan");
        System.out.println(json);
        final UkuranHewanDAO ukuran_hewan = gson.fromJson(json, UkuranHewanDAO.class);

        setAtribut();
        setText();

        btnUpdate = (Button) findViewById(R.id.btnUpdateUkuranHewan);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ukuranUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama ukuran kosong");
                }else
                {
                    updateUkuranHewan(ukuran_hewan);
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
        ukuranUpdate = (EditText) findViewById(R.id.etNamaUkuranHewanUpdate);
    }

    public void setText(){
        ukuranUpdate.setText(getIntent().getStringExtra("nama"));
    }

    private void startIntent(){
        Intent back = new Intent(EditUkuranHewanActivity.this, ListUkuranHewanActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateUkuranHewan(UkuranHewanDAO hasil){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> ukuranHewanDAOCall = apiService.ubahUkuranHewan(Integer.toString(hasil.getId_ukuran_hewan()), ukuranUpdate.getText().toString(), admin.getUsername());

        ukuranHewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(EditUkuranHewanActivity.this, "Sukses update ukuran hewan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditUkuranHewanActivity.this, "Gagal update ukuran hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditUkuranHewanActivity.this);

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
