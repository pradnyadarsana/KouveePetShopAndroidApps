package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.TransaksiLayananDAO;
import com.example.kouveepetshopapps.model.TransaksiProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTransaksiLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<TransaksiLayananDAO> listDataTransaksiLayanan;

    public GetTransaksiLayanan() {
    }

    public GetTransaksiLayanan(String error, List<TransaksiLayananDAO> listDataTransaksiLayanan) {
        this.error = error;
        this.listDataTransaksiLayanan = listDataTransaksiLayanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<TransaksiLayananDAO> getListDataTransaksiLayanan() {
        return listDataTransaksiLayanan;
    }

    public void setListDataTransaksiLayanan(List<TransaksiLayananDAO> listDataTransaksiLayanan) {
        this.listDataTransaksiLayanan = listDataTransaksiLayanan;
    }
}
