package com.example.lab2_5;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lab2_5.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.widget.SearchView;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final private int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView;
    private Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        mapSearchView = findViewById(R.id.mapSearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));


                    } else {
                        Toast.makeText(MapsActivity.this, "Location is not found", Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);

                    LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    MarkerOptions options = new MarkerOptions().position(myLocation).title("Current Location");
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.clear();
                    mMap.addMarker(options);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                } else {
                    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClickMyLoc(View v){
        getLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.latitude + ",  " + latLng.longitude)); //title??? will replace with (i)info
            }
        });
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.maps_view_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mapNone) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    return true;
                }
                if (id == R.id.mapNormal) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    return true;
                }
                if (id == R.id.mapSatellite) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;
                }
                if (id == R.id.mapHybrid) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;
                }
                if (id == R.id.mapTerrain) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}