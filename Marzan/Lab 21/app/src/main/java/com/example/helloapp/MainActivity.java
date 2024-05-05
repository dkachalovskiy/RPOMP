package com.example.helloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivityMainActivity extends AppCompatActivity {
    private TextView textview;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;
    private Sensor sensor;
    private Handler handler;
    private static int counter = 0;
    private Sensor accelerometerSensor;
    private double max_X, max_Y, max_Z;
    private Sensor currentSensor;
    private ArrayList<String> history;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private ImageView imageView;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean isShowingHistory;
    private boolean isShowingSatellitesInfo;
    private View dotView;
    private double prev_longitude;
    private double prev_latitude;
    private String provider_mode;
    private boolean provider_gn;
    List<GnssStatus.Callback> gnssStatusCallbacks = new ArrayList<>();
    private float previousTimestamp;
    private float previousVelocity;
    private float maxSpeed;
    private float maxAcceleration;
    private long prevTimestamp;
    private float prevAccelerationX;
    private float prevAccelerationY;
    private float prevAccelerationZ;
    private float light;
    private float prev_x;
    private float prev_y;
    private float prev_z;
    private boolean isSurvived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isSurvived = false;
        light = 0;
        prevAccelerationX = 0;
        prevAccelerationY = 0;
        prevAccelerationZ = 0;
        prev_x = 0;
        prev_y = 0;
        prev_z = 0;
        prevTimestamp = 0;
        provider_mode = "GPS(Dzmitry Kukharau connected):";
        maxSpeed = 0;
        maxAcceleration = 0;
        provider_gn = true;
        prev_longitude = 0;
        prev_latitude = 0;
        isShowingHistory = true;
        isShowingSatellitesInfo = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = new ArrayList<String>();

        textview = findViewById(R.id.TextView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        imageView = findViewById(R.id.imageView);

        imageView.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);
        button5.setVisibility(View.INVISIBLE);
        button6.setVisibility(View.INVISIBLE);
        //textview.setText(deviceSensors.toString());
        currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestroySatellite();
                ViewGroup rootView = findViewById(android.R.id.content);
                rootView.removeView(dotView);

                imageView.setVisibility(View.INVISIBLE);
                isShowingHistory = true;
                isShowingSatellitesInfo = false;
                button4.setVisibility(View.INVISIBLE);
                button5.setVisibility(View.INVISIBLE);
                button6.setVisibility(View.INVISIBLE);
                currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestroySatellite();

                imageView.setVisibility(View.VISIBLE);
                isShowingHistory = false;
                isShowingSatellitesInfo = false;
                button4.setVisibility(View.VISIBLE);
                button5.setVisibility(View.VISIBLE);
                button6.setVisibility(View.VISIBLE);
                currentSensor = null;
                if(isShowingHistory){
                    return;
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                }
                Location lastKnownLocation;
                if(provider_gn){
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }else{
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if(!isShowingHistory){
                    if (lastKnownLocation != null) {
                        double latitude = lastKnownLocation.getLatitude();
                        double longitude = lastKnownLocation.getLongitude();
                        textview.setText(provider_mode+"Last Known Location:\nLatitude: " + latitude + "\nLongitude: " + longitude);
                    } else {
                        textview.setText(provider_mode+ "\n\nLast Known Location is not available (But Kukharau is here!)");
                    }
                }

                if(provider_gn){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }

            }

        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxSpeed = 0;
                currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                DestroySatellite();
                ViewGroup rootView = findViewById(android.R.id.content);
                rootView.removeView(dotView);
                imageView.setVisibility(View.INVISIBLE);
                isShowingHistory = true;
                isShowingSatellitesInfo = false;
                button4.setVisibility(View.INVISIBLE);
                button5.setVisibility(View.INVISIBLE);
                button6.setVisibility(View.INVISIBLE);
                boolean isPrint = false;

                SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                Sensor speedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                SensorEventListener speedSensorEventListener = new SensorEventListener() {

                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        if (currentSensor != null && event.sensor.getType() == currentSensor.getType()) {
                            float accelerationX = event.values[0];
                            float accelerationY = event.values[1];
                            float accelerationZ = event.values[2];
                            long timestamp = event.timestamp;
                            float timeDelta = (timestamp - prevTimestamp) * 1e-9f;
                            float velocityX = (accelerationX + prevAccelerationX) / 2 * timeDelta;
                            float velocityY = (accelerationY + prevAccelerationY) / 2 * timeDelta;
                            float velocityZ = (accelerationZ + prevAccelerationZ) / 2 * timeDelta;
                            float speed = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
                            float acceleration = (float) Math.sqrt(accelerationX * accelerationX + accelerationY * accelerationY + accelerationZ * accelerationZ);
                            if(speed > maxSpeed){
                                maxSpeed = speed;
                            }
                            if(acceleration > maxAcceleration){
                                maxAcceleration = acceleration;
                            }
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                                return;
                            }
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                float altitude = (float) location.getAltitude();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                float accuracy = location.getAccuracy();

                                textview.setText("Other Sensors (belongs to Kukharau and his Poco X3)\n\nSpeed: " + speed + " m/s\nMax Speed: " + maxSpeed + " m/s\n\nAcceleration: " +
                                        acceleration + " m/s^2\nMax Acceleration: " + maxAcceleration + " m/s^2\n\n"
                                        + "Д Altitude: " + altitude + " m\n"
                                        + "и Latitude: " + latitude + "\n"
                                        + "м Longitude: " + longitude + "\n"
                                        + "а Accuracy: " + accuracy + " m\n\nLight Value (Light sensor): " + light);
                            }
                            prevAccelerationX = accelerationX;
                            prevAccelerationY = accelerationY;
                            prevAccelerationZ = accelerationZ;
                            prevTimestamp = timestamp;
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    }
                };
                Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                SensorEventListener lightSensorEventListener = new SensorEventListener() {

                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                            float lightValue = event.values[0];
                            light = lightValue;
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    }
                };
                sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(speedSensorEventListener, speedSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestroySatellite();
                counter = 1;
                ViewGroup rootView = findViewById(android.R.id.content);
                rootView.removeView(dotView);
                imageView.setVisibility(View.INVISIBLE);
                ShowHistory();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingSatellitesInfo = true;
                isShowingHistory = true;
                if(!isShowingSatellitesInfo){
                    return;
                }
                imageView.setVisibility(View.INVISIBLE);
                ViewGroup rootView = findViewById(android.R.id.content);
                rootView.removeView(dotView);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                }
                GnssStatus.Callback gnssStatusCallback = new GnssStatus.Callback() {
                    @Override
                    public void onSatelliteStatusChanged(GnssStatus status) {
                        int numberOfSatellites = status.getSatelliteCount();
                        StringBuilder satellitesInfo = new StringBuilder();
                        satellitesInfo.append("Dzmitry Kukharau found ").append(numberOfSatellites).append(" satellites: ");

                        for (int i = 0; i < numberOfSatellites; i++) {
                            int prn = status.getSvid(i);
                            float snr = status.getCn0DbHz(i);
                            float azimuth = status.getAzimuthDegrees(i);
                            float elevation = status.getElevationDegrees(i);

                            satellitesInfo.append("\nPRN: ").append(prn)
                                    .append(", SNR: ").append(snr)
                                    .append(", Azimuth: ").append(azimuth)
                                    .append(", Elevation: ").append(elevation);
                        }

                        textview.setText(satellitesInfo.toString());
                        ViewGroup rootView = findViewById(android.R.id.content);
                        rootView.removeView(dotView);
                    }
                };
                gnssStatusCallbacks.add(gnssStatusCallback);

                locationManager.registerGnssStatusCallback(gnssStatusCallback);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingHistory = false;
                isShowingSatellitesInfo = false;
                provider_gn = !provider_gn;
                if(provider_gn){
                    provider_mode = "GPS(By Dzmitry Kukharau):";
                }else {
                    provider_mode = "GPS(DK From PO9)(Network Mode):";
                }
                button2.callOnClick();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (!isShowingHistory) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String coordinates = provider_mode + "\nDzmitry Latitude: " + latitude + "\nKukharau Longitude: " + longitude;

                    ViewGroup rootView = findViewById(android.R.id.content);
                    if (dotView != null && rootView != null) {
                        rootView.removeView(dotView);
                    }
                    dotView = new View(MainActivity.this);
                    dotView.setBackgroundResource(R.drawable.dot);

                    int dotSize = 9;
                    dotView.setLayoutParams(new ViewGroup.LayoutParams(dotSize, dotSize));
                    int imageViewWidth = imageView.getWidth();
                    int imageViewHeight = imageView.getHeight();
                    float x = (float) (imageViewWidth * (longitude + 180) / 360.0 - 50);
                    float y = (float) (imageViewHeight * (latitude + 90) / 180.0 - 280);
                    dotView.setX(x - dotSize / 2);
                    dotView.setY(y - dotSize / 2);
                    rootView.addView(dotView);

                    textview.setText(coordinates);
                    DecimalFormat decimalFormat = new DecimalFormat("#.########");
                    if(Double.parseDouble(decimalFormat.format(latitude)) != Double.parseDouble(decimalFormat.format(prev_latitude)) &&
                            Double.parseDouble(decimalFormat.format(longitude)) != Double.parseDouble(decimalFormat.format(prev_longitude))){
                        history.add(coordinates);
                    }
                    prev_latitude = latitude; prev_longitude = longitude;
                }
            }
        };
    }
    private void SetShowSatInfo(){
        isShowingSatellitesInfo = true;
    }
    private void DestroySatellite(){
        for (GnssStatus.Callback callback : gnssStatusCallbacks) {
            locationManager.unregisterGnssStatusCallback(callback);
        }
        gnssStatusCallbacks.clear();
    }
    private void ShowHistory(){
        isShowingHistory = true;
        textview.setText("Dzmitry Kukharau travel history: \n\n");
        for(int i = history.size()-1; i >= 0; --i){
            textview.setText(textview.getText() + "" + counter + ". " + history.get(i) + "\n");
            ++counter;
        }
    }
    private void SetHistory(){
        isShowingHistory = true;
        button2.callOnClick();
    }
    private void RecognizeMovement(float x, float y, float z){
        float previous = prev_z + prev_y + prev_x;
        float current = x + y + z;
        float tmp;
        if(previous < current){
            tmp = previous;
            previous = current;
            current = tmp;
        }
        if((previous - current) < 0.5){
            textview.setText(textview.getText() + "\n\nDevice is not moving (Dzmitry did this)");
        }else if((previous - current) < 2){
            textview.setText(textview.getText() + "\n\nDevice barely moving");
        }else if((previous - current) < 5){
            textview.setText(textview.getText() + "\n\nDevice slightely shaking");
        }else if((previous - current) < 10){
            textview.setText(textview.getText() + "\n\nDevice shaking");
        }else if((previous - current) < 15){
            textview.setText(textview.getText() + "\n\nDevice hardly shaking");
        }else if((previous - current) < 20){
            textview.setText(textview.getText() + "\n\nDevice shaking really hard");
        }else{
            textview.setText(textview.getText() + "\n\nIs it earthquake?..");
            isSurvived = true;
        }
        if(isSurvived){
            textview.setText(textview.getText() + "\nToday Dmitry's phone survived an earthquake!");
        }
    }
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            //textview.setText(String.valueOf(sensor.getPower()) + counter);

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        locationManager.removeUpdates(locationListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (currentSensor != null && event.sensor.getType() == currentSensor.getType()) {
                    if (currentSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                            float x = event.values[0];
                            float y = event.values[1];
                            float z = event.values[2];
                            if (x > max_X) {
                                    max_X = x;
                            }
                            if (y > max_Y) {
                                    max_Y = y;
                            }
                            if (z > max_Z) {
                                    max_Z = z;
                            }
                            textview.setText("Accelerometer(by Dzmitry Kukharau):\n\nX: " + x + "\nY: " + y + "\nZ: " + z +
                                    "\n\nMax X: " + max_X + "\nMax Y: " + max_Y + "\nMax Z: " + max_Z);
                            RecognizeMovement(x, y, z);
                            prev_x = x; prev_y = y; prev_z = z;
                    }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
