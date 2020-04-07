package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetailTransaksiProduk {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<DetailTransaksiProdukDAO> listDataDetailTransaksiProduk;

    public GetDetailTransaksiProduk(){}

    public GetDetailTransaksiProduk(String error, List<DetailTransaksiProdukDAO> listDataDetailTransaksiProduk) {
        this.error = error;
        this.listDataDetailTransaksiProduk = listDataDetailTransaksiProduk;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DetailTransaksiProdukDAO> getListDataDetailTransaksiProduk() {
        return listDataDetailTransaksiProduk;
    }

    public void setListDataDetailTransaksiProduk(List<DetailTransaksiProdukDAO> listDataDetailTransaksiProduk) {
        this.listDataDetailTransaksiProduk = listDataDetailTransaksiProduk;
    }
}
