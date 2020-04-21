package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.google.gson.annotations.SerializedName;

public class SearchTransaksiProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    TransaksiProdukDAO transaksi_produk;

    public SearchTransaksiProduk() {
    }

    public SearchTransaksiProduk(String error, TransaksiProdukDAO transaksi_produk) {
        this.error = error;
        this.transaksi_produk = transaksi_produk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TransaksiProdukDAO getTransaksi_produk() {
        return transaksi_produk;
    }

    public void setTransaksi_produk(TransaksiProdukDAO transaksi_produk) {
        this.transaksi_produk = transaksi_produk;
    }
}
