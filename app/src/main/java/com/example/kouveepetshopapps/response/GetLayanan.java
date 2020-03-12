package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.LayananDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<LayananDAO> listDataLayanan;

    public GetLayanan(){}

    public GetLayanan(String error, List<LayananDAO> listDataLayanan) {
        this.error = error;
        this.listDataLayanan = listDataLayanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<LayananDAO> getListDataLayanan() {
        return listDataLayanan;
    }

    public void setListDataLayanan(List<LayananDAO> listDataLayanan) {
        this.listDataLayanan = listDataLayanan;
    }
}
