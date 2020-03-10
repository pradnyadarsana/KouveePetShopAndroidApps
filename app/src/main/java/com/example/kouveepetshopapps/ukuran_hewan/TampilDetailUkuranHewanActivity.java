package com.example.kouveepetshopapps.ukuran_hewan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;

public class TampilDetailUkuranHewanActivity extends AppCompatActivity {

    private TextView nama, id_ukuran_hewan, created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_ukuran_hewan);

        nama = findViewById(R.id.viewNamaUkuranHewan);
        id_ukuran_hewan = findViewById(R.id.viewIdUkuranHewan);
        created_at = findViewById(R.id.viewCreatedAtUkuranHewan);

        setData();
    }

    public void setData(){
        nama.setText(getIntent().getStringExtra("nama"));
        id_ukuran_hewan.setText(getIntent().getStringExtra("id_ukuran_hewan"));
        created_at.setText(getIntent().getStringExtra("created_at"));
    }
}
