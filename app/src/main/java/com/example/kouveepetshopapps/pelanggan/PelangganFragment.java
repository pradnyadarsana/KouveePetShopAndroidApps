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

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.PelangganAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.response.GetPelanggan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelangganFragment extends Fragment {
    private List<PelangganDAO> ListPelanggan;
    private RecyclerView recyclerPelanggan;
    private PelangganAdapter adapterPelanggan;
    private RecyclerView.LayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pelanggan, container, false);

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
                ListPelanggan.addAll(response.body().getListDataPelanggan());
                System.out.println(ListPelanggan.get(0).getNama());
                adapterPelanggan.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPelanggan> call, Throwable t) {
                //Toast.makeText(getContext(), "Failed to load report", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
