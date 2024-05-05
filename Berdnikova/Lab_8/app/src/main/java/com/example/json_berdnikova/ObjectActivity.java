package com.example.json_berdnikova;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ObjectActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewBook;

    private TextView textViewGenre;

    private TextView textViewId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        String name = getIntent().getStringExtra("name");
        String book = getIntent().getStringExtra("book");
        String idd = getIntent().getStringExtra("id");
        String genre = getIntent().getStringExtra("genre");
        String mobile = getIntent().getStringExtra("mobile");
        String home = getIntent().getStringExtra("home");

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewBook = findViewById(R.id.textViewBook);
        TextView textViewGenre = findViewById(R.id.textViewGenre);
        TextView textViewId = findViewById(R.id.textViewId);
        TextView textViewMobile = findViewById(R.id.textViewMobile);
        TextView textViewHome = findViewById(R.id.textViewHome);

        textViewName.setText(name);
        textViewBook.setText(book);
        textViewId.setText(idd);
        textViewGenre.setText(genre);
        textViewMobile.setText(mobile);
        textViewHome.setText(home);
    }
}
