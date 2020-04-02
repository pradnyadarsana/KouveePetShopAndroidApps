package com.example.kouveepetshopapps.pelanggan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.hewan.ListHewanFragment;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.google.gson.Gson;

public class TampilDetailPelangganFragment extends Fragment {
    private TextView id_pelanggan, nama, telp, tanggal_lahir, alamat, created_at, created_by,
            modified_at, modified_by, delete_at, delete_by;
    Bundle data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tampil_detail_pelanggan, container, false);
        init(view);

        Toolbar toolbar = view.findViewById(R.id.searchDetailPelangganToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar fancy stuff
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Detail Pelanggan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListPelangganFragment();
                loadFragment(fragment);
            }
        });

        data = this.getArguments();
        Gson gson = new Gson();
        String json = data.getString("pelanggan");
        System.out.println(json);
        PelangganDAO pelanggan = gson.fromJson(json, PelangganDAO.class);

        setField(pelanggan);
        return view;
    }

    public void init(View view){
        id_pelanggan = (TextView) view.findViewById(R.id.viewIdPelanggan);
        nama = (TextView) view.findViewById(R.id.viewNamaPelanggan);
        telp = (TextView) view.findViewById(R.id.viewTelpPelanggan);
        tanggal_lahir = (TextView) view.findViewById(R.id.viewTanggalLahirPelanggan);
        alamat = (TextView) view.findViewById(R.id.viewAlamatPelanggan);
        created_at = (TextView) view.findViewById(R.id.viewCreatedAtPelanggan);
        created_by = (TextView) view.findViewById(R.id.viewCreatedByPelanggan);
        modified_at = (TextView) view.findViewById(R.id.viewModifiedAtPelanggan);
        modified_by = (TextView) view.findViewById(R.id.viewModifiedByPelanggan);
        delete_at = (TextView) view.findViewById(R.id.viewDeleteAtPelanggan);
        delete_by = (TextView) view.findViewById(R.id.viewDeleteByPelanggan);
    }

    public void setField(PelangganDAO pelanggan){
        id_pelanggan.setText(Integer.toString(pelanggan.getId_pelanggan()));
        nama.setText(pelanggan.getNama());
        telp.setText(pelanggan.getTelp());
        tanggal_lahir.setText(pelanggan.getTanggal_lahir());
        alamat.setText(pelanggan.getAlamat());
        created_at.setText(pelanggan.getCreated_at());
        created_by.setText(pelanggan.getCreated_by());
        modified_at.setText(pelanggan.getModified_at());
        modified_by.setText(pelanggan.getModified_by());
        delete_at.setText(pelanggan.getDelete_at());
        delete_by.setText(pelanggan.getDelete_by());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
