package com.example.kouveepetshopapps.transaksi.layanan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.kouveepetshopapps.adapter.TransaksiLayananAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.response.GetTransaksiLayanan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTransaksiLayananSelesaiFragment extends Fragment {
    private List<TransaksiLayananDAO> ListTransaksiLayanan;
    private RecyclerView recyclerTransaksiLayanan;
    public TransaksiLayananAdapter adapterTransaksiLayanan;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addTransaksiLayananBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_transaksi_layanan_selesai, container, false);

        recyclerTransaksiLayanan = view.findViewById(R.id.recycler_view_transaksi_layanan_selesai);
        addTransaksiLayananBtn = view.findViewById(R.id.addTransaksiLayananSelesaiButton);
        addTransaksiLayananBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TambahTransaksiLayananActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListTransaksiLayanan = new ArrayList<>();
        adapterTransaksiLayanan = new TransaksiLayananAdapter(getContext(), ListTransaksiLayanan);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerTransaksiLayanan.setLayoutManager(mLayoutManager);
        recyclerTransaksiLayanan.setItemAnimator(new DefaultItemAnimator());
        recyclerTransaksiLayanan.setAdapter(adapterTransaksiLayanan);

        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetTransaksiLayanan> transaksiLayananDAOCall = apiService.getTransaksiLayananSelesaiDiproses();

        transaksiLayananDAOCall.enqueue(new Callback<GetTransaksiLayanan>() {
            @Override
            public void onResponse(Call<GetTransaksiLayanan> call, Response<GetTransaksiLayanan> response) {
                ListTransaksiLayanan.addAll(response.body().getListDataTransaksiLayanan());
                if(!ListTransaksiLayanan.isEmpty()){
                    //System.out.println(ListTransaksiLayanan.get(0).getId_transaksi_layanan());
                    adapterTransaksiLayanan.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Tidak ada transaksi layanan selesai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTransaksiLayanan> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal menampilkan transaksi layanan selesai", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
