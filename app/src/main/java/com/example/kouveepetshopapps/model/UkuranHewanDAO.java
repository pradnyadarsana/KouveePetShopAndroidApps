package com.example.kouveepetshopapps.model;

public class UkuranHewanDAO {
    int id_ukuran_hewan, aktif;
    String nama, created_at, created_by, modified_at, modified_by, delete_at, delete_by;

    public UkuranHewanDAO(){}

    public UkuranHewanDAO(int id_ukuran_hewan, int aktif, String nama, String created_at,
                          String created_by, String modified_at, String modified_by, String delete_at,
                          String delete_by) {
        this.id_ukuran_hewan = id_ukuran_hewan;
        this.aktif = aktif;
        this.nama = nama;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
        this.delete_at = delete_at;
        this.delete_by = delete_by;
    }

    public int getId_ukuran_hewan() {
        return id_ukuran_hewan;
    }

    public void setId_ukuran_hewan(int id_ukuran_hewan) {
        this.id_ukuran_hewan = id_ukuran_hewan;
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
