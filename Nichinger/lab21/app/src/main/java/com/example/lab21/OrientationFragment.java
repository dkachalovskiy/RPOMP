package com.example.lab21;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.content.Context.LOCATION_SERVICE;

public class OrientationFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private TextView textView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orientation, container, false);
        textView2 = view.findViewById(R.id.textView2);

        return view;
    }

    private void changeLocationProvider() {
        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Разрешения не предоставлены, запросите их у пользователя
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, request them from the user
            ActivityCompat.requestPermissions(requireActivity(),
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