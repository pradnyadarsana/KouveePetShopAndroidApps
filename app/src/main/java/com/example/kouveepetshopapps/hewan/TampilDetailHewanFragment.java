package com.example.kouveepetshopapps.hewan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kouveepetshopapps.R;

public class TampilDetailHewanFragment extends Fragment {
    private TextView id_hewan, nama, nama_jenis_hewan, nama_pemilik, tanggal_lahir, created_at, created_by;
    Bundle data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tampil_detail_hewan, container, false);
        init(view);
        setField();
        return view;
    }

    public void init(View view){
        id_hewan = (TextView) view.findViewById(R.id.viewIdHewan);
        nama = (TextView) view.findViewById(R.id.viewNamaHewan);
        nama_jenis_hewan = view.findViewById(R.id.viewJenisHewanPelanggan);
        nama_pemilik = view.findViewById(R.id.viewNamaPemilik);
        tanggal_lahir = (TextView) view.findViewById(R.id.viewTanggalLahirHewan);
        created_at = (TextView) view.findViewById(R.id.viewCreatedAtHewan);
        created_by = view.findViewById(R.id.viewCreatedByHewan);
    }

    public void setField(){
        data = this.getArguments();

        id_hewan.setText(data.getString("id_hewan"));
        nama.setText(data.getString("nama"));
        nama_jenis_hewan.setText(data.getString("nama_jenis_hewan"));
        nama_pemilik.setText(data.getString("nama_pelanggan"));
        tanggal_lahir.setText(data.getString("tanggal_lahir"));
        created_at.setText(data.getString("created_at"));
        created_by.setText(data.getString("created_by"));
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
