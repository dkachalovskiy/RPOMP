package com.example.mynotes_berdnikova.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.mynotes_berdnikova.NotesAdapter;


public class DB {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "goods";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    public NotesAdapter noteAdapter;
    public DB(Context ctx) {
        mCtx = ctx;
    }


    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void addRec(String description) {
        ContentValues cv = new ContentValues();
        cv.put("description", description);
        mDB.insert(DB_TABLE, null, cv);
    }

    public void updateDB(int id, String description) {
        ContentValues cv = new ContentValues();
        cv.put("description", description);
        mDB.update(DB_TABLE, cv, "id = ?",
                new String[]{String.valueOf(id)});
    }

    public void delRec(int id) {
        mDB.delete(DB_TABLE, "id = " + id, null);
    }
    public void delAll() {
        mDB.delete(DB_TABLE, null, null);
    }
    public boolean containRecById(long id) {
        long count = DatabaseUtils.queryNumEntries(mDB, DB_TABLE, "id = ?", new String[]{String.valueOf(id)});
        return count > 0;
    }
}
