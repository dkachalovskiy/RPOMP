package com.RPOMP.labrab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GPSActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private TextView textView2;
    private TextView textView3;
    private List<String> coordinateHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsactivity);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        coordinateHistory = new ArrayList<>();

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        Button changeProviderButton = findViewById(R.id.changeProviderButton);
        changeProviderButton.setOnClickListener(new View.OnClickListener() {
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
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Проверка текущего провайдера
        String currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER) != null ?
                LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;

        // Определение нового провайдера
        String newProvider;
        if (currentProvider.equals(LocationManager.GPS_PROVIDER)) {
            newProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            newProvider = LocationManager.GPS_PROVIDER;
        }

        // Остановка обновлений местоположения
        locationManager.removeUpdates(this);

        // Запрос обновлений местоположения с новым провайдером
        locationManager.requestLocationUpdates(newProvider, 1000, 1f, this);
    }

    @Override
    public void onResume() {
        super.onResume();

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

        // Разрешения предоставлены, запросите обновления местоположения
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
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
        // Обработка изменения местоположения
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        String coordinates = "Longitude: " + longitude + "\nLatitude: " + latitude;
        updateTextView2(coordinates);
        updateTextView3(coordinates);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Обработка изменения статуса провайдера местоположения
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Обработка включения провайдера местоположения
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Обработка отключения провайдера местоположения
    }

    private void updateTextView2(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView2.setText(message);
            }
        });
    }

    private void updateTextView3(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                coordinateHistory.add(message);
                StringBuilder history = new StringBuilder();
                for (String coordinate : coordinateHistory) {
                    history.append(coordinate).append("\n\n");
                }
                textView3.setText(history.toString());
            }
        });
    }
}