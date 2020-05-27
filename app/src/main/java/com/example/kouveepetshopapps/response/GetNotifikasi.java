package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.NotifikasiDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNotifikasi {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<NotifikasiDAO> listDataNotifikasi;

    public GetNotifikasi() { }

    public GetNotifikasi(String error, List<NotifikasiDAO> listDataNotifikasi) {
        this.error = error;
        this.listDataNotifikasi = listDataNotifikasi;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<NotifikasiDAO> getListDataNotifikasi() {
        return listDataNotifikasi;
    }

    public void setListDataNotifikasi(List<NotifikasiDAO> listDataNotifikasi) {
        this.listDataNotifikasi = listDataNotifikasi;
    }
}
