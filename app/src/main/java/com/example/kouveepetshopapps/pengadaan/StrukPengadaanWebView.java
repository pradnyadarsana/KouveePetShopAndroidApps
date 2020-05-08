package com.example.kouveepetshopapps.pengadaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.kouveepetshopapps.R;

import java.io.File;

public class StrukPengadaanWebView extends AppCompatActivity {
    private String id_pengadaan_produk=null;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_struk_pengadaan);

        id_pengadaan_produk = getIntent().getStringExtra("id_pengadaan_produk");

        webView = findViewById(R.id.webViewStrukPengadaan);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Tiga baris di bawah ini agar laman yang dimuat dapat
        // melakukan zoom.
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl("http://kouveepetshopapi.smithdev.xyz/index.php/pengadaanProduk/cetakStruk/"+id_pengadaan_produk);

        //handle downloading
        webView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType("application/pdf");
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading File...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File...", Toast.LENGTH_LONG).show();
            }
        });
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            onBackPressed();
            openFile(ctxt, id_pengadaan_produk+".pdf");
        }
    };

    @Override
    protected void onPause() {
        try {
            if(onComplete!=null){
                unregisterReceiver(onComplete);
            }
        }catch (Exception e){
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(StrukPengadaanWebView.this, PengadaanActivity.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        back.putExtra("firstView", "pesanan diproses");
        startActivity(back);
        super.onBackPressed();
    }

    protected void openFile(Context ctxt, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +
                fileName);
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //pdfOpenintent.setDataAndType(path, "application/pdf");
        Uri pdfUri = FileProvider.getUriForFile(
                ctxt,
                ctxt.getApplicationContext()
                        .getPackageName() + ".provider", file);
        pdfOpenintent.setDataAndType(pdfUri, "application/pdf");
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            ctxt.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
        }
    }
}
