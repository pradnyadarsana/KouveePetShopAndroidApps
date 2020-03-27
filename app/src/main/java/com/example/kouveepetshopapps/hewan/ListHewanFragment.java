package com.example.kouveepetshopapps.hewan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.HewanAdapter;
import com.example.kouveepetshopapps.adapter.PelangganAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.pelanggan.TambahPelangganActivity;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListHewanFragment extends Fragment {
    private List<HewanDAO> ListHewan;
    private RecyclerView recyclerHewan;
    private HewanAdapter adapterHewan;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addHewanBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_hewan, container, false);

        recyclerHewan = view.findViewById(R.id.recycler_view_hewan);
        addHewanBtn = view.findViewById(R.id.addHewanButton);
        addHewanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TambahHewanActivity.class);
                startActivity((i));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListHewan = new ArrayList<>();
        adapterHewan = new HewanAdapter(getContext(), ListHewan);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerHewan.setLayoutManager(mLayoutManager);
        recyclerHewan.setItemAnimator(new DefaultItemAnimator());
        recyclerHewan.setAdapter(adapterHewan);
        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetHewan> hewanDAOCall = apiService.getAllHewanAktif();

        hewanDAOCall.enqueue(new Callback<GetHewan>() {
            @Override
            public void onResponse(Call<GetHewan> call, Response<GetHewan> response) {
                ListHewan.addAll(response.body().getListDataHewan());
                System.out.println(ListHewan.get(0).getNama());
                adapterHewan.notifyDataSetChanged();
                //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetHewan> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
