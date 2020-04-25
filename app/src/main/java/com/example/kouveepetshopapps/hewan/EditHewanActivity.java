package com.example.kouveepetshopapps.hewan;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditHewanActivity extends AppCompatActivity{

    private TextInputEditText namaUpdate;
    private AutoCompleteTextView nama_pemilik;
    private Spinner jenis_hewan;
    public List<PelangganDAO> ListPelanggan;
    public List<JenisHewanDAO> ListJenisHewan;
    private TextView tanggal_lahir;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String TAG = "EditHewanActivity";
    private Button btnUpdate;
    String tanggal_lahir_temp;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hewan);

        sharedPreferences();

        ListPelanggan = new ArrayList<>();
        ListJenisHewan = new ArrayList<>();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("hewan");
        System.out.println(json);
        final HewanDAO hewan = gson.fromJson(json, HewanDAO.class);

        setAtribut();
        setText();

        getListJenisHewan();
        getListPelanggan();

        tanggal_lahir = (TextView) findViewById(R.id.etTanggalLahirHewan);
        datepicker();
        btnUpdate = (Button) findViewById(R.id.btnUpdateHewan);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama kosong");
                }else if (jenis_hewan.getSelectedItem().toString().isEmpty()){
                    showDialog("Kolom jenis hewan kosong");
                }else if (nama_pemilik.getText().toString().isEmpty()){
                    showDialog("Kolom nama pemilik hewan kosong");
                }else if (tanggal_lahir.getText().toString().isEmpty()){
                    showDialog("Kolom tanggal lahir hewan kosong");
                }else
                {
                    updateHewan(hewan);
                }
            }
        });
    }

    private void sharedPreferences(){
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
    }

    private void setAtribut(){
        namaUpdate = findViewById(R.id.etNamaHewanUpdate);
        jenis_hewan = findViewById(R.id.spinJenisHewan);
        //Getting the instance of AutoCompleteTextView
        nama_pemilik = (AutoCompleteTextView) findViewById(R.id.etNamaPemilik);
        nama_pemilik.setThreshold(1);//will start working from first character
    }

    private void setText(){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
    }

    public void getListJenisHewan(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetJenisHewan> jenishewanDAOCall = apiService.getAllJenisHewanAktif();

        jenishewanDAOCall.enqueue(new Callback<GetJenisHewan>() {
            @Override
            public void onResponse(Call<GetJenisHewan> call, Response<GetJenisHewan> response) {
                ListJenisHewan.addAll(response.body().getListDataJenisHewan());
                System.out.println(ListJenisHewan.get(0).getNama());
                String[] arrName = new String[ListJenisHewan.size()];
                int i = 0;
                for (JenisHewanDAO jenis: ListJenisHewan
                ) {
                    arrName[i] = jenis.getNama();
                    i++;
                }

                //Creating the instance of ArrayAdapter containing list of fruit names
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (EditHewanActivity.this, android.R.layout.simple_spinner_dropdown_item, arrName);
                jenis_hewan.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetJenisHewan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListPelanggan(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetPelanggan> pelangganDAOCall = apiService.getAllPelangganAktif();

        pelangganDAOCall.enqueue(new Callback<GetPelanggan>() {
            @Override
            public void onResponse(Call<GetPelanggan> call, Response<GetPelanggan> response) {
                EditHewanActivity.this.ListPelanggan.addAll(response.body().getListDataPelanggan());
                System.out.println(ListPelanggan.get(0).getNama());
                String[] arrName = new String[ListPelanggan.size()];
                int i = 0;
                for (PelangganDAO pel: ListPelanggan
                ) {
                    arrName[i] = pel.getNama();
                    i++;
                }

                //Creating the instance of ArrayAdapter containing list of fruit names
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (EditHewanActivity.this, android.R.layout.select_dialog_item, arrName);

                nama_pemilik.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPelanggan> call, Throwable t) {
                //Toast.makeText(getContext(), "Gagal menampilkan pelanggan", Toast.LENGTH_SHORT).show();
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

                DatePickerDialog dialog = new DatePickerDialog(EditHewanActivity.this,
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
        Intent back = new Intent(EditHewanActivity.this, CsMainMenu.class);
        back.putExtra("from", "hewan");
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public int getIdPelanggan(String nama)
    {
        for (PelangganDAO pel:ListPelanggan
        ) {
            if(pel.getNama().equalsIgnoreCase(nama)){
                return pel.getId_pelanggan();
            }
        }
        return -1;
    }

    public int getIdJenisHewan(String nama)
    {
        for (JenisHewanDAO jenis:ListJenisHewan
        ) {
            if(jenis.getNama().equalsIgnoreCase(nama)){
                return jenis.getId_jenis_hewan();
            }
        }
        return -1;
    }

    public void updateHewan(HewanDAO hasil){
        int id_pemilik = getIdPelanggan(nama_pemilik.getText().toString());
        int id_jenis_hewan = getIdJenisHewan(jenis_hewan.getSelectedItem().toString());

        if(id_pemilik==-1){
            showDialog("Pemilik belum terdaftar, daftar member terlebih dahulu");
        }else if(id_jenis_hewan==-1){
            showDialog("Jenis hewan tidak tersedia");
        }else {
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> hewanDAOCall = apiService.ubahHewan(Integer.toString(hasil.getId_hewan()),Integer.toString(id_pemilik),
                    Integer.toString(id_jenis_hewan),namaUpdate.getText().toString(), tanggal_lahir_temp,
                    pegawai.getUsername());

            hewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    Toast.makeText(EditHewanActivity.this, "Sukses update hewan", Toast.LENGTH_SHORT).show();
                    startIntent();
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                    Toast.makeText(EditHewanActivity.this, "Gagal update hewan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditHewanActivity.this);

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
