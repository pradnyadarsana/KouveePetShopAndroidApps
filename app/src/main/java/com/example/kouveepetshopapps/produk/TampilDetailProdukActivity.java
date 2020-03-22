package com.example.kouveepetshopapps.produk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;

public class TampilDetailProdukActivity extends AppCompatActivity {

    private TextView id_produk, nama, satuan, jumlah_stok, min_stok, harga, created_at;
    private ImageView gambar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_produk);
        id_produk = findViewById(R.id.viewIdProduk);
        nama = findViewById(R.id.viewNamaProduk);
        satuan = findViewById(R.id.viewSatuanProduk);
        jumlah_stok = findViewById(R.id.viewJumlahStokProduk);
        min_stok = findViewById(R.id.viewMinStokProduk);
        harga = findViewById(R.id.viewHargaProduk);
        created_at = findViewById(R.id.viewCreatedAtProduk);
        gambar = findViewById(R.id.viewGambarProduk);
        setData();
    }

    public void setData(){
        id_produk.setText(getIntent().getStringExtra("id_produk"));
        nama.setText(getIntent().getStringExtra("nama"));
        satuan.setText(getIntent().getStringExtra("satuan"));
        jumlah_stok.setText(getIntent().getStringExtra("jumlah_stok"));
        min_stok.setText(getIntent().getStringExtra("min_stok"));
        harga.setText(getIntent().getStringExtra("harga"));
        created_at.setText(getIntent().getStringExtra("created_at"));

        String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+getIntent().getStringExtra("gambar");
        Glide.with(TampilDetailProdukActivity.this).load(photo_url).into(gambar);
    }
}
