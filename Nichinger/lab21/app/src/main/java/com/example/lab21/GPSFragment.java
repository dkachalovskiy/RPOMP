package com.example.lab21;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class GPSFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private TextView textView2;
    private TextView textView3;
    private List<String> coordinateHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
        coordinateHistory = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                1f, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


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
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void updateTextView2(final String message) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView2.setText(message);
            }
        });
    }

    private void updateTextView3(final String message) {
        requireActivity().runOnUiThread(new Runnable() {
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