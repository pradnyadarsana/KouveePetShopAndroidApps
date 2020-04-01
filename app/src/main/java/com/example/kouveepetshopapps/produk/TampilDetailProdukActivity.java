package com.example.kouveepetshopapps.produk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.google.gson.Gson;

public class TampilDetailProdukActivity extends AppCompatActivity {

    private TextView id_produk, nama, satuan, jumlah_stok, min_stok, harga, created_at, created_by,
            modified_at, modified_by, delete_at, delete_by;
    private ImageView gambar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_produk);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("produk");
        System.out.println(json);
        ProdukDAO produk = gson.fromJson(json, ProdukDAO.class);

        id_produk = findViewById(R.id.viewIdProduk);
        nama = findViewById(R.id.viewNamaProduk);
        satuan = findViewById(R.id.viewSatuanProduk);
        jumlah_stok = findViewById(R.id.viewJumlahStokProduk);
        min_stok = findViewById(R.id.viewMinStokProduk);
        harga = findViewById(R.id.viewHargaProduk);
        created_at = findViewById(R.id.viewCreatedAtProduk);
        created_by = findViewById(R.id.viewCreatedByProduk);
        modified_at = findViewById(R.id.viewModifiedAtProduk);
        modified_by = findViewById(R.id.viewModifiedByProduk);
        delete_at = findViewById(R.id.viewDeleteAtProduk);
        delete_by = findViewById(R.id.viewDeleteByProduk);
        gambar = findViewById(R.id.viewGambarProduk);

        setData(produk);
    }

    public void setData(ProdukDAO produk){
        id_produk.setText(Integer.toString(produk.getId_produk()));
        nama.setText(produk.getNama());
        satuan.setText(produk.getSatuan());
        jumlah_stok.setText(Integer.toString(produk.getJumlah_stok()));
        min_stok.setText(Integer.toString(produk.getMin_stok()));
        harga.setText(Integer.toString(produk.getHarga()));
        created_at.setText(produk.getCreated_at());
        created_by.setText(produk.getCreated_by());
        modified_at.setText(produk.getModified_at());
        modified_by.setText(produk.getModified_by());
        delete_at.setText(produk.getDelete_at());
        delete_by.setText(produk.getDelete_by());

        String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
        Glide.with(TampilDetailProdukActivity.this).load(photo_url).into(gambar);
    }
}
