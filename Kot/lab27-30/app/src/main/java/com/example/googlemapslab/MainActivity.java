package com.example.googlemapslab;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFromLocation = findViewById(R.id.etFromLocation);
        etToLocation = findViewById(R.id.etToLocation);
    }

    //Лабораторная работа №27, открываем карту.
    public void onClickShowMap(View v) {
        startActivity(new Intent(this,
                MapsActivity.class));
    }

    //Лабораторная работа №30, выстраивание маршрута
    public void onClickShowDirection(View v) {
        String userLocation = etFromLocation.getText().toString();
        String userDestination = etToLocation.getText().toString();

        if (userLocation.isEmpty() || userDestination.isEmpty()) {
            Toast.makeText(this, "Please enter location and destination", Toast.LENGTH_SHORT).show();
        } else {
            getDirections(userLocation, userDestination);
        }
    }


    private EditText etFromLocation;
    private EditText etToLocation;

    private void getDirections(String from, String to) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + from + "/" + to);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
    //Конец лабораторной работы №30
}