package com.example.lab1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.lab1.CustomDbHelper.*;

public class BeforeUpdateActivity extends AppCompatActivity {
    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    EditText lastname;
    TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_update);

        databaseHelper = new CustomDbHelper(getApplicationContext());

        lastname = findViewById(R.id.beforeUpdate_lastnameTextEdit);
        errorView = findViewById(R.id.beforeUpdate_errorTextView);

        db = databaseHelper.getReadableDatabase();
    }


    public void onPerformBeforeUpdateClick(View view) {
        String oldLastName = lastname.getText().toString();
        cursor = db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_CREATED_AT + " FROM " + CustomDbHelper.TABLE_NAME + " WHERE " + CustomDbHelper.COLUMN_NAME + " like '" + oldLastName + "%';", null);
        if (cursor.moveToNext()) {
            Intent intent = new Intent(this, UpdateActivity.class);
            intent.putExtra("oldName", cursor.getString(1));
            startActivity(intent);
            cursor.close();
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}