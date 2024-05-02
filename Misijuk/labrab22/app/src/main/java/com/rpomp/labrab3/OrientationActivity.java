package com.rpomp.labrab3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OrientationActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor orientationSensor;
    private ImageView levelImageView;
    private TextView data_orient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);

        levelImageView = findViewById(R.id.levelImageView);
        data_orient = findViewById((R.id.data_orient));

        // Получаем экземпляр SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Получаем экземпляр сенсора ориентации
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Регистрируем слушатель событий ориентации
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Отменяем регистрацию слушателя событий ориентации при приостановке активности
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Получаем данные ориентации из события
        float roll = event.values[2];

        data_orient.setText(String.format("Deg: %s", roll));

        levelImageView.setRotation(roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Метод не требует реализации, оставляем пустым
    }
}
