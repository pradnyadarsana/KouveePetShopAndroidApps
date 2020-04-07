package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.google.gson.annotations.SerializedName;

public class SearchJenisHewan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    JenisHewanDAO jenishewan;

    public SearchJenisHewan(String error, JenisHewanDAO jenishewan) {
        this.error = error;
        this.jenishewan = jenishewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public JenisHewanDAO getJenishewan() {
        return jenishewan;
    }

    public void setJenishewan(JenisHewanDAO jenishewan) {
        this.jenishewan = jenishewan;
    }
}
