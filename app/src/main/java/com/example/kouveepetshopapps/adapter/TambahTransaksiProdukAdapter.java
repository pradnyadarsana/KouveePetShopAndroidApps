package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.LoginActivity;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;

import java.util.List;

public class TambahTransaksiProdukAdapter extends RecyclerView.Adapter<TambahTransaksiProdukAdapter.MyViewHolder> {
    private Context context;
    private List<ProdukDAO> listProduk;
    private List<DetailTransaksiProdukDAO> result;

    int counter;

    public TambahTransaksiProdukAdapter(Context context, List<DetailTransaksiProdukDAO> result, List<ProdukDAO> produk){
        this.context = context;
        this.result = result;
        this.listProduk = produk;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_tambah_transaksi_produk, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DetailTransaksiProdukDAO detailtransaksiproduk = result.get(position);
        //System.out.println(result.get(position).getId_ukuran_hewan()+" "+position);
        holder.nama_produk.setText("");
        counter=1;
        holder.jumlah_produk.setText(Integer.toString(counter));
        holder.harga_produk.setText("0");
        holder.total_harga.setText("0");

        holder.kurang_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                System.out.println(counter);
                if(counter<1){
                    showDialog(holder.nama_produk.getText().toString(), position);
                }else {
                    holder.jumlah_produk.setText(Integer.toString(counter));
                }

            }
        });
        holder.tambah_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                System.out.println(counter);
                holder.jumlah_produk.setText(Integer.toString(counter));
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView harga_produk, jumlah_produk, total_harga;
        private AutoCompleteTextView nama_produk;
        private Button kurang_produk, tambah_produk;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            nama_produk = itemView.findViewById(R.id.etNamaProdukTransaksi);
            harga_produk = itemView.findViewById(R.id.tvHargaProdukTransaksi);
            jumlah_produk = itemView.findViewById(R.id.tvJumlahProdukTransaksi);
            total_harga = itemView.findViewById(R.id.tvTotalHargaProdukTransaksi);
            kurang_produk = itemView.findViewById(R.id.btnKurangJumlahProdukTransaksi);
            tambah_produk = itemView.findViewById(R.id.btnTambahJumlahProdukTransaksi);
            parent = itemView.findViewById(R.id.ParentTambahItemTransaksiProduk);
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String nama_produk, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus item?");
        alertDialogBuilder.setMessage(nama_produk);

        // set pesan dari dialog
        alertDialogBuilder
            .setIcon(R.drawable.ic_delete_black_24dp)
            .setCancelable(false)
            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    delete(position);
                }
            }).setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void delete(int position) { //removes the row
        result.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
