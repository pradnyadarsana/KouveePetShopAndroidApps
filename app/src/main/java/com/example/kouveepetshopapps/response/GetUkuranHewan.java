package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.SupplierDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUkuranHewan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<UkuranHewanDAO> listDataUkuranHewan;

    public GetUkuranHewan(){}

    public GetUkuranHewan(String error, List<UkuranHewanDAO> listDataUkuranHewan) {
        this.error = error;
        this.listDataUkuranHewan = listDataUkuranHewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<UkuranHewanDAO> getListDataUkuranHewan() {
        return listDataUkuranHewan;
    }

    public void setListDataUkuranHewan(List<UkuranHewanDAO> listDataUkuranHewan) {
        this.listDataUkuranHewan = listDataUkuranHewan;
    }
}
