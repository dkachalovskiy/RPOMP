package com.example.lab15;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CustomDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int VERSION = 1;
    static final String TABLE_NAME = "notifications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT_VALUE = "text_value";
    public static final String COLUMN_RECEIVE_DATETIME = "receive_datetime";

    public CustomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_TEXT_VALUE + " TEXT, "
                + COLUMN_RECEIVE_DATETIME + " DATETIME);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getLastId(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1;", null);
        cursor.moveToNext();
        String id = cursor.getString(0);
        cursor.close();
        return id;
    }

    public boolean insert(SQLiteDatabase db, String lastname, String name, Calendar receiveDateTime) {
        if (db.isReadOnly()) return false;


        try {
            db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_TITLE + ", " + COLUMN_TEXT_VALUE + ", " + COLUMN_RECEIVE_DATETIME
                    + ") VALUES ('" + lastname + "', '" + name + "', '" + receiveDateTime.getTime() + "');");
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean delete(SQLiteDatabase db, String id) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id);
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean update(SQLiteDatabase db, String oldLastName, String lastName, String name) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_TITLE + " = '" + lastName + "', " + COLUMN_TEXT_VALUE + " = '" + name + "' WHERE " + COLUMN_TITLE + " like '" + oldLastName + "';");
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }
}
