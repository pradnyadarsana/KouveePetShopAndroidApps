package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchPegawai;

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
    Call<PostUpdateDelete> tambahPelanggan(@Field("nama")String nama,
                                           @Field("alamat")String alamat,
                                           @Field("tanggal_lahir")String tanggal_lahir,
                                           @Field("telp")String telp,
                                           @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("pelanggan")
    Call<GetPelanggan> getAllPelangganAktif();

    @GET("pelanggan/search/{id_pelanggan}")
    Call<GetPelanggan> searchPelanggan(@Path("id_pelanggan")String id_pelanggan);

    @POST("pelanggan/update/{id_pelanggan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahPelanggan(@Path("id_pelanggan") String id_pelanggan,
                                         @Field("nama")String nama,
                                         @Field("alamat")String alamat,
                                         @Field("tanggal_lahir")String tanggal_lahir,
                                         @Field("telp")String telp,
                                         @Field("modified_by")String modified_by);

    @POST("pelanggan/delete/{id_pelanggan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusPelanggan(@Path("id_pelanggan") String id_pelanggan,
                                          @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);


    //KELOLA HEWAN
    @POST("hewan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahHewan(@Field("id_pelanggan") String id_pelanggan,
                                     @Field("id_jenis_hewan") String id_jenis_hewan,
                                     @Field("nama") String nama,
                                     @Field("tanggal_lahir") String tanggal_lahir,
                                     @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("hewan")
    Call<GetHewan> getAllHewanAktif();

    @POST("hewan/update/{id_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahHewan(@Path("id_hewan") String id_hewan,
                                     @Field("id_pelanggan") String id_pelanggan,
                                     @Field("id_jenis_hewan") String id_jenis_hewan,
                                     @Field("nama") String nama,
                                     @Field("tanggal_lahir") String tanggal_lahir,
                                     @Field("modified_by")String modified_by);

    @POST("hewan/delete/{id_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusHewan(@Path("id_hewan") String id_hewan,
                                      @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);

    //AUTHENTICATION
    @POST("pegawai/auth")
    @FormUrlEncoded
    Call<SearchPegawai> authPegawai(@Field("username") String username,
                                    @Field("password") String password);

}
