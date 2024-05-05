package com.example.lab15;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.lab15.CustomDbHelper.*;

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

        cursor = db.rawQuery("select " + COLUMN_ID + ", " + COLUMN_TITLE + ", "
                + COLUMN_RECEIVE_DATETIME + " from " + CustomDbHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
            TextView _id = tableRow.findViewById(R.id._id_column);
            TextView title = tableRow.findViewById(R.id.title_column);
            TextView receiveDateTime = tableRow.findViewById(R.id.receive_date_time_column);

            _id.setText(cursor.getString(0));
            title.setText(cursor.getString(1));
            receiveDateTime.setText(cursor.getString(2));

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), NotificationViewActivity.class);
                    intent.putExtra("_id", _id.getText());
                    startActivity(intent);
                }
            });
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