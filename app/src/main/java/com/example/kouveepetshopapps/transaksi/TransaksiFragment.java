package com.example.kouveepetshopapps.transaksi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.example.kouveepetshopapps.hewan.ListHewanFragment;

public class TransaksiFragment extends Fragment {
    public SearchView searchView;
    public Toolbar toolbar;
    public int ID = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        loadFragment(new TransaksiProdukFragment());

        toolbar = view.findViewById(R.id.searchTransaksiToolbar);
        toolbar.setBackgroundResource(R.color.colorAccentOrange);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar fancy stuff
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transaksi Produk");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.transaction_option_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if(ID==0){
            searchView.setQueryHint("ID Transaksi Produk");
        }else if(ID==1){
            searchView.setQueryHint("ID Transaksi Layanan Dalam Proses");
        }else if(ID==2){
            searchView.setQueryHint("ID Transaksi Layanan Selesai Diproses");
        }


        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //adapterPelanggan.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //adapterPelanggan.getFilter().filter(query);
                return false;
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.transaksi_produk){
            toolbar.setTitle("Transaksi Produk");
            ID = 0;
            loadFragment(new TransaksiProdukFragment());
            return true;
        } else if (item.getItemId() == R.id.transaksi_layanan) {
            toolbar.setTitle("Transaksi Layanan");
            loadFragment(new TransaksiLayananFragment());
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
