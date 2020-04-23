package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.google.gson.annotations.SerializedName;

public class SearchHargaLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    HargaLayananDAO harga_layanan;

    public SearchHargaLayanan() {
    }

    public SearchHargaLayanan(String error, HargaLayananDAO harga_layanan) {
        this.error = error;
        this.harga_layanan = harga_layanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HargaLayananDAO getHarga_layanan() {
        return harga_layanan;
    }

    public void setHarga_layanan(HargaLayananDAO harga_layanan) {
        this.harga_layanan = harga_layanan;
    }
}
