package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.google.gson.annotations.SerializedName;

public class SearchUkuranHewan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    UkuranHewanDAO ukuranhewan;

    public SearchUkuranHewan(String error, UkuranHewanDAO ukuranhewan) {
        this.error = error;
        this.ukuranhewan = ukuranhewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UkuranHewanDAO getUkuranhewan() {
        return ukuranhewan;
    }

    public void setUkuranhewan(UkuranHewanDAO ukuranhewan) {
        this.ukuranhewan = ukuranhewan;
    }
}
