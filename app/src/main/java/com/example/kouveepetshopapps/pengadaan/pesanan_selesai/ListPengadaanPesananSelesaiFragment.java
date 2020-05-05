package com.example.kouveepetshopapps.pengadaan.pesanan_selesai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.kouveepetshopapps.adapter.PengadaanProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.response.GetPengadaanProduk;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPengadaanPesananSelesaiFragment extends Fragment {
    private List<PengadaanProdukDAO> ListPengadaanProduk;
    private RecyclerView recyclerPengadaanProduk;
    public PengadaanProdukAdapter adapterPengadaanProduk;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_pengadaan_pesanan_selesai, container, false);
        recyclerPengadaanProduk = view.findViewById(R.id.recycler_view_pengadaan_pesanan_selesai);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListPengadaanProduk = new ArrayList<>();
        adapterPengadaanProduk = new PengadaanProdukAdapter(getContext(), ListPengadaanProduk);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerPengadaanProduk.setLayoutManager(mLayoutManager);
        recyclerPengadaanProduk.setItemAnimator(new DefaultItemAnimator());
        recyclerPengadaanProduk.setAdapter(adapterPengadaanProduk);

        setRecycleView();
    }

    public void setRecycleView(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetPengadaanProduk> pengadaanProdukDAOCall = apiService.getPengadaanProdukPesananSelesai();

        pengadaanProdukDAOCall.enqueue(new Callback<GetPengadaanProduk>() {
            @Override
            public void onResponse(Call<GetPengadaanProduk> call, Response<GetPengadaanProduk> response) {
                ListPengadaanProduk.addAll(response.body().getListDataPengadaanProduk());
                if(!ListPengadaanProduk.isEmpty()){
                    //System.out.println(ListTransaksiProduk.get(0).getId_transaksi_produk());
                    adapterPengadaanProduk.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Tidak ada pengadaan produk", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetPengadaanProduk> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal menampilkan pengadaan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
