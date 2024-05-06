package ru.startandroid.develop.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 1;

    private TextView tvOut;
    private TextView tvLon;
    private TextView tvLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.textView1);
        tvLon = findViewById(R.id.longitude);
        tvLat = findViewById(R.id.latitude);

        // Проверяем, есть ли у приложения разрешение на доступ к местоположению
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не предоставлено, запрашиваем его у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        } else {
            // Разрешение уже предоставлено, запускаем получение местоположения
            startLocationUpdates();
        }
    }

    // Обрабатываем результат запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, запускаем получение местоположения
                startLocationUpdates();
            } else {
                // Разрешение не получено, выводим сообщение или выполняем альтернативные действия
                tvOut.setText("Permission denied. Unable to access location.");
            }
        }
    }

    // Метод для запуска получения местоположения
    private void startLocationUpdates() {
        // Получаем сервис
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener mlocListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                tvLat.setText("Широта: " + String.valueOf(location.getLatitude()));
                tvLon.setText("Долгота: " + String.valueOf(location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Подписываемся на изменения в показаниях датчика
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        // Если gps включен, то ..., иначе вывести "GPS is not turned on..."
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            tvOut.setText("GPS is turned on...");
        } else {
            tvOut.setText("GPS is not turned on...");
        }
    }
}