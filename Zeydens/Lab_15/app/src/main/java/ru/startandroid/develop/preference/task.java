package ru.startandroid.develop.preference;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class task extends AppCompatActivity {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Уведомления";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_HEADER = "Заголовок";
    private static final String COLUMN_CONTENT = "Содержимое";
    private static final String COLUMN_TIME = "Дата_Получения";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        openDatabase();
        createTable();

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(task.this, MainActivity.class);
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

        Button deleteButton = findViewById(R.id.deleteRow);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание диалогового окна для ввода заголовка
                AlertDialog.Builder builder = new AlertDialog.Builder(task.this);
                builder.setTitle("Введите заголовок уведомления");
                final EditText input = new EditText(task.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Обработчик нажатия кнопки "Удалить"
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String header = input.getText().toString();
                        if (database != null && database.isOpen()) {
                            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_HEADER + " = ?";
                            database.execSQL(deleteQuery, new String[]{header});
                            displayDataInTableLayout();
                        }
                    }
                });

                // Обработчик нажатия кнопки "Отмена"
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Отображение диалогового окна
                builder.show();
            }
        });

        Button addRowButton = findViewById(R.id.addRow);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем диалоговое окно для добавления записи
                AlertDialog.Builder builder = new AlertDialog.Builder(task.this);
                builder.setTitle("Добавить уведомление");

                // Создаем макет для диалогового окна
                LinearLayout layout = new LinearLayout(task.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Создаем поле ввода для темы
                final EditText etDialogHeader = new EditText(task.this);
                etDialogHeader.setHint("Тема уведомления");
                layout.addView(etDialogHeader);

                // Создаем поле ввода для содержимого
                final EditText etDialogContent = new EditText(task.this);
                etDialogContent.setHint("Содержимое уведомления");
                layout.addView(etDialogContent);

                // Создаем кнопку для выбора даты и времени
                final Button btnDialogDateTime = new Button(task.this);
                btnDialogDateTime.setText("Выбрать дату и время");
                layout.addView(btnDialogDateTime);

                // Устанавливаем обработчик нажатия на кнопку выбора даты и времени
                btnDialogDateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateTimePicker();
                    }
                });

                builder.setView(layout);

                // Устанавливаем кнопки "Добавить" и "Отмена" в диалоговом окне
                builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String header = etDialogHeader.getText().toString();
                        String content = etDialogContent.getText().toString();

                        // Проверяем, что тема и содержимое не пустые
                        if (header.isEmpty() || content.isEmpty()) {
                            Toast.makeText(task.this, "Уведомление не была добавлена. Нет данных", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Добавляем запись в базу данных
                        addRecordToDatabase(header, content, selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);

                        dialog.dismiss();
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
            // Create the table
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HEADER + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_TIME + " TEXT)";
            database.execSQL(createTableQuery);

            // Удалить записи, время добавления которых меньше текущего
            String currentTime = getCurrentDateTime(); // Получаем текущее время
            String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME + " < '" + currentTime + "'";
            database.execSQL(deleteQuery);

            database.setVersion(DATABASE_VERSION);
            displayDataInTableLayout();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void addRecordToDatabase(String header, String content, int selectedYear, int selectedMonth, int selectedDay, int selectedHour, int selectedMinute) {
        // Форматирование выбранной даты и времени
        String dateTime = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);

        // Добавляем запись в базу данных
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEADER, header);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_TIME, dateTime);
        long rowId = database.insert(TABLE_NAME, null, values);

        displayDataInTableLayout();

        preNotif(header, content, dateTime, rowId);
        // Определение текущей даты и времени
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// Определение разницы во времени между текущим временем и выбранным временем
        LocalDateTime selectedDateTime = LocalDateTime.parse(dateTime, formatter);
        long delayInMillis = java.time.Duration.between(currentDateTime, selectedDateTime).toMillis();

// Создание компонента компоновщика задач
        ComponentName componentName = new ComponentName(getApplicationContext(), NotificationJobService.class);

// Создание данных для передачи в задачу
        PersistableBundle extras = new PersistableBundle();
        extras.putString("header", header);
        extras.putString("content", content);
        extras.putLong("rowId", rowId);

// Создание задачи для выполнения
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setMinimumLatency(delayInMillis)
                .setExtras(extras)
                .build();

// Запуск задачи в JobScheduler
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

