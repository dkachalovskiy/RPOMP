package com.example.mobileslab1;

import android.content.Intent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSensList(View v) {
        startActivity(new Intent(this, SensorListActivity.class));
    }

    public void onClickSensLight(View v) {
        startActivity(new Intent(this, LightSensorActivity.class));
    }

    public void onClickSensAcceleration(View v) {
        startActivity(new Intent(this, AccelerationSensorActivity.class));
    }

    public void onClickSensOrientation(View v) {
        startActivity(new Intent(this, OrientationSensorActivity.class));
    }

}