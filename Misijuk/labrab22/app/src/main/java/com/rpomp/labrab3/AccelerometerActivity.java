package com.rpomp.labrab3;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ImageView arrowImageView;
    private TextView accel_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        arrowImageView = findViewById(R.id.arrowImageView);
        accel_data = findViewById(R.id.accel_data);

        // Получаем экземпляр SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Получаем экземпляр акселерометра
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Регистрируем слушатель событий акселерометра
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Отменяем регистрацию слушателя событий акселерометра при приостановке активности
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Получаем данные акселерометра из события
        float x = event.values[0];
        float y = event.values[1];

        accel_data.setText(String.format("X: %s, Y: %s", x, y));

        // Определяем направление движения по данным акселерометра
        int direction;
        if (Math.abs(x) > Math.abs(y)) {
            // Движение влево или вправо
            direction = (x < 0) ? R.drawable.right_arrow : R.drawable.left_arrow;
        } else {
            // Движение вверх или вниз
            direction = (y < 0) ? R.drawable.up_arrow : R.drawable.down_arrow;
        }

        // Обновляем отображение стрелок на интерфейсе
        arrowImageView.setImageResource(direction);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // dont change anything
    }

}