package com.example.lab2_5;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    List<Address> addressList;
    final private int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView mapSearchView, firstSearchView, secondSearchView;
    boolean isCurrentShow = false;
    boolean isRouteViewVisible = false;
    MarkerOptions choise1;
    MarkerOptions choise2;
    boolean isMarker1 = false;
    boolean isMarker2 = false;
    LatLng locationCords;
    LatLng destinationCords;
    Polyline currentPolyline;
    String dirMode = "driving";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        mapSearchView = findViewById(R.id.mapSearch);
        firstSearchView = findViewById(R.id.mapSearch2);
        secondSearchView = findViewById(R.id.mapSearch3);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        Button navBat = findViewById(R.id.getCurLoc);
        navBat.setVisibility(View.INVISIBLE);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;
                navBat.setVisibility(View.INVISIBLE);

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

                        MarkerOptions options = new MarkerOptions().position(latLng)
                                .title(address.getCountryName() + ", " + address.getAdminArea()
                                        + (address.getLocality() != null ? ", " + address.getLocality()  : "")
                                        + ((address.getFeatureName() != null &&
                                        !address.getFeatureName().equals(address.getLocality()))
                                        ? ", " + address.getFeatureName()  : ""))
                                .snippet(latLng.latitude + ",  " + latLng.longitude);

                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMap.clear();
                        mMap.addMarker(options);
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
        firstSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location =firstSearchView.getQuery().toString();
                List<Address> addressList = null;
                navBat.setVisibility(View.INVISIBLE);

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

                        MarkerOptions options = new MarkerOptions().position(latLng)
                                .title(address.getCountryName() + ", " + address.getAdminArea()
                                        + (address.getLocality() != null ? ", " + address.getLocality()  : "")
                                        + ((address.getFeatureName() != null &&
                                        !address.getFeatureName().equals(address.getLocality()))
                                        ? ", " + address.getFeatureName()  : ""))
                                .snippet(latLng.latitude + ",  " + latLng.longitude);

                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                        locationCords = latLng;
                        choise1 = options;

                        mMap.addMarker(options);
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
        secondSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = secondSearchView.getQuery().toString();
                List<Address> addressList = null;
                navBat.setVisibility(View.INVISIBLE);

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

                        MarkerOptions options = new MarkerOptions().position(latLng)
                                .title(address.getCountryName() + ", " + address.getAdminArea()
                                        + (address.getLocality() != null ? ", " + address.getLocality()  : "")
                                        + ((address.getFeatureName() != null &&
                                        !address.getFeatureName().equals(address.getLocality()))
                                        ? ", " + address.getFeatureName()  : ""))
                                .snippet(latLng.latitude + ",  " + latLng.longitude);

                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        destinationCords = latLng;
                        choise2 = options;

                        mMap.addMarker(options);
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
        Button route_b = findViewById(R.id.route_button);
        LinearLayout route_v = findViewById(R.id.route_view);
        mapFragment.getMapAsync(MapsActivity.this);
        route_v.setVisibility(View.INVISIBLE);
        route_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRouteViewVisible){
                    route_v.setVisibility(View.INVISIBLE);
                    isRouteViewVisible = false;
                }else{
                    route_v.setVisibility(View.VISIBLE);
                    isRouteViewVisible = true;
                }

            }
        });

        Button choise1btn = findViewById(R.id.choose1);
        choise1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMarker1 = true;
                navBat.setVisibility(View.VISIBLE);
            }
        });
        Button choise2btn = findViewById(R.id.choose2);
        choise2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMarker2 = true;
                navBat.setVisibility(View.VISIBLE);
            }
        });
        navBat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation == null){
                    Toast.makeText(MapsActivity.this, "Your current location is disabled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isMarker1){
                    SearchView search1 = findViewById(R.id.mapSearch2);
                    search1.setQuery(currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), true);

                    isMarker1 = false;
                }
                if(isMarker2){
                    SearchView search2 = findViewById(R.id.mapSearch3);
                    search2.setQuery(currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), true);
                    isMarker2 = false;
                }
            }
        });

        Button clearbtn = findViewById(R.id.clear_route);
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMarker1 = false;
                isMarker2 = false;
                choise1 = null;
                choise2 = null;
                locationCords = null;
                destinationCords = null;
                mMap.clear();
                SearchView mapSearchView1 = findViewById(R.id.mapSearch2);
                mapSearchView1.setQuery(null, false);
                SearchView mapSearchView2 = findViewById(R.id.mapSearch3);
                mapSearchView2.setQuery(null, false);
            }
        });
        Button direction = findViewById(R.id.build_route);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationCords == null || destinationCords == null){
                    MissingCordsError();
                    return;
                }else{
                    //MarkerOptions position1 = new MarkerOptions().position(locationCords);
                    //MarkerOptions position2 = new MarkerOptions().position(destinationCords);
                    //position1.getPosition() === latlng  //!!!
//                MarkerOptions position1 = new MarkerOptions().position(new LatLng(20, 20));
//                MarkerOptions position2 = new MarkerOptions().position(new LatLng(20, 21));
                    mMap.clear();
                    mMap.addMarker(choise1);
                    mMap.addMarker(choise2);
                    String url = getUrl(locationCords, destinationCords, dirMode);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new FetchURL(MapsActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, "driving");
                    } else {
                        new FetchURL(MapsActivity.this).execute(url, dirMode);
                    }
                }

            }
        });

        Button setDirMode = findViewById(R.id.vehicle);
        setDirMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dirMode.equals("driving")){
                    dirMode = "walking";
                    Drawable d = getDrawable(R.drawable.walk);
                    setDirMode.setBackground(d);
                }else{
                    dirMode = "driving";
                    Drawable d = getDrawable(R.drawable.drive);
                    setDirMode.setBackground(d);
                }
            }
        });

    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);

        // Получите PolylineOptions из возвращаемых значений
        PolylineOptions polylineOptions = (PolylineOptions) values[0];
        // Нарисуйте маршрут на карте
        mMap.addPolyline(polylineOptions);
        String data = Transfer.getDistance() + "; " + Transfer.getDuration();

        TextView output = findViewById(R.id.textView);
        output.setText(data);
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAEQ3KKE-cyhPjb6OZmnvsmFmT9M39XTzg";

        Log.d("URL", url);

        return url;
    }
    public void MissingCordsError(){
        Toast.makeText(this, "Both location and destination must be filled!", Toast.LENGTH_SHORT).show();
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
                    options.rotation(currentLocation.getBearing());

                    //mMap.clear();
                    //mMap.addMarker(options);

                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);

                } else {
                    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickMyLoc(View v) {
        if(isCurrentShow){
            isCurrentShow = false;
        }else {
            isCurrentShow = true;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(isCurrentShow);
        getLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запросить разрешение на доступ к местоположению
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
            return;
        }
        mMap.setMyLocationEnabled(isCurrentShow);

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                if(!(choise1 == null)){
                    mMap.addMarker(choise1);
                }
                if(!(choise2 == null)){
                    mMap.addMarker(choise2);
                }

                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    addressList = null;
                }
                Address address = addressList.get(0);

                MarkerOptions option = new MarkerOptions().position(latLng)
                        .title("Kuharev in " + address.getCountryName() + ", " + address.getAdminArea()
                                + (address.getLocality() != null ? ", " + address.getLocality()  : "")
                                + ((address.getFeatureName() != null &&
                                !address.getFeatureName().equals(address.getLocality()))
                                ? ", " + address.getFeatureName()  : ""))
                        .snippet(latLng.latitude + ",  " + latLng.longitude);

                if(isMarker1){
                    isMarker1 = false;
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    choise1 = option;
                    SearchView mapSearchView = findViewById(R.id.mapSearch2);
                    String query = latLng.latitude + ", " + latLng.longitude;
                    locationCords = latLng;
                    mapSearchView.setQuery(query, false);
                }
                if(isMarker2){
                    isMarker2 = false;
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    choise2 = option;
                    SearchView mapSearchView = findViewById(R.id.mapSearch3);
                    String query = latLng.latitude + ", " + latLng.longitude;
                    destinationCords = latLng;
                    mapSearchView.setQuery(query, false);
                }
                mMap.addMarker(option); //title??? will replace with (i)info
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

//mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
//
//
//        try {
//                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                } catch (IOException e) {
////            throw new RuntimeException(e);
//                addressList = null;
//                }
//
//                if (addressList != null && !addressList.isEmpty()){
//                Address address = addressList.get(0);
//                mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title(address.getCountryName() + ", " + address.getAdminArea()
//                + (address.getLocality() != null ? ", " + address.getLocality()  : "")
//                + ((address.getFeatureName() != null &&
//                !address.getFeatureName().equals(address.getLocality()))
//                ? ", " + address.getFeatureName()  : ""))
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))));
////            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                }
//                else {
//                Toast.makeText(MapsActivity.this, "Not found", Toast.LENGTH_SHORT).show();
//                }