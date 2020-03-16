package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PelangganDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GetPelanggan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<PelangganDAO> listDataPelanggan;

    public GetPelanggan(){}

    public GetPelanggan(String error, List<PelangganDAO> listDataHewan) {
        this.error = error;
        this.listDataPelanggan = listDataPelanggan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<PelangganDAO> getListDataPelanggan() {
        return listDataPelanggan;
    }

    public void setListDataHewan(List<PelangganDAO> listDataPelanggan) {
        this.listDataPelanggan = listDataPelanggan;
    }
}

