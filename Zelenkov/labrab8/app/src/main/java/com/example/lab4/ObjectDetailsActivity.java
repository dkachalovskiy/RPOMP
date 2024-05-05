package com.example.lab4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ObjectDetailsActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewEmail;

    private TextView textViewGender;

    private TextView textViewId;


    @SuppressLint("MissingInflatedId")
    // В методе onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // Получаем переданные значения имени и электронной почты
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String idd = getIntent().getStringExtra("id");
        String gender = getIntent().getStringExtra("gender");
        String mobile = getIntent().getStringExtra("mobile");
        String home = getIntent().getStringExtra("home");


        // Связываем TextView с ресурсами в макете
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewGender = findViewById(R.id.textViewGender);
        TextView textViewId = findViewById(R.id.textViewId);
        TextView textViewMobile = findViewById(R.id.textViewMobile);
        TextView textViewHome = findViewById(R.id.textViewHome);



        // Устанавливаем текст в TextView
        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewId.setText(idd);
        textViewGender.setText(gender);
        textViewMobile.setText(mobile);
        textViewHome.setText(home);


    }
}