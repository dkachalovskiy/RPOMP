package com.example.lab18;

import androidx.appcompat.app.AppCompatActivity;
//android:usesCleartextTraffic="true"
//<uses-permission android:name="android.permission.INTERNET" />
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public String strText;
    private static final int CAMERA_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClImageView(View view) {
        Intent intent = new Intent(this, ViewResourceActivity.class);
        intent.putExtra("fileName", strText);
        startActivity(intent);
    }

    public void onClCall(View view) {
        Intent intent = new Intent(this, Call.class);
        intent.putExtra("fileName", strText);
        startActivity(intent);
    }

    public void onClInternet(View view) {
        Intent intent = new Intent(this, Internet.class);
        intent.putExtra("fileName", strText);
        startActivity(intent);
    }






}