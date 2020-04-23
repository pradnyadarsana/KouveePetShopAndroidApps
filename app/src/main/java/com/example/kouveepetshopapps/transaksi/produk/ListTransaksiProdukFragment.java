package com.example.kouveepetshopapps.transaksi.produk;

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
import com.example.kouveepetshopapps.adapter.TransaksiProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPegawai;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetTransaksiProduk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTransaksiProdukFragment extends Fragment {
    private List<TransaksiProdukDAO> ListTransaksiProduk;
    private RecyclerView recyclerTransaksiProduk;
    public TransaksiProdukAdapter adapterTransaksiProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton addTransaksiProdukBtn;

//    public List<HewanDAO> ListHewan;
//    public List<JenisHewanDAO> ListJenisHewan;
//    public List<PelangganDAO> ListPelanggan;
//    public List<PegawaiDAO> ListPegawai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_transaksi_produk, container, false);

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

//        ListHewan = new ArrayList<>();
//        ListJenisHewan = new ArrayList<>();
//        ListPelanggan = new ArrayList<>();
//        ListPegawai = new ArrayList<>();

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
                Toast.makeText(getContext(), "Gagal menampilkan transaksi produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getHewan(){
//        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
//        Call<GetHewan> hewanDAOCall = apiService.getAllHewan();
//
//        hewanDAOCall.enqueue(new Callback<GetHewan>() {
//            @Override
//            public void onResponse(Call<GetHewan> call, Response<GetHewan> response) {
//                ListHewan.addAll(response.body().getListDataHewan());
//                if(!ListHewan.isEmpty()){
//                    System.out.println(ListHewan.get(0).getNama());
////                    adapterTransaksiProduk.notifyDataSetChanged();
//                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetHewan> call, Throwable t) {
//                //Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getJenisHewan(){
//        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
//        Call<GetJenisHewan> jenishewanDAOCall = apiService.getAllJenisHewan();
//
//        jenishewanDAOCall.enqueue(new Callback<GetJenisHewan>() {
//            @Override
//            public void onResponse(Call<GetJenisHewan> call, Response<GetJenisHewan> response) {
//                ListJenisHewan.addAll(response.body().getListDataJenisHewan());
//                if(!ListJenisHewan.isEmpty()){
//                    System.out.println(ListJenisHewan.get(0).getNama());
//                    //adapterTransaksiProduk.notifyDataSetChanged();
//                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetJenisHewan> call, Throwable t) {
//                //Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getPelanggan(){
//        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
//        Call<GetPelanggan> pelangganDAOCall = apiService.getAllPelanggan();
//
//        pelangganDAOCall.enqueue(new Callback<GetPelanggan>() {
//            @Override
//            public void onResponse(Call<GetPelanggan> call, Response<GetPelanggan> response) {
//                ListPelanggan.addAll(response.body().getListDataPelanggan());
//                if(!ListPelanggan.isEmpty()){
//                    System.out.println(ListPelanggan.get(0).getNama());
//                    //adapterTransaksiProduk.notifyDataSetChanged();
//                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetPelanggan> call, Throwable t) {
//                //Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getPegawai(){
//        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
//        Call<GetPegawai> pegawaiDAOCall = apiService.getAllPegawai();
//
//        pegawaiDAOCall.enqueue(new Callback<GetPegawai>() {
//            @Override
//            public void onResponse(Call<GetPegawai> call, Response<GetPegawai> response) {
//                ListPegawai.addAll(response.body().getListDataPegawai());
//                if(!ListPegawai.isEmpty()){
//                    System.out.println(ListPegawai.get(0).getNama());
//                    //adapterTransaksiProduk.notifyDataSetChanged();
//                    //Toast.makeText(getActivity(), "Sukses ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetPegawai> call, Throwable t) {
//                //Toast.makeText(getContext(), "Gagal menampilkan hewan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
