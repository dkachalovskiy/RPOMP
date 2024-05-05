package com.example.lab1;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

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

        cursor =  db.rawQuery("select " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_CREATED_AT + " from "+ CustomDbHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
            TextView _id  = tableRow.findViewById(R.id._id_column);
            TextView name  = tableRow.findViewById(R.id.name_column);
            TextView createdAt  = tableRow.findViewById(R.id.created_at_column);

            _id.setText(cursor.getString(0));
            name.setText(cursor.getString(1));
            createdAt.setText(cursor.getString(2));
            tableLayout.addView(tableRow);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        cursor.close();
    }
}