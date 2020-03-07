package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.pelanggan.TampilDetailPelangganFragment;
import com.example.kouveepetshopapps.response.PostUpDelPelanggan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.MyViewHolder> {
    private Context context;
    private List<PelangganDAO> result;

    public PelangganAdapter(Context context, List<PelangganDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_pelanggan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PelangganDAO pelanggan = result.get(position);
        holder.nama.setText(pelanggan.getNama());
        holder.id_pelanggan.setText(Integer.toString(pelanggan.getId_pelanggan()));
        holder.telp.setText(pelanggan.getTelp());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                Fragment fragment = new TampilDetailPelangganFragment();

                data.putString("id_pelanggan", Integer.toString(pelanggan.getId_pelanggan()));
                data.putString("nama", pelanggan.getNama());
                data.putString("alamat", pelanggan.getAlamat());
                data.putString("tanggal_lahir", pelanggan.getTanggal_lahir());
                data.putString("telp", pelanggan.getTelp());
                data.putString("created_at", pelanggan.getCreated_at());

                fragment.setArguments(data);
                loadFragment(fragment);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(pelanggan);
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
        return result.size();
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

    private void showDialog(final PelangganDAO hasil){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle("What's next?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Edit",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // update report
                        startIntent(hasil);
                    }
                })
                .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete report
                        deleteReport(hasil.getId_pelanggan(),"pradnyadarsana");
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

    private void startIntent(PelangganDAO hasil){
//        Intent edit = new Intent(context, EditReport.class);
//        edit.putExtra("id", hasil.getId());
//        edit.putExtra("username", hasil.getUsername());
//        edit.putExtra("waktu", hasil.getDatetime());
//        edit.putExtra("alamat", hasil.getAddress());
//        edit.putExtra("deskripsi", hasil.getDescription());
//        edit.putExtra("kategori", hasil.getKategori());
//        context.startActivity(edit);
    }

    private void deleteReport(int id, String delete_by){
            ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
            Call<PostUpDelPelanggan> pelangganDAOCall = apiService.hapusPelanggan(Integer.toString(id),delete_by);

            pelangganDAOCall.enqueue(new Callback<PostUpDelPelanggan>() {
                @Override
                public void onResponse(Call<PostUpDelPelanggan> call, Response<PostUpDelPelanggan> response) {
                    //reverse close
                    System.out.println(response.body().getMessage());
                    Toast.makeText(context, "Success Deleting report", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PostUpDelPelanggan> call, Throwable t) {

                    Toast.makeText(context, "Fail Deleting report", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
