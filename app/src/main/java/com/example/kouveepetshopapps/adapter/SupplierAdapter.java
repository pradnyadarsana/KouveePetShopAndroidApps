package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.supplier.ListSupplierActivity;
import com.example.kouveepetshopapps.supplier.TampilDetailSupplierActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<SupplierDAO> result;
    private List<SupplierDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO admin;

    public SupplierAdapter(Context context, List<SupplierDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_supplier, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SupplierDAO supplier = resultFiltered.get(position);
        System.out.println(result.get(position).getNama()+" "+position);
        holder.nama.setText(supplier.getNama());
        holder.id_supplier.setText(Integer.toString(supplier.getId_supplier()));
        holder.telp.setText(supplier.getTelp());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(supplier, TampilDetailSupplierActivity.class);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(supplier, position);
                return false;
            }
        });
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
                    List<SupplierDAO> filteredList = new ArrayList<>();
                    for (SupplierDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || Integer.toString(row.getId_supplier()).contains(charSequence)) {
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
                resultFiltered = (ArrayList<SupplierDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, id_supplier, telp;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaSupplier);
            id_supplier = itemView.findViewById(R.id.tvIdSupplier);
            telp = itemView.findViewById(R.id.tvTelpSupplier);
            parent = itemView.findViewById(R.id.ParentSupplier);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final SupplierDAO hasil, final int position){
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
                        deleteSupplier(hasil.getId_supplier(), admin.getUsername(), position);
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

    private void startIntent(SupplierDAO hasil, Class nextView){
        Intent view = new Intent(context, nextView);
        Gson gson = new Gson();
        String json = gson.toJson(hasil);
        view.putExtra("supplier", json);
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }

    private void deleteSupplier(int id, String delete_by, final int position){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> supplierDAOCall = apiService.hapusSupplier(Integer.toString(id),delete_by);

        supplierDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus supplier", Toast.LENGTH_SHORT).show();
                delete(position);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {

                Toast.makeText(context, "Gagal menghapus supplier", Toast.LENGTH_SHORT).show();
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
