package com.example.kouveepetshopapps.notifikasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.NotifikasiAdapter;
import com.example.kouveepetshopapps.adapter.StokProdukAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.response.GetNotifikasi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNotifikasiFragment extends Fragment {
    private RecyclerView recyclerNotifikasi;
    private NotifikasiAdapter adapterNotifikasi;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;

    private List<NotifikasiDAO> ListNotifikasi;
    private List<ProdukDAO> ListProduk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_notifikasi, container, false);

        toolbar = view.findViewById(R.id.searchNotifikasiToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifikasi");

        recyclerNotifikasi = view.findViewById(R.id.recycler_view_notifikasi);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListNotifikasi = new ArrayList<>();

        ListProduk = new ArrayList<>();
        adapterNotifikasi = new NotifikasiAdapter(getContext(), ListNotifikasi);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerNotifikasi.setLayoutManager(mLayoutManager);
        recyclerNotifikasi.setItemAnimator(new DefaultItemAnimator());
        recyclerNotifikasi.setAdapter(adapterNotifikasi);

        getAllNotifications();
    }

    public void getAllNotifications(){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetNotifikasi> notifDAOCall = apiService.getAllNotifikasiDesc();

        notifDAOCall.enqueue(new Callback<GetNotifikasi>() {
            @Override
            public void onResponse(Call<GetNotifikasi> call, Response<GetNotifikasi> response) {
                if(!response.body().getListDataNotifikasi().isEmpty()){
                    ListNotifikasi.addAll(response.body().getListDataNotifikasi());
                    System.out.println(ListNotifikasi.get(0).getId_notifikasi());
                    adapterNotifikasi.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetNotifikasi> call, Throwable t) {
            }
        });
    }
}
