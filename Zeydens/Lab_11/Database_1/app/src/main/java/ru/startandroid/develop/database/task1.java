package ru.startandroid.develop.database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class task1 extends AppCompatActivity {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Одногруппники";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "ФИО";
    private static final String COLUMN_TIMESTAMP = "Время";
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        openDatabase();
        createTable();


        TextView dataBaseVersion = findViewById(R.id.dataBaseVersion);
        dataBaseVersion.setText("Версия БД: " + database.getVersion());

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(task1.this, MainActivity.class);
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

        Button addRowButton = findViewById(R.id.addRow);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем диалоговое окно с полем ввода ФИО
                AlertDialog.Builder builder = new AlertDialog.Builder(task1.this);
                builder.setTitle("Добавить запись");

                // Создаем поле ввода для ФИО
                final EditText inputEditText = new EditText(task1.this);
                builder.setView(inputEditText);

                // Устанавливаем кнопки "Добавить" и "Отмена" в диалоговом окне
                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fio = inputEditText.getText().toString();

                        if (fio.isEmpty()) {
                            Toast.makeText(task1.this, "Запись не была добавлена. Нет данных", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String[] words = fio.split(" ");
                        if (words.length != 3) {
                            Toast.makeText(task1.this, "Запись не была добавлена. Ожидались Фамилия Имя Отчество", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (String word : words) {
                            if (!word.matches("[\\p{L}]+")) {
                                Toast.makeText(task1.this, "Запись не была добавлена. Ожидались только буквы", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // Добавляем ФИО в базу данных
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentTime = dateFormat.format(new Date());

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_NAME, fio);
                        values.put(COLUMN_TIMESTAMP, currentTime);

                        long insertedId = database.insert(TABLE_NAME, null, values);

                        if (insertedId != -1) {
                            // Успешно добавлено, обновляем таблицу
                            displayDataInTableLayout();
                        } else {
                            // Возникла ошибка при добавлении
                            Toast.makeText(task1.this, "Ошибка при добавлении строки", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Установка русских надписей на кнопках
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setText("Добавить");

                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setText("Отмена");
                    }
                });

                // Отображаем диалоговое окно
                dialog.show();
            }
        });

        Button addIvanButton = findViewById(R.id.addIvan);
        addIvanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastId = -1;
                String ivan = "Иванов Иван Иванович";
                // Выполните запрос для получения последнего ID
                Cursor cursor = database.rawQuery("SELECT ID FROM " + TABLE_NAME + " ORDER BY ID DESC LIMIT 1", null);
                if (cursor.moveToFirst()) {
                    lastId = cursor.getInt(cursor.getColumnIndex("ID"));
                }
                cursor.close();
                database.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME + " = '" + ivan + "' WHERE " + COLUMN_ID + " = " + lastId);
                displayDataInTableLayout();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDatabase();
    }

    private void openDatabase() {
        try {
            database = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(DATABASE_NAME), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    private void createTable() {
        if (database != null && database.isOpen()) {
            // Удалить таблицу, если она существует
            String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            database.execSQL(dropTableQuery);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = dateFormat.format(new Date());

            // Создать таблицу
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TIMESTAMP + " TEXT DEFAULT '" + currentTime + "')";
            database.execSQL(createTableQuery);

            // Добавление изначальных записей
            String insertQuery = "INSERT INTO " + TABLE_NAME +
                    "(" + COLUMN_NAME + ") VALUES " +
                    "('Зайд Халдун Алькатури'), " +
                    "('Семга Иван Васильевич'), " +
                    "('Крест Владимир Иванович'), " +
                    "('Шмыг Евгений Анатольевич'), " +
                    "('Блок Александр Сергеевич')";
            database.execSQL(insertQuery);
            database.setVersion(DATABASE_VERSION);
            displayDataInTableLayout();
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
            TextView nameHeader = new TextView(this);
            nameHeader.setText(COLUMN_NAME);
            TextView timeHeader = new TextView(this);
            timeHeader.setText(COLUMN_TIMESTAMP);

            // Установить отступы между столбцами
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);

            // Установить отступы для заголовков столбцов
            idHeader.setLayoutParams(layoutParams);
            nameHeader.setLayoutParams(layoutParams);
            timeHeader.setLayoutParams(layoutParams);

            headerRow.addView(idHeader);
            headerRow.addView(nameHeader);
            headerRow.addView(timeHeader);
            tableLayout.addView(headerRow);

            // Создать строки данных
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
                String time = timestamp.substring(timestamp.indexOf(' ') + 1);

                TableRow dataRow = new TableRow(this);
                TextView idText = new TextView(this);
                idText.setText(String.valueOf(id));
                TextView nameText = new TextView(this);
                nameText.setText(name);
                TextView timeText = new TextView(this);
                timeText.setText(time);

                // Установить отступы между столбцами данных
                TableRow.LayoutParams dataLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                dataLayoutParams.setMargins(10, 0, 10, 0);

                idText.setLayoutParams(dataLayoutParams);
                nameText.setLayoutParams(dataLayoutParams);
                timeText.setLayoutParams(dataLayoutParams);

                dataRow.addView(idText);
                dataRow.addView(nameText);
                dataRow.addView(timeText);

                tableLayout.addView(dataRow);
            }
            cursor.close();
        }
    }
}