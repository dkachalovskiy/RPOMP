package com.RPOMP.labrab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OthersActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView2 = findViewById(R.id.textView2);

        Button changeProviderButton3 = findViewById(R.id.changeProviderButton3);
        changeProviderButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocationProvider();
            }
        });
    }
    private void changeLocationProvider() {
        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Разрешения не предоставлены, запросите их у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Проверка текущего провайдера
        String currentProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null ?
                LocationManager.NETWORK_PROVIDER : LocationManager.GPS_PROVIDER;

        // Определение нового провайдера
        String newProvider;
        if (currentProvider.equals(LocationManager.NETWORK_PROVIDER)) {
            newProvider = LocationManager.GPS_PROVIDER;
        } else {
            newProvider = LocationManager.NETWORK_PROVIDER;
        }

        // Остановка обновлений местоположения
        locationManager.removeUpdates(this);

        // Запрос обновлений местоположения с новым провайдером
        locationManager.requestLocationUpdates(newProvider, 1000, 1f, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, request them from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Permissions granted, request location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                1f, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location) {
        // Handle location changes
        float speed = location.getSpeed();
        float acceleration = location.getAccuracy();
        double altitude = location.getAltitude();

        String info = "Speed: " + speed + "\nAcceleration: " + acceleration + "\nAltitude: " + altitude;
        textView2.setText(info);

        Log.d("OrientationFragment", "Location changed: " + info);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle location provider status changes
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle location provider enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle location provider disabled
    }
}