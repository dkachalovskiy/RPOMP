package com.example.lab19;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;


public class Fragment3 extends Fragment {
    TextView tvText;
    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorMagnet;

    StringBuilder sb = new StringBuilder();

    Timer timer;

    int rotation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);

        tvText = rootView.findViewById(R.id.tvText3);
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceOrientation();
                        getActualDeviceOrientation();
                        showInfo();
                    }
                });
            }
        };
        timer.schedule(task, 0, 400);

        WindowManager windowManager = ((WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE));
        Display display = windowManager.getDefaultDisplay();
        rotation = display.getRotation();

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    @SuppressLint("DefaultLocale")
    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1], values[2]);
    }

    void showInfo() {
        sb.setLength(0);
        sb.append("Orientation: ").append(format(valuesResult))
        ;
        tvText.setText(sb);
    }

    float[] r = new float[9];

    void getDeviceOrientation() {
        SensorManager.getRotationMatrix(r, null, valuesAccel, valuesMagnet);
        SensorManager.getOrientation(r, valuesResult);

        valuesResult[0] = (float) Math.toDegrees(valuesResult[0]);
        valuesResult[1] = (float) Math.toDegrees(valuesResult[1]);
        valuesResult[2] = (float) Math.toDegrees(valuesResult[2]);
        return;
    }

    float[] inR = new float[9];
    float[] outR = new float[9];

    void getActualDeviceOrientation() {
        SensorManager.getRotationMatrix(inR, null, valuesAccel, valuesMagnet);
        int x_axis = SensorManager.AXIS_X;
        int y_axis = SensorManager.AXIS_Y;
        switch (rotation) {
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

    float[] valuesAccel = new float[3];
    float[] valuesMagnet = new float[3];
    float[] valuesResult = new float[3];
    float[] valuesResult2 = new float[3];


    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, valuesAccel, 0, 3);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, valuesMagnet, 0, 3);
                    break;
            }
        }
    };
}