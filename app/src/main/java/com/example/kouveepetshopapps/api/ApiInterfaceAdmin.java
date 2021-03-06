package com.example.kouveepetshopapps.api;

import com.example.kouveepetshopapps.model.HargaLayananDAO;
import com.example.kouveepetshopapps.response.GetDetailPengadaan;
import com.example.kouveepetshopapps.response.GetHargaLayanan;
import com.example.kouveepetshopapps.response.GetJenisHewan;
import com.example.kouveepetshopapps.response.GetLayanan;
import com.example.kouveepetshopapps.response.GetNotifikasi;
import com.example.kouveepetshopapps.response.GetPengadaanProduk;
import com.example.kouveepetshopapps.response.GetProduk;
import com.example.kouveepetshopapps.response.GetSupplier;
import com.example.kouveepetshopapps.response.GetTransaksiProduk;
import com.example.kouveepetshopapps.response.GetUkuranHewan;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.example.kouveepetshopapps.response.SearchHargaLayanan;
import com.example.kouveepetshopapps.response.SearchJenisHewan;
import com.example.kouveepetshopapps.response.SearchLayanan;
import com.example.kouveepetshopapps.response.SearchPengadaanProduk;
import com.example.kouveepetshopapps.response.SearchProduk;
import com.example.kouveepetshopapps.response.SearchSupplier;
import com.example.kouveepetshopapps.response.SearchUkuranHewan;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @GET("supplier/search/{id_supplier}")
    Call<SearchSupplier> searchSupplier(@Path("id_supplier") String id_supplier);

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

    @GET("JenisHewan/all")
    Call<GetJenisHewan> getAllJenisHewan();

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

    @GET("UkuranHewan")
    Call<GetUkuranHewan> getAllUkuranHewanAktif();

    @GET("UkuranHewan/all")
    Call<GetUkuranHewan> getAllUkuranHewan();

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

    @GET("produk/all")
    Call<GetProduk> getAllProduk();

    @GET("produk/underMinStok")
    Call<GetProduk> getProdukUnderMinStok();

    @GET("produk/search/{id_produk}")
    Call<SearchProduk> searchProduk(@Path("id_produk") String id_produk);

    @POST("produk/update/{id_produk}")
    @Multipart
    Call<PostUpdateDelete> ubahProduk(@Path("id_produk") String id_produk,
                                      @Part("nama") RequestBody nama,
                                      @Part("satuan")RequestBody satuan,
                                      @Part("jumlah_stok")RequestBody jumlah_stok,
                                      @Part("harga")RequestBody harga,
                                      @Part("min_stok") RequestBody min_stok,
                                      @Part MultipartBody.Part gambar,
                                      @Part("modified_by")RequestBody modified_by);

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

    @GET("layanan/all")
    Call<GetLayanan> getAllLayanan();

    @GET("layanan/search/{id_layanan}")
    Call<SearchLayanan> searchLayanan(@Path("id_layanan") String id_layanan);

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

    @GET("hargaLayanan/search/{id_harga_layanan}")
    Call<SearchHargaLayanan> searchHargaLayanan(@Path("id_harga_layanan") String id_harga_layanan);

    @GET("hargaLayanan/searchByIdLayanan/{id_layanan}")
    Call<GetHargaLayanan> searchHargaLayananByIdLayanan(@Path("id_layanan")String id_layanan);

    @POST("hargaLayanan/update/{id_harga_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahHargaLayanan(@Path("id_harga_layanan") String id_harga_layanan,
                                            @Field("id_layanan")String id_layanan,
                                            @Field("id_ukuran_hewan")String id_ukuran_hewan,
                                            @Field("harga")String harga,
                                            @Field("modified_by")String modified_by);

    @POST("hargaLayanan/delete/{id_layanan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusHargaLayanan(@Path("id_harga_layanan") String id_harga_layanan,
                                        @Field("delete_by")String delete_by);

    @DELETE("hargaLayanan/{id}")
    Call<PostUpdateDelete> hapusPermanentHargaLayanan(@Path("id_layanan") String id_layanan);


    //PENGADAAN PRODUK
    @POST("pengadaanProduk/insertAndGet")
    @FormUrlEncoded
    Call<SearchPengadaanProduk> tambahPengadaanProduk(@Field("id_supplier") String id_supplier,
                                                      @Field("total")String total,
                                                      @Field("created_by") String created_by);

    @GET("PengadaanProduk/unconfirmed")
    Call<GetPengadaanProduk> getPengadaanProdukMenungguKonfirmasi();

    @GET("pengadaanProduk/processed")
    Call<GetPengadaanProduk> getPengadaanProdukPesananDiproses();

    @GET("pengadaanProduk/completed")
    Call<GetPengadaanProduk> getPengadaanProdukPesananSelesai();

    @GET("pengadaanProduk/search/{id_pengadaan_produk}")
    Call<SearchPengadaanProduk> searchPengadaanProduk(@Path("id_pengadaan_produk") String id_pengadaan_produk);

    @GET("pengadaanProduk/cetakStruk/{id_pengadaan_produk}")
    @Streaming
    Call<ResponseBody> cetakPengadaanProduk(@Path("id_pengadaan_produk") String id_pengadaan_produk);

    @POST("pengadaanProduk/update/{id_pengadaan_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahPengadaanProduk(@Path("id_pengadaan_produk") String id_pengadaan_produk,
                                                @Field("id_supplier") String id_supplier,
                                                @Field("total")String total,
                                                @Field("modified_by") String modified_by);

    @POST("pengadaanProduk/updateStatusToProses/{id_pengadaan_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahStatusPengadaanToProses(@Path("id_pengadaan_produk") String id_pengadaan_produk,
                                                       @Field("modified_by") String modified_by);

    @POST("pengadaanProduk/updateStatusToSelesai/{id_pengadaan_produk}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahStatusPengadaanToSelesai(@Path("id_pengadaan_produk") String id_pengadaan_produk,
                                                       @Field("modified_by") String modified_by);

    @DELETE("pengadaanProduk/{id_pengadaan_produk}")
    Call<PostUpdateDelete> hapusPengadaanProduk(@Path("id_pengadaan_produk") String id_pengadaan_produk);


    //DETAIL PENGADAAN PRODUK
    @POST("detailPengadaan")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahDetailPengadaan(@Field("id_pengadaan_produk") String id_pengadaan_produk,
                                                 @Field("id_produk") String id_produk,
                                                 @Field("jumlah") String jumlah,
                                                 @Field("harga") String harga,
                                                 @Field("total_harga") String total_harga,
                                                 @Field("created_by") String created_by);

    @POST("detailPengadaan/insertMultiple")
    @FormUrlEncoded
    Call<PostUpdateDelete> tambahDetailPengadaanMultiple(@Field("detail_pengadaan") String detail_pengadaan);

    @GET("detailPengadaan")
    Call<GetDetailPengadaan> getAllDetailPengadaan();

    @GET("detailPengadaan/getByIdPengadaan/{id_pengadaan_produk}")
    Call<GetDetailPengadaan> getDetailPengadaanByIdPengadaan(@Path("id_pengadaan_produk") String id_pengadaan_produk);

    @POST("detailPengadaan/update/{id_detail_pengadaan}")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahDetailPengadaan(@Path("id_detail_pengadaan") String id_detail_pengadaan,
                                               @Field("id_produk") String id_produk,
                                               @Field("jumlah") String jumlah,
                                               @Field("harga") String harga,
                                               @Field("total_harga") String total_harga,
                                               @Field("modified_by") String modified_by);

    @POST("detailPengadaan/updateMultiple")
    @FormUrlEncoded
    Call<PostUpdateDelete> ubahDetailPengadaanMultiple(@Field("detail_pengadaan") String detail_pengadaan);

    @DELETE("detailPengadaan/{id_detail_pengadaan}")
    Call<PostUpdateDelete> hapusDetailPengadaan(@Path("id_detail_pengadaan") String id_detail_pengadaan);

    @POST("detailPengadaan/deleteMultiple")
    @FormUrlEncoded
    Call<PostUpdateDelete> hapusDetailPengadaanMultiple(@Field("id_detail_pengadaan") String id_detail_pengadaan);

    //KELOLA NOTIFIKASI
    @GET("Notifikasi/newOrderAsc")
    Call<GetNotifikasi> getAllNotifikasiBelumTerbacaAsc();

    @GET("Notifikasi/opened")
    Call<GetNotifikasi> getAllNotifikasiTerbaca();

    @GET("Notifikasi/allOrderDesc")
    Call<GetNotifikasi> getAllNotifikasiDesc();

//    @GET("Notifikasi/search/{id_notifikasi}")
//    Call<SearchUkuranHewan> searchUkuran(@Path("id_ukuran_hewan")String id_ukuran_hewan);

    @POST("Notifikasi/updateStatus/{id_notifikasi}")
    Call<PostUpdateDelete> ubahStatusNotifTerbaca(@Path("id_notifikasi") String id_notifikasi);
}
