package com.example.lab18;

import static com.example.lab18.R.id.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Internet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
    }
    public void onClActionView(View view) {
        WebView browser = new WebView(this);
        browser.loadUrl("yandex.ru");
    }
    public void onActivity_Main(View view) {
        WebView browser = (WebView) findViewById(webBrowser);
        browser.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view1, String url){
                view1.loadUrl(url);
                return true;
            }
        });
        TextView t;
        t =(TextView) findViewById(R.id.urla);
        browser.loadUrl(t.getText().toString());
    }

}