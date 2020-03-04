package com.example.kouveepetshopapps.model;

public class ProdukDAO {
    int id_produk, jumlah_stok, harga, min_stok, aktif;
    String nama, satuan, gambar, created_at, created_by, modified_at, modified_by, delete_at, delete_by;

    public ProdukDAO(){}

    public ProdukDAO(int id_produk, int jumlah_stok, int harga, int min_stok, int aktif, String nama,
                     String satuan, String gambar, String created_at, String created_by,
                     String modified_at, String modified_by, String delete_at, String delete_by) {
        this.id_produk = id_produk;
        this.jumlah_stok = jumlah_stok;
        this.harga = harga;
        this.min_stok = min_stok;
        this.aktif = aktif;
        this.nama = nama;
        this.satuan = satuan;
        this.gambar = gambar;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
        this.delete_at = delete_at;
        this.delete_by = delete_by;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public int getJumlah_stok() {
        return jumlah_stok;
    }

    public void setJumlah_stok(int jumlah_stok) {
        this.jumlah_stok = jumlah_stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getMin_stok() {
        return min_stok;
    }

    public void setMin_stok(int min_stok) {
        this.min_stok = min_stok;
    }

    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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
}
