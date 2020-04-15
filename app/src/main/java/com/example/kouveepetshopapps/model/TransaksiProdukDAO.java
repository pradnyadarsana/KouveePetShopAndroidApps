package com.example.kouveepetshopapps.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kouveepetshopapps.BR;
import com.google.gson.annotations.SerializedName;

public class TransaksiProdukDAO extends BaseObservable {
    @SerializedName("id_transaksi_produk")
    String id_transaksi_produk;
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

    public TransaksiProdukDAO(){}

    public TransaksiProdukDAO(String id_transaksi_produk, int id_customer_service, int id_kasir,
                              int id_hewan, int subtotal, int diskon, int total, String status,
                              String tanggal_lunas, String created_at, String created_by,
                              String modified_at, String modified_by) {
        this.id_transaksi_produk = id_transaksi_produk;
        this.id_customer_service = id_customer_service;
        this.id_kasir = id_kasir;
        this.id_hewan = id_hewan;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.total = total;
        this.status = status;
        this.tanggal_lunas = tanggal_lunas;
        this.created_at = created_at;
        this.created_by = created_by;
        this.modified_at = modified_at;
        this.modified_by = modified_by;
    }

    public boolean isHaveDiskon(){
        if(id_hewan!=-1 && id_hewan!=0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Bindable
    public String getId_transaksi_produk() {
        return id_transaksi_produk;
    }

    public void setId_transaksi_produk(String id_transaksi_produk) {
        this.id_transaksi_produk = id_transaksi_produk;
        notifyPropertyChanged(BR.id_transaksi_produk);
    }

    @Bindable
    public int getId_customer_service() {
        return id_customer_service;
    }

    public void setId_customer_service(int id_customer_service) {
        this.id_customer_service = id_customer_service;
        notifyPropertyChanged(BR.id_customer_service);
    }

    @Bindable
    public int getId_kasir() {
        return id_kasir;
    }

    public void setId_kasir(int id_kasir) {
        this.id_kasir = id_kasir;
        notifyPropertyChanged(BR.id_kasir);
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
    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
        notifyPropertyChanged(BR.subtotal);
    }

    @Bindable
    public String getStringSubtotal() {
        return String.valueOf(subtotal);
    }

    public void setStringSubtotal(String stringSubtotal) {
        if(!stringSubtotal.isEmpty()){
            this.subtotal = Integer.parseInt(stringSubtotal);
        }else{
            this.subtotal = 0;
        }
        notifyPropertyChanged(BR.subtotal);
        System.out.println(this.subtotal);
    }

    @Bindable
    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
        notifyPropertyChanged(BR.diskon);
    }

    @Bindable
    public String getStringDiskon() {
        return String.valueOf(diskon);
    }

    public void setStringDiskon(String stringDiskon) {
        if(!stringDiskon.isEmpty()){
            this.diskon = Integer.parseInt(stringDiskon);
        }else{
            this.diskon = 0;
        }
        notifyPropertyChanged(BR.diskon);
        System.out.println(this.diskon);
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public String getStringTotal() {
        return String.valueOf(diskon);
    }

    public void setStringTotal(String stringTotal) {
        if(!stringTotal.isEmpty()) {
            this.total = Integer.parseInt(stringTotal);
        }else{
            this.total = 0;
        }
        notifyPropertyChanged(BR.total);
        System.out.println(this.total);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getTanggal_lunas() {
        return tanggal_lunas;
    }

    public void setTanggal_lunas(String tanggal_lunas) {
        this.tanggal_lunas = tanggal_lunas;
        notifyPropertyChanged(BR.tanggal_lunas);
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
}
