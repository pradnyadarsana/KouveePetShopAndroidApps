package com.example.kouveepetshopapps.supplier;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.google.gson.Gson;


public class TampilDetailSupplierActivity extends AppCompatActivity {

    private TextView nama, id_supplier, telp, alamat, created_at, created_by, modified_at, modified_by,
            delete_at, delete_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_supplier);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("supplier");
        System.out.println(json);
        SupplierDAO supplier = gson.fromJson(json, SupplierDAO.class);

        nama = findViewById(R.id.viewNamaSupplier);
        id_supplier = findViewById(R.id.viewIdSupplier);
        telp = findViewById(R.id.viewTelpSupplier);
        alamat = findViewById(R.id.viewAlamatSupplier);
        created_at = findViewById(R.id.viewCreatedAtSupplier);
        created_by = findViewById(R.id.viewCreatedBySupplier);
        modified_at = findViewById(R.id.viewModifiedAtSupplier);
        modified_by = findViewById(R.id.viewModifiedBySupplier);
        delete_at = findViewById(R.id.viewDeleteAtSupplier);
        delete_by = findViewById(R.id.viewDeleteBySupplier);

        setData(supplier);
    }

    public void setData(SupplierDAO supplier){
        nama.setText(supplier.getNama());
        id_supplier.setText(Integer.toString(supplier.getId_supplier()));
        telp.setText(supplier.getTelp());
        alamat.setText(supplier.getAlamat());
        created_at.setText(supplier.getCreated_at());
        created_by.setText(supplier.getCreated_by());
        modified_at.setText(supplier.getModified_at());
        modified_by.setText(supplier.getModified_by());
        delete_at.setText(supplier.getDelete_at());
        delete_by.setText(supplier.getDelete_by());
    }
}
