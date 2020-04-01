package com.example.kouveepetshopapps.ukuran_hewan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.supplier.ListSupplierActivity;
import com.example.kouveepetshopapps.supplier.TambahSupplierActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahUkuranHewanActivity extends AppCompatActivity {
    private EditText nama;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ukuran_hewan);
        nama = (EditText) findViewById(R.id.etNamaUkuranHewan);
        btnTambah = (Button) findViewById(R.id.btnTambahUkuranHewan);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama ukuran hewan kosong");
                }else
                {
                    tambahUkuranHewan();
                }
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(TambahUkuranHewanActivity.this, ListUkuranHewanActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void tambahUkuranHewan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> ukuranHewanDAOCall = apiService.tambahUkuranHewan(nama.getText().toString(),
                "admin");

        ukuranHewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(TambahUkuranHewanActivity.this, "Sukses menambahkan ukuran hewan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(TambahUkuranHewanActivity.this, "Gagal menambahkan ukuran hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahUkuranHewanActivity.this);

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
