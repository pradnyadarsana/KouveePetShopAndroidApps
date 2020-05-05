package com.example.kouveepetshopapps.response;

import com.example.kouveepetshopapps.model.SupplierDAO;
import com.google.gson.annotations.SerializedName;

public class SearchSupplier {
    @SerializedName("error")
    String error;
    @SerializedName("message")
    SupplierDAO supplier;

    public SearchSupplier() {
    }

    public SearchSupplier(String error, SupplierDAO supplier) {
        this.error = error;
        this.supplier = supplier;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SupplierDAO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDAO supplier) {
        this.supplier = supplier;
    }
}
