package com.example.kouveepetshopapps.hewan;

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
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.adapter.HewanAdapter;
import com.example.kouveepetshopapps.adapter.PelangganAdapter;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.navigation.AdminMainMenu;
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
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_hewan, container, false);

        Toolbar toolbar = view.findViewById(R.id.searchHewanToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar fancy stuff
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Hewan");

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("ID / Nama Hewan");

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterHewan.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapterHewan.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
