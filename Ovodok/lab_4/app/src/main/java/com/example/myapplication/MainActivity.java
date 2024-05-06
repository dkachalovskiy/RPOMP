package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://www.google.com/search?tbm=isch&q=";
    private EditText editText;

    private Button searchButton;
    private ImageView imageView;
    private ImageButton likeButton, dislikeButton, downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        searchButton = findViewById(R.id.searchButton);
        imageView = findViewById(R.id.imageView);
        likeButton = findViewById(R.id.imageButtonLike);
        dislikeButton = findViewById(R.id.imageButtonDislike);
        downloadButton = findViewById(R.id.imageButtonLink);

    }
    public void onClickSearchButton(View view)
    {
        String userInput = editText.getText().toString();
        if (!userInput.isEmpty())
        {
            String searchUrl = BASE_URL + userInput;
            new UploadingImagesFromInternet().execute(searchUrl);
        }
    }

    String imageURL = "";
    String pageURL = "";

    public void onClickLikeButton(View view) {
        Toast.makeText(MainActivity.this, "Нравится", Toast.LENGTH_SHORT).show();
    }

    public void onClickDislikeButton(View view) {
        Toast.makeText(MainActivity.this, "Не нравится", Toast.LENGTH_SHORT).show();
    }

    public void onClickLinkButton(View view) {
        Pair<String, String> result = loadImageFromUrl(imageURL, pageURL);
        String pageUrl = result.second;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
        startActivity(intent);
    }

    public void onClickDownloadButton (View view)
    {

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 123);
            Toast.makeText(MainActivity.this, "Необходимо предоставить разрешение для скачивания изображений", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Скачивание изображения...", Toast.LENGTH_SHORT).show();
            new DownloadsImage().execute(imageURL);
        }
    }
    @SuppressLint("StaticFieldLeak")
    class DownloadsImage extends AsyncTask<String, Void,Void>
    {
        protected Void doInBackground(String... strings)
        {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                assert url != null;
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            if(!path.exists())
            {
                path.mkdirs();
            }

            File imageFile = new File(path, String.valueOf(System.currentTimeMillis())+".png");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                assert bm != null;
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                assert out != null;
                out.flush();
                out.close();

                MediaScannerConnection.scanFile(MainActivity.this,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener()
                {
                    public void onScanCompleted(String imageFile, Uri uri)
                    {
                        Log.i("ExternalStorage", "Местонахождение: " + imageFile);
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch(Exception ignored) {
            }
            return null;
        }

        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Изображение сохранено в Загрузки", Toast.LENGTH_SHORT).show();
        }
    }


    private class UploadingImagesFromInternet extends AsyncTask<String, Void, Document>
    {
        protected Document doInBackground(String... params)
        {
            try {
                String searchUrl = params[0];
                String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
                return Jsoup.connect(searchUrl).userAgent(userAgent).referrer("https://www.google.com/").get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(Document doc)
        {
            if (doc != null)
            {
                Elements elementsImg = doc.select("img[src]");
                for (Element imgElement : elementsImg)
                {
                    String imgUrl = imgElement.attr("src");
                    if (!imgUrl.endsWith(".gif"))
                    {
                        try {
                            URL url = new URL(new URL(doc.baseUri()), imgUrl);
                            imageURL = url.toString() + "?size=original";

                            String pageUrl = Objects.requireNonNull(imgElement.parents().select("a[href]").first()).attr("href");
                            URL baseUrl = new URL(new URL(doc.baseUri()), pageUrl);
                            pageURL = baseUrl.toString();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

                UploadingImagesFromInternet uploader = new UploadingImagesFromInternet();
                loadImageFromUrl(imageURL, pageURL);
            }
        }
    }
    private Pair<String, String> loadImageFromUrl(String imageURL, String pageURL)
    {
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get().load(imageURL).into(imageView);

        return new Pair<>(imageURL, pageURL);
    }

}
