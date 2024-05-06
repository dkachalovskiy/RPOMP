package com.example.mobileslab2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class GPSActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView textView, coordList, satelliteList;
    StringBuilder coordinates;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        textView = findViewById(R.id.tvText);
        coordList = findViewById(R.id.tvCoordList);
        satelliteList = findViewById(R.id.tvSatelliteList);
        coordinates = new StringBuilder();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(this, "Location manager is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(GPSActivity.this, "Probably have bad signal", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(GPSActivity.this, "Should be ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(GPSActivity.this, "Please enable location services", Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        //Toast.makeText(GPSActivity.this, locationManager.getGnssHardwareModelName(), Toast.LENGTH_SHORT).show();

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, locationListener);
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, locationListener);
    }


    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        StringBuilder sb = new StringBuilder();
        sb.append("Kot A. \n")
                .append("Latitude: ")
                .append(latitude)
                .append("\n")
                .append("Longitude: ")
                .append(longitude);

        coordinates.append(latitude)
                .append(", ")
                .append(longitude)
                .append("\n");

        textView.setText(sb.toString());
        coordList.setText(coordinates.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
        }
    }

    private final GnssStatus.Callback gnssStatusCallback = new GnssStatus.Callback() {
        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            super.onSatelliteStatusChanged(status);
            int satelliteCount = status.getSatelliteCount();
            StringBuilder sb = new StringBuilder();
            sb.append("Satellite count: ").append(satelliteCount).append("\n\n");
            for (int i = 0; i < satelliteCount; i++) {
                int prn = status.getSvid(i);
                float cn0DbHz = status.getCn0DbHz(i);
                boolean hasAlmanac = status.hasAlmanacData(i);
                boolean hasEphemeris = status.hasEphemerisData(i);
                boolean usedInFix = status.usedInFix(i);
                sb.append("PRN: ").append(prn).append("\n")
                        .append("CN0: ").append(cn0DbHz).append("\n")
                        .append("Almanac: ").append(hasAlmanac).append("\n")
                        .append("Ephemeris: ").append(hasEphemeris).append("\n")
                        .append("UsedInFix: ").append(usedInFix).append("\n\n");
            }
            satelliteList.setText(sb.toString());
        }

        @Override
        public void onStarted() {
            super.onStarted();
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }
    };

    }
