package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.ProdukDAO;
import com.google.gson.annotations.SerializedName;

public class SearchProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    ProdukDAO produk;

    public SearchProduk() {
    }

    public SearchProduk(String error, ProdukDAO produk) {
        this.error = error;
        this.produk = produk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ProdukDAO getProduk() {
        return produk;
    }

    public void setProduk(ProdukDAO produk) {
        this.produk = produk;
    }
}
