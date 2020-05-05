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
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.pengadaan.EditPengadaanProdukActivity;
import com.example.kouveepetshopapps.pengadaan.TampilDetailPengadaanActivity;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchSupplier;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengadaanProdukAdapter extends RecyclerView.Adapter<PengadaanProdukAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<PengadaanProdukDAO> result;
    private List<PengadaanProdukDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    public PengadaanProdukAdapter(Context context, List<PengadaanProdukDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_pengadaan_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        //System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final PengadaanProdukDAO pengadaan = resultFiltered.get(position);
        holder.id_pengadaan_produk.setText(pengadaan.getId_pengadaan_produk());
        holder.total.setText(Integer.toString(pengadaan.getTotal()));
        System.out.println("position: "+position+" id supplier: "+pengadaan.getId_supplier());

        if(pengadaan.getId_supplier()==0) {
            holder.nama_supplier.setText("-");
            System.out.println("Position: "+position+" Search Result Supplier Pengadaan: "+pengadaan.getId_supplier());
        }else {
            setNamaSupplier(holder, pengadaan.getId_supplier());
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(pengadaan);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(pengadaan.getStatus().equalsIgnoreCase("Menunggu Konfirmasi")){
                    showDialog(pengadaan, position);
                }
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
                    List<PengadaanProdukDAO> filteredList = new ArrayList<>();
                    for (PengadaanProdukDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId_pengadaan_produk().toLowerCase().contains(charString.toLowerCase())) {
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
                resultFiltered = (ArrayList<PengadaanProdukDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_pengadaan_produk, total, nama_supplier;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_pengadaan_produk = itemView.findViewById(R.id.tvIdPengadaanProduk);
            total = itemView.findViewById(R.id.tvTotalPengadaanProduk);
            nama_supplier = itemView.findViewById(R.id.tvNamaSupplierPengadaanProduk);
            parent = itemView.findViewById(R.id.ParentPengadaanProduk);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void startIntent(PengadaanProdukDAO hasil){
        Gson gson = new Gson();
        String jsonResult = gson.toJson(hasil);

        System.out.println("KIRIM KE DETAIL PENGADAAN");
        System.out.println(jsonResult);

        Intent detail = new Intent(context, TampilDetailPengadaanActivity.class);
        detail.putExtra("pengadaan_produk",jsonResult);
        context.startActivity(detail);
    }

    private void startIntentEdit(PengadaanProdukDAO hasil){
        Gson gson = new Gson();
        String jsonResult = gson.toJson(hasil);

        Intent edit = new Intent(context, EditPengadaanProdukActivity.class);
        edit.putExtra("pengadaan_produk",jsonResult);

        context.startActivity(edit);
    }

    private void showDialog(final PengadaanProdukDAO hasil, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(hasil.getId_pengadaan_produk());

        // set pesan dan pilihan dari dialog
        String[] option = {"Ubah","Hapus","Batal"};
        alertDialogBuilder.setItems(option, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    case 0:
                        // update report
                        startIntentEdit(hasil);
                        break;
                    case 1:
                        //delete report
                        deletePengadaanProduk(hasil.getId_pengadaan_produk(), position);
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

    public void setNamaSupplier(final MyViewHolder holder, final int id_supplier){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchSupplier> supplierDAOCall = apiService.searchSupplier(Integer.toString(id_supplier));
        supplierDAOCall.enqueue(new Callback<SearchSupplier>() {
            @Override
            public void onResponse(Call<SearchSupplier> call, Response<SearchSupplier> response) {
                SupplierDAO supplier = response.body().getSupplier();
                System.out.println("Position: "+holder.getAdapterPosition()+" Search Result Hewan Pelanggan: "+supplier.getId_supplier());
                holder.nama_supplier.setText(supplier.getNama());
            }

            @Override
            public void onFailure(Call<SearchSupplier> call, Throwable t) {
                holder.nama_supplier.setText(Integer.toString(id_supplier));
            }
        });
    }

    private void deletePengadaanProduk(String id, final int position){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> pengadaanProdukDAOCall = apiService.hapusPengadaanProduk(id);

        pengadaanProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus data transaksi", Toast.LENGTH_SHORT).show();
                delete(position);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(context, "Gagal menghapus data transaksi", Toast.LENGTH_SHORT).show();
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
