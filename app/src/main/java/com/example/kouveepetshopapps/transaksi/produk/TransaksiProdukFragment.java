package com.example.kouveepetshopapps.transaksi.produk;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.HewanAdapter;
import com.example.kouveepetshopapps.adapter.TransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.hewan.ListHewanFragment;
import com.example.kouveepetshopapps.hewan.TambahHewanActivity;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetTransaksiProduk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiProdukFragment extends Fragment {
    private List<TransaksiProdukDAO> ListTransaksiProduk;
    private RecyclerView recyclerTransaksiProduk;
    public TransaksiProdukAdapter adapterTransaksiProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addTransaksiProdukBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi_produk, container, false);

        recyclerTransaksiProduk = view.findViewById(R.id.recycler_view_transaksi_produk);
        addTransaksiProdukBtn = view.findViewById(R.id.addTransaksiProdukButton);
        addTransaksiProdukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TambahTransaksiProdukActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListTransaksiProduk = new ArrayList<>();
        adapterTransaksiProduk = new TransaksiProdukAdapter(getContext(), ListTransaksiProduk);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerTransaksiProduk.setLayoutManager(mLayoutManager);
        recyclerTransaksiProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerTransaksiProduk.setAdapter(adapterTransaksiProduk);
        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetTransaksiProduk> transaksiProdukDAOCall = apiService.getTransaksiProdukMenungguPembayaran();

        transaksiProdukDAOCall.enqueue(new Callback<GetTransaksiProduk>() {
            @Override
            public void onResponse(Call<GetTransaksiProduk> call, Response<GetTransaksiProduk> response) {
                ListTransaksiProduk.addAll(response.body().getListDataTransaksiProduk());
                if(!ListTransaksiProduk.isEmpty()){
                    System.out.println(ListTransaksiProduk.get(0).getId_transaksi_produk());
                    adapterTransaksiProduk.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Tidak ada transaksi produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTransaksiProduk> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
