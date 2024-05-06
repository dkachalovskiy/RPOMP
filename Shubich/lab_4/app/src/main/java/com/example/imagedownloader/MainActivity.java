package com.example.imagedownloader;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imagedownloader.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    String imageUrl = "";
    String siteLink = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickDisplayImage(View view) {
        imageView = findViewById(R.id.imageView);
        EditText editText = findViewById(R.id.simpleEditText); // Получаем ссылку на EditText

        String query = editText.getText().toString();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"q\":\"" + query + "\"}");
        Request request = new Request.Builder()
                .url("https://google.serper.dev/images")
                .method("POST", body)
                .addHeader("X-API-KEY", "48b6accb43289b72f2488bee6248e20568da00f5")
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    // Обработка JSON-ответа
                    //imageData = response.body().bytes();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray imagesArray = jsonObject.getJSONArray("images");
                        if (imagesArray.length() > 0) {
                            JSONObject firstImage = imagesArray.getJSONObject(0); // Берем первое изображение в качестве примера
                            imageUrl = firstImage.getString("imageUrl");
                            siteLink = firstImage.getString("link");

                            // Загрузка изображения по URL с помощью Picasso
                            runOnUiThread(() -> {
                                Picasso.get().load(imageUrl).into(imageView);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void goToUrl(View view) {
        if (!imageUrl.equals("")) {
            String url = siteLink;
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }

    public void onClickDownloadImage(View view) {

        imageView = findViewById(R.id.imageView);
        EditText editText = findViewById(R.id.simpleEditText); // Получаем ссылку на EditText

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imageUrl)
                .get()
                .addHeader("X-API-KEY", "48b6accb43289b72f2488bee6248e20568da00f5")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] imageData = response.body().bytes();
                    saveImageToStorage(imageData);
                }
            }

        });


    }
    private void saveImageToStorage(byte[] imageData) {
        try {
            // Создание папки в общей области хранения (например, в Downloads)
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // Создание файла для сохранения изображения
            File file = new File(directory, "image" + Math.random() + ".jpg" );
            // Запись данных изображения в файл
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(imageData);
            outputStream.close();

            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Image was save in Downloads", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Image wasnt save in Downloads", Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void onClickLike(View view) {
        if (!imageUrl.equals("")) {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Dislike it!", Toast.LENGTH_SHORT).show();
            });
        } else {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void onClickDislike(View view) {
        if (!imageUrl.equals("")) {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Like it!", Toast.LENGTH_SHORT).show();
            });
        } else {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
