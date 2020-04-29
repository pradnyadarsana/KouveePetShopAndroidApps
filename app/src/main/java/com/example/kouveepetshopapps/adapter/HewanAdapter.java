package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.hewan.EditHewanActivity;
import com.example.kouveepetshopapps.hewan.TampilDetailHewanFragment;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchJenisHewan;
import com.example.kouveepetshopapps.response.SearchPelanggan;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HewanAdapter extends RecyclerView.Adapter<HewanAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<HewanDAO> result;
    private List<HewanDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    public HewanAdapter(Context context, List<HewanDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_hewan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HewanDAO hewan = resultFiltered.get(position);
        holder.nama.setText(hewan.getNama());
        holder.id_hewan.setText(Integer.toString(hewan.getId_hewan()));
        setNamaJenisHewan(holder, hewan.getId_jenis_hewan());
        setNamaPelanggan(holder, hewan.getId_pelanggan());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(hewan, holder);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(hewan, holder, position);
                return false;
            }
        });
    }

    private void startFragment(HewanDAO hasil, MyViewHolder holder){
        Bundle data = new Bundle();
        Fragment fragment = new TampilDetailHewanFragment();

        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        data.putString("hewan", json);
        data.putString("nama_jenis_hewan", holder.nama_jenis_hewan.getText().toString());
        data.putString("nama_pelanggan", holder.nama_pemilik.getText().toString());

        fragment.setArguments(data);
        loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return resultFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resultFiltered = result;
                } else {
                    List<HewanDAO> filteredList = new ArrayList<>();
                    for (HewanDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || Integer.toString(row.getId_hewan()).contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    resultFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = resultFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                resultFiltered = (ArrayList<HewanDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_hewan, nama, nama_jenis_hewan, nama_pemilik;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaHewan);
            id_hewan = itemView.findViewById(R.id.tvIdHewan);
            nama_jenis_hewan = itemView.findViewById(R.id.tvJenisHewan);
            nama_pemilik = itemView.findViewById(R.id.tvNamaPemilikHewan);
            parent = itemView.findViewById(R.id.ParentHewan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final HewanDAO hasil, final MyViewHolder holder, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(hasil.getNama());

        // set pesan dan pilihan dari dialog
        String[] option = {"Ubah","Hapus","Batal"};
        alertDialogBuilder.setItems(option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    case 0:
                        // update report
                        //startIntent(hasil);
                        startIntent(hasil, EditHewanActivity.class, holder);
                        break;
                    case 1:
                        //delete report
                        deleteHewan(hasil.getId_hewan(), pegawai.getUsername(), position);
                        break;
                    case 2:
                        dialog.cancel();
                        break;
                }
            }
        });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void startIntent(HewanDAO hasil, Class nextView, MyViewHolder holder){
        Intent view = new Intent(context, nextView);
        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        view.putExtra("hewan", json);
        view.putExtra("nama_jenis_hewan",holder.nama_jenis_hewan.getText().toString());
        view.putExtra("nama_pelanggan",holder.nama_pemilik.getText().toString());
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }

    public void setNamaJenisHewan(final HewanAdapter.MyViewHolder holder, final int id_jenis_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchJenisHewan> jenishewanDAOCall = apiService.searchJenisHewan(Integer.toString(id_jenis_hewan));

        jenishewanDAOCall.enqueue(new Callback<SearchJenisHewan>() {
            @Override
            public void onResponse(Call<SearchJenisHewan> call, Response<SearchJenisHewan> response) {
                JenisHewanDAO jenishewan = response.body().getJenishewan();
                if(jenishewan.getNama()!=null){
                    System.out.println(jenishewan.getNama());
                    holder.nama_jenis_hewan.setText(jenishewan.getNama());
                }else{
                    holder.nama_jenis_hewan.setText(Integer.toString(id_jenis_hewan));
                }

            }

            @Override
            public void onFailure(Call<SearchJenisHewan> call, Throwable t) {
                holder.nama_jenis_hewan.setText(Integer.toString(id_jenis_hewan));
            }
        });
    }

    public void setNamaPelanggan(final HewanAdapter.MyViewHolder holder, final int id_pelanggan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPelanggan> pelangganDAOCall = apiService.searchPelanggan(Integer.toString(id_pelanggan));

        pelangganDAOCall.enqueue(new Callback<SearchPelanggan>() {
            @Override
            public void onResponse(Call<SearchPelanggan> call, Response<SearchPelanggan> response) {
                PelangganDAO pelanggan = response.body().getPelanggan();
                if(pelanggan.getNama()!=null){
                    System.out.println(pelanggan.getNama());
                    holder.nama_pemilik.setText(pelanggan.getNama());
                }else{
                    holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
                }

            }

            @Override
            public void onFailure(Call<SearchPelanggan> call, Throwable t) {
                holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    private void deleteHewan(int id, String delete_by, final int position){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> hewanDAOCall = apiService.hapusHewan(Integer.toString(id),delete_by);

        hewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus data hewan", Toast.LENGTH_SHORT).show();
                delete(position);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {

                Toast.makeText(context, "Gagal menghapus data hewan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(int position) { //removes the row
        if(result.size()!=resultFiltered.size()){
            int index = result.indexOf(resultFiltered.get(position));
            result.remove(index);
            resultFiltered.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }else{
            result.remove(position);
            resultFiltered = result;
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }
}
