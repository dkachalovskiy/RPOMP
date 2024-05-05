package com.example.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int VERSION = 1;
    static final String TABLE_NAME = "group_mates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_AT = "created_at";

    public CustomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);");
        initTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME + " TEXT;");
        } catch (SQLiteException e) {

        }
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME + " = last_name || ' ' || first_name || ' ' || middle_name;");
    }

    public void initTable(SQLiteDatabase db) {
        if (db.isReadOnly()) return;
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME +"';");
        insert(db, "Харитонович Захар Сергеевич");
        insert(db, "Ступак Дмитрий Русланович");
        insert(db, "Тусюк Тимофей Владимирович");
        insert(db, "Шубич Дарья Константиновна");
        insert(db, "Оводок Вадим Вячеславович");
    }

    public boolean insert(SQLiteDatabase db, String name) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ") VALUES ('" + name + "');");
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

    public boolean update(SQLiteDatabase db, String oldName, String name) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME + " = '" + name + "' WHERE " + COLUMN_NAME + " like '" + oldName + "';");
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }
}
