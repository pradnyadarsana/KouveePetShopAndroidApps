package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTransaksiProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<TransaksiProdukDAO> listDataTransaksiProduk;

    public GetTransaksiProduk(){
    }

    public GetTransaksiProduk(String error, List<TransaksiProdukDAO> listDataTransaksiProduk) {
        this.error = error;
        this.listDataTransaksiProduk = listDataTransaksiProduk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<TransaksiProdukDAO> getListDataTransaksiProduk() {
        return listDataTransaksiProduk;
    }

    public void setListDataTransaksiProduk(List<TransaksiProdukDAO> listDataTransaksiProduk) {
        this.listDataTransaksiProduk = listDataTransaksiProduk;
    }
}
