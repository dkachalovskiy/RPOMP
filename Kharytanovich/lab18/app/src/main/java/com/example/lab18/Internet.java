package com.example.lab18;

import static com.example.lab18.R.id.*;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Internet extends AppCompatActivity {

    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        url = findViewById(R.id.urla);
    }

    public void onClActionView(View view) {
        String urlText = url.getText().toString();
        if (!urlText.startsWith("http://") && !urlText.startsWith("https://"))
            urlText = "http://" + urlText;
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
            startActivity(browserIntent);
        } catch (RuntimeException ignored) {

        }
    }

    public void onActivity_Main(View view) {
        WebView browser = findViewById(webBrowser);
        String urlText = url.getText().toString();
        if (!urlText.startsWith("http://") && !urlText.startsWith("https://"))
            urlText = "http://" + urlText;
        browser.loadUrl(urlText);
    }

}