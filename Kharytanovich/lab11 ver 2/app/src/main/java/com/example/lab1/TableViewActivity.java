package com.example.lab1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.lab1.CustomDbHelper.*;

public class TableViewActivity extends AppCompatActivity {
    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        tableLayout = findViewById(R.id.tableLayout);

        db = databaseHelper.getReadableDatabase();

        cursor = db.rawQuery("select " + COLUMN_ID + ", " + COLUMN_LAST_NAME + ", " + COLUMN_FIRST_NAME + ", "
                + COLUMN_MIDDLE_NAME + ", " + COLUMN_CREATED_AT + " from " + CustomDbHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
            TextView _id = tableRow.findViewById(R.id._id_column);
            TextView lastname = tableRow.findViewById(R.id.lastname_column);
            TextView name = tableRow.findViewById(R.id.name_column);
            TextView middleName = tableRow.findViewById(R.id.middle_name_column);
            TextView createdAt = tableRow.findViewById(R.id.createad_at_column);

            _id.setText(cursor.getString(0));
            lastname.setText(cursor.getString(1));
            name.setText(cursor.getString(2));
            middleName.setText(cursor.getString(3));
            createdAt.setText(cursor.getString(4));
            tableLayout.addView(tableRow);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }
}