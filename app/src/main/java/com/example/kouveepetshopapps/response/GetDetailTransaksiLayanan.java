package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.DetailTransaksiLayananDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetailTransaksiLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<DetailTransaksiLayananDAO> listDataDetailTransaksiLayanan;

    public GetDetailTransaksiLayanan() {
    }

    public GetDetailTransaksiLayanan(String error, List<DetailTransaksiLayananDAO> listDataDetailTransaksiLayanan) {
        this.error = error;
        this.listDataDetailTransaksiLayanan = listDataDetailTransaksiLayanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DetailTransaksiLayananDAO> getListDataDetailTransaksiLayanan() {
        return listDataDetailTransaksiLayanan;
    }

    public void setListDataDetailTransaksiLayanan(List<DetailTransaksiLayananDAO> listDataDetailTransaksiLayanan) {
        this.listDataDetailTransaksiLayanan = listDataDetailTransaksiLayanan;
    }
}
