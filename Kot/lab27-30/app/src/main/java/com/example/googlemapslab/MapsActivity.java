package com.example.googlemapslab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    FrameLayout map;
    GoogleMap gMap;
    Marker marker;
    SearchView searchView;
    private Polyline currentPolyline;
    private Button showTrackButton;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showTrackButton = findViewById(R.id.showTrackButton);
        map = findViewById(R.id.map);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        //Лабораторная работа №29, поиск
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                    if (!addressList.isEmpty()){
                        LatLng latLng = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                        if (marker != null){
                            marker.remove();
                        }
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5);
                        gMap.animateCamera(cameraUpdate);
                        marker = gMap.addMarker(markerOptions);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //Лабораторная работа №28, определяем текущую позицию
    private void getLocation () {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }




    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        //Лабораторная работа №28, выставляем маркер на текущую позицию
        this.gMap = googleMap;
        LatLng latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
        googleMap.addMarker(markerOptions);

        //Лабораторная работа №27, выставляем маркер
        /*this.googleMap = googleMap;
        LatLng mapBelarus = new LatLng(52.068026, 23.717695);
        this.googleMap.addMarker(new MarkerOptions().position(mapBelarus).title("Homeland of Kot A."));
        LatLng mapHoliday = new LatLng(52.211973, 24.361426);
        this.googleMap.addMarker(new MarkerOptions().position(mapHoliday).title("Grandparents of Kot A."));
        LatLng mapUni = new LatLng(52.097418, 23.757090);
        this.googleMap.addMarker(new MarkerOptions().position(mapUni).title("University of Kot A."));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(mapBelarus));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    //Лабораторная работа №28
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Лабораторная работа №28
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mapNone) {
            gMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        } else if (id == R.id.mapNormal) {
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.mapSatellite) {
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.mapTerrain) {
            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        } else if (id == R.id.mapHybrid) {
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        return super.onOptionsItemSelected(item);
    }
}
