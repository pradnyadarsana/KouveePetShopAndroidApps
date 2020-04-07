package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchJenisHewan;
import com.example.kouveepetshopapps.response.SearchUkuranHewan;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("JenisHewan/search/{id_jenis_hewan}")
    Call<SearchJenisHewan> searchJenisHewan(@Path("id_jenis_hewan")String id_jenis_hewan);

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

    @GET("UkuranHewan/search/{id_ukuran_hewan}")
    Call<SearchUkuranHewan> searchUkuran(@Path("id_ukuran_hewan")String id_ukuran_hewan);

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


    //KELOLA PRODUK
    @POST("produk")
    @Multipart
    Call<PostUpdateDelete> tambahProduk(@Part("nama") RequestBody nama,
                                        @Part("satuan")RequestBody satuan,
                                        @Part("jumlah_stok")RequestBody jumlah_stok,
                                        @Part("harga")RequestBody harga,
                                        @Part("min_stok") RequestBody min_stok,
                                        @Part MultipartBody.Part gambar,
                                        @Part("created_by")RequestBody created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("produk")
    Call<GetProduk> getAllProdukAktif();

    @POST("produk/update/{id_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahProduk(@Path("id_supplier") String id_supplier,
                                        @Field("nama")String nama,
                                        @Field("alamat")String alamat,
                                        @Field("telp")String telp,
                                        @Field("modified_by")String modified_by);

    @POST("produk/delete/{id_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusProduk(@Path("id_produk") String id_supplier,
                                         @Field("delete_by")String delete_by);
//    @DELETE("Profile/deleteProfile/{id}")
//    Call<String> deleteUser(@Path("id") String id);


    //KELOLA LAYANAN
    @POST("layanan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahLayanan(@Field("nama")String nama,
                                             @Field("created_by")String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("layanan")
    Call<GetLayanan> getAllLayananAktif();

    @POST("layanan/update/{id_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahLayanan(@Path("id_layanan") String id_layanan,
                                           @Field("nama")String nama,
                                           @Field("modified_by")String modified_by);

    @POST("layanan/delete/{id_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusLayanan(@Path("id_layanan") String id_layanan,
                                            @Field("delete_by")String delete_by);

    @DELETE("layanan/{id}")
    Call<PostUpdateDelete> hapusPermanentLayanan(@Path("id_layanan") String id_layanan);

    //KELOLA HARGA LAYANAN
    @POST("hargaLayanan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahHargaLayanan(@Field("id_layanan")String id_layanan,
                                              @Field("id_ukuran_hewan") String id_ukuran_hewan,
                                              @Field("harga") String harga,
                                              @Field("created_by") String created_by);

//    @GET("viewProfile.php/{email}")
    //   Call<UserDAO> getUser(@Path("email") String email);

    @GET("hargaLayanan")
    Call<GetHargaLayanan> getAllHargaLayananAktif();

    @GET("hargaLayanan/searchByIdLayanan/{id_layanan}")
    Call<GetHargaLayanan> searchHargaLayananByIdLayanan(@Path("id_layanan")String id_layanan);

    @POST("hargaLayanan/update/{id_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahHargaLayanan(@Path("id_harga_layanan") String id_harga_layanan,
                                       @Field("harga")String harga,
                                       @Field("modified_by")String modified_by);

    @POST("hargaLayanan/delete/{id_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusHargaLayanan(@Path("id_harga_layanan") String id_harga_layanan,
                                        @Field("delete_by")String delete_by);

    @DELETE("hargaLayanan/{id}")
    Call<PostUpdateDelete> hapusPermanentHargaLayanan(@Path("id_layanan") String id_layanan);
}
