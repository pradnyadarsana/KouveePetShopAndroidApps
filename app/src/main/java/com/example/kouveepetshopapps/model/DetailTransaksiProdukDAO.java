package com.example.kouveepetshopapps.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kouveepetshopapps.BR;
import com.google.gson.annotations.SerializedName;

public class DetailTransaksiProdukDAO extends BaseObservable {
    @SerializedName("id_detail_transaksi_produk")
    int id_detail_transaksi_produk;
    @SerializedName("id_transaksi_produk")
    String id_transaksi_produk;
    @SerializedName("id_produk")
    int id_produk;
    @SerializedName("jumlah")
    int jumlah;
    @SerializedName("total_harga")
    int total_harga;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("modified_at")
    String modified_at;
    @SerializedName("modified_by")
    String modified_by;

    public DetailTransaksiProdukDAO(){}

    public DetailTransaksiProdukDAO(int id_detail_transaksi_produk, String id_transaksi_produk,
                                    int id_produk, int jumlah, int total_harga, String created_at,
                                    String created_by, String modified_at, String modified_by) {
        this.id_detail_transaksi_produk = id_detail_transaksi_produk;
        this.id_transaksi_produk = id_transaksi_produk;
        this.id_produk = id_produk;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
    }

    @Bindable
    public int getId_detail_transaksi_produk() {
        return id_detail_transaksi_produk;
    }

    public void setId_detail_transaksi_produk(int id_detail_transaksi_produk) {
        this.id_detail_transaksi_produk = id_detail_transaksi_produk;
        notifyPropertyChanged(BR.id_detail_transaksi_produk);
    }

    @Bindable
    public String getId_transaksi_produk() {
        return id_transaksi_produk;
    }

    public void setId_transaksi_produk(String id_transaksi_produk) {
        this.id_transaksi_produk = id_transaksi_produk;
        notifyPropertyChanged(BR.id_transaksi_produk);
    }

    @Bindable
    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
        notifyPropertyChanged(BR.id_produk);
    }

    @Bindable
    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        notifyPropertyChanged(BR.jumlah);
    }

    @Bindable
    public String getStringJumlah() {
        return String.valueOf(jumlah);
    }

    public void setStringJumlah(String stringJumlah) {
        this.jumlah = Integer.parseInt(stringJumlah);
        notifyPropertyChanged(BR.jumlah);
    }

    @Bindable
    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
        notifyPropertyChanged(BR.total_harga);
    }

    @Bindable
    public String getStringTotal_harga() {
        return String.valueOf(total_harga);
    }

    public void setStringTotal_harga(String stringTotal_harga) {
        this.total_harga = Integer.parseInt(stringTotal_harga);
        notifyPropertyChanged(BR.total_harga);
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
