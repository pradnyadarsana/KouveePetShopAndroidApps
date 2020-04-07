package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.PelangganDAO;
import com.google.gson.annotations.SerializedName;

public class SearchPelanggan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    PelangganDAO pelanggan;

    public SearchPelanggan(String error, PelangganDAO pelanggan) {
        this.error = error;
        this.pelanggan = pelanggan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PelangganDAO getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(PelangganDAO pelanggan) {
        this.pelanggan = pelanggan;
    }
}
