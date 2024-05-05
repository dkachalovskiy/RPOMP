package com.example.lab1;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class CustomApplication extends Application {

    public CustomApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CustomDbHelper databaseHelper = new CustomDbHelper(getApplicationContext());
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        databaseHelper.initTable(db);
//        db.close();
    }

}
