package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private List<ProdukDAO> produkPilihan;
    private int counter;
    //int position;
    DetailTransaksiProdukDAO detailtransaksiproduk;
    ProdukDAO produk;

    public TambahTransaksiProdukAdapter(Context context, List<DetailTransaksiProdukDAO> result, List<ProdukDAO> produk, List<ProdukDAO> produkPilihan){
        this.context = context;
        this.result = result;
        this.listProduk = produk;
        this.produkPilihan = produkPilihan;
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
        detailtransaksiproduk = result.get(position);
        produk = produkPilihan.get(position);
        System.out.println(detailtransaksiproduk);
        System.out.println(produk);

        holder.nama_produk.setText("");
        holder.jumlah_produk.setText(String.valueOf(detailtransaksiproduk.getJumlah()));
        this.counter=1;

        holder.kurang_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kurangJumlah();
                System.out.println(getCounter());
                if(getCounter()<1){
                    tambahJumlah();
                    showDialog(holder.nama_produk.getText().toString(), position);
                }else {

                    holder.jumlah_produk.setText(Integer.toString(counter));
                    detailtransaksiproduk.setJumlah(counter);
                    result.get(position).setJumlah(counter);

                    System.out.println("posisi adapter: "+position);
                    System.out.println("harga produk: "+produkPilihan.get(position).getHarga()+", jumlah produk: "+result.get(position).getJumlah());
                    int total = produkPilihan.get(position).getHarga()*result.get(position).getJumlah();
                    holder.total_harga.setText(String.valueOf(total));
                }

            }
        });

        holder.tambah_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahJumlah();
                System.out.println(counter);

                holder.jumlah_produk.setText(Integer.toString(counter));
                detailtransaksiproduk.setJumlah(counter);
                result.get(position).setJumlah(counter);

                System.out.println("posisi adapter: "+position);
                System.out.println("harga produk: "+produkPilihan.get(position).getHarga()+", jumlah produk: "+result.get(position).getJumlah());
                int total = produkPilihan.get(position).getHarga()*result.get(position).getJumlah();
                holder.total_harga.setText(String.valueOf(total));
            }
        });

        holder.nama_produk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                produk = getProduk(s.toString());
                if(produk!=null){
                    produkPilihan.set(position, produk);
                    detailtransaksiproduk.setId_produk(produk.getId_produk());
                    result.get(position).setId_produk(produk.getId_produk());

                    System.out.println("posisi adapter: "+position);
                    System.out.println("harga produk: "+produkPilihan.get(position).getHarga()+", jumlah produk: "+result.get(position).getJumlah());
                    int total = produkPilihan.get(position).getHarga()*result.get(position).getJumlah();
                    holder.harga_produk.setText(String.valueOf(produkPilihan.get(position).getHarga()));
                    holder.total_harga.setText(String.valueOf(total));
                }else{
                    produkPilihan.set(position, new ProdukDAO());
                    detailtransaksiproduk.setId_produk(0);
                    result.get(position).setId_produk(0);

                    System.out.println("harga produk: "+produkPilihan.get(position).getHarga()+", jumlah produk: "+result.get(position).getJumlah());
                    holder.harga_produk.setText(String.valueOf(produkPilihan.get(position).getHarga()));
                    int total = produkPilihan.get(position).getHarga()*result.get(position).getJumlah();
                    holder.total_harga.setText(String.valueOf(total));

                }


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
            //create Autocompletetextview for nama hewan
            nama_produk = (AutoCompleteTextView) itemView.findViewById(R.id.etNamaProdukTransaksi);
            nama_produk.setThreshold(1);//will start working from first character

            harga_produk = itemView.findViewById(R.id.tvHargaProdukTransaksi);
            jumlah_produk = itemView.findViewById(R.id.tvJumlahProdukTransaksi);
            total_harga = itemView.findViewById(R.id.tvTotalHargaProdukTransaksi);
            kurang_produk = itemView.findViewById(R.id.btnKurangJumlahProdukTransaksi);
            tambah_produk = itemView.findViewById(R.id.btnTambahJumlahProdukTransaksi);
            parent = itemView.findViewById(R.id.ParentTambahItemTransaksiProduk);

            //set adapter autocompletetextview
            String[] arrName = new String[listProduk.size()];
            int i = 0;
            for (ProdukDAO produk: listProduk
            ) {
                arrName[i] = produk.getNama();
                i++;
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (context, android.R.layout.select_dialog_item, arrName);

            nama_produk.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
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
                counter++;
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
        produkPilihan.remove(position);
        notifyItemRemoved(position);
        //notifyDataSetChanged();

    }

    public ProdukDAO getProduk(String nama)
    {
        for (ProdukDAO produk:listProduk
        ) {
            if(produk.getNama().equalsIgnoreCase(nama)){
                return produk;
            }
        }
        return null;
    }

    public void tambahJumlah(){
        this.counter++;
    }
    public void kurangJumlah(){
        this.counter--;
    }
    public int getCounter(){
        return this.counter;
    }
}
