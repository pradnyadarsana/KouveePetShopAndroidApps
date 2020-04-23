package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.LayananDAO;
import com.google.gson.annotations.SerializedName;

public class SearchLayanan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    LayananDAO layanan;

    public SearchLayanan() {
    }

    public SearchLayanan(String error, LayananDAO layanan) {
        this.error = error;
        this.layanan = layanan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LayananDAO getLayanan() {
        return layanan;
    }

    public void setLayanan(LayananDAO layanan) {
        this.layanan = layanan;
    }
}
