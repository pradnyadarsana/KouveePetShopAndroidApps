package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.google.gson.annotations.SerializedName;

public class SearchHewan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    HewanDAO hewan;

    public SearchHewan(String error, HewanDAO hewan) {
        this.error = error;
        this.hewan = hewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HewanDAO getHewan() {
        return hewan;
    }

    public void setHewan(HewanDAO hewan) {
        this.hewan = hewan;
    }
}
