package com.example.kouveepetshopapps.layanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.TambahLayananAdapter;
import com.example.kouveepetshopapps.adapter.UkuranHewanAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.ukuran_hewan.ListUkuranHewanActivity;
import com.example.kouveepetshopapps.ukuran_hewan.TambahUkuranHewanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLayananActivity extends AppCompatActivity{

    private TextInputEditText namaUpdate;
    private Button btnUpdateLayanan;

    private List<UkuranHewanDAO> ListUkuranHewan;
    private RecyclerView recyclerUkuranHewan;
    private TambahLayananAdapter adapterUkuranHewan;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layanan);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("layanan");
        System.out.println(json);
        final LayananDAO layanan = gson.fromJson(json, LayananDAO.class);

        setAtribut();
        setText();

        btnUpdateLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaUpdate.getText().toString().isEmpty())
                {
                    showDialog("Kolom nama layanan kosong");
                }else
                {
                    updateLayanan(layanan);
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
        namaUpdate = findViewById(R.id.etNamaLayananUpdate);
        btnUpdateLayanan = findViewById(R.id.btnUpdateLayanan);

        //recyclerview
        recyclerUkuranHewan = findViewById(R.id.recycler_view_tambah_layanan);
        ListUkuranHewan = new ArrayList<>();
        adapterUkuranHewan = new TambahLayananAdapter(EditLayananActivity.this, ListUkuranHewan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerUkuranHewan.setLayoutManager(mLayoutManager);
        recyclerUkuranHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerUkuranHewan.setAdapter(adapterUkuranHewan);
        setRecycleView();
    }

    private void setText(){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetUkuranHewan> ukuranHewanDAOCall = apiService.getAllUkuranHewanAktif();

        ukuranHewanDAOCall.enqueue(new Callback<GetUkuranHewan>() {
            @Override
            public void onResponse(Call<GetUkuranHewan> call, Response<GetUkuranHewan> response) {
                ListUkuranHewan.addAll(response.body().getListDataUkuranHewan());
                System.out.println(ListUkuranHewan.get(0).getNama());
                adapterUkuranHewan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUkuranHewan> call, Throwable t) {
                Toast.makeText(EditLayananActivity.this, "Gagal menampilkan Ukuran Hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startIntent(){
        Intent back = new Intent(EditLayananActivity.this, ListLayananActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateLayanan(final LayananDAO hasil){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> layananDAOCall = apiService.ubahLayanan(Integer.toString(hasil.getId_layanan()),namaUpdate.getText().toString(),
                admin.getUsername());

        layananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                final String id_layanan = response.body().getMessage();

                Gson gson = new Gson();
                String json = getIntent().getStringExtra("layanan");
                System.out.println(json);
                final LayananDAO layanan = gson.fromJson(json, LayananDAO.class);

                System.out.println("response message: "+id_layanan);
                System.out.println("child count: "+recyclerUkuranHewan.getChildCount());


                //get text from adapter
                for(int i=0;i<recyclerUkuranHewan.getChildCount();i++)
                {
                    final TextView id_ukuran_hewan = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdTambahLayanan);
                    final EditText harga = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.etHargaLayanan);

                    ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
                    Call<PostUpdateDelete> hargalayananDAOCall = apiService.tambahHargaLayanan(Integer.toString(hasil.getId_layanan()),
                            id_ukuran_hewan.getText().toString(), harga.getText().toString(), admin.getUsername());
                    hargalayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                        @Override
                        public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                            //Toast.makeText(TambahLayananActivity.this, "Sukses menambahkan harga layanan", Toast.LENGTH_SHORT).show();
                            System.out.println("sukses update harga layanan id ukuran: "+
                                    id_ukuran_hewan.getText().toString()+" "+harga.getText().toString());
                        }

                        @Override
                        public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                            //Toast.makeText(TambahLayananActivity.this, "Gagal menambahkan harga layanan", Toast.LENGTH_SHORT).show();
                            System.out.println("gagal update harga layanan id ukuran: "+
                                    id_ukuran_hewan.getText().toString()+" "+harga.getText().toString());
                            deletePermanentLayanan(layanan);
                            deletePermanentHargaLayanan(layanan);
                        }
                    });
                }
                System.out.println("sukses update layanan dan harganya");
                Toast.makeText(EditLayananActivity.this, "Sukses update layanan", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                Toast.makeText(EditLayananActivity.this, "Gagal update layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditLayananActivity.this);

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

    private void deletePermanentLayanan(LayananDAO hasil) {
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> layananDAOCall = apiService.hapusPermanentLayanan(Integer.toString(hasil.getId_layanan()));

        layananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                System.out.println("sukses menghapus layanan");
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println("gagal menghapus layanan");
            }
        });
    }

    private void deletePermanentHargaLayanan(LayananDAO hasil) {
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> hargalayananDAOCall = apiService.hapusPermanentHargaLayanan(Integer.toString(hasil.getId_layanan()));

        hargalayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                System.out.println("sukses menghapus harga layanan");
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println("gagal menghapus harga layanan");
            }
        });
    }
}
