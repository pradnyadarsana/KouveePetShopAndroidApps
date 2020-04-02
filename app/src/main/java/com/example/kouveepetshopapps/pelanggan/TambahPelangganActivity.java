package com.example.kouveepetshopapps.pelanggan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPelangganActivity extends AppCompatActivity {

    private EditText nama, telp, alamat;
    private TextView tanggal_lahir;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String TAG = "TambahPelangganActivity";
    private Button btnTambah;
    String tanggal_lahir_temp;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pelanggan);

        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        nama = (EditText) findViewById(R.id.etNamaPelanggan);
        telp = (EditText) findViewById(R.id.etTelpPelanggan);
        alamat = (EditText) findViewById(R.id.etAlamatPelanggan);
        tanggal_lahir = (TextView) findViewById(R.id.etTanggalLahirPelanggan);
        datepicker();
        btnTambah = (Button) findViewById(R.id.btnTambahPelanggan);

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
                }else if (tanggal_lahir.getText().toString().isEmpty()){
                    showDialog("Kolom tanggal lahir kosong");
                }else
                {
                    tambahPelanggan();
                }

            }
        });
    }

    public void datepicker()
    {
        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TambahPelangganActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd-mm-yyyy: " + day + "-" + month + "-" + year);

                String date = day + " - " +  month + " - " + year;
                tanggal_lahir_temp = year+"-"+month+"-"+day;
                tanggal_lahir.setText(date);
                System.out.println(tanggal_lahir.getText().toString());
                System.out.println(tanggal_lahir_temp);
            }
        };
    }

    private void startIntent(){
        Intent back = new Intent(TambahPelangganActivity.this, CsMainMenu.class);
        back.putExtra("from", "pelanggan");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void tambahPelanggan(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> pelangganDAOCall = apiService.tambahPelanggan(nama.getText().toString(),
                alamat.getText().toString(), tanggal_lahir_temp, telp.getText().toString(),
                pegawai.getUsername());

        pelangganDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(TambahPelangganActivity.this, "Sukses menambahkan pelanggan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(TambahPelangganActivity.this, "Gagal menambahkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahPelangganActivity.this);

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
