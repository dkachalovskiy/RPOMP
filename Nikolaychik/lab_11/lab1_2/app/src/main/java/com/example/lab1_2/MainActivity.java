package com.example.lab1_2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lab1_2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void VievStart(View v){
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, VievBase.class);
        // запуск activity
        startActivity(intent);
    }
    public void GOVNO(View v){
//        // действия, совершаемые после нажатия на кнопку
//        // Создаем объект Intent для вызова новой Activity
//        Intent iintent = new Intent(this, MyActions.class);
//        // запуск activity
//        startActivity(iintent);
        // a=findViewById(R.id.textView);
        //a.setText(get);
    }
}