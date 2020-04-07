package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.layanan.TampilDetailLayananActivity;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchUkuranHewan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HargaLayananAdapter extends RecyclerView.Adapter<HargaLayananAdapter.MyViewHolder> {
    private Context context;
    private List<HargaLayananDAO> result;

    public HargaLayananAdapter(Context context, List<HargaLayananDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_harga_layanan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final HargaLayananDAO harga_layanan = result.get(position);
        System.out.println(result.get(position).getHarga() + " " + position);

        setNamaUkuran(holder, harga_layanan.getId_ukuran_hewan());
        holder.harga.setText(Integer.toString(harga_layanan.getHarga()));
//        holder.parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startIntent(layanan, TampilDetailLayananActivity.class);
//            }
//        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(harga_layanan, position);
                return false;
            }
        });
    }

//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fl_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nama_ukuran_hewan, harga;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_ukuran_hewan = itemView.findViewById(R.id.tvNamaUkuranLayanan);
            harga = itemView.findViewById(R.id.tvHargaLayanan);
            parent = itemView.findViewById(R.id.ParentHargaLayanan);
        }

        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final HargaLayananDAO hasil, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle("What's next?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // update report
                        //startIntent(hasil);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete report
                        deleteHargaLayanan(hasil.getId_harga_layanan(), "admin", position);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

//    private void startIntent(HargaLayananDAO hasil, Class nextView) {
//        Intent view = new Intent(context, nextView);
//        view.putExtra("id_layanan", Integer.toString(hasil.getId_layanan()));
//        view.putExtra("nama_layanan", hasil.getNama());
//        view.putExtra("created_at", hasil.getCreated_at());
//        view.putExtra("created_by", hasil.getCreated_by());
//        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(view);
//    }

    private void deleteHargaLayanan(int id, String delete_by, final int position) {
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> hargalayananDAOCall = apiService.hapusLayanan(Integer.toString(id), delete_by);

        hargalayananDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus harga layanan", Toast.LENGTH_SHORT).show();
                delete(position);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {

                Toast.makeText(context, "Gagal menghapus harga layanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setNamaUkuran(final MyViewHolder holder, final int id_ukuran_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<SearchUkuranHewan> ukuranDAOCall = apiService.searchUkuran(Integer.toString(id_ukuran_hewan));

        ukuranDAOCall.enqueue(new Callback<SearchUkuranHewan>() {
            @Override
            public void onResponse(Call<SearchUkuranHewan> call, Response<SearchUkuranHewan> response) {
                UkuranHewanDAO ukuran = response.body().getUkuranhewan();
                if(ukuran.getNama()!=null){
                    holder.nama_ukuran_hewan.setText(ukuran.getNama());
                }else{
                    holder.nama_ukuran_hewan.setText(Integer.toString(id_ukuran_hewan));
                }

            }

            @Override
            public void onFailure(Call<SearchUkuranHewan> call, Throwable t) {
                holder.nama_ukuran_hewan.setText(Integer.toString(id_ukuran_hewan));
            }
        });
    }

    public void delete(int position) { //removes the row
        result.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
