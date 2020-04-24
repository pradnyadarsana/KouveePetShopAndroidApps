package com.example.kouveepetshopapps.model;

import com.google.gson.annotations.SerializedName;

public class NamaHargaLayanan {
    @SerializedName("id_harga_layanan")
    int id_harga_layanan;
    @SerializedName("nama_harga_layanan")
    String nama_harga_layanan;

    public NamaHargaLayanan() {
    }

    public NamaHargaLayanan(int id_harga_layanan, String nama_harga_layanan) {
        this.id_harga_layanan = id_harga_layanan;
        this.nama_harga_layanan = nama_harga_layanan;
    }

    public int getId_harga_layanan() {
        return id_harga_layanan;
    }

    public void setId_harga_layanan(int id_harga_layanan) {
        this.id_harga_layanan = id_harga_layanan;
    }

    public String getNama_harga_layanan() {
        return nama_harga_layanan;
    }

    public void setNama_harga_layanan(String nama_harga_layanan) {
        this.nama_harga_layanan = nama_harga_layanan;
    }
}
