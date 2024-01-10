package com.interview.interviewtask.ui.home.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.interview.interviewtask.R;
import com.interview.interviewtask.databinding.ActivityHomeBinding;
import com.interview.interviewtask.retrofit.Api;
import com.interview.interviewtask.ui.detail.view.ActivityDetail;
import com.interview.interviewtask.ui.home.adapter.AdapterList;
import com.interview.interviewtask.ui.home.response.ListResposne;
import com.interview.interviewtask.utils.customProgressDialog.CustomProgressDialog;
import com.interview.interviewtask.utils.methods.GlobalMethods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {
    public Context context;
    public ActivityHomeBinding binding;
    public AdapterList adapterList;
    public AdapterList.ItemCLickListner itemCLickListner;
    public CustomProgressDialog progressDialog;
    public String url;
    public ImageView tick;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        context = ActivityHome.this;

        init();
    }

    public void init(){



        itemCLickListner = (title, id, download, type, pdf) -> {

            url = pdf;


            if (type.equals("navigation")){
                Intent intent = new Intent(context, ActivityDetail.class);
                intent.putExtra("title",title);
                intent.putExtra("url",pdf);
                startActivity(intent);
            }else {
                tick = download;
                callMultiplePermissions();
                downloadPdf(pdf);

            }

        };

        List();



    }

    private void downloadPdf(String pdfUrl) {

        new DownloadPdfTask(context).execute(pdfUrl);
    }

    public void List(){
        if (GlobalMethods.isNetworkAvailable(context)) {
            progressDialog = new CustomProgressDialog(context);
            progressDialog.showProgressDialog();

            Api.getClient().getList().enqueue(new Callback<List<ListResposne>>() {
                @Override
                public void onResponse(@NonNull Call<List<ListResposne>> call, @NonNull Response<List<ListResposne>> response) {
                    Log.d("NotesResponse", "NotesResponse:" + new Gson().toJson(response.body()));
                    progressDialog.dismiss();
                    if (response.isSuccessful()){

                        List<ListResposne> responseLists = response.body();
                        adapterList = new AdapterList(context,responseLists,itemCLickListner);
                        binding.lists.setHasFixedSize(true);
                        binding.lists.setLayoutManager(new LinearLayoutManager(context));
                        binding.lists.setAdapter(adapterList);


                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<ListResposne>> call, @NonNull Throwable t) {

                    progressDialog.dismiss();
                }
            });

        }
        else {

            GlobalMethods.Toast(context,"No Internet Connection");

        }

    }

    public static class DownloadPdfTask extends AsyncTask<String, Void, Long> {

        private Context context;

        public DownloadPdfTask(Context context) {
            this.context = context;
        }

        @Override
        protected Long doInBackground(String... params) {
            String pdfUrl = params[0];

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
            request.setTitle("Downloading PDF");
            request.setDescription("PDF file is being downloaded");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "downloaded_pdf.pdf");

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            return downloadManager.enqueue(request);
        }

        @Override
        protected void onPostExecute(Long downloadId) {
            if (downloadId != -1) {
                GlobalMethods.Toast(context,"PDF download started");

            } else {
                GlobalMethods.Toast(context,"Failed to start PDF download");
            }
        }
    }



    private void callMultiplePermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.READ_MEDIA_IMAGES))
            permissionsNeeded.add("READ_MEDIA_IMAGES");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    // Pre-Marshmallow
                }

                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        }

    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        } else {
            // Pre-Marshmallow
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                int i = 0;
                // Fill with results
                for (i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (
                        perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                ){

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
