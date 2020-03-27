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
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.api.ApiInterfaceCS;
import com.example.kouveepetshopapps.hewan.TampilDetailHewanFragment;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HewanAdapter extends RecyclerView.Adapter<HewanAdapter.MyViewHolder> {
    private Context context;
    private List<HewanDAO> result;

    public HewanAdapter(Context context, List<HewanDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_hewan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HewanDAO hewan = result.get(position);
        holder.nama.setText(hewan.getNama());
        setNamaJenisHewan(holder, hewan.getId_jenis_hewan());
        setNamaPelanggan(holder, hewan.getId_pelanggan());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                Fragment fragment = new TampilDetailHewanFragment();

                data.putString("id_hewan", Integer.toString(hewan.getId_hewan()));
                data.putString("nama", hewan.getNama());
                data.putString("id_pelanggan", Integer.toString(hewan.getId_pelanggan()));
                data.putString("nama_pelanggan", holder.nama_pemilik.getText().toString());
                data.putString("id_jenis_hewan", Integer.toString(hewan.getId_jenis_hewan()));
                data.putString("nama_jenis_hewan", holder.nama_jenis_hewan.getText().toString());
                data.putString("tanggal_lahir", hewan.getTanggal_lahir());
                data.putString("created_at", hewan.getCreated_at());
                data.putString("created_by", hewan.getCreated_by());

                fragment.setArguments(data);
                loadFragment(fragment);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(hewan, position);
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
        private TextView nama, nama_jenis_hewan, nama_pemilik;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.tvNamaHewan);
            nama_jenis_hewan = itemView.findViewById(R.id.tvJenisHewan);
            nama_pemilik = itemView.findViewById(R.id.tvNamaPemilikHewan);
            parent = itemView.findViewById(R.id.ParentHewan);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final HewanDAO hasil, final int position){
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
                        deleteHewan(hasil.getId_hewan(),"pradnyadarsana", position);
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

    private void startIntent(HewanDAO hasil){
//        Intent edit = new Intent(context, EditReport.class);
//        edit.putExtra("id", hasil.getId());
//        edit.putExtra("username", hasil.getUsername());
//        edit.putExtra("waktu", hasil.getDatetime());
//        edit.putExtra("alamat", hasil.getAddress());
//        edit.putExtra("deskripsi", hasil.getDescription());
//        edit.putExtra("kategori", hasil.getKategori());
//        context.startActivity(edit);
    }

    public void setNamaJenisHewan(final HewanAdapter.MyViewHolder holder, final int id_jenis_hewan){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<GetJenisHewan> jenishewanDAOCall = apiService.searchJenisHewan(Integer.toString(id_jenis_hewan));

        jenishewanDAOCall.enqueue(new Callback<GetJenisHewan>() {
            @Override
            public void onResponse(Call<GetJenisHewan> call, Response<GetJenisHewan> response) {
                List<JenisHewanDAO> ListJenisHewan = response.body().getListDataJenisHewan();
                JenisHewanDAO jenishewan = new JenisHewanDAO();
                for (JenisHewanDAO tempUkuran: ListJenisHewan) {
                    if(tempUkuran.getId_jenis_hewan()==id_jenis_hewan){
                        jenishewan = tempUkuran;
                    }
                }
                holder.nama_jenis_hewan.setText(jenishewan.getNama());
            }

            @Override
            public void onFailure(Call<GetJenisHewan> call, Throwable t) {
                holder.nama_jenis_hewan.setText(id_jenis_hewan);
            }
        });
    }

    public void setNamaPelanggan(final HewanAdapter.MyViewHolder holder, final int id_pelanggan){
        ApiInterfaceCS apiService = ApiClient.getClient().create(ApiInterfaceCS.class);
        Call<GetPelanggan> pelangganDAOCall = apiService.searchPelanggan(Integer.toString(id_pelanggan));

        pelangganDAOCall.enqueue(new Callback<GetPelanggan>() {
            @Override
            public void onResponse(Call<GetPelanggan> call, Response<GetPelanggan> response) {
                List<PelangganDAO> ListPelanggan = response.body().getListDataPelanggan();
                PelangganDAO pelanggan = new PelangganDAO();
                for (PelangganDAO tempPelanggan: ListPelanggan) {
                    if(tempPelanggan.getId_pelanggan()==id_pelanggan){
                        pelanggan = tempPelanggan;
                    }
                }
                holder.nama_pemilik.setText(pelanggan.getNama());
            }

            @Override
            public void onFailure(Call<GetPelanggan> call, Throwable t) {
                holder.nama_pemilik.setText(id_pelanggan);
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
        result.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
