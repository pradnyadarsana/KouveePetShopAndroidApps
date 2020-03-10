package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterfaceAdmin {

    //KELOLA SUPPLIER
    @POST("supplier")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahSupplier(@Field("nama")String nama,
                                          @Field("alamat")String alamat,
                                          @Field("telp")String telp,
                                          @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("supplier")
    Call<GetSupplier> getAllSupplierAktif();

    @POST("supplier/update/{id_supplier}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahSupplier(@Path("id_supplier") String id_supplier,
                                        @Field("nama")String nama,
                                        @Field("alamat")String alamat,
                                        @Field("telp")String telp,
                                        @Field("modified_by")String modified_by);

    @POST("supplier/delete/{id_supplier}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusSupplier(@Path("id_supplier") String id_supplier,
                                         @Field("delete_by")String delete_by);


    //KELOLA JENIS HEWAN
    @POST("JenisHewan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahJenisHewan(@Field("nama")String nama,
                                            @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("JenisHewan")
    Call<GetJenisHewan> getAllJenisHewanAktif();

    @POST("JenisHewan/update/{id_jenis_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahJenisHewan(@Path("id_jenis_hewan") String id_jenis_hewan,
                                        @Field("nama")String nama,
                                        @Field("modified_by")String modified_by);

    @POST("JenisHewan/delete/{id_jenis_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusJenisHewan(@Path("id_jenis_hewan") String id_jenis_hewan,
                                         @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);



    //KELOLA UKURAN HEWAN
    @POST("UkuranHewan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahUkuranHewan(@Field("nama")String nama,
                                          @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("UkuranHewan")
    Call<GetUkuranHewan> getAllUkuranHewanAktif();

    @POST("UkuranHewan/update/{id_ukuran_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahUkuranHewan(@Path("id_ukuran_hewan") String id_ukuran_hewan,
                                        @Field("nama")String nama,
                                        @Field("modified_by")String modified_by);

    @POST("UkuranHewan/delete/{id_ukuran_hewan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusUkuranHewan(@Path("id_ukuran_hewan") String id_ukuran_hewan,
                                         @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);


//    //KELOLA PRODUK
//    @POST("supplier")
//    @Multipart
//    Call<PostUpdateDelete> tambahProduk(@Field("nama")String nama,
//                                        @Field("satuan")String satuan,
//                                        @Field("jumlah_stok")String jumlah_stok,
//                                        @Field("harga")String harga,
//                                        @Field("min_stok")String min_stok,
//                                        @Part MultipartBody.Part ("gambar")String gambar,
//                                        @Field("created_by")String created_by);
//
////    @GET("viewProfile.php/{email}")
//    //   Call<UserDAO> getUser(@Path("email") String email);
//
//    @GET("supplier")
//    Call<GetSupplier> getAllSupplierAktif();
//
//    @POST("supplier/update/{id_supplier}")
//    @FormUrlEncoded
//    Call<PostUpdateDelete> ubahSupplier(@Path("id_supplier") String id_supplier,
//                                        @Field("nama")String nama,
//                                        @Field("alamat")String alamat,
//                                        @Field("telp")String telp,
//                                        @Field("modified_by")String modified_by);
//
//    @POST("supplier/delete/{id_supplier}")
//    @FormUrlEncoded
//    Call<PostUpdateDelete> hapusSupplier(@Path("id_supplier") String id_supplier,
//                                         @Field("delete_by")String delete_by);
////    @DELETE("Profile/deleteProfile/{id}")
////    Call<String> deleteUser(@Path("id") String id);


}
