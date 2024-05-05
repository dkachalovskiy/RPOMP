package com.example.json;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ObjectActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewBook;

    private TextView textViewGenre;

    private TextView textViewId;


    @SuppressLint("MissingInflatedId")
    // В методе onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        // Получаем переданные значения имени и электронной почты
        String name = getIntent().getStringExtra("name");
        String book = getIntent().getStringExtra("book");
        String idd = getIntent().getStringExtra("id");
        String genre = getIntent().getStringExtra("genre");
        String mobile = getIntent().getStringExtra("mobile");
        String home = getIntent().getStringExtra("home");


        // Связываем TextView с ресурсами в макете
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewBook = findViewById(R.id.textViewBook);
        TextView textViewGenre = findViewById(R.id.textViewGenre);
        TextView textViewId = findViewById(R.id.textViewId);
        TextView textViewMobile = findViewById(R.id.textViewMobile);
        TextView textViewHome = findViewById(R.id.textViewHome);



        // Устанавливаем текст в TextView
        textViewName.setText(name);
        textViewBook.setText(book);
        textViewId.setText(idd);
        textViewGenre.setText(genre);
        textViewMobile.setText(mobile);
        textViewHome.setText(home);


    }
}