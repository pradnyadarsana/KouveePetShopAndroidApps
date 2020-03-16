package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.produk.TampilDetailProdukActivity;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.supplier.TampilDetailSupplierActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.MyViewHolder> {
    private Context context;
    private List<ProdukDAO> result;

    public ProdukAdapter(Context context, List<ProdukDAO> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ProdukDAO produk = result.get(position);
        System.out.println(result.get(position).getNama()+" "+position);
        holder.nama.setText(produk.getNama());
        //holder.id_supplier.setText(Integer.toString(produk.getId_produk()));
        holder.satuan.setText(produk.getSatuan());
        holder.jumlah_stok.setText(Integer.toString(produk.getJumlah_stok()));
        holder.harga.setText(Integer.toString(produk.getHarga()));
        holder.min_stok.setText(Integer.toString(produk.getMin_stok()));

        String photo_url = "http://kouveepetshopapi.smithdev.tech/upload/produk/"+produk.getGambar();
        System.out.println(photo_url);
        Glide.with(context).load(photo_url).into(holder.gambar);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(produk, TampilDetailProdukActivity.class);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(produk, position);
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
        private TextView id_produk, nama, satuan, jumlah_stok, min_stok, harga;
        private ImageView gambar;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //id_produk = itemView.findViewById(R.)
            nama = itemView.findViewById(R.id.tvNamaProduk);
            satuan = itemView.findViewById(R.id.tvSatuanProduk);
            jumlah_stok = itemView.findViewById(R.id.tvJumlahStokProduk);
            harga = itemView.findViewById(R.id.tvHargaProduk);
            min_stok = itemView.findViewById(R.id.tvMinStokProduk);
            gambar = itemView.findViewById(R.id.ivGambarProduk);
            parent = itemView.findViewById(R.id.ParentProduk);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(final ProdukDAO hasil, final int position){
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
                        deleteSupplier(hasil.getId_produk(),"admin", position);
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

    private void startIntent(ProdukDAO hasil, Class nextView){
        Intent view = new Intent(context, nextView);
        view.putExtra("id_produk", Integer.toString(hasil.getId_produk()));
        view.putExtra("nama", hasil.getNama());
        view.putExtra("satuan", hasil.getSatuan());
        view.putExtra("jumlah_stok", Integer.toString(hasil.getJumlah_stok()));
        view.putExtra("min_stok", Integer.toString(hasil.getMin_stok()));
        view.putExtra("harga", Integer.toString(hasil.getHarga()));
        view.putExtra("gambar", hasil.getGambar());
        view.putExtra("created_at", hasil.getCreated_at());
        view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(view);
    }

    private void deleteSupplier(int id, String delete_by, final int position){
        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> supplierDAOCall = apiService.hapusProduk(Integer.toString(id),delete_by);

        supplierDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                //reverse close
                System.out.println(response.body().getMessage());
                Toast.makeText(context, "Sukses menghapus produk", Toast.LENGTH_SHORT).show();
                delete(position);
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {

                Toast.makeText(context, "Gagal menghapus produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(int position) { //removes the row
        result.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}