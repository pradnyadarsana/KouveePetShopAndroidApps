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
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchPelanggan;
import com.example.kouveepetshopapps.transaksi.layanan.EditTransaksiLayananActivity;
import com.example.kouveepetshopapps.transaksi.layanan.TampilDetailTransaksiLayananActivity;
import com.example.kouveepetshopapps.transaksi.produk.EditTransaksiProdukActivity;
import com.example.kouveepetshopapps.transaksi.produk.TampilDetailTransaksiProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiLayananAdapter extends RecyclerView.Adapter<TransaksiLayananAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<TransaksiLayananDAO> result;
    private List<TransaksiLayananDAO> resultFiltered;

    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    public TransaksiLayananAdapter(Context context, List<TransaksiLayananDAO> result){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_transaksi_layanan, parent, false);
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
        final TransaksiLayananDAO transaksi = resultFiltered.get(position);
        //System.out.println("id_cust_service: "+transaksi.getId_customer_service());
        holder.id_transaksi_layanan.setText(transaksi.getId_transaksi_layanan());
        holder.total.setText(Integer.toString(transaksi.getTotal()));
        System.out.println("position: "+position+" id hewan "+transaksi.getId_hewan());

        if(transaksi.getId_hewan()==0) {
            holder.nama_hewan.setText("Guest");
            holder.nama_pemilik.setText("Guest");
            System.out.println("Position: "+position+" Search Result Hewan Pelanggan: "+transaksi.getId_hewan());
        }else {
            setNamaHewan(holder, transaksi.getId_hewan());
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(transaksi);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(transaksi, position);
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
                    List<TransaksiLayananDAO> filteredList = new ArrayList<>();
                    for (TransaksiLayananDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId_transaksi_layanan().toLowerCase().contains(charString.toLowerCase())) {
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
                resultFiltered = (ArrayList<TransaksiLayananDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_transaksi_layanan, total, nama_hewan, nama_pemilik;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_transaksi_layanan = itemView.findViewById(R.id.tvIdTransaksiLayanan);
            total = itemView.findViewById(R.id.tvTotalTransaksiLayanan);
            nama_hewan = itemView.findViewById(R.id.tvNamaHewanTransaksiLayanan);
            nama_pemilik = itemView.findViewById(R.id.tvNamaPemilikTransaksiLayanan);
            parent = itemView.findViewById(R.id.ParentTransaksiLayanan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void startIntent(TransaksiLayananDAO hasil){
        Gson gson = new Gson();
        String jsonResult = gson.toJson(hasil);

        System.out.println("KIRIM KE DETAIL TRANSAKSI");
        System.out.println(jsonResult);

        Intent detail = new Intent(context, TampilDetailTransaksiLayananActivity.class);
        detail.putExtra("transaksi_layanan",jsonResult);
        context.startActivity(detail);
    }

    private void startIntentEdit(TransaksiLayananDAO hasil){
        Gson gson = new Gson();
        String jsonResult = gson.toJson(hasil);

        Intent edit = new Intent(context, EditTransaksiLayananActivity.class);
        edit.putExtra("transaksi_layanan",jsonResult);
        context.startActivity(edit);
    }

    private void showDialog(final TransaksiLayananDAO hasil, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(hasil.getId_transaksi_layanan());

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
                        deleteTransaksiProduk(hasil.getId_transaksi_layanan(), position);
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

    public void setNamaHewan(final MyViewHolder holder, final int id_hewan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchHewan> hewanDAOCall = apiService.searchHewan(Integer.toString(id_hewan));
        hewanDAOCall.enqueue(new Callback<SearchHewan>() {
            @Override
            public void onResponse(Call<SearchHewan> call, Response<SearchHewan> response) {
                HewanDAO hewan = response.body().getHewan();
                System.out.println("Position: "+holder.getAdapterPosition()+" Search Result Hewan Pelanggan: "+hewan.getId_hewan());
                holder.nama_hewan.setText(hewan.getNama());
                setNamaPelanggan(holder, hewan.getId_pelanggan());
            }

            @Override
            public void onFailure(Call<SearchHewan> call, Throwable t) {
                holder.nama_hewan.setText(Integer.toString(id_hewan));
            }
        });

    }

    public void setNamaPelanggan(final MyViewHolder holder, final int id_pelanggan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchPelanggan> pelangganDAOCall = apiService.searchPelanggan(Integer.toString(id_pelanggan));

        System.out.println("id pelanggan/pemilik = "+id_pelanggan);

        pelangganDAOCall.enqueue(new Callback<SearchPelanggan>() {
            @Override
            public void onResponse(Call<SearchPelanggan> call, Response<SearchPelanggan> response) {
                PelangganDAO pelanggan = response.body().getPelanggan();
                holder.nama_pemilik.setText(pelanggan.getNama());
            }

            @Override
            public void onFailure(Call<SearchPelanggan> call, Throwable t) {
                holder.nama_pemilik.setText(Integer.toString(id_pelanggan));
            }
        });
    }

    private void deleteTransaksiProduk(String id, final int position){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiLayananDAOCall = apiService.hapusTransaksiLayanan(id);

        transaksiLayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
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
