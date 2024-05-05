package com.example.lab15;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CustomDbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new CustomDbHelper(getApplicationContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void onViewClick(View view) {
        Intent intent = new Intent(this, TableViewActivity.class);
        startActivity(intent);
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

}