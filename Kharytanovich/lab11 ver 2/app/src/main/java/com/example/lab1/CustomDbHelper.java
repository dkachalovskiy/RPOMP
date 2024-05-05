package com.example.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int VERSION = 2;
    static final String TABLE_NAME = "group_mates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_MIDDLE_NAME = "middle_name";
    public static final String COLUMN_CREATED_AT = "created_at";

    public CustomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_MIDDLE_NAME + " TEXT, "
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);");
        initTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_LAST_NAME + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FIRST_NAME + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MIDDLE_NAME + " TEXT;");
        } catch (SQLiteException exception) {

        }
        db.execSQL("UPDATE " + TABLE_NAME + " " +
                "SET " + COLUMN_LAST_NAME + " = SUBSTR(name, 1, INSTR(name, ' ') - 1)," +
                "    " + COLUMN_FIRST_NAME + " = SUBSTR(name, INSTR(name, ' ') + 1, " +
                "                        INSTR(SUBSTR(name, INSTR(name, ' ') + 1), ' ') - 1)," +
                "    " + COLUMN_MIDDLE_NAME + " = SUBSTR(SUBSTR(name, INSTR(name, ' ') + 1), " +
                "                         INSTR(SUBSTR(name, INSTR(name, ' ') + 1), ' ') + 1);");

    }

    public void initTable(SQLiteDatabase db) {
        if (db.isReadOnly()) return;
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "';");
        insert(db, "Харитонович", "Захар", "Сергеевич");
        insert(db, "Ступак", "Дмитрий", "Русланович");
        insert(db, "Тусюк", "Тимофей", "Владимирович");
        insert(db, "Шубич", "Дарья", "Константиновна");
        insert(db, "Оводок", "Вадим", "Вячеславович");
    }

    public boolean insert(SQLiteDatabase db, String lastname, String name, String middleName) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_LAST_NAME + ", " + COLUMN_FIRST_NAME + ", " + COLUMN_MIDDLE_NAME
                    + ") VALUES ('" + lastname + "', '" + name + "', '" + middleName + "');");
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

    public boolean update(SQLiteDatabase db, String oldLastName, String lastName, String name, String middleName) {
        if (db.isReadOnly()) return false;
        try {
            db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_NAME + " = '" + lastName + "', " + COLUMN_FIRST_NAME + " = '" + name + "', " + COLUMN_MIDDLE_NAME + " = '" + middleName + "' WHERE " + COLUMN_LAST_NAME + " like '" + oldLastName + "';");
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }
}
