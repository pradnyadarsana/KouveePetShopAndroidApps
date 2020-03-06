package com.example.kouveepetshopapps.pelanggan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.PelangganAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.response.GetPelanggan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends Fragment {

    private ArrayList<PelangganDAO> ListPelanggan;
    private RecyclerView recyclerPelanggan;
    private PelangganAdapter adapterPelanggan;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customer, container, false);

        recyclerPelanggan = view.findViewById(R.id.recycler_view_pelanggan);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListPelanggan = new ArrayList<>();
        adapterPelanggan = new PelangganAdapter(getContext(), ListPelanggan);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerPelanggan.setLayoutManager(mLayoutManager);
        recyclerPelanggan.setItemAnimator(new DefaultItemAnimator());
        recyclerPelanggan.setAdapter(adapterPelanggan);
        setRecycleView();

    }

    public void setRecycleView(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetPelanggan> pelangganDAOCall = apiService.getAllPelangganAktif();

        pelangganDAOCall.enqueue(new Callback<GetPelanggan>() {
            @Override
            public void onResponse(Call<GetPelanggan> call, Response<GetPelanggan> response) {
                System.out.println(response.body().getListDataPelanggan().get(0).getNama());
                ListPelanggan = response.body().getListDataPelanggan();
//                for (PelangganDAO pel: ListPelanggan) {
//                    System.out.println(pel.getNama() + " ");
//                    System.out.println(pel.getAlamat() + " ");
//                    System.out.println(pel.getTanggal_lahir() + " ");
//                    System.out.println(pel.getTelp() + " ");
//                }
                adapterPelanggan.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Data pelanggan loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPelanggan> call, Throwable t) {
                System.out.println(t.getMessage());;
                Toast.makeText(getActivity(), "Failed to load report", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
