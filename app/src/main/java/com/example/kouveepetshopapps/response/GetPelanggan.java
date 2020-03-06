package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PelangganDAO;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GetPelanggan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    ArrayList<PelangganDAO> listDataPelanggan;

    public GetPelanggan(){}

    public GetPelanggan(String error, ArrayList<PelangganDAO> listDataPelanggan) {
        this.error = error;
        this.listDataPelanggan = listDataPelanggan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<PelangganDAO> getListDataPelanggan() {
        return listDataPelanggan;
    }

    public void setListDataPelanggan(ArrayList<PelangganDAO> listDataPelanggan) {
        this.listDataPelanggan = listDataPelanggan;
    }
}
