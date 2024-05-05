package com.example.lab_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ntv.ifmo.ru/file/journal/").build();

    APIService service = retrofit.create(APIService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button btnDownload = findViewById(R.id.btnDownload);
        Button btnWatch = findViewById(R.id.btnWatch);
        Button btnDelete = findViewById(R.id.btnDelete);
        File appSpecificInternalStorageDirectory = MainActivity.this.getFilesDir();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(){
                    public void run(){
                        String id = editText.getText().toString();

                        if(id.isEmpty()){
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnDelete.setEnabled(false);
                                btnDownload.setEnabled(false);
                                btnWatch.setEnabled(false);
                                
                            }
                        });

                            service.downloadFile(id+".pdf").enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.e("RESPONSE 1", response.toString());
                                    ResponseBody body = response.body();

                                    boolean isExist = response.headers().get("content-type").equals("application/pdf");

                                    if(isExist) {
                                        try {

                                            File f = new File(appSpecificInternalStorageDirectory, "myDownloader");
                                            if (!f.exists()) {
                                                boolean res = f.mkdirs();
                                            }

                                            File file = new File(appSpecificInternalStorageDirectory + "/myDownloader", id + ".pdf");

                                            FileOutputStream fIS = new FileOutputStream(file);
                                            fIS.write(body.bytes());
                                            fIS.close();
                                        } catch (IOException e) {
                                            runOnUiThread(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  btnDelete.setEnabled(false);
                                                                  btnDownload.setEnabled(true);
                                                                  btnWatch.setEnabled(false);

                                                              }
                                                          });
                                            Log.e("Exception", "File write failed: " + e.toString());
                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                btnDelete.setEnabled(true);
                                                btnDownload.setEnabled(true);
                                                btnWatch.setEnabled(true);

                                            }});
                                    }
                                    else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                btnDelete.setEnabled(false);
                                                btnDownload.setEnabled(true);
                                                btnWatch.setEnabled(false);

                                            }});
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }
                };

                thread.start();
            }
        });

        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editText.getText().toString();
                File file = new File(appSpecificInternalStorageDirectory + "/myDownloader", id+".pdf");

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDelete.setEnabled(false);
                btnWatch.setEnabled(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("settings2", MODE_PRIVATE);
        boolean isPopup = settings.getBoolean("popup",true);

        if(isPopup) {
            Dialog dialog = new Dialog();
            dialog.show(getSupportFragmentManager(), "aa");
        }
    }
}