package com.example.lab29;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
        AddressDialogFragment.AddressDialogListener {
    private SearchView mapSearchView;
    private GoogleMap gMap;
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker myPosition;
    private ArrayList<Marker> searchLocationMarkers = new ArrayList<>();
    private ArrayList<Marker> clickLocationMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mapSearchView = findViewById(R.id.mapSearch);
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.isEmpty() || gMap == null)
                    return false;

                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MainActivity.this);

                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (addressList != null && !addressList.isEmpty()){
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    if (!searchLocationMarkers.isEmpty()){
                        searchLocationMarkers.get(0).remove();
                        searchLocationMarkers.clear();
                    }

                    searchLocationMarkers.add(gMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(location)
                            .snippet("latitude: " + latLng.latitude + ", longitude: " + latLng.longitude)));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                else {
                    Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnMarkerClickListener(this);
        gMap.setOnMapClickListener(this);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);

        LatLng meCoords = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLng(meCoords));

        myPosition = gMap.addMarker(new MarkerOptions()
                                    .position(meCoords)
                                    .title("MyPosition")
                                    .snippet("latitude: " + meCoords.latitude + ", longitude: " + meCoords.longitude)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Not allowed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        return false;
    }

    @Override
    public void onMapClick(@NonNull @NotNull LatLng latLng) {
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            addressList = null;
        }

        if (addressList != null && !addressList.isEmpty()){
            Address address = addressList.get(0);

            if (!clickLocationMarkers.isEmpty()){
                clickLocationMarkers.get(0).remove();
                clickLocationMarkers.clear();
            }

            clickLocationMarkers.add(gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(address.getCountryName() + ", " + address.getAdminArea()
                            + (address.getLocality() != null ? ", " + address.getLocality()  : "")
                            + ((address.getFeatureName() != null &&
                                !address.getFeatureName().equals(address.getLocality()))
                                ? ", " + address.getFeatureName()  : ""))
                    .snippet("latitude: " + latLng.latitude + ", longitude: " + latLng.longitude)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))));
//            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
        else {
            Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddressesSelected(String startAddress, String endAddress) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+startAddress+"/"+endAddress);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void onBuildRouteClick(View view) {
        AddressDialogFragment dialogFragment = new AddressDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Build route");
    }
}