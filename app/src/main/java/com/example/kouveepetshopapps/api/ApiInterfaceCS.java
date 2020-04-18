package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.model.DetailTransaksiProdukDAO;
import com.example.kouveepetshopapps.model.HewanDAO;
import com.example.kouveepetshopapps.model.UkuranHewanDAO;
import com.example.kouveepetshopapps.response.GetDetailTransaksiProduk;
import com.example.kouveepetshopapps.response.GetHewan;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetPegawai;
import com.example.kouveepetshopapps.response.GetPelanggan;
import com.example.kouveepetshopapps.response.GetTransaksiProduk;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHewan;
import com.example.kouveepetshopapps.response.SearchPegawai;
import com.example.kouveepetshopapps.response.SearchPelanggan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
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

    @GET("pelanggan")
    Call<GetPelanggan> getAllPelangganAktif();

    @GET("pelanggan/all")
    Call<GetPelanggan> getAllPelanggan();

    @GET("pelanggan/search/{id_pelanggan}")
    Call<SearchPelanggan> searchPelanggan(@Path("id_pelanggan")String id_pelanggan);

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

    //KELOLA HEWAN
    @POST("hewan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahHewan(@Field("id_pelanggan") String id_pelanggan,
                                     @Field("id_jenis_hewan") String id_jenis_hewan,
                                     @Field("nama") String nama,
                                     @Field("tanggal_lahir") String tanggal_lahir,
                                     @Field("created_by")String created_by);

    @GET("hewan")
    Call<GetHewan> getAllHewanAktif();

    @GET("hewan/all")
    Call<GetHewan> getAllHewan();

    @GET("hewan/search/{id_hewan}")
    Call<SearchHewan> searchHewan(@Path("id_hewan")String id_hewan);

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

    //AUTHENTICATION
    @POST("pegawai/auth")
    @FormUrlEncoded
    Call<SearchPegawai> authPegawai(@Field("username") String username,
                                    @Field("password") String password);

    //PEGAWAI
    @GET("pegawai/all")
    Call<GetPegawai> getAllPegawai();

    //TRANSAKSI PRODUK
    @POST("transaksiProduk")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahTransaksiProduk(@Field("id_customer_service") String id_customer_service,
                                                 @Field("id_hewan") String id_hewan,
                                                 @Field("subtotal") String subtotal,
                                                 @Field("diskon") String diskon,
                                                 @Field("total")String total,
                                                 @Field("created_by") String created_by);

    @GET("transaksiProduk/waitingPayment")
    Call<GetTransaksiProduk> getTransaksiProdukMenungguPembayaran();

    @POST("transaksiProduk/update/{id_transaksi_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahTransaksiProduk(@Path("id_transaksi_produk") String id_transaksi_produk,
                                               @Field("id_hewan") String id_hewan,
                                               @Field("subtotal") String subtotal,
                                               @Field("diskon") String diskon,
                                               @Field("total")String total,
                                               @Field("modified_by") String modified_by);

    @DELETE("transaksiProduk/{id_transaksi_produk}")
    Call<PostUpdateDelete> hapusTransaksiProduk(@Path("id_transaksi_produk") String id_transaksi_produk);

    //DETAIL TRANSAKSI PRODUK
    @POST("detailTransaksiProduk")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahDetailTransaksiProduk(@Field("id_transaksi_produk") String id_transaksi_produk,
                                                       @Field("id_produk") String id_produk,
                                                       @Field("jumlah") String jumlah,
                                                       @Field("total_harga") String total_harga,
                                                       @Field("created_by") String created_by);

    @POST("detailTransaksiProduk/insertMultiple")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahDetailTransaksiProdukMultiple(@Field("detail_transaksi_produk") String detail_transaksi_produk);

    @GET("detailTransaksiProduk")
    Call<GetDetailTransaksiProduk> getAllDetailTransaksiProduk();

    @GET("detailTransaksiProduk/getByTransactionId/{id_transaksi_produk}")
    Call<GetDetailTransaksiProduk> getDetailTransaksiProdukByIdTransaksi(@Path("id_transaksi_produk") String id_transaksi_produk);

    @POST("detailTransaksiProduk/update/{id_detail_transaksi_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahDetailTransaksiProduk(@Path("id_detail_transaksi_produk") String id_detail_transaksi_produk,
                                                     @Field("id_produk") String id_produk,
                                                     @Field("jumlah") String jumlah,
                                                     @Field("total_harga") String total_harga,
                                                     @Field("modified_by") String modified_by);

    @DELETE("detailTransaksiProduk/{id_detail_transaksi_produk}")
    Call<PostUpdateDelete> hapusDetailTransaksiProduk(@Path("id_detail_transaksi_produk") String id_detail_transaksi_produk);
}
