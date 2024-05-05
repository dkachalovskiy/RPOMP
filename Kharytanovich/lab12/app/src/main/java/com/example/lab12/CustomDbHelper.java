package com.example.lab12;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int VERSION = 2;
    static final String TABLE_NAME = "group_mates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TRACK = "first";
    public static final String COLUMN_CREATED_AT = "created_at";

    public CustomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_TRACK + " TEXT, "
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insert(SQLiteDatabase db, String author, String track) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_AUTHOR + ", " + COLUMN_TRACK
                    + ") VALUES ('" + author + "', '" + track + "');");
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean isTrackEqualsLast(SQLiteDatabase db, String author, String track) {
        if (db.isReadOnly()) return false;
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    "(SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC " + "LIMIT 1) " +
                    "WHERE " + COLUMN_AUTHOR + " like '" + author + "' " +
                    "AND " + COLUMN_TRACK + " like '" + track + "';", null);
            return cursor.moveToNext();
        } catch (SQLiteException e) {
            return false;
        }
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

}
