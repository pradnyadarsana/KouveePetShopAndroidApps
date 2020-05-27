package com.example.kouveepetshopapps.model;

import com.google.gson.annotations.SerializedName;

public class NotifikasiDAO {
    @SerializedName("id_notifikasi")
    int id_notifikasi;
    @SerializedName("id_produk")
    int id_produk;
    @SerializedName("status")
    int status;
    @SerializedName("created_at")
    String created_at;

    public NotifikasiDAO() { }

    public NotifikasiDAO(int id_notifikasi, int id_produk, int status, String created_at) {
        this.id_notifikasi = id_notifikasi;
        this.id_produk = id_produk;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId_notifikasi() {
        return id_notifikasi;
    }

    public void setId_notifikasi(int id_notifikasi) {
        this.id_notifikasi = id_notifikasi;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
