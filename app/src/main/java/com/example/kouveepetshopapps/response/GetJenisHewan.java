package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.JenisHewanDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetJenisHewan {

    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<JenisHewanDAO> listDataJenisHewan;

    public GetJenisHewan(){}

    public GetJenisHewan(String error, List<JenisHewanDAO> listDataJenisHewan) {
        this.error = error;
        this.listDataJenisHewan = listDataJenisHewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<JenisHewanDAO> getListDataJenisHewan() {
        return listDataJenisHewan;
    }

    public void setListDataJenisHewan(List<JenisHewanDAO> listDataJenisHewan) {
        this.listDataJenisHewan = listDataJenisHewan;
    }
}
