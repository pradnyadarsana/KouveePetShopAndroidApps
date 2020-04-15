package com.example.kouveepetshopapps.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kouveepetshopapps.BR;
import com.google.gson.annotations.SerializedName;

public class HewanDAO extends BaseObservable {
    int id_hewan, id_pelanggan, id_jenis_hewan, aktif;
    String nama, tanggal_lahir, created_at, created_by, modified_at, modified_by, delete_at, delete_by;

    public HewanDAO(){}

    public HewanDAO(int id_hewan, int id_pelanggan, int id_jenis_hewan, int aktif, String nama,
                    String tanggal_lahir, String created_at, String created_by, String modified_at,
                    String modified_by, String delete_at, String delete_by) {
        this.id_hewan = id_hewan;
        this.id_pelanggan = id_pelanggan;
        this.id_jenis_hewan = id_jenis_hewan;
        this.aktif = aktif;
        this.nama = nama;
        this.tanggal_lahir = tanggal_lahir;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
        this.delete_at = delete_at;
        this.delete_by = delete_by;
    }

    @Bindable
    public int getId_hewan() {
        return id_hewan;
    }

    public void setId_hewan(int id_hewan) {
        this.id_hewan = id_hewan;
        notifyPropertyChanged(BR.id_hewan);
    }

    @Bindable
    public int getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(int id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
        notifyPropertyChanged(BR.id_pelanggan);
    }

    @Bindable
    public int getId_jenis_hewan() {
        return id_jenis_hewan;
    }

    public void setId_jenis_hewan(int id_jenis_hewan) {
        this.id_jenis_hewan = id_jenis_hewan;
        notifyPropertyChanged(BR.id_jenis_hewan);
    }

    @Bindable
    public int getAktif() {
        return aktif;
    }

    public void setAktif(int aktif) {
        this.aktif = aktif;
        notifyPropertyChanged(BR.aktif);
    }

    @Bindable
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
        notifyPropertyChanged(BR.nama);
    }

    @Bindable
    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
        notifyPropertyChanged(BR.tanggal_lahir);
    }

    @Bindable
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
        notifyPropertyChanged(BR.created_at);
    }

    @Bindable
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
        notifyPropertyChanged(BR.created_by);
    }

    @Bindable
    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
        notifyPropertyChanged(BR.modified_at);
    }

    @Bindable
    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
        notifyPropertyChanged(BR.modified_by);
    }

    @Bindable
    public String getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(String delete_at) {
        this.delete_at = delete_at;
        notifyPropertyChanged(BR.delete_at);
    }

    @Bindable
    public String getDelete_by() {
        return delete_by;
    }

    public void setDelete_by(String delete_by) {
        this.delete_by = delete_by;
        notifyPropertyChanged(BR.delete_by);
    }
}
