package com.example.lab2_5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vishal on 10/20/2018.
 */

public class FetchURL extends AsyncTask<String, Void, String> {

    Context mContext;
    String directionMode = "driving";
    public String sendDistanceText = "";
    public String sendDurationText = "";

    public FetchURL(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
            Log.d("mylog", "Background task data " + data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s); // Парсинг JSON-ответа

            // Распарсить расстояние и время
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            JSONObject route = routesArray.getJSONObject(0);
            JSONArray legsArray = route.getJSONArray("legs");
            JSONObject leg = legsArray.getJSONObject(0);

            JSONObject distanceObject = leg.getJSONObject("distance");
            String distanceText = distanceObject.getString("text");
            int distanceValue = distanceObject.getInt("value");

            JSONObject durationObject = leg.getJSONObject("duration");
            String durationText = durationObject.getString("text");
            int durationValue = durationObject.getInt("value");


            // Теперь у вас есть расстояние и время в пути в текстовом и числовом форматах
            Log.d("mylog", "Расстояние: " + distanceText);
            Log.d("mylog", "Время в пути: " + durationText);

            Transfer.setDistance(distanceText);
            Transfer.setDuration(durationText);

            // Вызвать PointsParser для парсинга точек
            PointsParser parserTask = new PointsParser(mContext, directionMode);
            parserTask.execute(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

