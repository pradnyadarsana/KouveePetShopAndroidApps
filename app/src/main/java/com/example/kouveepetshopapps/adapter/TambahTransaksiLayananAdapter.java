package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiLayananBinding;
import com.example.kouveepetshopapps.databinding.ActivityTambahTransaksiProdukBinding;
import com.example.kouveepetshopapps.model.DetailTransaksiLayananDAO;
import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.NamaHargaLayanan;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;

import java.util.List;

public class TambahTransaksiLayananAdapter extends RecyclerView.Adapter<TambahTransaksiLayananAdapter.MyViewHolder> {

    private Context context;
    private List<HargaLayananDAO> listHargaLayanan;
    private List<NamaHargaLayanan> listNamaHargaLayanan;
    private List<DetailTransaksiLayananDAO> result;
    private List<HargaLayananDAO> hargaLayananPilihan;
    TransaksiLayananDAO transaksilayanan;
    HargaLayananDAO hargalayanan;

    private ActivityTambahTransaksiLayananBinding binding;

    public TambahTransaksiLayananAdapter(Context context, TransaksiLayananDAO transaksiLayanan,
                                         List<DetailTransaksiLayananDAO> result, List<HargaLayananDAO> hargalayanan,
                                         List<NamaHargaLayanan> namaHargaLayanan, List<HargaLayananDAO> hargaLayananPilihan,
                                         ActivityTambahTransaksiLayananBinding binding){
        this.context = context;
        this.result = result;
        this.listHargaLayanan = hargalayanan;
        this.listNamaHargaLayanan = namaHargaLayanan;
        this.hargaLayananPilihan = hargaLayananPilihan;
        this.transaksilayanan = transaksiLayanan;
        this.binding = binding;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_tambah_transaksi_layanan, parent, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String nama = "";
        if(hargaLayananPilihan.get(position).getId_harga_layanan()!=0){
            nama = getNamaHargaLayanan(hargaLayananPilihan.get(position).getId_harga_layanan()).getNama_harga_layanan();
        }
        holder.nama_harga_layanan.setText(nama);
        holder.jumlah_layanan.setText(String.valueOf(result.get(position).getJumlah()));
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView harga_layanan, jumlah_layanan, total_harga;
        private AutoCompleteTextView nama_harga_layanan;
        private Button kurang_produk, tambah_produk;
        private CardView parent;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            harga_layanan = itemView.findViewById(R.id.tvHargaLayananTransaksi);
            jumlah_layanan = itemView.findViewById(R.id.tvJumlahLayananTransaksi);
            total_harga = itemView.findViewById(R.id.tvTotalHargaLayananTransaksi);
            kurang_produk = itemView.findViewById(R.id.btnKurangJumlahLayananTransaksi);
            tambah_produk = itemView.findViewById(R.id.btnTambahJumlahLayananTransaksi);
            parent = itemView.findViewById(R.id.ParentTambahItemTransaksiLayanan);

            //create Autocompletetextview for nama hewan
            nama_harga_layanan = (AutoCompleteTextView) itemView.findViewById(R.id.etNamaLayananTransaksi);
            nama_harga_layanan.setThreshold(1);//will start working from first character
//            //set adapter autocompletetextview
//            String[] arrName = new String[listProduk.size()];
//            int i = 0;
//            for (ProdukDAO produk: listProduk
//            ) {
//                arrName[i] = produk.getNama();
//                i++;
//            }
//            //Creating the instance of ArrayAdapter containing list
            AutoCompleteHargaLayananAdapter adapter = new AutoCompleteHargaLayananAdapter(context,listNamaHargaLayanan);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>
//                    (context, android.R.layout.select_dialog_item, arrName);
            //setting the adapter data into the AutoCompleteTextView
            nama_harga_layanan.setAdapter(adapter);


            //SETTING COUNTER VIEW AND BUTTON
            kurang_produk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = result.get(getAdapterPosition()).getJumlah()-1;
                    //System.out.println(count);
                    if(count<1){
                        showDialog(nama_harga_layanan.getText().toString(), getAdapterPosition());
                    }else {
                        jumlah_layanan.setText(String.valueOf(count));
                        result.get(getAdapterPosition()).setJumlah(count);

                        System.out.println("posisi adapter: "+getAdapterPosition());
                        System.out.println("harga layanan: "+hargaLayananPilihan.get(getAdapterPosition()).getHarga()+", jumlah layanan: "+result.get(getAdapterPosition()).getJumlah());
                        int total = hargaLayananPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
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

                    jumlah_layanan.setText(String.valueOf(count));
                    result.get(getAdapterPosition()).setJumlah(count);

                    System.out.println("posisi adapter: "+getAdapterPosition());
                    System.out.println("harga layanan: "+hargaLayananPilihan.get(getAdapterPosition()).getHarga()+", jumlah layanan: "+result.get(getAdapterPosition()).getJumlah());
                    int total = hargaLayananPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                    result.get(getAdapterPosition()).setTotal_harga(total);
                    total_harga.setText(String.valueOf(total));

                    hitungTotal();
                }
            });

            //SET TEXT WATCHER FOR INPUT NAMA PRODUK
            nama_harga_layanan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    hargalayanan = getHargaLayanan(s.toString());
                    if(hargalayanan!=null){
                        hargaLayananPilihan.set(getAdapterPosition(), hargalayanan);
                        result.get(getAdapterPosition()).setId_harga_layanan(hargalayanan.getId_harga_layanan());

                        System.out.println("posisi adapter: "+getAdapterPosition());
                        System.out.println("id harga layanan: "+result.get(getAdapterPosition()).getId_harga_layanan());
                        System.out.println("harga layanan: "+hargaLayananPilihan.get(getAdapterPosition()).getHarga()+", jumlah layanan: "+result.get(getAdapterPosition()).getJumlah());
                        int total = hargaLayananPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
                        result.get(getAdapterPosition()).setTotal_harga(total);
                        harga_layanan.setText(String.valueOf(hargaLayananPilihan.get(getAdapterPosition()).getHarga()));
                        total_harga.setText(String.valueOf(total));

                        hitungTotal();
                    }else{
                        hargaLayananPilihan.set(getAdapterPosition(), new HargaLayananDAO());
                        result.get(getAdapterPosition()).setId_harga_layanan(0);

                        System.out.println("harga layanan: "+hargaLayananPilihan.get(getAdapterPosition()).getHarga()+", jumlah layanan: "+result.get(getAdapterPosition()).getJumlah());
                        harga_layanan.setText(String.valueOf(hargaLayananPilihan.get(getAdapterPosition()).getHarga()));
                        int total = hargaLayananPilihan.get(getAdapterPosition()).getHarga()*result.get(getAdapterPosition()).getJumlah();
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

    private void showDialog(String nama_layanan, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus item?");
        alertDialogBuilder.setMessage(nama_layanan);

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
        hargaLayananPilihan.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, result.size());
        hitungTotal();
    }

    public NamaHargaLayanan getNamaHargaLayanan(int id){
        for (NamaHargaLayanan item:listNamaHargaLayanan
        ) {
            if(item.getId_harga_layanan()==id){
                return item;
            }
        }
        return null;
    }

    public HargaLayananDAO getHargaLayanan(String nama)
    {
        NamaHargaLayanan temp = new NamaHargaLayanan();
        for (NamaHargaLayanan item:listNamaHargaLayanan
        ) {
            if(item.getNama_harga_layanan().equalsIgnoreCase(nama)){
                temp=item;
                break;
            }
        }
        for (HargaLayananDAO harga: listHargaLayanan
             ) {
            if(harga.getId_harga_layanan()==temp.getId_harga_layanan()){
                return harga;
            }
        }
        return null;
    }

    public int hitungSubtotal(List<DetailTransaksiLayananDAO> detailtransaksi){
        int subtotal=0;
        for (DetailTransaksiLayananDAO detail: detailtransaksi
        ) {
            subtotal = subtotal+detail.getTotal_harga();
        }
        return subtotal;
    }

    public void hitungTotal(){
        transaksilayanan.setSubtotal(hitungSubtotal(result));
        int total_trans = transaksilayanan.getSubtotal()-transaksilayanan.getDiskon();
        if(total_trans<0){
            transaksilayanan.setTotal(0);
        }else{
            transaksilayanan.setTotal(total_trans);
        }
        System.out.println("subtotal: "+transaksilayanan.getSubtotal());
        System.out.println("diskon: "+transaksilayanan.getDiskon());
        System.out.println("total: "+transaksilayanan.getTotal());
        binding.setTransaksiLayanan(transaksilayanan);
    }
}
