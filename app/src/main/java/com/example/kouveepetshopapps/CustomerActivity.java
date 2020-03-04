package com.example.kouveepetshopapps;

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

import com.example.kouveepetshopapps.model.PelangganDAO;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends Fragment {

    private List<PelangganDAO> ListPelanggan;
    private RecyclerView recyclerPelanggan;
    private PelangganAdapter adapterPelanggan;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customer, container, false);

        //recyclerPelanggan = view.findViewById(R.id.recycler_view_report);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ListPelanggan = new ArrayList<>();
//        adapterPelanggan = new PelangganAdapter(getContext(), ListPelanggan);
//        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        recyclerPelanggan.setLayoutManager(mLayoutManager);
//        recyclerPelanggan.setItemAnimator(new DefaultItemAnimator());
//        recyclerPelanggan.setAdapter(adapterPelanggan);
        //setRecycleView();
    }

    /*public void setRecycleView(){
        ApiUserInterface apiService = ApiClient.getClient().create(ApiUserInterface.class);
        Call<List<ReportDAO>> reportDAOCall = apiService.getAllReport();

        reportDAOCall.enqueue(new Callback<List<ReportDAO>>() {
            @Override
            public void onResponse(Call<List<ReportDAO>> call, Response<List<ReportDAO>> response) {
                ListReport.addAll(response.body());
                System.out.println(ListReport.get(0).getAddress());
                adapterReport.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ReportDAO>> call, Throwable t) {
                //Toast.makeText(getContext(), "Failed to load report", Toast.LENGTH_SHORT).show();
            }
        });
    }*/


}