//    private void addRecordToDatabase(String header, String content, int selectedYear, int selectedMonth, int selectedDay, int selectedHour, int selectedMinute) {
//        // Форматирование выбранной даты и времени
//        String dateTime = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
//
//        // Добавляем запись в базу данных
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_HEADER, header);
//        values.put(COLUMN_CONTENT, content);
//        values.put(COLUMN_TIME, dateTime);
//        long rowId = database.insert(TABLE_NAME, null, values);
//
//        displayDataInTableLayout();
//
//        preNotif(header, content, dateTime, rowId);
//        // Определение текущей даты и времени
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // Определение разницы во времени между текущим временем и выбранным временем
//        LocalDateTime selectedDateTime = LocalDateTime.parse(dateTime, formatter);
//        long delayInMillis = java.time.Duration.between(currentDateTime, selectedDateTime).toMillis();
//
//        // Планирование задачи ожидания до времени dateTime
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Код, который будет выполнен после ожидания
//                sendNotif(header, content, rowId);
//            }
//        }, delayInMillis);
//    }

    private void preNotif(String header, String content, String dateTime, long rowId) {
        // Создаем намерение для запуска активити с отображением уведомления
        Intent notificationIntent = new Intent(this, Notification.class);
        notificationIntent.putExtra("notificationTitle", header);
        notificationIntent.putExtra("notificationContent", content);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) rowId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создаем канал уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Создаем уведомление в шторке
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Уведомление: " + header)
                .setContentText("Запланировано на: " + dateTime)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));

        // Отображаем уведомление в шторке
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) rowId, notificationBuilder.build());
    }

    public void sendNotif(String header, String content, long rowId) {
        // Создаем намерение для запуска активити с отображением уведомления
        Intent notificationIntent = new Intent(this, Notification.class);
        notificationIntent.putExtra("notificationTitle", header);
        notificationIntent.putExtra("notificationContent", content);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) rowId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создаем канал уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Создаем уведомление в шторке
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Тема: " + header)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));

        // Отображаем уведомление в шторке
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) rowId, notificationBuilder.build());
    }


    // Глобальные переменные для хранения выбранной даты и времени
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    // Обработчик нажатия на кнопку выбора даты и времени
    private void showDateTimePicker() {
        // Получаем текущую дату и время
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Создаем DatePickerDialog для выбора даты
        DatePickerDialog datePickerDialog = new DatePickerDialog(task.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;

                // Создаем TimePickerDialog для выбора времени
                TimePickerDialog timePickerDialog = new TimePickerDialog(task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;

                        // Выводим выбранную дату и время
                        String dateTime = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay + " " +
                                selectedHour + ":" + selectedMinute;
                        Toast.makeText(task.this, "Выбранная дата и время: " + dateTime, Toast.LENGTH_SHORT).show();
                    }
                }, currentHour, currentMinute, true);

                // Отображаем TimePickerDialog
                timePickerDialog.show();
            }
        }, currentYear, currentMonth, currentDay);

        // Отображаем DatePickerDialog
        datePickerDialog.show();
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
            TextView headerText1 = new TextView(this);
            headerText1.setText(COLUMN_HEADER);
            TextView headerText2 = new TextView(this);
            headerText2.setText(COLUMN_CONTENT);
            TextView headerText3 = new TextView(this);
            headerText3.setText(COLUMN_TIME);

            // Установить отступы между столбцами
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);

            // Установить отступы для заголовков столбцов
            headerText1.setLayoutParams(layoutParams);
            headerText2.setLayoutParams(layoutParams);
            headerText3.setLayoutParams(layoutParams);

            headerRow.addView(headerText1);
            headerRow.addView(headerText2);
            headerRow.addView(headerText3);
            tableLayout.addView(headerRow);

            // Создать строки данных
            while (cursor.moveToNext()) {
                String header = cursor.getString(cursor.getColumnIndex(COLUMN_HEADER));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));

                TableRow dataRow = new TableRow(this);
                TextView dataText1 = new TextView(this);
                dataText1.setText(header);
                TextView dataText2 = new TextView(this);
                dataText2.setText(content);
                TextView dataText3 = new TextView(this);
                dataText3.setText(time);

                // Установить отступы между столбцами данных
                TableRow.LayoutParams dataLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                dataLayoutParams.setMargins(10, 0, 10, 0);

                dataText1.setLayoutParams(dataLayoutParams);
                dataText2.setLayoutParams(dataLayoutParams);
                dataText3.setLayoutParams(dataLayoutParams);

                dataRow.addView(dataText1);
                dataRow.addView(dataText2);
                dataRow.addView(dataText3);

                tableLayout.addView(dataRow);
            }
            cursor.close();
        }
    }
}