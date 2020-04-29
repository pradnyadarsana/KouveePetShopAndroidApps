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
import com.example.kouveepetshopapps.adapter.EditLayananAdapter;
import com.example.kouveepetshopapps.adapter.TambahLayananAdapter;
import com.example.kouveepetshopapps.adapter.UkuranHewanAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHargaLayanan;
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

    private List<HargaLayananDAO> ListHargaLayanan;
    private RecyclerView recyclerUkuranHewan;
    private EditLayananAdapter adapterUkuranHewan;
    private RecyclerView.LayoutManager mLayoutManager;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    LayananDAO layanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layanan);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("layanan");
        System.out.println(json);
        layanan = gson.fromJson(json, LayananDAO.class);

        setAtribut();
        setText(layanan);

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
        recyclerUkuranHewan = findViewById(R.id.recycler_view_edit_layanan);
        ListHargaLayanan = new ArrayList<>();
        adapterUkuranHewan = new EditLayananAdapter(EditLayananActivity.this, ListHargaLayanan);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerUkuranHewan.setLayoutManager(mLayoutManager);
        recyclerUkuranHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerUkuranHewan.setAdapter(adapterUkuranHewan);
        setRecycleView();
    }

    private void setText(LayananDAO layanan){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
        namaUpdate.setText(layanan.getNama());
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetHargaLayanan> hargaLayananDAOCall = apiService.searchHargaLayananByIdLayanan(String.valueOf(layanan.getId_layanan()));

        hargaLayananDAOCall.enqueue(new Callback<GetHargaLayanan>() {
            @Override
            public void onResponse(Call<GetHargaLayanan> call, Response<GetHargaLayanan> response) {
                ListHargaLayanan.addAll(response.body().getListDataHargaLayanan());
                System.out.println(ListHargaLayanan.get(0).getHarga());
                adapterUkuranHewan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetHargaLayanan> call, Throwable t) {
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

//                Gson gson = new Gson();
//                String json = getIntent().getStringExtra("layanan");
//                System.out.println(json);
//                final LayananDAO layanan = gson.fromJson(json, LayananDAO.class);

                System.out.println("response message: "+id_layanan);
                System.out.println("child count: "+recyclerUkuranHewan.getChildCount());


                //get text from adapter
                for(int i=0;i<recyclerUkuranHewan.getChildCount();i++)
                {
                    final TextView id_harga_layanan = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdEditLayanan);
                    final TextView id_layanan_edit = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdLayananEditLayanan);
                    final TextView id_ukuran = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tvIdUkuranEditLayanan);
                    final EditText harga = recyclerUkuranHewan.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.etEditHargaLayanan);

                    System.out.println(id_harga_layanan +"harga: "+harga);
                    ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
                    Call<PostUpdateDelete> hargalayananDAOCall = apiService.ubahHargaLayanan(String.valueOf(id_harga_layanan),
                            id_layanan_edit.getText().toString(), id_ukuran.getText().toString(), harga.getText().toString(), admin.getUsername());
                    hargalayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                        @Override
                        public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                            //Toast.makeText(TambahLayananActivity.this, "Sukses menambahkan harga layanan", Toast.LENGTH_SHORT).show();
                            System.out.println("sukses update harga layanan id ukuran: "+
                                    id_harga_layanan.getText().toString()+" "+harga.getText().toString());

                        }

                        @Override
                        public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                            //Toast.makeText(TambahLayananActivity.this, "Gagal menambahkan harga layanan", Toast.LENGTH_SHORT).show();
                            System.out.println("gagal update harga layanan id ukuran: "+
                                    id_harga_layanan.getText().toString()+" "+harga.getText().toString());
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
}
