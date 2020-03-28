package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchPegawai {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    PegawaiDAO pegawai;

    public SearchPegawai() {}

    public SearchPegawai(String error, PegawaiDAO pegawai) {
        this.error = error;
        this.pegawai = pegawai;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PegawaiDAO getPegawai() {
        return pegawai;
    }

    public void setPegawai(PegawaiDAO pegawai) {
        this.pegawai = pegawai;
    }
}
