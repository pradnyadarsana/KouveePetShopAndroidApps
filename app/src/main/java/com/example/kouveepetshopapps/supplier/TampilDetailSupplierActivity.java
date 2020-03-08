package com.example.kouveepetshopapps.supplier;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;


public class TampilDetailSupplierActivity extends AppCompatActivity {

    private TextView nama, id_supplier, telp, alamat, created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_supplier);
        nama = findViewById(R.id.viewNamaSupplier);
        id_supplier = findViewById(R.id.viewIdSupplier);
        telp = findViewById(R.id.viewTelpSupplier);
        alamat = findViewById(R.id.viewAlamatSupplier);
        created_at = findViewById(R.id.viewCreatedAtSupplier);

        setData();
    }

    public void setData(){
        nama.setText(getIntent().getStringExtra("nama"));
        id_supplier.setText(getIntent().getStringExtra("id_supplier"));
        telp.setText(getIntent().getStringExtra("telp"));
        alamat.setText(getIntent().getStringExtra("alamat"));
        created_at.setText(getIntent().getStringExtra("created_at"));
    }
}
