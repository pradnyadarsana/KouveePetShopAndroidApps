package com.example.kouveepetshopapps.ukuran_hewan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.google.gson.Gson;

public class TampilDetailUkuranHewanActivity extends AppCompatActivity {

    private TextView nama, id_ukuran_hewan, created_at, created_by, modified_at, modified_by,
            delete_at, delete_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_ukuran_hewan);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("ukuran_hewan");
        System.out.println(json);
        UkuranHewanDAO ukuran_hewan = gson.fromJson(json, UkuranHewanDAO.class);

        nama = findViewById(R.id.viewNamaUkuranHewan);
        id_ukuran_hewan = findViewById(R.id.viewIdUkuranHewan);
        created_at = findViewById(R.id.viewCreatedAtUkuranHewan);
        created_by = findViewById(R.id.viewCreatedByUkuranHewan);
        modified_at = findViewById(R.id.viewModifiedAtUkuranHewan);
        modified_by = findViewById(R.id.viewModifiedByUkuranHewan);
        delete_at = findViewById(R.id.viewDeleteAtUkuranHewan);
        delete_by = findViewById(R.id.viewDeleteByUkuranHewan);

        setData(ukuran_hewan);
    }

    public void setData(UkuranHewanDAO ukuran_hewan){
        nama.setText(ukuran_hewan.getNama());
        id_ukuran_hewan.setText(Integer.toString(ukuran_hewan.getId_ukuran_hewan()));
        created_at.setText(ukuran_hewan.getCreated_at());
        created_by.setText(ukuran_hewan.getCreated_by());
        modified_at.setText(ukuran_hewan.getModified_at());
        modified_by.setText(ukuran_hewan.getModified_by());
        delete_at.setText(ukuran_hewan.getDelete_at());
        delete_by.setText(ukuran_hewan.getDelete_by());
    }
}
