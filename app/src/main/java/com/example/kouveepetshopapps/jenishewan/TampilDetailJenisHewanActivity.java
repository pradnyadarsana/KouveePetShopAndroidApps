package com.example.kouveepetshopapps.jenishewan;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.google.gson.Gson;

public class TampilDetailJenisHewanActivity extends AppCompatActivity {

    private TextView nama, id_jenis_hewan, created_at, created_by, modified_at, modified_by,
            delete_at, delete_by;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_jenishewan);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("jenis_hewan");
        System.out.println(json);
        JenisHewanDAO jenis_hewan = gson.fromJson(json, JenisHewanDAO.class);

        nama = findViewById(R.id.viewNamaJenisHewan);
        id_jenis_hewan = findViewById(R.id.viewIdJenisHewan);
        created_at = findViewById(R.id.viewCreatedAtJenisHewan);
        created_by = findViewById(R.id.viewCreatedByJenisHewan);
        modified_at = findViewById(R.id.viewModifiedAtJenisHewan);
        modified_by = findViewById(R.id.viewModifiedByJenisHewan);
        delete_at = findViewById(R.id.viewDeleteAtJenisHewan);
        delete_by = findViewById(R.id.viewDeleteByJenisHewan);
        setData(jenis_hewan);
    }

    public void setData(JenisHewanDAO jenishewan){
        nama.setText(jenishewan.getNama());
        id_jenis_hewan.setText(Integer.toString(jenishewan.getId_jenis_hewan()));
        created_at.setText(jenishewan.getCreated_at());
        created_by.setText(jenishewan.getCreated_by());
        modified_at.setText(jenishewan.getModified_at());
        modified_by.setText(jenishewan.getModified_by());
        delete_at.setText(jenishewan.getDelete_at());
        delete_by.setText(jenishewan.getDelete_by());
    }
}
