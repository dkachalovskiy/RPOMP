package com.example.lab15;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.lab15.CustomDbHelper.*;

public class NotificationViewActivity extends AppCompatActivity {

    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        try {
            cursor = db.rawQuery("select " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_TEXT_VALUE + ", "
                    + COLUMN_RECEIVE_DATETIME + " from " + CustomDbHelper.TABLE_NAME + " WHERE " + COLUMN_ID + " = "
                    + getIntent().getStringExtra("_id"), null);
        } catch (SQLiteException e) {
            finish();
        }

        if (cursor.moveToNext()) {

            TextView title = findViewById(R.id.view_titleText);
            TextView textValue = findViewById(R.id.view_textValue);
            TextView receiveDateTime = findViewById(R.id.view_receiveDateTime);

            id = cursor.getString(0);
            title.setText(cursor.getString(1));
            textValue.setText(cursor.getString(2));
            receiveDateTime.setText(cursor.getString(3));
        } else {
            finish();
        }
    }

    public void onPerformDeleteClick(View view) {
        if (databaseHelper.delete(db, id)) {
            Intent intent = new Intent(this, TableViewActivity.class);
            startActivity(intent);
        }
    }
}