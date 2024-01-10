package com.interview.interviewtask.ui.detail.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.interview.interviewtask.R;
import com.interview.interviewtask.databinding.ActivityDetailBinding;
import com.interview.interviewtask.ui.home.view.ActivityHome;
import com.interview.interviewtask.utils.customProgressDialog.CustomProgressDialog;
import com.interview.interviewtask.utils.methods.GlobalMethods;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityDetail extends AppCompatActivity {

    public Context context;
    public ActivityDetailBinding binding;
    public String urlpdf,title;
    public WebSettings webSettings;
    public CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = ActivityDetail.this;

        init();
    }

    public void init(){


        urlpdf = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        binding.title.setText(title);
        webSettings = binding.pdfView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        loadPdf();


    }

    public void loadPdf(){
        progressDialog = new CustomProgressDialog(context);
        progressDialog.showProgressDialog();


        binding.pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + urlpdf);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        binding.pdfView.setWebViewClient(new WebViewClient());

        binding.pdfView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                progressDialog.dismiss();

            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                progressDialog.dismiss();
                GlobalMethods.Toast(context,"please try again...");

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, ActivityHome.class));
    }
}