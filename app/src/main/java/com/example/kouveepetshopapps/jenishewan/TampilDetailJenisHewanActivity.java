package com.example.kouveepetshopapps.jenishewan;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshopapps.R;

public class TampilDetailJenisHewanActivity extends AppCompatActivity {

    private TextView nama, created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_jenishewan);
        nama = findViewById(R.id.viewNamaJenisHewan);
        created_at = findViewById(R.id.viewCreatedAtJenisHewan);

        setData();
    }

    public void setData(){
        nama.setText(getIntent().getStringExtra("nama"));
        created_at.setText(getIntent().getStringExtra("created_at"));
    }
}
