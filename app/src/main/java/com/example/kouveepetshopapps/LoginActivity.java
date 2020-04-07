package com.example.kouveepetshopapps;

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

import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
import com.example.kouveepetshopapps.navigation.CsMainMenu;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.example.kouveepetshopapps.ukuran_hewan.ListUkuranHewanActivity;
import com.example.kouveepetshopapps.ukuran_hewan.TambahUkuranHewanActivity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button loginBtn;
    PegawaiDAO pegawai = new PegawaiDAO();
    SharedPreferences loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);

        loggedUser = this.getSharedPreferences("logged_user", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = loggedUser.getString("user", null);
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
        if(pegawai==null) {

        }else if(pegawai.getRole().equalsIgnoreCase("admin")){
            startIntent(AdminMainMenu.class);
        }else if(pegawai.getRole().equalsIgnoreCase("customer service")){
            startIntent(CsMainMenu.class);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    showDialog("Kolom username kosong.");
                }else if(password.getText().toString().isEmpty()){
                    showDialog("Kolom password kosong.");
                }else{
                    if (username.getText().toString().equalsIgnoreCase("admin")){
                        if(password.getText().toString().equalsIgnoreCase("admin123")) {
                            pegawai = new PegawaiDAO(-1,"Administrator","Jalan Babarsari No 43, Yogyakarta",
                                    "2020-02-11","082219273849","admin",
                                    "admin123","admin",null,null,
                                    null,null,null,null,1);
                            //Save Logged User into SharedPreferences
                            SharedPreferences.Editor editor = loggedUser.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(pegawai);
                            editor.putString("user", json);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Welcome "+pegawai.getNama(), Toast.LENGTH_SHORT).show();
                            startIntent(AdminMainMenu.class);

                        }
                    }else{
                        loginPegawai();
                    }
                }
            }
        });
    }

    public void loginPegawai(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPegawai> pegawaiDAOCall = apiService.authPegawai(username.getText().toString(),
                password.getText().toString());

        pegawaiDAOCall.enqueue(new Callback<SearchPegawai>() {
            @Override
            public void onResponse(Call<SearchPegawai> call, Response<SearchPegawai> response) {
                pegawai = response.body().getPegawai();
                System.out.println(pegawai.getNama());
                if(pegawai.getRole().equalsIgnoreCase("customer service")){
                    //Save Logged User into SharedPreferences
                    SharedPreferences.Editor editor = loggedUser.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(pegawai);
                    editor.putString("user", json);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Welcome "+pegawai.getNama(), Toast.LENGTH_SHORT).show();
                    startIntent(CsMainMenu.class);
                }else {
                    Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchPegawai> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form login dengan benar!");
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

    public void startIntent(Class nextClass){
        Intent login = new Intent(LoginActivity.this, nextClass);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
    }
}
