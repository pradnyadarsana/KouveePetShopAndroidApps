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

import com.bumptech.glide.Glide;
import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.api.ApiClient;
import com.example.kouveepetshopapps.api.ApiInterfaceAdmin;
import com.example.kouveepetshopapps.model.PegawaiDAO;
import com.example.kouveepetshopapps.model.ProdukDAO;
import com.example.kouveepetshopapps.response.PostUpdateDelete;
import com.google.gson.Gson;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProdukActivity extends AppCompatActivity {

    private EditText namaUpdate, satuanUpdate, jumlah_stokUpdate, min_stokUpdate, hargaUpdate;
    private TextView desc_gambarUpdate;
    private Button btnUpdateProduk;
    private LinearLayout btnAmbilGambar;
    private ImageView gambarUpdate;

    final int cameraCode = 99;
    final int galleryCode = 100;

    String imageUriUpdate = null;
    Bitmap bitmapImg;

    // Defining Permission codes.
    // We can give any value
    // but unique for each permission.
    //private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    int USER_PERMISSIONS_REQUEST;

    SharedPreferences loggedUser;
    PegawaiDAO admin;

    public boolean isOnlineImage=true;
    URL url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_produk);

        sharedPreferences();

        Gson gson = new Gson();
        String json = getIntent().getStringExtra("produk");
        System.out.println(json);
        final ProdukDAO produk = gson.fromJson(json, ProdukDAO.class);

        setAtribut();
        setText(produk);

        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);
            }
        });
        btnUpdateProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaUpdate.getText().toString().isEmpty()){
                    showDialog("Kolom nama produk kosong.");
                }else if (satuanUpdate.getText().toString().isEmpty()){
                    showDialog("Kolom satuan produk kosong.");
                }else if (hargaUpdate.getText().toString().isEmpty()){
                    showDialog("Kolom harga produk kosong.");
                }else if (jumlah_stokUpdate.getText().toString().isEmpty()){
                    showDialog("Kolom jumlah stok produk kosong.");
                }else if (min_stokUpdate.getText().toString().isEmpty()){
                    showDialog("Kolom minimum stok produk kosong.");
                }else if (imageUriUpdate==null){
                    showDialog("Gambar produk belum ditambahkan.");
                }else if (Integer.parseInt(hargaUpdate.getText().toString())<0){
                    showDialog("Harga produk tidak boleh kurang dari 0.");
                }else if (Integer.parseInt(jumlah_stokUpdate.getText().toString())<0){
                    showDialog("Jumlah stok produk tidak boleh kurang dari 0.");
                }else if (Integer.parseInt(min_stokUpdate.getText().toString())<0){
                    showDialog("Minimum stok produk tidak boleh kurang dari 0.");
                }else {
                    updateProduk(produk);
                }
            }
        });
    }

    private void sharedPreferences(){
        loggedUser = getSharedPreferences("logged_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loggedUser.getString("user", "missing");
        admin = gson.fromJson(json, PegawaiDAO.class);
        System.out.println(json);
    }

    private void setAtribut(){
        namaUpdate = findViewById(R.id.etNamaProdukUpdate);
        satuanUpdate = findViewById(R.id.etSatuanProdukUpdate);
        jumlah_stokUpdate = findViewById(R.id.etJumlahStokProdukUpdate);
        min_stokUpdate = findViewById(R.id.etMinStokProdukUpdate);
        hargaUpdate = findViewById(R.id.etHargaProdukUpdate);
        desc_gambarUpdate = findViewById(R.id.etGambarUpdate);
        gambarUpdate = findViewById(R.id.addGambarProdukUpdate);
        btnAmbilGambar = findViewById(R.id.layoutgambarUpdate);
        btnUpdateProduk = findViewById(R.id.btnUpdateProduk);
    }

    private void setText(ProdukDAO produk){
        namaUpdate.setText(getIntent().getStringExtra("nama"));
        satuanUpdate.setText(getIntent().getStringExtra("satuan"));


        namaUpdate.setText(produk.getNama());
        satuanUpdate.setText(produk.getSatuan());
        hargaUpdate.setText(String.valueOf(produk.getHarga()));
        jumlah_stokUpdate.setText(String.valueOf(produk.getJumlah_stok()));
        min_stokUpdate.setText(String.valueOf(produk.getMin_stok()));

        String photo_url = "http://kouveepetshopapi.smithdev.xyz/upload/produk/"+produk.getGambar();
        System.out.println(photo_url);
        Glide.with(EditProdukActivity.this).load(photo_url).into(gambarUpdate);
        imageUriUpdate = photo_url;
    }

    // Function to check and request permission
    public int checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(EditProdukActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditProdukActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(EditProdukActivity.this, "Permission already granted",
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
                Toast.makeText(EditProdukActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(EditProdukActivity.this,
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
                                imageUriUpdate = picturePath;
                                System.out.println(imageUriUpdate);
                                gambarUpdate.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void startIntent(){
        Intent back = new Intent(EditProdukActivity.this, ListProdukActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }

    public void updateProduk(ProdukDAO hasil){
        //Create a file object using file path
        File file = new File(imageUriUpdate);

        System.out.println("FILE: "+file);

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part partUpdate = MultipartBody.Part.createFormData("gambar", file.getName(), fileReqBody);
        //Create request body with text description and text media type

       // RequestBody id_produkForm = RequestBody.create(MediaType.parse("text/plain"), Integer.toString(hasil.getId_produk()));
        RequestBody namaFormUpdate = RequestBody.create(MediaType.parse("text/plain"), namaUpdate.getText().toString());
        RequestBody satuanFormUpdate = RequestBody.create(MediaType.parse("text/plain"), satuanUpdate.getText().toString());
        RequestBody jumlah_stokFormUpdate = RequestBody.create(MediaType.parse("text/plain"), jumlah_stokUpdate.getText().toString());
        RequestBody hargaFormUpdate = RequestBody.create(MediaType.parse("text/plain"), hargaUpdate.getText().toString());
        RequestBody min_stokFormUpdate = RequestBody.create(MediaType.parse("text/plain"), min_stokUpdate.getText().toString());
        RequestBody modified_byForm = RequestBody.create(MediaType.parse("text/plain"), admin.getUsername());

        ApiInterfaceAdmin apiService = ApiClient.getClient().create(ApiInterfaceAdmin.class);
        Call<PostUpdateDelete> produkDAOCall = apiService.ubahProduk(Integer.toString(hasil.getId_produk()),namaFormUpdate, satuanFormUpdate,
                jumlah_stokFormUpdate, hargaFormUpdate, min_stokFormUpdate, partUpdate, modified_byForm);

        produkDAOCall.enqueue(new Callback<PostUpdateDelete>() {
            @Override
            public void onResponse(Call<PostUpdateDelete> call, Response<PostUpdateDelete> response) {
                Toast.makeText(EditProdukActivity.this, "Sukses update produk", Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(Call<PostUpdateDelete> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EditProdukActivity.this, "Gagal update produk", Toast.LENGTH_SHORT).show();
            }
        });
}

    private void showDialog(String rules){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProdukActivity.this);

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
