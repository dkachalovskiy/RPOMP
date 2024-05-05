package com.example.lab1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.lab1.CustomDbHelper.*;

public class MainActivity extends AppCompatActivity {

    CustomDbHelper databaseHelper;
    SQLiteDatabase db;

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

    public void onDeleteClick(View view) {
        Intent intent = new Intent(this, DeleteActivity.class);
        startActivity(intent);
    }

    public void onUpdateClick(View view) {
        Intent intent = new Intent(this, BeforeUpdateActivity.class);
        startActivity(intent);
    }
}