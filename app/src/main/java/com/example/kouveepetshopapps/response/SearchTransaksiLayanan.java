package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.google.gson.annotations.SerializedName;

public class SearchTransaksiLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    TransaksiLayananDAO transaksi_layanan;

    public SearchTransaksiLayanan() {
    }

    public SearchTransaksiLayanan(String error, TransaksiLayananDAO transaksi_layanan) {
        this.error = error;
        this.transaksi_layanan = transaksi_layanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TransaksiLayananDAO getTransaksi_layanan() {
        return transaksi_layanan;
    }

    public void setTransaksi_layanan(TransaksiLayananDAO transaksi_layanan) {
        this.transaksi_layanan = transaksi_layanan;
    }
}
