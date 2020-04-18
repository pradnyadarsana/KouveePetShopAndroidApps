package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPegawai {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<PegawaiDAO> listDataPegawai;

    public GetPegawai(){}

    public GetPegawai(String error, List<PegawaiDAO> listDataPegawai) {
        this.error = error;
        this.listDataPegawai = listDataPegawai;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<PegawaiDAO> getListDataPegawai() {
        return listDataPegawai;
    }

    public void setListDataPegawai(List<PegawaiDAO> listDataPegawai) {
        this.listDataPegawai = listDataPegawai;
    }
}
