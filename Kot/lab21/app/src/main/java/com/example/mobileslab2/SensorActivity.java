package com.example.mobileslab2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor pressureSensor;

    private TextView speedTextView;
    private TextView accelerationTextView;
    private TextView altitudeTextView;

    private float[] lastAccelerometerValues = new float[3];
    private long lastAccelerometerTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        speedTextView = findViewById(R.id.speedTextView);
        accelerationTextView = findViewById(R.id.accelerationTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            System.arraycopy(event.values, 0, lastAccelerometerValues, 0, event.values.length);
            lastAccelerometerTimestamp = event.timestamp;
            updateAcceleration();
            updateSpeed();
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            updateAltitude(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    private long lastUpdateTimestamp = 0;

    private void updateSpeed() {
        // Получаем данные об ускорении по каждой из осей
        float accelerationX = lastAccelerometerValues[0];
        float accelerationY = lastAccelerometerValues[1];
        float accelerationZ = lastAccelerometerValues[2];

        // Вычисляем общее ускорение
        float acceleration = (float) Math.sqrt(accelerationX * accelerationX +
                accelerationY * accelerationY +
                accelerationZ * accelerationZ);

        // Получаем текущее время
        long currentTime = System.nanoTime();

        // Вычисляем deltaTime
        float deltaTimeSeconds = (currentTime - lastUpdateTimestamp) / 1_000_000_000.0f;

        // Обновляем метку времени последнего обновления
        lastUpdateTimestamp = currentTime;

        // Вычисляем скорость
        float speed = acceleration * deltaTimeSeconds;

        // Обновляем текстовое представление скорости на экране
        speedTextView.setText(String.format("Kot A. Speed: %.2f m/s", speed));
    }


    private void updateAcceleration() {
        float acceleration = (float) Math.sqrt(lastAccelerometerValues[0] * lastAccelerometerValues[0]
                + lastAccelerometerValues[1] * lastAccelerometerValues[1]
                + lastAccelerometerValues[2] * lastAccelerometerValues[2]);
        accelerationTextView.setText(String.format("Acceleration: %.2f m^2/s", acceleration));
    }

    private void updateAltitude(float pressure) {
        // Calculate altitude using pressure sensor data
        float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
        altitudeTextView.setText(String.format("Altitude: %.2f meters", altitude));
    }
}
