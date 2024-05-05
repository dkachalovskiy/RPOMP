package com.example.lab12;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.example.lab12.CustomDbHelper.*;
import static com.example.lab12.CustomDbHelper.COLUMN_CREATED_AT;

public class MainActivity extends AppCompatActivity {
    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TableLayout tableLayout;
    TextView statusView;
    TextView fioView;
    final static String URL_STRING = "https://media.itmo.ru/api_get_current_song.php";
    final static String LOGIN = "4707login";
    final static String PASSWORD = "4707pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        tableLayout = findViewById(R.id.tableLayout);
        statusView = findViewById(R.id.statusView);
        fioView = findViewById(R.id.fioView);
        db = databaseHelper.getReadableDatabase();

        reloadTable();

        new MyAsyncTask().execute();
    }

    private void showStatus(int time) {
        if (time == 1) reloadTable();
        statusView.setText("Статус обновления: " + time + " секунд");
    }

    private void reloadTable() {
        cursor = db.rawQuery("select " + COLUMN_ID + ", " + COLUMN_AUTHOR + ", " + COLUMN_TRACK + ", "
                + COLUMN_CREATED_AT + " from " + CustomDbHelper.TABLE_NAME, null);

        tableLayout.removeAllViews();
        tableLayout.addView(fioView);
        tableLayout.addView(statusView);
        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
            TextView _id = tableRow.findViewById(R.id._id_column);
            TextView author = tableRow.findViewById(R.id.author_column);
            TextView track = tableRow.findViewById(R.id.track_column);
            TextView createdAt = tableRow.findViewById(R.id.created_at_column);

            _id.setText(cursor.getString(0));
            author.setText(cursor.getString(1));
            track.setText(cursor.getString(2));
            createdAt.setText(cursor.getString(3));
            tableLayout.addView(tableRow);
        }
    }

    private void insertData(String author, String track) {
        if (!databaseHelper.isTrackEqualsLast(db, author, track)) {
            databaseHelper.insert(db, author, track);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }

    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    void showNoInternetToast() {
        statusView.setText("Автономный режим");
        Toast toast = Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_LONG);
        toast.show();
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            showStatus(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                while (isConnected()) {
                    startCycle(20);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showNoInternetToast();
        }

        private void startCycle(int time) throws InterruptedException {
            postData();
            for (int i = 0; i < 20; i++) {
                waitTime(1);
                publishProgress(i + 1);
            }

        }

        private void postData() {
            try {
                URL url = new URL(URL_STRING);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                sendRequest(urlConnection);
                getResponse(urlConnection);

                urlConnection.disconnect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void sendRequest(HttpURLConnection urlConnection) throws IOException, JSONException {
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            JSONObject requestJson = new JSONObject();
            requestJson.put("login", LOGIN);
            requestJson.put("password", PASSWORD);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(requestJson.toString());

            os.flush();
            os.close();
        }

        void getResponse(HttpURLConnection urlConnection) throws IOException {
            int responseCode = urlConnection.getResponseCode();

            InputStream inputStream;
            if (responseCode >= 200 && responseCode < 400) {
                inputStream = urlConnection.getInputStream();
            } else {
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String responseBody = response.toString();

            try {
                JSONObject jsonResponse = new JSONObject(responseBody);

                String result = jsonResponse.getString("result");
                String info = jsonResponse.getString("info");

                if (result.equalsIgnoreCase("error")) return;
                String[] splitInfo = info.split(" - ");
                insertData(splitInfo[0], splitInfo[1]);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void waitTime(int timeout) throws InterruptedException {
            TimeUnit.SECONDS.sleep(timeout);
        }
    }


}