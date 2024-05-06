package com.example.mobileslab2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.VideoView;

public class AccelerationOverviewActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private VideoView videoView;
    private final int[] videoResources = {R.raw.miku1, R.raw.miku2, R.raw.miku3, R.raw.miku4, R.raw.miku5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);
        videoView = findViewById(R.id.videoView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.miku1);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        // Release media players when the activity is paused
        videoView.stopPlayback();
    }

    private static final float SHAKE_THRESHOLD = 9f;
    private float lastX = 0;
    private int currentVideoIndex = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate acceleration magnitude
            float deltaX = Math.abs(x - lastX);
            lastX = x;

            // If the device is shaken, switch to the second video
            if (deltaX > SHAKE_THRESHOLD && x < 0) {
                if (currentVideoIndex < videoResources.length - 1) {
                    currentVideoIndex++; // Увеличиваем индекс для воспроизведения следующего видео
                }
                videoView.pause(); // Pause the first video
                Uri uri = Uri.parse("android.resource://" + getPackageName()
                        + "/" + videoResources[currentVideoIndex]);
                videoView.setVideoURI(uri);
                videoView.start(); // Start playing the second video
            }

            if (deltaX > SHAKE_THRESHOLD && x > 0) {
                if (currentVideoIndex > 0) {
                    currentVideoIndex--; // Уменьшаем индекс для воспроизведения предыдущего видео
                }
                videoView.pause(); // Pause the first video
                Uri uri = Uri.parse("android.resource://" + getPackageName()
                        + "/" + videoResources[currentVideoIndex]);
                videoView.setVideoURI(uri);
                videoView.start(); // Start playing the second video
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
