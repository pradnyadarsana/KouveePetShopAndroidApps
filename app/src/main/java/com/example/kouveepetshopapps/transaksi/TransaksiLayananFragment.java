package com.example.kouveepetshopapps.transaksi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kouveepetshopapps.R;
import com.google.android.material.tabs.TabLayout;

public class TransaksiLayananFragment extends Fragment {

    private TabLayout tabLayout;
//    View view;
//    Adapter adapter;
    TransaksiFragment transaksiFragment;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi_layanan, container, false);

        transaksiFragment = new TransaksiFragment();
        //toolbar = transaksiFragment.toolbar;
        //transaksiFragment.toolbar.setTitle("Transaksi Layanan");

        tabLayout = view.findViewById(R.id.tabLayananLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Diproses"));
        tabLayout.addTab(tabLayout.newTab().setText("Selesai"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("onTabSelected");
                System.out.println(tab.getPosition());
                System.out.println(tab.getText());
                switch (tab.getPosition()){
                    case 0://diproses
                        transaksiFragment.ID=1;
                        break;
                    case 1://selesai
                        transaksiFragment.ID=2;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("onTabUnselected");
                System.out.println(tab.getPosition());
                System.out.println(tab.getText());
                switch (tab.getPosition()){
                    case 0://diproses
                        break;
                    case 1://selesai
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("onTabReselected");
                System.out.println(tab.getPosition());
                System.out.println(tab.getText());
                switch (tab.getPosition()){
                    case 0://diproses
                        break;
                    case 1://selesai
                        break;
                }
            }
        });

        return view;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaksi_layanan_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
