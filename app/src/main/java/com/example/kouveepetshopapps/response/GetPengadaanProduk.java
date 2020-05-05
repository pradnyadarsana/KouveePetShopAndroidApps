package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PengadaanProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPengadaanProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<PengadaanProdukDAO> listDataPengadaanProduk;

    public GetPengadaanProduk() {
    }

    public GetPengadaanProduk(String error, List<PengadaanProdukDAO> listDataPengadaanProduk) {
        this.error = error;
        this.listDataPengadaanProduk = listDataPengadaanProduk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<PengadaanProdukDAO> getListDataPengadaanProduk() {
        return listDataPengadaanProduk;
    }

    public void setListDataPengadaanProduk(List<PengadaanProdukDAO> listDataPengadaanProduk) {
        this.listDataPengadaanProduk = listDataPengadaanProduk;
    }
}
