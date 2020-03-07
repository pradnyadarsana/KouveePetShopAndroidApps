package com.example.kouveepetshopapps.pelanggan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;

public class TampilDetailPelangganFragment extends Fragment {
    private TextView id_pelanggan, nama, telp, tanggal_lahir, alamat, created_at;
    Bundle data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tampil_detail_pelanggan, container, false);
        init(view);
        setField();
        return view;
    }

    public void init(View view){
        id_pelanggan = (TextView) view.findViewById(R.id.viewIdPelanggan);
        nama = (TextView) view.findViewById(R.id.viewNamaPelanggan);
        telp = (TextView) view.findViewById(R.id.viewTelpPelanggan);
        tanggal_lahir = (TextView) view.findViewById(R.id.viewTanggalLahirPelanggan);
        alamat = (TextView) view.findViewById(R.id.viewAlamatPelanggan);
        created_at = (TextView) view.findViewById(R.id.viewCreatedAt);
    }

    public void setField(){
        data = this.getArguments();

        id_pelanggan.setText(data.getString("id_pelanggan"));
        nama.setText(data.getString("nama"));
        telp.setText(data.getString("telp"));
        tanggal_lahir.setText(data.getString("tanggal_lahir"));
        alamat.setText(data.getString("alamat"));
        created_at.setText(data.getString("created_at"));
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
