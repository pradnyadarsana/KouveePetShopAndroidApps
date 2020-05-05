package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.google.gson.annotations.SerializedName;

public class SearchPengadaanProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    PengadaanProdukDAO pengadaan_produk;

    public SearchPengadaanProduk() {
    }

    public SearchPengadaanProduk(String error, PengadaanProdukDAO pengadaan_produk) {
        this.error = error;
        this.pengadaan_produk = pengadaan_produk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PengadaanProdukDAO getPengadaan_produk() {
        return pengadaan_produk;
    }

    public void setPengadaan_produk(PengadaanProdukDAO pengadaan_produk) {
        this.pengadaan_produk = pengadaan_produk;
    }
}
