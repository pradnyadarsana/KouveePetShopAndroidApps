package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;

import java.util.List;

public class TambahTransaksiProdukAdapter extends RecyclerView.Adapter<TambahTransaksiProdukAdapter.MyViewHolder> {
    private Context context;
    private List<ProdukDAO> listProduk;
    private List<DetailTransaksiProdukDAO> result;
    private List<ProdukDAO> produkPilihan;
    TransaksiProdukDAO transaksiproduk;
    ProdukDAO produk;

    private ActivityTambahTransaksiProdukBinding binding;

    public TambahTransaksiProdukAdapter(Context context, TransaksiProdukDAO transaksiProduk,
                                        List<DetailTransaksiProdukDAO> result, List<ProdukDAO> produk,
                                        List<ProdukDAO> produkPilihan, ActivityTambahTransaksiProdukBinding binding){
        this.context = context;
        this.result = result;
        this.listProduk = produk;
        this.produkPilihan = produkPilihan;
        this.transaksiproduk = transaksiProduk;
        this.binding = binding;
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
        holder.nama_produk.setText(produkPilihan.get(position).getNama());
        holder.jumlah_produk.setText(String.valueOf(result.get(position).getJumlah()));
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
            harga_produk = itemView.findViewById(R.id.tvHargaProdukTransaksi);
            jumlah_produk = itemView.findViewById(R.id.tvJumlahProdukTransaksi);
            total_harga = itemView.findViewById(R.id.tvTotalHargaProdukTransaksi);
            kurang_produk = itemView.findViewById(R.id.btnKurangJumlahProdukTransaksi);
            tambah_produk = itemView.findViewById(R.id.btnTambahJumlahProdukTransaksi);
            parent = itemView.findViewById(R.id.ParentTambahItemTransaksiProduk);

            //create Autocompletetextview for nama hewan
            nama_produk = (AutoCompleteTextView) itemView.findViewById(R.id.etNamaProdukTransaksi);
            nama_produk.setThreshold(1);//will start working from first character
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
            //setting the adapter data into the AutoCompleteTextView
            nama_produk.setAdapter(adapter);


            //SETTING COUNTER VIEW AND BUTTON
            kurang_produk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = result.get(getAdapterPosition()).getJumlah()-1;
                    //System.out.println(count);
                    if(count<1){
                        showDialog(nama_produk.getText().toString(), getAdapterPosition());
                    }else {
                        jumlah_produk.setText(String.valueOf(count));
                        result.get(getAdapterPosition()).setJumlah(count);

                        System.out.println("posisi adapter: "+getAdapterPosition());
                        System.out.println("harga produk: "+produkPilihan.get(getAdapterPosition()).getHarga()+", jumlah produk: "+result.get(getAdapterPosition()).getJumlah());
                        int total = produkPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                        result.get(getAdapterPosition()).setTotal_harga(total);
                        total_harga.setText(String.valueOf(total));

                        hitungTotal();
                    }

                }
            });

            tambah_produk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = result.get(getAdapterPosition()).getJumlah()+1;
                    //System.out.println(count);

                    jumlah_produk.setText(String.valueOf(count));
                    result.get(getAdapterPosition()).setJumlah(count);

                    System.out.println("posisi adapter: "+getAdapterPosition());
                    System.out.println("harga produk: "+produkPilihan.get(getAdapterPosition()).getHarga()+", jumlah produk: "+result.get(getAdapterPosition()).getJumlah());
                    int total = produkPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                    result.get(getAdapterPosition()).setTotal_harga(total);
                    total_harga.setText(String.valueOf(total));

                    hitungTotal();
                }
            });

            //SET TEXT WATCHER FOR INPUT NAMA PRODUK
            nama_produk.addTextChangedListener(new TextWatcher() {
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
                        produkPilihan.set(getAdapterPosition(), produk);
                        result.get(getAdapterPosition()).setId_produk(produk.getId_produk());

                        System.out.println("posisi adapter: "+getAdapterPosition());
                        System.out.println("id produk: "+result.get(getAdapterPosition()).getId_produk());
                        System.out.println("harga produk: "+produkPilihan.get(getAdapterPosition()).getHarga()+", jumlah produk: "+result.get(getAdapterPosition()).getJumlah());
                        int total = produkPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                        result.get(getAdapterPosition()).setTotal_harga(total);
                        harga_produk.setText(String.valueOf(produkPilihan.get(getAdapterPosition()).getHarga()));
                        total_harga.setText(String.valueOf(total));

                        hitungTotal();
                    }else{
                        produkPilihan.set(getAdapterPosition(), new ProdukDAO());
                        result.get(getAdapterPosition()).setId_produk(0);

                        System.out.println("harga produk: "+produkPilihan.get(getAdapterPosition()).getHarga()+", jumlah produk: "+result.get(getAdapterPosition()).getJumlah());
                        harga_produk.setText(String.valueOf(produkPilihan.get(getAdapterPosition()).getHarga()));
                        int total = produkPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                        result.get(getAdapterPosition()).setTotal_harga(total);
                        total_harga.setText(String.valueOf(total));

                        hitungTotal();
                    }
                }
            });
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
        produkPilihan.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, result.size());
        hitungTotal();

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

    public int hitungSubtotal(List<DetailTransaksiProdukDAO> detailtransaksiproduk){
        int subtotal=0;
        for (DetailTransaksiProdukDAO detail: detailtransaksiproduk
        ) {
            subtotal = subtotal+detail.getTotal_harga();
        }
        return subtotal;
    }

    public void hitungTotal(){
        transaksiproduk.setSubtotal(hitungSubtotal(result));
        int total_trans = transaksiproduk.getSubtotal()-transaksiproduk.getDiskon();
        if(total_trans<0){
            transaksiproduk.setTotal(0);
        }else{
            transaksiproduk.setTotal(total_trans);
        }
        System.out.println("subtotal: "+transaksiproduk.getSubtotal());
        System.out.println("diskon: "+transaksiproduk.getDiskon());
        System.out.println("total: "+transaksiproduk.getTotal());
        binding.setTransaksiProduk(transaksiproduk);
    }
}
