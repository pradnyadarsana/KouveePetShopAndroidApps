package com.example.kouveepetshopapps.model;

import com.google.gson.annotations.SerializedName;

public class TransaksiLayananDAO {
    @SerializedName("id_transaksi_layanan")
    String id_transaksi_layanan;
    @SerializedName("id_customer_service")
    int id_customer_service;
    @SerializedName("id_kasir")
    int id_kasir;
    @SerializedName("id_hewan")
    int id_hewan;
    @SerializedName("subtotal")
    int subtotal;
    @SerializedName("diskon")
    int diskon;
    @SerializedName("total")
    int total;
    @SerializedName("progress")
    String progress;
    @SerializedName("status")
    String status;
    @SerializedName("tanggal_lunas")
    String tanggal_lunas;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("modified_at")
    String modified_at;
    @SerializedName("modified_by")
    String modified_by;

    public TransaksiLayananDAO() {
    }

    public TransaksiLayananDAO(String id_transaksi_layanan, int id_customer_service, int id_kasir,
                               int id_hewan, int subtotal, int diskon, int total, String progress,
                               String status, String tanggal_lunas, String created_at, String created_by,
                               String modified_at, String modified_by) {
        this.id_transaksi_layanan = id_transaksi_layanan;
        this.id_customer_service = id_customer_service;
        this.id_kasir = id_kasir;
        this.id_hewan = id_hewan;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.total = total;
        this.progress = progress;
        this.status = status;
        this.tanggal_lunas = tanggal_lunas;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
    }

    public String getId_transaksi_layanan() {
        return id_transaksi_layanan;
    }

    public void setId_transaksi_layanan(String id_transaksi_layanan) {
        this.id_transaksi_layanan = id_transaksi_layanan;
    }

    public int getId_customer_service() {
        return id_customer_service;
    }

    public void setId_customer_service(int id_customer_service) {
        this.id_customer_service = id_customer_service;
    }

    public int getId_kasir() {
        return id_kasir;
    }

    public void setId_kasir(int id_kasir) {
        this.id_kasir = id_kasir;
    }

    public int getId_hewan() {
        return id_hewan;
    }

    public void setId_hewan(int id_hewan) {
        this.id_hewan = id_hewan;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal_lunas() {
        return tanggal_lunas;
    }

    public void setTanggal_lunas(String tanggal_lunas) {
        this.tanggal_lunas = tanggal_lunas;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }
}
