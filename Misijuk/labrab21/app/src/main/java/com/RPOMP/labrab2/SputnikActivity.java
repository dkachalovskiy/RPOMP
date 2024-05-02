package com.RPOMP.labrab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SputnikActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sputnik);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView3 = findViewById(R.id.textView3);

        Button changeProviderButton2 = findViewById(R.id.changeProviderButton2);
        changeProviderButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocationProvider();
            }
        });
    }

    private void changeLocationProvider() {
        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Разрешения не предоставлены, запросите их у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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

        // Остановка обновлений местоположения и гнсс-статуса
        locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);

        // Запрос обновлений местоположения с новым провайдером и гнсс-статуса
        locationManager.requestLocationUpdates(newProvider, 1000, 1f, this);
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, request them from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Permissions granted, request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                1f, this);
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location) {
        // Handle location changes
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

    private GnssStatus.Callback gnssStatusCallback = new GnssStatus.Callback() {
        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            StringBuilder satellitesInfo = new StringBuilder();
            int satelliteCount = status.getSatelliteCount();
            for (int i = 0; i < satelliteCount; i++) {
                int prn = status.getSvid(i);
                float snr = status.getCn0DbHz(i);
                boolean hasAlmanac = status.hasAlmanacData(i);
                boolean hasEphemeris = status.hasEphemerisData(i);
                satellitesInfo.append("Satellite ").append(i + 1).append(": PRN = ").append(prn)
                        .append(", SNR = ").append(snr)
                        .append(", Almanac = ").append(hasAlmanac)
                        .append(", Ephemeris = ").append(hasEphemeris)
                        .append("\n");
            }
            updateTextView3(satellitesInfo.toString());
        }
    };

    private void updateTextView3(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView3.setText(message);
            }
        });
    }
}