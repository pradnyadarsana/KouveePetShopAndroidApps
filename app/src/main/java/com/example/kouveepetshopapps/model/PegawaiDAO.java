package com.example.kouveepetshopapps.model;

import com.google.gson.annotations.SerializedName;

public class PegawaiDAO {
    @SerializedName("id_pegawai")
    int id_pegawai;
    @SerializedName("nama")
    String nama;
    @SerializedName("alamat")
    String alamat;
    @SerializedName("tanggal_lahir")
    String tanggal_lahir;
    @SerializedName("telp")
    String telp;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;
    @SerializedName("role")
    String role;
    @SerializedName("created_at")
    String created_at;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("modified_at")
    String modified_at;
    @SerializedName("modified_by")
    String modified_by;
    @SerializedName("delete_at")
    String delete_at;
    @SerializedName("delete_by")
    String delete_by;
    @SerializedName("aktif")
    int aktif;

    public PegawaiDAO(){}

    public PegawaiDAO(int id_pegawai, String nama, String alamat, String tanggal_lahir, String telp,
                      String username, String password, String role, String created_at, String created_by,
                      String modified_at, String modified_by, String delete_at, String delete_by, int aktif) {
        this.id_pegawai = id_pegawai;
        this.nama = nama;
        this.alamat = alamat;
        this.tanggal_lahir = tanggal_lahir;
        this.telp = telp;
        this.username = username;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
        this.delete_at = delete_at;
        this.delete_by = delete_by;
        this.aktif = aktif;
    }

    public int getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(int id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
    }
}
