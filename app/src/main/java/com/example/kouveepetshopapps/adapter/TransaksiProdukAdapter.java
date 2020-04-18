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
import com.example.kouveepetshopapps.hewan.TampilDetailHewanFragment;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchPelanggan;
import com.example.kouveepetshopapps.transaksi.produk.TampilDetailTransaksiProdukActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiProdukAdapter extends RecyclerView.Adapter<TransaksiProdukAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<TransaksiProdukDAO> result;
    private List<TransaksiProdukDAO> resultFiltered;
    SharedPreferences loggedUser;
    PegawaiDAO pegawai;

    private List<HewanDAO> listHewan;
    private List<JenisHewanDAO> listJenisHewan;
    private List<PelangganDAO> listPelanggan;
    private List<PegawaiDAO> listPegawai;
    private List<ProdukDAO> listProduk;

    public TransaksiProdukAdapter(Context context, List<TransaksiProdukDAO> result, List<HewanDAO> listHewan,
                                  List<JenisHewanDAO> listJenisHewan, List<PelangganDAO> listPelanggan,
                                  List<PegawaiDAO> listPegawai, List<ProdukDAO> listProduk){
        this.context = context;
        this.result = result;
        this.resultFiltered = result;
        this.listHewan = listHewan;
        this.listJenisHewan = listJenisHewan;
        this.listPelanggan = listPelanggan;
        this.listPegawai = listPegawai;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_transaksi_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        loggedUser = context.getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        pegawai = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TransaksiProdukAdapter.MyViewHolder holder, final int position) {
        final TransaksiProdukDAO transaksi = resultFiltered.get(position);
        System.out.println("id_cust_service: "+transaksi.getId_customer_service());
        holder.id_transaksi_produk.setText(transaksi.getId_transaksi_produk());
        holder.total.setText(Integer.toString(transaksi.getTotal()));
        System.out.println("id hewan "+transaksi.getId_hewan());
        setNamaHewan(holder, transaksi.getId_hewan());
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

    private void startIntent(TransaksiProdukDAO hasil){
        PegawaiDAO cs = findPegawai(hasil.getId_customer_service());
        PegawaiDAO kasir = findPegawai(hasil.getId_kasir());
        HewanDAO hewan = findHewan(hasil.getId_hewan());
        PelangganDAO pelanggan = null;
        JenisHewanDAO jenis_hewan = null;
        if(hewan!=null)
        {
            pelanggan = findPelanggan(hewan.getId_pelanggan());
            jenis_hewan = findJenisHewan(hewan.getId_jenis_hewan());
        }
        Gson gson = new Gson();
        String jsonResult = gson.toJson(hasil);
        String jsonCust_Service = gson.toJson(cs);
        String jsonKasir = gson.toJson(kasir);
        String jsonHewan = gson.toJson(hewan);
        String jsonPelanggan = gson.toJson(pelanggan);
        String jsonJenisHewan = gson.toJson(jenis_hewan);
        String jsonProduk = gson.toJson(listProduk);

        Intent detail = new Intent(context, TampilDetailTransaksiProdukActivity.class);
        detail.putExtra("transaksi_produk",jsonResult);
        detail.putExtra("cust_service", jsonCust_Service);
        detail.putExtra("kasir",jsonKasir);
        detail.putExtra("hewan", jsonHewan);
        detail.putExtra("pelanggan", jsonPelanggan);
        detail.putExtra("jenis_hewan",jsonJenisHewan);
        detail.putExtra("produk",jsonProduk);
        context.startActivity(detail);
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
                    List<TransaksiProdukDAO> filteredList = new ArrayList<>();
                    for (TransaksiProdukDAO row : result) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getId_transaksi_produk().toLowerCase().contains(charString.toLowerCase())) {
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
                resultFiltered = (ArrayList<TransaksiProdukDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_transaksi_produk, total, nama_hewan, nama_pemilik;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            id_transaksi_produk = itemView.findViewById(R.id.tvIdTransaksiProduk);
            total = itemView.findViewById(R.id.tvTotalTransaksiProduk);
            nama_hewan = itemView.findViewById(R.id.tvNamaHewanTransaksiProduk);
            nama_pemilik = itemView.findViewById(R.id.tvNamaPemilikTransaksiProduk);
            parent = itemView.findViewById(R.id.ParentTransaksiProduk);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final TransaksiProdukDAO hasil, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(hasil.getId_transaksi_produk());

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
                        deleteHewan(hasil.getId_transaksi_produk(), pegawai.getUsername(), position);
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

    public void setNamaHewan(final TransaksiProdukAdapter.MyViewHolder holder, final int id_hewan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<SearchHewan> hewanDAOCall = apiService.searchHewan(Integer.toString(id_hewan));

        if(id_hewan==0){
            holder.nama_hewan.setText("Guest");
            holder.nama_pemilik.setText("Guest");
        }else{
            hewanDAOCall.enqueue(new Callback<SearchHewan>() {
                @Override
                public void onResponse(Call<SearchHewan> call, Response<SearchHewan> response) {
                    HewanDAO hewan = response.body().getHewan();
                    //if(hewan!=null){
                        holder.nama_hewan.setText(hewan.getNama());
                        setNamaPelanggan(holder, hewan.getId_pelanggan());
//                    }else{
//                        holder.nama_hewan.setText(Integer.toString(id_hewan));
//                    }

                }

                @Override
                public void onFailure(Call<SearchHewan> call, Throwable t) {
                    holder.nama_hewan.setText(Integer.toString(id_hewan));
                }
            });
        }


    }

    public void setNamaPelanggan(final TransaksiProdukAdapter.MyViewHolder holder, final int id_pelanggan){
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
        //System.out.println(pelangganDAOCall.isExecuted());
    }

    private void deleteHewan(String id, String delete_by, final int position){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<PostUpdateDelete> transaksiProdukDAOCall = apiService.hapusTransaksiProduk(id);

        transaksiProdukDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus data hewan", Toast.LENGTH_SHORT).show();
                delete(position);
            }
            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                System.out.println(t.getMessage());
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

    public HewanDAO findHewan(int id_hewan){
        for (HewanDAO hewan:listHewan
             ) {
            if(hewan.getId_hewan()==id_hewan){
                return hewan;
            }
        }
        return null;
    }

    public JenisHewanDAO findJenisHewan(int id_jenis_hewan){
        for (JenisHewanDAO jenishewan:listJenisHewan
        ) {
            if(jenishewan.getId_jenis_hewan()==id_jenis_hewan){
                return jenishewan;
            }
        }
        return null;
    }

    public PelangganDAO findPelanggan(int id_pelanggan){
        for (PelangganDAO pelanggan:listPelanggan
        ) {
            if(pelanggan.getId_pelanggan()==id_pelanggan){
                return pelanggan;
            }
        }
        return null;
    }

    public PegawaiDAO findPegawai(int id_pegawai){
        for (PegawaiDAO pegawai:listPegawai
        ) {
            if(pegawai.getId_pegawai()==id_pegawai){
                return pegawai;
            }
        }
        return null;
    }
}
