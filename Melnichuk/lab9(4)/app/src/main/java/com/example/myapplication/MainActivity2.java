package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Найти кнопку "назад" по идентификатору
       // @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton btnBack = findViewById(R.id.btnBack);

        // Установить слушатель кликов на кнопку "назад"
        //btnBack.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
            //    onBackPressed(); // Вызов метода onBackPressed() при клике на кнопку "назад"
            //}
       // });
    }
}