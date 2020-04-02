package com.example.kouveepetshopapps.produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahProdukActivity extends AppCompatActivity {
    private EditText nama, satuan, jumlah_stok, min_stok, harga;
    private TextView desc_gambar;
    private ImageView gambar;
    private Button btnTambahProduk;
    private LinearLayout btnAmbilGambar;

    final int cameraCode = 99;
    final int galleryCode = 100;

    String imageUri = null;
    Bitmap bitmapImg;

    // Defining Permission codes.
    // We can give any value
    // but unique for each permission.
    //private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    int USER_PERMISSIONS_REQUEST;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);

        nama = findViewById(R.id.etNamaProduk);
        satuan = findViewById(R.id.etSatuanProduk);
        jumlah_stok = findViewById(R.id.etJumlahStokProduk);
        min_stok = findViewById(R.id.etMinStokProduk);
        harga = findViewById(R.id.etHargaProduk);
        desc_gambar = findViewById(R.id.etGambar);
        gambar = findViewById(R.id.addGambarProduk);
        btnAmbilGambar = findViewById(R.id.layoutgambar);

        btnTambahProduk = findViewById(R.id.btnTambahProduk);

        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);
            }
        });
        btnTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nama.getText().toString().isEmpty()){
                    showDialog("Kolom nama produk kosong.");
                }else if (satuan.getText().toString().isEmpty()){
                    showDialog("Kolom satuan produk kosong.");
                }else if (harga.getText().toString().isEmpty()){
                    showDialog("Kolom harga produk kosong.");
                }else if (jumlah_stok.getText().toString().isEmpty()){
                    showDialog("Kolom jumlah stok produk kosong.");
                }else if (min_stok.getText().toString().isEmpty()){
                    showDialog("Kolom minimum stok produk kosong.");
                }else if (imageUri==null){
                    showDialog("Gambar produk belum ditambahkan.");
                }else if (Integer.parseInt(harga.getText().toString())<0){
                    showDialog("Harga produk tidak boleh kurang dari 0.");
                }else if (Integer.parseInt(jumlah_stok.getText().toString())<0){
                    showDialog("Jumlah stok produk tidak boleh kurang dari 0.");
                }else if (Integer.parseInt(min_stok.getText().toString())<0){
                    showDialog("Minimum stok produk tidak boleh kurang dari 0.");
                }else {
                    tambahProduk();
                }
            }
        });
    }

    // Function to check and request permission
    public int checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(TambahProdukActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(TambahProdukActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(TambahProdukActivity.this, "Permission already granted",
                            Toast.LENGTH_SHORT).show();
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, 1);

        }
        return requestCode;
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(TambahProdukActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(TambahProdukActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        gambar.setImageBitmap(selectedImage);
//                    }
//
//                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageUri = picturePath;
                                System.out.println(imageUri);
                                gambar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void startIntent(){
        Intent back = new Intent(TambahProdukActivity.this, ListProdukActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void tambahProduk(){
        //Create a file object using file path
        File file = new File(imageUri);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody namaForm = RequestBody.create(MediaType.parse("text/plain"), nama.getText().toString());
        RequestBody satuanForm = RequestBody.create(MediaType.parse("text/plain"), satuan.getText().toString());
        RequestBody jumlah_stokForm = RequestBody.create(MediaType.parse("text/plain"), jumlah_stok.getText().toString());
        RequestBody hargaForm = RequestBody.create(MediaType.parse("text/plain"), harga.getText().toString());
        RequestBody min_stokForm = RequestBody.create(MediaType.parse("text/plain"), min_stok.getText().toString());
        RequestBody created_byForm = RequestBody.create(MediaType.parse("text/plain"), admin.getUsername());

        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> produkDAOCall = apiService.tambahProduk(namaForm, satuanForm,
                jumlah_stokForm, hargaForm, min_stokForm, part, created_byForm);

        produkDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(TambahProdukActivity.this, "Sukses menambahkan produk", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TambahProdukActivity.this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TambahProdukActivity.this);

        // set title dialog
        alertDialogBuilder.setTitle("Isi form dengan benar!");
        alertDialogBuilder.setMessage(rules);

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setCancelable(false)
                .setPositiveButton("SIAP!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // close dialog
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
