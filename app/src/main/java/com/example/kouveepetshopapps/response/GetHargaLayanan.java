package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHargaLayanan {

    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<HargaLayananDAO> listDataHargaLayanan;

    public  GetHargaLayanan(){}

    public GetHargaLayanan(String error, List<HargaLayananDAO> listDataHargaLayanan) {
        this.error = error;
        this.listDataHargaLayanan = listDataHargaLayanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<HargaLayananDAO> getListDataHargaLayanan() {
        return listDataHargaLayanan;
    }

    public void setListDataHargaLayanan(List<HargaLayananDAO> listDataHargaLayanan) {
        this.listDataHargaLayanan = listDataHargaLayanan;
    }
}
