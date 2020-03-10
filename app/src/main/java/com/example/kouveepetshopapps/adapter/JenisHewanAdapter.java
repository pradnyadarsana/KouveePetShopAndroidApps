package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
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
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.jenishewan.TampilDetailJenisHewanActivity;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JenisHewanAdapter extends RecyclerView.Adapter<JenisHewanAdapter.MyViewHolder> {
    private Context context;
    private List<JenisHewanDAO> result;

    public JenisHewanAdapter(Context context, List<JenisHewanDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_jenishewan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final JenisHewanDAO jenishewan = result.get(position);
        System.out.println(result.get(position).getNama()+" "+position);
        holder.nama.setText(jenishewan.getNama());
        holder.id_jenis_hewan.setText(Integer.toString(jenishewan.getId_jenis_hewan()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(jenishewan, TampilDetailJenisHewanActivity.class);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(jenishewan, position);
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
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama, id_jenis_hewan;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaJenisHewan);
            id_jenis_hewan = itemView.findViewById(R.id.tvIdJenisHewan);
            parent = itemView.findViewById(R.id.ParentJenisHewan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final JenisHewanDAO hasil, final int position){
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
                        //startIntent(hasil);
                    }
                })
                .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete report
                        deleteJenisHewan(hasil.getId_jenis_hewan(),"admin", position);
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

    private void startIntent(JenisHewanDAO hasil, Class nextView){
        Intent view = new Intent(context, nextView);
        view.putExtra("id_supplier", Integer.toString(hasil.getId_jenis_hewan()));
        view.putExtra("nama", hasil.getNama());
        view.putExtra("created_at", hasil.getCreated_at());
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }

    private void deleteJenisHewan(int id, String delete_by, final int position){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> jenisHewanDAOCall = apiService.hapusSupplier(Integer.toString(id),delete_by);

        jenisHewanDAOCall.enqueue(new Callback<PostUpdateDelete>() {
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
        result.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
