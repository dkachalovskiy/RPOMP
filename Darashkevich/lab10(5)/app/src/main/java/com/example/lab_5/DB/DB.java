package com.example.lab_5.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab_5.NoteAdapter;

public class DB {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "goods";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    public NoteAdapter noteAdapter;
    public DB(Context ctx) {
        mCtx = ctx;
    }


    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }


    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }





    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }


    // добавить запись в DB_TABLE
    public void addRec(String description) {
        ContentValues cv = new ContentValues();
        cv.put("description", description);
        mDB.insert(DB_TABLE, null, cv);
    }

    // обновить запись в DB_TABLE
    public void update(int id, String description) {
        ContentValues cv = new ContentValues();
        cv.put("description", description);
        mDB.update(DB_TABLE, cv, "id = ?",
                new String[]{String.valueOf(id)});
    }


    // удалить запись из DB_TABLE
    public void delRec(int id) {
        mDB.delete(DB_TABLE, "id = " + id, null);
    }


    // удалить все записи из DB_TABLE
    public void delAll() {
        mDB.delete(DB_TABLE, null, null);
    }

        // получить запись из DB_TABLE по идентификатору (ID)
        public boolean containRecById(long id) {
            long count = DatabaseUtils.queryNumEntries(mDB, DB_TABLE, "id = ?", new String[]{String.valueOf(id)});
            return count > 0;
        }
}
