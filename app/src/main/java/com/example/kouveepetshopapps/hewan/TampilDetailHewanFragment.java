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
    private TextView id_hewan, nama, tanggal_lahir, created_at;
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
        tanggal_lahir = (TextView) view.findViewById(R.id.viewTanggalLahirHewan);
        created_at = (TextView) view.findViewById(R.id.viewCreatedAtHewan);
    }

    public void setField(){
        data = this.getArguments();

        id_hewan.setText(data.getString("id_hewan"));
        nama.setText(data.getString("nama"));
        tanggal_lahir.setText(data.getString("tanggal_lahir"));
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
