package ru.startandroid.develop.intents;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        // Очистка кэша WebView
        webView.clearCache(true);
        webView.clearHistory();

        webView.loadUrl("https://www.google.com"); // Замените URL на свой адрес веб-страницы
    }
}