package com.example.lab15;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity {

    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    EditText title;
    EditText textValue;
    TextView errorView;
    Calendar receiveDateTime = new GregorianCalendar(TimeZone.getTimeZone("GMT+3"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        title = findViewById(R.id.create_titleTextEdit);
        textValue = findViewById(R.id.create_textValueTextEdit);
        errorView = findViewById(R.id.create_errorTextVie);

    }

    public void onPerformCreateClick(View view) {
        if (databaseHelper.insert(db, title.getText().toString(), textValue.getText().toString(), receiveDateTime)) {
            setAlarm();
            Intent intent = new Intent(this, TableViewActivity.class);
            startActivity(intent);
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    public void setTime(View view) {
        int hour = receiveDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = receiveDateTime.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    receiveDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    receiveDateTime.set(Calendar.MINUTE, minute);
                    receiveDateTime.set(Calendar.SECOND, 0);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void setDate(View view) {
        int year = receiveDateTime.get(Calendar.YEAR);
        int month = receiveDateTime.get(Calendar.MONTH);
        int dayOfMonth = receiveDateTime.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (view.isShown()) {
                    receiveDateTime.set(Calendar.YEAR, year);
                    receiveDateTime.set(Calendar.MONTH, month);
                    receiveDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myDateListener, year, month, dayOfMonth);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void setAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);

        intent.putExtra("_id", databaseHelper.getLastId(db));
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("text", textValue.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE );
        am.set(AlarmManager.RTC_WAKEUP, receiveDateTime.getTimeInMillis(), pendingIntent);
    }
}