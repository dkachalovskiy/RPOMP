package com.example.ratingimagesberdnikova;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import android.Manifest;

public class MainActivity extends AppCompatActivity
{
    EditText editText;
    Button button;
    private static final String BASE_URL = "https://www.google.com/search?tbm=isch&q=";
    private ImageButton imageButLike;
    private ImageButton imageButDislike;
    private Button butBack;
    private Button butNext;
    private ImageButton imageButDownload;
    private ImageButton imageButLink;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editMessageForSearch);
        button = findViewById(R.id.butSearch);

        imageButLike = findViewById(R.id.imageButtonLike);
        imageButLike.setEnabled(false);

        imageButDislike = findViewById(R.id.imageButtonDislike);
        imageButDislike.setEnabled(false);

        butBack = findViewById(R.id.buttonBack);
        butBack.setEnabled(false);

        butNext = findViewById(R.id.buttonNext);
        butNext.setEnabled(false);

        imageButDownload = findViewById(R.id.imageButtonDownload);
        imageButDownload.setEnabled(false);

        imageButLink = findViewById(R.id.imageButtonLink);
        imageButLink.setEnabled(false);
    }

    public void GetSearchText(View view)
    {
        EditText editText = findViewById(R.id.editMessageForSearch);
        imageButLink.setEnabled(false);
        imageButDownload.setEnabled(false);
        imageButDislike.setEnabled(false);
        imageButLike.setEnabled(false);
        butNext.setEnabled(false);
        butBack.setEnabled(false);

        String userInput = editText.getText().toString();
        Log.d("MainActivity", "User request: " + userInput);
        if (!userInput.isEmpty())
        {
            String searchUrl = BASE_URL + userInput;
            Log.d("MainActivity", "Created URL: " + searchUrl);
            curIndex = 0;
            imageUrls.clear();
            pageUrls.clear();
            new UploadImageFromInternet().execute(searchUrl);
        } else {
            android.widget.Toast.makeText(MainActivity.this, "Enter your request", android.widget.Toast.LENGTH_LONG).show();
            ImageView imageView = findViewById(R.id.imageViewDefaultDog);
            Picasso.get().load(R.drawable.back).into(imageView);
        }
    }

    List<String> imageUrls = new ArrayList<>();
    List<String> pageUrls = new ArrayList<>();
    private class UploadImageFromInternet extends AsyncTask<String, Void, Document>
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
                            String absoluteUrl = url.toString() + "?size=original";
                            imageUrls.add(absoluteUrl);
                            String pageUrl = Objects.requireNonNull(imgElement.parents().select("a[href]").first()).attr("href");
                            URL baseUrl = new URL(new URL(doc.baseUri()), pageUrl);
                            String absolutePageUrl = baseUrl.toString();
                            pageUrls.add(absolutePageUrl);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.d("MainActivity", "Image URLs: " + imageUrls);
                Log.d("MainActivity", "Page URLs: " + pageUrls);

                UploadImageFromInternet uploader = new UploadImageFromInternet();
                LoadImageFromUrl(imageUrls, pageUrls);
            } else {
                Log.d("MainActivity", "Doc is null");
            }
        }
    }
    private int curIndex = 0;
    private Pair<String, String> loadImageFromUrl(List<String> imageUrls, List<String> pageUrls, int index)
    {
        String imageUrl = null;
        String pageUrl = null;
        ImageView imageView = findViewById(R.id.imageViewDefaultDog);
        if (index >= 0 && index < imageUrls.size())
        {
            imageUrl = imageUrls.get(index);
            pageUrl = pageUrls.get(index);
            Picasso.get().load(imageUrl).fit().into(imageView);
        }
        curIndex = index;
        imageButLike.setEnabled(true);
        imageButDislike.setEnabled(true);
        butBack.setEnabled(true);
        butNext.setEnabled(true);
        imageButDownload.setEnabled(true);
        imageButLink.setEnabled(true);
        return new Pair<>(imageUrl, pageUrl);
    }
    public void LoadImageFromUrl(List<String> imageUrls, List<String> pageUrls)
    {
        loadImageFromUrl(imageUrls, pageUrls, curIndex);
        Log.d("MainActivity", "Index of the image: "  + curIndex);
    }

    public void clickLike(View view)
    {
        Toast.makeText(MainActivity.this, "Liked (Berdnikova)", Toast.LENGTH_SHORT).show();
        imageButLike.setEnabled(false);
        new Handler().postDelayed(() -> {
            imageButLike.setEnabled(true);
        }, 3500);
    }

    public void clickDisLike(View view)
    {
        Toast.makeText(MainActivity.this, "Disliked (Berdnikova)", Toast.LENGTH_SHORT).show();
        imageButDislike.setEnabled(false);
        new Handler().postDelayed(() -> {
            imageButDislike.setEnabled(true);
        }, 3500);
    }

    public void clickBack(View view)
    {
        if (curIndex == 0)
        {
            curIndex = imageUrls.size() - 1;
        } else {
            curIndex--;
        }
        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, curIndex);
        String imageUrl = result.first;
        String pageUrl = result.second;
        Log.d("MainActivity", "URL of image: " + imageUrl);
        Log.d("MainActivity", "URL of page: " + pageUrl);

        butBack.setEnabled(false);
        new Handler().postDelayed(() -> {
            butBack.setEnabled(true);
        }, 1000);
    }

    public void clickNext(View view)
    {
        if (curIndex == imageUrls.size() - 1)
        {
            curIndex = 0;
        } else {
            curIndex++;
        }
        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, curIndex);
        String imageUrl = result.first;
        String pageUrl = result.second;
        Log.d("MainActivity", "URL of image: " + imageUrl);
        Log.d("MainActivity", "URL of page with image: " + pageUrl);

        butNext.setEnabled(false);
        new Handler().postDelayed(() -> {
            butNext.setEnabled(true);
        }, 1000);
    }

    public void clickDownload (View view)
    {
        imageButDownload.setEnabled(false);
        new Handler().postDelayed(() -> {
            imageButDownload.setEnabled(true);
        }, 5000);

        String ImageUrl = imageUrls.get(curIndex);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            Toast.makeText(MainActivity.this, "Give permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
            new DownloadImage().execute(ImageUrl);
        }
    }
    @SuppressLint("StaticFieldLeak")
    class DownloadImage extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                Log.e("DownloadsImage", "Malformed URL: " + e.getMessage());
                return null;
            }

            Bitmap bm = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bm = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                Log.e("DownloadsImage", "Error downloading image: " + e.getMessage());
                return null;
            }

            if (bm == null) {
                Log.e("DownloadsImage", "Bitmap is null after downloading image");
                return null;
            }

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    Log.e("DownloadsImage", "Failed to create directory");
                    return null;
                }
            }

            File imageFile = new File(path, String.valueOf(System.currentTimeMillis()) + ".png");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (FileNotFoundException e) {
                Log.e("DownloadsImage", "File not found: " + e.getMessage());
                return null;
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            MediaScannerConnection.scanFile(MainActivity.this,
                    new String[]{imageFile.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("DownloadsImage", "Path to image: " + path);
                            Log.i("DownloadsImage", "-> uri=" + uri);
                        }
                    });
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Image is saved", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickLink (View view)
    {
        imageButLink.setEnabled(false);
        new Handler().postDelayed(() -> {
            imageButLink.setEnabled(true);
        }, 5000);

        String PageURL = pageUrls.get(curIndex);
        Toast.makeText(MainActivity.this, "Go to the site", Toast.LENGTH_SHORT).show();

        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, curIndex);
        String pageUrl = result.second;
        Log.d("MainActivity", "URL of site with image: " + pageUrl);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
        startActivity(intent);

    }

}