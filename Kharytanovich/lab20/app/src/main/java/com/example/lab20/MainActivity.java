package com.example.lab20;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.MapView;
import android.location.LocationListener;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_LOCATION = 99;
    private Button button5;

    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        button5 = findViewById(R.id.button5);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this, android.Manifest.permission.
                        ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        final Location location = locationManager.getLastKnownLocation(provider);

        mylistener = new MyLocationListener();
        mylistener.context = getApplicationContext();

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (location != null) {

                    mylistener.onLocationChanged(location);
                } else {
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
                    //Using 12 seconds timer till it gets location
                    final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Определение местоположения");
                    alertDialog.setMessage("12");
                    alertDialog.show();

                    new CountDownTimer(12000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            alertDialog.setMessage("" + (millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            alertDialog.dismiss();
                        }
                    }.start();
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
                if (location != null) {
                    Double lat = location.getLatitude();
                    Double lon = location.getLongitude();
                } else {
                    Toast.makeText(MainActivity.this, "Please open your location", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    class MyLocationListener implements LocationListener {
        public Context context;

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MainActivity.this, location.getLatitude()  + " " + location.getLongitude(),
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_LONG).show();
        }
    }
}