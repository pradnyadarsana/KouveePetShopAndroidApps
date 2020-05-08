package com.example.kouveepetshopapps.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kouveepetshopapps.BR;
import com.google.gson.annotations.SerializedName;

public class PengadaanProdukDAO extends BaseObservable {
    @SerializedName("id_pengadaan_produk")
    String id_pengadaan_produk;
    @SerializedName("id_supplier")
    int id_supplier;
    @SerializedName("total")
    int total;
    @SerializedName("status")
    String status;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("modified_at")
    String modified_at;
    @SerializedName("modified_by")
    String modified_by;

    public PengadaanProdukDAO() {
    }

    public PengadaanProdukDAO(String id_pengadaan_produk, int id_supplier, int total, String status,
                              String created_at, String created_by, String modified_at, String modified_by) {
        this.id_pengadaan_produk = id_pengadaan_produk;
        this.id_supplier = id_supplier;
        this.total = total;
        this.status = status;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
    }

    public boolean isAllowToEditAndDelete(){
        return this.status.equalsIgnoreCase("Menunggu Konfirmasi");
    }

    public boolean isPesananDiproses(){
        return this.status.equalsIgnoreCase("Pesanan Diproses");
    }

    public boolean isPesananSelesai(){
        return this.status.equalsIgnoreCase("Pesanan Selesai");
    }

    public String updateStatusButtonText(){
        if(this.status.equalsIgnoreCase("Menunggu Konfirmasi")){
            return "Cetak Struk dan Proses Pesanan";
        }else if(this.status.equalsIgnoreCase("Pesanan Diproses")){
            return "Pesanan Sudah Sampai";
        }
        return "";
    }

    public String keteranganStatus(){
        if(this.status.equalsIgnoreCase("Menunggu Konfirmasi")){
            return "Struk pengadaan ini belum dicetak untuk melakukan pemesanan.";
        }else if(this.status.equalsIgnoreCase("Pesanan Diproses")){
            return "Pemesanan telah dilakukan, tunggu sampai semua produk datang.";
        }else{
            return "Produk yang dipesan sudah datang, pengadaan selesai.";
        }
    }

    @Bindable
    public String getId_pengadaan_produk() {
        return id_pengadaan_produk;
    }

    public void setId_pengadaan_produk(String id_pengadaan_produk) {
        this.id_pengadaan_produk = id_pengadaan_produk;
        notifyPropertyChanged(BR.id_pengadaan_produk);
    }

    @Bindable
    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(int id_supplier) {
        this.id_supplier = id_supplier;
        notifyPropertyChanged(BR.id_supplier);
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public String getStringTotal() {
        return String.valueOf(total);
    }

    public void setStringTotal(String stringTotal) {
        if(!stringTotal.isEmpty()) {
            this.total = Integer.parseInt(stringTotal);
        }else{
            this.total = 0;
        }
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
        notifyPropertyChanged(BR.created_at);
    }

    @Bindable
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
        notifyPropertyChanged(BR.created_by);
    }

    @Bindable
    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
        notifyPropertyChanged(BR.modified_at);
    }

    @Bindable
    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
        notifyPropertyChanged(BR.modified_by);
    }
}
