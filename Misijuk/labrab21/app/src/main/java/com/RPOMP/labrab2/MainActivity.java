package com.RPOMP.labrab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccelerometerActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GPSActivity.class);
            startActivity(intent);
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SputnikActivity.class);
            startActivity(intent);
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OthersActivity.class);
            startActivity(intent);
        });
    }
}
