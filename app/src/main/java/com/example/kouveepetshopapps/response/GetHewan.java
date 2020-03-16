package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.HewanDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GetHewan {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<HewanDAO> listDataHewan;

    public GetHewan(){}

    public GetHewan(String error, List<HewanDAO> listDataHewan) {
        this.error = error;
        this.listDataHewan = listDataHewan;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<HewanDAO> getListDataHewan() {
        return listDataHewan;
    }

    public void setListDataHewan(List<HewanDAO> listDataHewan) {
        this.listDataHewan = listDataHewan;
    }
}

