package com.example.kouveepetshopapps.model;

import com.google.gson.annotations.SerializedName;

public class DetailTransaksiLayananDAO {
    @SerializedName("id_detail_transaksi_layanan")
    int id_detail_transaksi_layanan;
    @SerializedName("id_transaksi_layanan")
    String id_transaksi_layanan;
    @SerializedName("id_harga_layanan")
    int id_harga_layanan;
    @SerializedName("jumlah")
    int jumlah;
    @SerializedName("total_harga")
    int total_harga;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("modified_at")
    String modified_at;
    @SerializedName("modified_by")
    String modified_by;

    public DetailTransaksiLayananDAO(){}

    public DetailTransaksiLayananDAO(int id_detail_transaksi_layanan, String id_transaksi_layanan,
                                     int id_harga_layanan, int jumlah, int total_harga, String created_at,
                                     String created_by, String modified_at, String modified_by) {
        this.id_detail_transaksi_layanan = id_detail_transaksi_layanan;
        this.id_transaksi_layanan = id_transaksi_layanan;
        this.id_harga_layanan = id_harga_layanan;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
    }

    public int getId_detail_transaksi_layanan() {
        return id_detail_transaksi_layanan;
    }

    public void setId_detail_transaksi_layanan(int id_detail_transaksi_layanan) {
        this.id_detail_transaksi_layanan = id_detail_transaksi_layanan;
    }

    public String getId_transaksi_layanan() {
        return id_transaksi_layanan;
    }

    public void setId_transaksi_layanan(String id_transaksi_layanan) {
        this.id_transaksi_layanan = id_transaksi_layanan;
    }

    public int getId_harga_layanan() {
        return id_harga_layanan;
    }

    public void setId_harga_layanan(int id_harga_layanan) {
        this.id_harga_layanan = id_harga_layanan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
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
