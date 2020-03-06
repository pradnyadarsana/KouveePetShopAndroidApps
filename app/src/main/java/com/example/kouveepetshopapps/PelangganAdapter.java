package com.example.kouveepetshopapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.model.PelangganDAO;

import java.util.List;

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
        View v = LayoutInflater.from(context).inflate(R.layout.activity_pelanggan_adapter, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PelangganDAO pelanggan = result.get(position);
        holder.nama.setText(pelanggan.getNama());
        //holder.id.setText(pelanggan.getId());
        holder.telp.setText(pelanggan.getTelp());
//        holder.parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle data = new Bundle();
//                Fragment fragment = new ViewReport();
//
//                data.putString("username", report.getUsername());
//                data.putString("waktu", report.getDatetime());
//                data.putString("alamat", report.getAddress());
//                data.putString("gambar", report.getImg());
//                data.putString("deskripsi", report.getDescription());
//                data.putString("kategori", report.getKategori());
//
//                fragment.setArguments(data);
//                loadFragment(fragment);
//            }
//        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //showDialog(pelanggan);
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
        private TextView nama, telp, id;
        private LinearLayout parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaPelanggan);
            telp = itemView.findViewById(R.id.tvTelpPelanggan);
            id = itemView.findViewById(R.id.tvIdPelanggan);
            parent = itemView.findViewById(R.id.Parent);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

//    private void showDialog(final PelangganDAO hasil){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//
//        // set title dialog
//        alertDialogBuilder.setTitle("What's next?");
//
//        // set pesan dari dialog
//        alertDialogBuilder
//                .setIcon(R.mipmap.ic_launcher)
//                .setCancelable(false)
//                .setPositiveButton("Edit",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//                        // update report
//                        startIntent(hasil);
//                    }
//                })
//                .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //delete report
//                        deleteReport(hasil.getId());
//                    }
//                })
//                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        dialog.cancel();
//                    }
//                });
//
//        // membuat alert dialog dari builder
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // menampilkan alert dialog
//        alertDialog.show();
//    }

//    private void startIntent(PelangganDAO hasil){
//        Intent edit = new Intent(context, EditReport.class);
//        edit.putExtra("id", hasil.getId());
//        edit.putExtra("username", hasil.getUsername());
//        edit.putExtra("waktu", hasil.getDatetime());
//        edit.putExtra("alamat", hasil.getAddress());
//        edit.putExtra("deskripsi", hasil.getDescription());
//        edit.putExtra("kategori", hasil.getKategori());
//        context.startActivity(edit);
//    }

//    private void deleteReport(int id){
//        ApiUserInterface apiService = ApiClient.getClient().create(ApiUserInterface.class);
//        Call<Void> reportDAOCall = apiService.deleteReport(id);
//
//        reportDAOCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                //reverse close
//                Toast.makeText(context, "Success Deleting report", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//                Toast.makeText(context, "Fail Deleting report", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
