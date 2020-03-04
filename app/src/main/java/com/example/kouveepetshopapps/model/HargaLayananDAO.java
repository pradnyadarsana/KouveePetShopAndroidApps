package com.example.kouveepetshopapps.model;

public class HargaLayananDAO {
    int id_harga_layanan, harga, aktif;
    String created_at, created_by, modified_at, modified_by, delete_at, delete_by;
    LayananDAO layanan;
    UkuranHewanDAO ukuran_hewan;

    public HargaLayananDAO(){}

    public HargaLayananDAO(int id_harga_layanan, int harga, int aktif, String created_at,
                           String created_by, String modified_at, String modified_by, String delete_at,
                           String delete_by, LayananDAO layanan, UkuranHewanDAO ukuran_hewan) {
        this.id_harga_layanan = id_harga_layanan;
        this.harga = harga;
        this.aktif = aktif;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
        this.delete_at = delete_at;
        this.delete_by = delete_by;
        this.layanan = layanan;
        this.ukuran_hewan = ukuran_hewan;
    }

    public int getId_harga_layanan() {
        return id_harga_layanan;
    }

    public void setId_harga_layanan(int id_harga_layanan) {
        this.id_harga_layanan = id_harga_layanan;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
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

    public String getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(String delete_at) {
        this.delete_at = delete_at;
    }

    public String getDelete_by() {
        return delete_by;
    }

    public void setDelete_by(String delete_by) {
        this.delete_by = delete_by;
    }

    public LayananDAO getLayanan() {
        return layanan;
    }

    public void setLayanan(LayananDAO layanan) {
        this.layanan = layanan;
    }

    public UkuranHewanDAO getUkuran_hewan() {
        return ukuran_hewan;
    }

    public void setUkuran_hewan(UkuranHewanDAO ukuran_hewan) {
        this.ukuran_hewan = ukuran_hewan;
    }
}
