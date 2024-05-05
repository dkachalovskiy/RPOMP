package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.w3c.dom.Node;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Найти кнопку "назад" по идентификатору
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton btnBack =
                findViewById(R.id.btnBack);

        // Установить слушатель кликов на кнопку "назад"
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed(); // Вызов метода onBackPressed() при клике на кнопку "назад"
            }
        });
    }


    public void onClickerdBtnBack(View view) {
       // Intent intent = new Intent(this, Node.class);
        //startActivity(intent);
        setContentView(R.layout.activity_main);
    }
}