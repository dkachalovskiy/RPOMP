package com.example.lab_3;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends AsyncTask<String, Void, String> {

    private OnDataLoadedListener listener;

    public interface OnDataLoadedListener {
        void onDataLoaded(String jsonData);
    }

    public DataLoader(OnDataLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonData = null;

        try {
            // Создание объекта URL для передачи в HttpURLConnection
            URL url = new URL(urls[0]);

            // Создание HttpURLConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Получение InputStream
            InputStream inputStream = urlConnection.getInputStream();

            // Создание BufferedReader для чтения данных из InputStream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Чтение данных и добавление их в StringBuilder
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            // Преобразование StringBuilder в строку JSON
            jsonData = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Закрытие соединения и BufferedReader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonData;
    }

    @Override
    protected void onPostExecute(String jsonData) {
        if (listener != null) {
            listener.onDataLoaded(jsonData);
        }
    }
}