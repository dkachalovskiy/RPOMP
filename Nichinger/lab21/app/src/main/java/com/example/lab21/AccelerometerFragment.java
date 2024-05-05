package com.example.lab21;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import static android.content.Context.SENSOR_SERVICE;

public class AccelerometerFragment extends Fragment implements SensorEventListener {
    enum ShakeDirection {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN,
        FORWARD,
        BACKWARD
    }

    private static final float SHAKE_THRESHOLD = 2.0f;
    private static final int SHAKE_TIMEOUT = 500; // Время ожидания между встряхиваниями
    private static final int SHAKE_DURATION = 1000; // Продолжительность встряхивания

    long shakeTimestamp;
    ShakeDirection currentShakeDirection;
    View rootView;
    SensorManager sensorManager;
    Sensor mSensor;
    int currentColor;
    int color1;
    int color2;
    boolean isColor1Active;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        color1 = getResources().getColor(R.color.white);
        color2 = getResources().getColor(R.color.black);
        currentColor = color1;
        isColor1Active = true;
        shakeTimestamp = 0;
        currentShakeDirection = ShakeDirection.NONE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        textView = rootView.findViewById(R.id.shakeDirView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float accelerationSquareRoot = (x * x + y * y + z * z) /
                    (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - shakeTimestamp;

            if (accelerationSquareRoot >= SHAKE_THRESHOLD) {
                if (elapsedTime > SHAKE_TIMEOUT) {
                    currentShakeDirection = detectShakeDirection(x, y, z);
                    shakeTimestamp = currentTime;
                } else {
                    if (elapsedTime < SHAKE_DURATION) {
                        ShakeDirection newShakeDirection = detectShakeDirection(x, y, z);
                        if (newShakeDirection != currentShakeDirection) {
                            currentShakeDirection = newShakeDirection;
                            handleShake(currentShakeDirection);
                        }
                    }
                }
            }
        }
    }

    private ShakeDirection detectShakeDirection(float x, float y, float z) {
        float deltaX = Math.abs(x);
        float deltaY = Math.abs(y);
        float deltaZ = Math.abs(z);

        if (deltaX > deltaY && deltaX > deltaZ) {
            if (x > 0) {
                return ShakeDirection.RIGHT;
            } else {
                return ShakeDirection.LEFT;
            }
        } else if (deltaY > deltaX && deltaY > deltaZ) {
            if (y > 0) {
                return ShakeDirection.UP;
            } else {
                return ShakeDirection.DOWN;
            }
        } else if (deltaZ > deltaX && deltaZ > deltaY) {
            if (z > 0) {
                return ShakeDirection.BACKWARD;
            } else {
                return ShakeDirection.FORWARD;
            }
        }

        return ShakeDirection.NONE;
    }

    private void handleShake(ShakeDirection direction) {
        switch (direction) {
            case LEFT:
                changeShakeText("LEFT");
                changeBackgroundColor(getResources().getColor(R.color.colorLeft));
                break;
            case RIGHT:
                changeShakeText("RIGHT");
                changeBackgroundColor(getResources().getColor(R.color.colorRight));
                break;
            case UP:
                changeShakeText("UP");
                changeBackgroundColor(getResources().getColor(R.color.colorUp));
                break;
            case DOWN:
                changeShakeText("DOWN");
                changeBackgroundColor(getResources().getColor(R.color.colorDown));
                break;
            case FORWARD:
                changeShakeText("FORWARD");
                changeBackgroundColor(getResources().getColor(R.color.colorForward));
                break;
            case BACKWARD:
                changeShakeText("BACKWARD");
                changeBackgroundColor(getResources().getColor(R.color.colorBackward));
                break;
            case NONE:
                changeShakeText(getResources().getString(R.string.accelerometer_text));
                break;
        }
    }

    private void changeBackgroundColor(int color) {
//        rootView.setBackgroundColor(color);
    }

    private void changeShakeText(String text){
        textView.setText(text);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Обработка изменения точности датчика
    }
}