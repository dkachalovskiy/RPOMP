package ru.startandroid.develop.database;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class task2 extends AppCompatActivity {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "Одногруппники";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "ФИО";
    private static final String COLUMN_LAST_NAME = "Фамилия";
    private static final String COLUMN_FIRST_NAME = "Имя";
    private static final String COLUMN_MIDDLE_NAME = "Отчество";
    private static final String COLUMN_TIMESTAMP = "Время";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2);

        openDatabase();
        upgrade();

        TextView dataBaseVersion = findViewById(R.id.dataBaseVersion);
        dataBaseVersion.setText("Версия БД: " + database.getVersion());

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(task2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button hideButton = findViewById(R.id.hide);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout tableLayout = findViewById(R.id.tableLayout);
                tableLayout.setVisibility(View.GONE);
            }
        });

        Button showButton = findViewById(R.id.show);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout tableLayout = findViewById(R.id.tableLayout);
                tableLayout.setVisibility(View.VISIBLE);
            }
        });
        displayDataInTableLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabase();
    }
    private void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    private void openDatabase() {
        try {
            database = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(DATABASE_NAME), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upgrade() {
        if (database != null && database.isOpen()) {
            if(database.getVersion() == 2) {
                return;
            }
            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = database.rawQuery(selectQuery, null);
            List<String[]> tempData = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String[] nameParts = name.split(" ");
                String last_name = nameParts[0];
                String first_name = nameParts[1];
                String middle_name = nameParts[2];
                String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

                String[] rowData = {id, last_name, first_name, middle_name, timestamp};
                tempData.add(rowData);
            }
            cursor.close();

            String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            database.execSQL(dropTableQuery);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = dateFormat.format(new Date());
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_MIDDLE_NAME + " TEXT, " +
                    COLUMN_TIMESTAMP + " TEXT DEFAULT '" + currentTime + "')";
            database.execSQL(createTableQuery);

            for (String[] rowData : tempData) {
                int id = Integer.parseInt(rowData[0]);
                String lastName = rowData[1];
                String firstName = rowData[2];
                String middleName = rowData[3];
                String timestamp = rowData[4];

                String insertQuery = "INSERT INTO " + TABLE_NAME +
                        "(" + COLUMN_ID + ", " + COLUMN_LAST_NAME + ", " + COLUMN_FIRST_NAME + ", " +
                        COLUMN_MIDDLE_NAME + ", " + COLUMN_TIMESTAMP + ")" +
                        " VALUES (" + id + ", '" + lastName + "', '" + firstName + "', '" + middleName + "', '" + timestamp + "')";
                database.execSQL(insertQuery);
            }
            // Установка новой версии базы данных
            database.setVersion(DATABASE_VERSION);
            closeDatabase();
            openDatabase();
        }
    }

    private void displayDataInTableLayout() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Очистить все существующие строки, если есть
        tableLayout.removeAllViews();

        // Получить данные из базы данных
        if (database != null && database.isOpen()) {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = database.rawQuery(selectQuery, null);

            // Создать заголовок таблицы
            TableRow headerRow = new TableRow(this);
            TextView idHeader = new TextView(this);
            idHeader.setText(COLUMN_ID);
            idHeader.setPadding(10, 0, 10, 0);
            TextView lastNameHeader = new TextView(this);
            lastNameHeader.setText(COLUMN_LAST_NAME);
            lastNameHeader.setPadding(10, 0, 10, 0);
            TextView firstNameHeader = new TextView(this);
            firstNameHeader.setText(COLUMN_FIRST_NAME);
            firstNameHeader.setPadding(10, 0, 10, 0);
            TextView middleNameHeader = new TextView(this);
            middleNameHeader.setText(COLUMN_MIDDLE_NAME);
            middleNameHeader.setPadding(10, 0, 10, 0);
            TextView timestampHeader = new TextView(this);
            timestampHeader.setText(COLUMN_TIMESTAMP);
            timestampHeader.setPadding(10, 0, 10, 0);

            headerRow.addView(idHeader);
            headerRow.addView(lastNameHeader);
            headerRow.addView(firstNameHeader);
            headerRow.addView(middleNameHeader);
            headerRow.addView(timestampHeader);
            tableLayout.addView(headerRow);

            // Создать строки данных
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
                String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
                String middleName = cursor.getString(cursor.getColumnIndex(COLUMN_MIDDLE_NAME));
                String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                String time = timestamp.substring(timestamp.indexOf(' ') + 1);

                TableRow dataRow = new TableRow(this);
                TextView idText = new TextView(this);
                idText.setText(String.valueOf(id));
                idText.setPadding(10, 0, 10, 0);
                TextView lastNameText = new TextView(this);
                lastNameText.setText(lastName);
                lastNameText.setPadding(10, 0, 10, 0);
                TextView firstNameText = new TextView(this);
                firstNameText.setText(firstName);
                firstNameText.setPadding(10, 0, 10, 0);
                TextView middleNameText = new TextView(this);
                middleNameText.setText(middleName);
                middleNameText.setPadding(10, 0, 10, 0);
                TextView timestampText = new TextView(this);
                timestampText.setText(time);
                timestampText.setPadding(10, 0, 10, 0);

                dataRow.addView(idText);
                dataRow.addView(lastNameText);
                dataRow.addView(firstNameText);
                dataRow.addView(middleNameText);
                dataRow.addView(timestampText);
                tableLayout.addView(dataRow);
            }
            cursor.close();
        }
    }
}