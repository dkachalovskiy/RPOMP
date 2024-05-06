package com.example.mobileslab2;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAcceleration(View v) {
        startActivity(new Intent(this,
                AccelerationOverviewActivity.class));
    }

    public void onClickGPS(View v) {
        startActivity(new Intent(this, GPSActivity.class));
    }

    public void onClickSensor(View v) {
        startActivity(new Intent(this, SensorActivity.class));
    }
}