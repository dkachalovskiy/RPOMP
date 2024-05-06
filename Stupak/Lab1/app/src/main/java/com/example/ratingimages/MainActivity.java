package com.example.ratingimages;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import android.Manifest;

public class MainActivity extends AppCompatActivity
{
    EditText editText;
    Button button;
    private static final String BASE_URL = "https://www.google.com/search?tbm=isch&q=";
    private Button buttonLike;
    private Button buttonDislike;
    private Button buttonBack;
    private Button buttonNext;
    private ImageButton buttonDownload;
    private ImageButton buttonInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editMessageForSearch);
        button = findViewById(R.id.butSearch);

        buttonLike = findViewById(R.id.buttonLike);
        buttonLike.setEnabled(false); // Выключаем кнопку "Лайк" при запуске приложения

        buttonDislike = findViewById(R.id.buttonDislike);
        buttonDislike.setEnabled(false);   // Выключаем кнопку "Дизлайк" при запуске приложения

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setEnabled(false); // Выключаем кнопку "Предыдущая" при запуске приложения

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false); // Выключаем кнопку "Следующая" при запуске приложения

        buttonDownload = findViewById(R.id.buttonDownload);
        buttonDownload.setEnabled(false); // Выключаем кнопку "Скачать" при запуске приложения

        buttonInternet = findViewById(R.id.buttonInternet);
        buttonInternet.setEnabled(false); // Выключаем кнопку "Перейти к источнику" при запуске приложения
        
    }

    public void GetSearchText(View view)
    {
        EditText editText = findViewById(R.id.editMessageForSearch);
        buttonInternet.setEnabled(false);
        buttonDownload.setEnabled(false);
        buttonDislike.setEnabled(false);
        buttonLike.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonBack.setEnabled(false);

        String userInput = editText.getText().toString();
        Log.d("MainActivity", "Запрос пользователя: " + userInput);
        if (!userInput.isEmpty())
        {
            String searchUrl = BASE_URL + userInput;
            Log.d("MainActivity", "Созданный URL: " + searchUrl);
            currentIndex = 0;
            imageUrls.clear();
            pageUrls.clear();

            new UploadingImagesFromInternet().execute(searchUrl);

        } else {
            // Показываем сообщение об ошибке
            android.widget.Toast.makeText(MainActivity.this, "Введите текстовый запрос", android.widget.Toast.LENGTH_LONG).show();

            // Загрузка дефолтной фотографии
            ImageView imageView = findViewById(R.id.imageViewDefaultDog);
        }
    }

    // поиск изображений по запросу
    List<String> imageUrls = new ArrayList<>();
    List<String> pageUrls = new ArrayList<>();
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
                    // Проверяем, не заканчивается ли ссылка расширением .gif
                    if (!imgUrl.endsWith(".gif"))
                    {
                        // Преобразуем относительный URL в абсолютный
                        try {
                            URL url = new URL(new URL(doc.baseUri()), imgUrl);
                            String absoluteUrl = url.toString() + "?size=original";
                            imageUrls.add(absoluteUrl);

                            // Получаем ссылку на страницу с изображением
                            String pageUrl = Objects.requireNonNull(imgElement.parents().select("a[href]").first()).attr("href");

                            URL baseUrl = new URL(new URL(doc.baseUri()), pageUrl);
                            String absolutePageUrl = baseUrl.toString();
                            pageUrls.add(absolutePageUrl);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Выводим массивы абсолютных ссылок на изображения и ссылок на страницы с изображениями
                Log.d("MainActivity", "Image URLs: " + imageUrls);
                Log.d("MainActivity", "Page URLs: " + pageUrls);

                // Создаем экземпляр класса UploadingImagesFromInternet
                UploadingImagesFromInternet uploader = new UploadingImagesFromInternet();
                // Передаем список абсолютных URL-адресов изображений uploader
                loadImageFromUrl(imageUrls, pageUrls);
            } else {
                // Обработка ошибки, если doc равен null
                Log.d("MainActivity", "Doc is null");
            }
        }
    }
    private int currentIndex = 0; // переменная для хранения текущего индекса загруженной картинки
    // Метод загрузки картинки по указанному индексу из списка imageUrls

    private Pair<String, String> loadImageFromUrl(List<String> imageUrls, List<String> pageUrls, int index)
    {
        String imageUrl = null;
        String pageUrl = null;

        ImageView imageView = findViewById(R.id.imageViewDefaultDog);

        if (index >= 0 && index < imageUrls.size())
        {
            imageUrl = imageUrls.get(index);
            pageUrl = pageUrls.get(index);
            Picasso.get().load(imageUrl).into(imageView);
        }
        currentIndex = index;

        buttonLike.setEnabled(true);
        buttonDislike.setEnabled(true);
        buttonBack.setEnabled(true);
        buttonNext.setEnabled(true);
        buttonDownload.setEnabled(true);
        buttonInternet.setEnabled(true);

        return new Pair<>(imageUrl, pageUrl);
    }
    public void loadImageFromUrl(List<String> imageUrls, List<String> pageUrls)
    {
        loadImageFromUrl(imageUrls, pageUrls, currentIndex); // Загружаем первую картинку (индекс 0)
        Log.d("MainActivity", "Индекс текущей картинки: "  + currentIndex);
    }

    public void clickLike(View view)
    {
        Toast.makeText(MainActivity.this, "Like", Toast.LENGTH_SHORT).show();

        buttonLike.setEnabled(false); // Отключаем кнопку после нажатия
        new Handler().postDelayed(() -> {
            buttonLike.setEnabled(true); // Включаем кнопку после таймаута
        }, 3500); // 3.5с
    }

    public void clickDisLike(View view)
    {
        Toast.makeText(MainActivity.this, "Dislike", Toast.LENGTH_SHORT).show();

        buttonDislike.setEnabled(false); // Отключаем кнопку после нажатия
        new Handler().postDelayed(() -> {
            buttonDislike.setEnabled(true); // Включаем кнопку после таймаута
        }, 3500); // 3.5с
    }

    public void clickBack(View view)
    {
        if (currentIndex == 0)
        {
            currentIndex = imageUrls.size() - 1;
        } else {
            currentIndex--;
        }
        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, currentIndex);
        String imageUrl = result.first;
        String pageUrl = result.second;

        Log.d("MainActivity", "URL загруженной картинки: " + imageUrl);
        Log.d("MainActivity", "URL сайта: " + pageUrl);

        buttonBack.setEnabled(false);
        new Handler().postDelayed(() -> {
            buttonBack.setEnabled(true);
        }, 1000);
    }

    public void clickNext(View view)
    {
        if (currentIndex == imageUrls.size() - 1)
        {
            currentIndex = 0; // Устанавливаем индекс первого элемента в imageUrls
        } else {
            currentIndex++;
        }
        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, currentIndex);
        String imageUrl = result.first;
        String pageUrl = result.second;

        Log.d("MainActivity", "URL загруженной картинки: " + imageUrl);
        Log.d("MainActivity", "URL сайта с загруженной картинкой: " + pageUrl);

        buttonNext.setEnabled(false);
        new Handler().postDelayed(() -> {
            buttonNext.setEnabled(true); // Включаем кнопку после таймаута
        }, 1000); // 1с
    }

    public void clickDownload (View view)
    {
        buttonDownload.setEnabled(false); // Отключаем кнопку после нажатия
        new Handler().postDelayed(() -> {
            buttonDownload.setEnabled(true); // Включаем кнопку после таймаута
        }, 5000); //5с

        String ImageUrl = imageUrls.get(currentIndex);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            Toast.makeText(MainActivity.this, "Необходимо предоставить разрешение для скачивания изображений", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Скачивание изображения...", Toast.LENGTH_SHORT).show();

            new DownloadsImage().execute(ImageUrl);
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
                        Toast.makeText(MainActivity.this, imageFile, Toast.LENGTH_SHORT).show();
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

    public void clickInternet (View view)
    {
        buttonInternet.setEnabled(false); // Отключаем кнопку после нажатия
        new Handler().postDelayed(() -> {
            buttonInternet.setEnabled(true); // Включаем кнопку после таймаута
        }, 5000); //5с

        String PageURL = pageUrls.get(currentIndex);

        Toast.makeText(MainActivity.this, "Переход на сайт", Toast.LENGTH_SHORT).show();

        Pair<String, String> result = loadImageFromUrl(imageUrls, pageUrls, currentIndex);
        String pageUrl = result.second;
        Log.d("MainActivity", "URL сайта с загруженной картинкой: " + pageUrl);

        // Создаем Intent для открытия ссылки
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
        startActivity(intent);

    }














}