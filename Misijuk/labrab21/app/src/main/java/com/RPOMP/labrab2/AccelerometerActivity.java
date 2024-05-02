package com.RPOMP.labrab2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        color1 = getResources().getColor(R.color.white);
        color2 = getResources().getColor(R.color.black);
        currentColor = color1;
        isColor1Active = true;
        shakeTimestamp = 0;
        currentShakeDirection = ShakeDirection.NONE;
        rootView = getWindow().getDecorView();
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
                    // Начало нового встряхивания
                    currentShakeDirection = detectShakeDirection(x, y, z);
                    shakeTimestamp = currentTime;
                } else {
                    if (elapsedTime < SHAKE_DURATION) {
                        // Продолжение встряхивания
                        ShakeDirection newShakeDirection = detectShakeDirection(x, y, z);
                        if (newShakeDirection != ShakeDirection.NONE && newShakeDirection != currentShakeDirection) {
                            // Направление встряхивания изменилось
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
        // Обработка встряхивания в различных направлениях
        switch (direction) {
            case LEFT:
                // Действия при встряхивании влево
                changeBackgroundColor(getResources().getColor(R.color.colorLeft));
                break;
            case RIGHT:
                // Действия при встряхивании вправо
                changeBackgroundColor(getResources().getColor(R.color.colorRight));
                break;
            case UP:
                // Действия при встряхивании вверх
                changeBackgroundColor(getResources().getColor(R.color.colorUp));
                break;
            case DOWN:
                // Действия при встряхивании вниз
                changeBackgroundColor(getResources().getColor(R.color.colorDown));
                break;
            case FORWARD:
                // Действия при встряхивании вперед
                changeBackgroundColor(getResources().getColor(R.color.colorForward));
                break;
            case BACKWARD:
                // Действия при встряхивании назад
                changeBackgroundColor(getResources().getColor(R.color.colorBackward));
                break;
            default:
                break;
        }
    }

    private void changeBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ниче не меняем
    }
}