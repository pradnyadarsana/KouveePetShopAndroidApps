package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.SupplierDAO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSupplier {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    List<SupplierDAO> listDataSupplier;

    public GetSupplier(){}

    public GetSupplier(String error, List<SupplierDAO> listDataSupplier) {
        this.error = error;
        this.listDataSupplier = listDataSupplier;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SupplierDAO> getListDataSupplier() {
        return listDataSupplier;
    }

    public void setListDataSupplier(List<SupplierDAO> listDataSupplier) {
        this.listDataSupplier = listDataSupplier;
    }
}
