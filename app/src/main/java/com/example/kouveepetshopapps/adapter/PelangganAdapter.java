package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.hewan.TampilDetailHewanFragment;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.pelanggan.TampilDetailPelangganFragment;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<PelangganDAO> result;
    private List<PelangganDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    public PelangganAdapter(Context context, List<PelangganDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_pelanggan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final PelangganDAO pelanggan = resultFiltered.get(position);
        holder.nama.setText(pelanggan.getNama());
        holder.id_pelanggan.setText(Integer.toString(pelanggan.getId_pelanggan()));
        holder.telp.setText(pelanggan.getTelp());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(pelanggan);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(pelanggan, position);
                return false;
            }
        });
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

    private void startFragment(PelangganDAO hasil){
        Bundle data = new Bundle();
        Fragment fragment = new TampilDetailPelangganFragment();

        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        data.putString("pelanggan", json);

        fragment.setArguments(data);
        loadFragment(fragment);
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
                    List<PelangganDAO> filteredList = new ArrayList<>();
                    for (PelangganDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || Integer.toString(row.getId_pelanggan()).contains(charSequence)) {
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
                resultFiltered = (ArrayList<PelangganDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, id_pelanggan, telp;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaPelanggan);
            id_pelanggan = itemView.findViewById(R.id.tvIdPelanggan);
            telp = itemView.findViewById(R.id.tvTelpPelanggan);
            parent = itemView.findViewById(R.id.ParentPelanggan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final PelangganDAO hasil, final int position){
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
                        break;
                    case 1:
                        //delete report
                        deletePelanggan(hasil.getId_pelanggan(), pegawai.getUsername(), position);
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



    private void deletePelanggan(int id, String delete_by, final int position){
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpdateDelete> pelangganDAOCall = apiService.hapusPelanggan(Integer.toString(id),delete_by);

            pelangganDAOCall.enqueue(new Callback<PostUpdateDelete>() {
                @Override
                public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                    //reverse close
                    System.out.println(response.body().getMessage());
                    Toast.makeText(context, "Sukses menghapus pelanggan", Toast.LENGTH_SHORT).show();
                    delete(position);
                }

                @Override
                public void onFailure(Call<PostUpdateDelete> call, Throwable t) {

                    Toast.makeText(context, "Gagal menghapus pelanggan", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void delete(int position) { //removes the row
        int index = result.indexOf(resultFiltered.get(position));
        result.remove(index);
        resultFiltered.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
