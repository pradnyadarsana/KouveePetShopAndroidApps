package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.model.PelangganDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetPelanggan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterfaceCS {

    //KELOLA PELANGGAN
    @POST("pelanggan")
    @FormUrlEncoded
    Call<String> tambahPelanggan(@Field("nama")String nama,
                                       @Field("alamat")String alamat,
                                       @Field("tanggal_lahir")String tanggal_lahir,
                                       @Field("telp")String telp,
                                       @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("pelanggan")
    Call<GetPelanggan> getAllPelangganAktif();

    @POST("pelanggan/update/{id_pelanggan}")
    @FormUrlEncoded
    Call<String> ubahPelanggan(@Path("id_pelanggan") String id_pelanggan,
                                     @Field("nama")String nama,
                                     @Field("alamat")String alamat,
                                     @Field("tanggal_lahir")String tanggal_lahir,
                                     @Field("telp")String telp,
                                     @Field("modified_by")String modified_by);

    @POST("pelanggan/delete/{id_pelanggan}")
    @FormUrlEncoded
    Call<String> hapusPelanggan(@Path("id_pelanggan") String id_pelanggan,
                                     @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);


    //KELOLA UKURAN HEWAN
    @POST("ukuran_hewan")
    @FormUrlEncoded
    Call<UkuranHewanDAO> tambahUkuranHewan(@Field("nama")String nama,
                                         @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("ukuran_hewan")
    Call<List<UkuranHewanDAO>> getAllUkuranHewanAktif();

    @POST("ukuran_hewan/update/{id_ukuran_hewan}")
    @FormUrlEncoded
    Call<UkuranHewanDAO> ubahUkuranHewan(@Path("id_ukuran_hewan") String id_ukuran_hewan,
                                     @Field("nama")String nama,
                                     @Field("modified_by")String modified_by);

    @POST("ukuran_hewan/delete/{id_ukuran_hewan}")
    @FormUrlEncoded
    Call<UkuranHewanDAO> hapusUkuranHewan(@Path("id_ukuran_hewan") String id_ukuran_hewan,
                                      @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);
}
