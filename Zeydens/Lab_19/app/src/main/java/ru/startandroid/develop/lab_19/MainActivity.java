package ru.startandroid.develop.lab_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSens = (Button) findViewById(R.id.btnSens);
        Button btnAcc = (Button) findViewById(R.id.btnAcc);
        Button btnOri = (Button) findViewById(R.id.btnOri);
        View.OnClickListener oclBtnSens = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Sensors.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclBtnAcc = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Acceleration.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclBtnOri = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Orientation.class);
                startActivity(intent);
            }
        };
        btnSens.setOnClickListener(oclBtnSens);
        btnAcc.setOnClickListener(oclBtnAcc);
        btnOri.setOnClickListener(oclBtnOri);
    }

}