package com.example.kouveepetshopapps.hewan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.google.gson.Gson;

public class TampilDetailHewanFragment extends Fragment {
    private TextView id_hewan, nama, nama_jenis_hewan, nama_pemilik, tanggal_lahir, created_at,
            created_by, modified_at, modified_by, delete_at, delete_by;
    Bundle data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tampil_detail_hewan, container, false);
        init(view);

        Toolbar toolbar = view.findViewById(R.id.searchDetailHewanToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar fancy stuff
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Detail Hewan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListHewanFragment();
                loadFragment(fragment);
            }
        });

        data = this.getArguments();
        Gson gson = new Gson();
        String json = data.getString("hewan");
        System.out.println(json);
        HewanDAO hewan = gson.fromJson(json, HewanDAO.class);

        setField(hewan, data);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void init(View view){
        id_hewan = view.findViewById(R.id.viewIdHewan);
        nama = view.findViewById(R.id.viewNamaHewan);
        nama_jenis_hewan = view.findViewById(R.id.viewJenisHewanPelanggan);
        nama_pemilik = view.findViewById(R.id.viewNamaPemilik);
        tanggal_lahir = view.findViewById(R.id.viewTanggalLahirHewan);
        created_at = view.findViewById(R.id.viewCreatedAtHewan);
        created_by = view.findViewById(R.id.viewCreatedByHewan);
        modified_at = view.findViewById(R.id.viewModifiedAtHewan);
        modified_by = view.findViewById(R.id.viewModifiedByHewan);
        delete_at = view.findViewById(R.id.viewDeleteAtHewan);
        delete_by = view.findViewById(R.id.viewDeleteByHewan);
    }

    public void setField(HewanDAO hewan, Bundle data){
        id_hewan.setText(Integer.toString(hewan.getId_hewan()));
        nama.setText(hewan.getNama());
        nama_jenis_hewan.setText(data.getString("nama_jenis_hewan"));
        nama_pemilik.setText(data.getString("nama_pelanggan"));
        tanggal_lahir.setText(hewan.getTanggal_lahir());
        created_at.setText(hewan.getCreated_at());
        created_by.setText(hewan.getCreated_by());
        modified_at.setText(hewan.getModified_at());
        modified_by.setText(hewan.getModified_by());
        delete_at.setText(hewan.getDelete_at());
        delete_by.setText(hewan.getDelete_by());
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
