package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.DetailPengadaanDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDetailPengadaan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<DetailPengadaanDAO> listDataDetailPengadaan;

    public GetDetailPengadaan() {
    }

    public GetDetailPengadaan(String error, List<DetailPengadaanDAO> listDataDetailPengadaan) {
        this.error = error;
        this.listDataDetailPengadaan = listDataDetailPengadaan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DetailPengadaanDAO> getListDataDetailPengadaan() {
        return listDataDetailPengadaan;
    }

    public void setListDataDetailPengadaan(List<DetailPengadaanDAO> listDataDetailPengadaan) {
        this.listDataDetailPengadaan = listDataDetailPengadaan;
    }
}
