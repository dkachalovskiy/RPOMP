package com.example.lab21;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
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

import static android.content.Context.LOCATION_SERVICE;

public class GPSSputniksFragment extends Fragment implements LocationListener {
    private LocationManager locationManager;
    private TextView textView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gpssputniks, container, false);
        textView3 = view.findViewById(R.id.textView3);

        return view;
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
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView3.setText(message);
            }
        });
    }
}