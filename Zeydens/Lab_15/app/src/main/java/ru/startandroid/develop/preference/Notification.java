package ru.startandroid.develop.preference;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Получение текста уведомления из Intent
        Intent intent = getIntent();
        if (intent != null) {
            String notificationTitle = intent.getStringExtra("notificationTitle");
            String notificationText = intent.getStringExtra("notificationContent");
            if (notificationText != null) {
                // Установка текста уведомления в TextView
                TextView titleTextView = findViewById(R.id.notificationTitle);
                TextView contentTextView = findViewById(R.id.notificationContent);
                titleTextView.setText("Тема: " + notificationTitle);
                contentTextView.setText(notificationText);
            }
        }
    }
}