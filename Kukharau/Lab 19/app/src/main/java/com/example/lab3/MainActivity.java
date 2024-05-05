package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tvText;
    SensorManager sensorManager;
    List<Sensor> sensors;
    Sensor sensorLight;
    Sensor sensorAccel;
    Sensor sensorLinAccel;
    Sensor sensorGravity;

    StringBuilder sb = new StringBuilder();

    Timer timer;
    Sensor sensorMagnet;
    int counter;

    int rotation;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvText = (TextView) findViewById(R.id.tvText);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLinAccel = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }
    public void killAccelerometer(){
        sensorManager.unregisterListener(listenerLight, sensorLight);
        sensorManager.unregisterListener(listenerAccelerometer, sensorAccel);
        sensorManager.unregisterListener(listenerAccelerometer, sensorLinAccel);
        sensorManager.unregisterListener(listenerAccelerometer, sensorGravity);
        timer.cancel();
    }
    public void killOrientation(){
        sensorManager.unregisterListener(listener, sensorAccel);
        sensorManager.unregisterListener(listener, sensorMagnet);
        timer.cancel();
    }
    public void onClickSensList(View v) {
        sensorManager.unregisterListener(listenerLight, sensorLight);
        counter = 0;
        try{
            killAccelerometer();
            timer.cancel();
        }catch (Exception ex){

        }
        try{
            killOrientation();
            timer.cancel();
        }catch (Exception ex){

        }
        StringBuilder sb = new StringBuilder();
        sb.append("Kuharev's Poco X3 sensors list:\n");
        for (Sensor sensor : sensors) {
            ++counter;
            sb.append("name = ").append(sensor.getName())
                    .append(", type = ").append(sensor.getType())
                    .append("\nvendor = ").append(sensor.getVendor())
                    .append(" ,version = ").append(sensor.getVersion())
                    .append("\nmax = ").append(sensor.getMaximumRange())
                    .append(", resolution = ").append(sensor.getResolution())
                    .append("\n--------------------------------------\n");
        }
        tvText.setText(sb + "\nTotal: " + counter);
    }

    public void onClickSensLight(View v) {

        try{
            killAccelerometer();
            timer.cancel();
        }catch (Exception ex){

        }
        try{
            killOrientation();
            timer.cancel();
        }catch (Exception ex){

        }
        sensorManager.registerListener(listenerLight, sensorLight,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void onClickSensAccelerometer(View v) {
        sensorManager.unregisterListener(listenerLight, sensorLight);
        try{
            killOrientation();
            timer.cancel();
        }catch (Exception ex){

        }
        sensorManager.registerListener(listenerAccelerometer, sensorAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerAccelerometer, sensorLinAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerAccelerometer, sensorGravity,
                SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);
    }
    public void onClickSensOrientation(View v) {
        sensorManager.unregisterListener(listenerLight, sensorLight);
        try{
            killAccelerometer();
            timer.cancel();
        }catch (Exception ex){

        }
        sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceOrientation();
                        getActualDeviceOrientation();
                        showInfoOrientation();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);

        WindowManager windowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        Display display = windowManager.getDefaultDisplay();
        rotation = display.getRotation();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerLight, sensorLight);
        sensorManager.unregisterListener(listenerAccelerometer, sensorAccel);
        sensorManager.unregisterListener(listenerAccelerometer, sensorLinAccel);
        sensorManager.unregisterListener(listenerAccelerometer, sensorGravity);
        sensorManager.unregisterListener(listenerAccelerometer);
        sensorManager.unregisterListener(listener, sensorAccel);
        sensorManager.unregisterListener(listener, sensorMagnet);
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    SensorEventListener listenerLight = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            tvText.setText("Light power around Kuharev\n" + String.valueOf(event.values[0]));
        }
    };
    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1],
                values[2]);
    }

    void showInfo() {
        sb.setLength(0);
        sb.append("Kuharev Accelerometer: " + format(valuesAccel))
                .append("\n\nAccel motion: " + format(valuesAccelMotion))
                .append("\nAccel gravity : " + format(valuesAccelGravity))
                .append("\n\nLin accel : " + format(valuesLinAccel))
                .append("\nGravity : " + format(valuesGravity));
        tvText.setText(sb);
    }

    String formatOrientation(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1], values[2]);
    }

    void showInfoOrientation() {
        sb.setLength(0);
        sb.append("Kuharev position\nOrientation : " + formatOrientation(valuesResult))
                .append("\nOrientation 2: " + format(valuesResult2))
        ;
        tvText.setText(sb);
    }
    void getDeviceOrientation() {
        SensorManager.getRotationMatrix(r, null, valuesAccel, valuesMagnet);
        SensorManager.getOrientation(r, valuesResult);

        valuesResult[0] = (float) Math.toDegrees(valuesResult[0]);
        valuesResult[1] = (float) Math.toDegrees(valuesResult[1]);
        valuesResult[2] = (float) Math.toDegrees(valuesResult[2]);
        return;
    }
    void getActualDeviceOrientation() {
        SensorManager.getRotationMatrix(inR, null, valuesAccel, valuesMagnet);
        int x_axis = SensorManager.AXIS_X;
        int y_axis = SensorManager.AXIS_Y;
        switch (rotation) {
            case (Surface.ROTATION_0): break;
            case (Surface.ROTATION_90):
                x_axis = SensorManager.AXIS_Y;
                y_axis = SensorManager.AXIS_MINUS_X;
                break;
            case (Surface.ROTATION_180):
                y_axis = SensorManager.AXIS_MINUS_Y;
                break;
            case (Surface.ROTATION_270):
                x_axis = SensorManager.AXIS_MINUS_Y;
                y_axis = SensorManager.AXIS_X;
                break;
            default: break;
        }
        SensorManager.remapCoordinateSystem(inR, x_axis, y_axis, outR);
        SensorManager.getOrientation(outR, valuesResult2);
        valuesResult2[0] = (float) Math.toDegrees(valuesResult2[0]);
        valuesResult2[1] = (float) Math.toDegrees(valuesResult2[1]);
        valuesResult2[2] = (float) Math.toDegrees(valuesResult2[2]);
        return;
    }

    float[] valuesMagnet = new float[3];
    float[] valuesResult = new float[3];
    float[] valuesResult2 = new float[3];
    float[] inR = new float[9];
    float[] outR = new float[9];
    float[] r = new float[9];

    float[] valuesAccel = new float[3];
    float[] valuesAccelMotion = new float[3];
    float[] valuesAccelGravity = new float[3];
    float[] valuesLinAccel = new float[3];
    float[] valuesGravity = new float[3];

    SensorEventListener listenerAccelerometer = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        valuesAccel[i] = event.values[i];
                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
                        valuesAccelMotion[i] = event.values[i]
                                - valuesAccelGravity[i];
                    }
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    for (int i = 0; i < 3; i++) {
                        valuesLinAccel[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_GRAVITY:
                    for (int i = 0; i < 3; i++) {
                        valuesGravity[i] = event.values[i];
                    }
                    break;
            }

        }

    };
    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i=0; i < 3; i++){
                        valuesAccel[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    for (int i=0; i < 3; i++){
                        valuesMagnet[i] = event.values[i];
                    }
                    break;
            }
        }
    };
}