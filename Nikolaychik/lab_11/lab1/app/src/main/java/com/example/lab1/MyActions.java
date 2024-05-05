package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;

public class MyActions extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_actions);
        
    }
    public void ADDA(View e){
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        TextView name_text_view=findViewById(R.id.editText_ADD);
        String FIO = name_text_view.getText().toString();

        Cursor query = db.rawQuery("SELECT * FROM students WHERE (FIO='" + FIO + "');", null);
        if(query.getCount()>0)
        {
            TextView TV = findViewById(R.id.textViewADD);
            TV.setText("Запись уже существует");
            db.close();
        }
        else {
            db.execSQL("INSERT OR IGNORE INTO students (\"FIO\",\"TIME\") VALUES ('" + FIO + "', '" + new Date().toString() + "');");
        //db.execSQL("INSERT OR IGNORE INTO students (\"FIO\",\"TIME\") VALUES ('pedro', '45');");
            Cursor qquery = db.rawQuery("SELECT * FROM students WHERE (FIO='" + FIO + "');", null);

            if (qquery.getCount() > 0) {
                TextView TV = findViewById(R.id.textViewADD);
                TV.setText("Запись добавлена");
            }
            db.close();
        }
    }

    public void DELA(View e){
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        TextView name_text_view=findViewById(R.id.editText_DEL);
        String ID = name_text_view.getText().toString();

        Cursor query = db.rawQuery("SELECT * FROM students WHERE (ID='" + ID + "');", null);
        if(query.getCount()==0)
        {
            TextView TV = findViewById(R.id.textView_DEL);
            TV.setText("Запись не существует");
            db.close();
        }
        else {
            db.execSQL("DELETE FROM students WHERE (ID='" + ID + "');");
            Cursor qquery = db.rawQuery("SELECT * FROM students WHERE (ID='" + ID + "');", null);

            if (qquery.getCount() == 0) {
                TextView TV = findViewById(R.id.textView_DEL);
                TV.setText("Запись удалена");
            }
            db.close();
        }
    }
    public void ZAMA(View e){

        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        TextView name_text_view_old=findViewById(R.id.editText_ZAM1);
        TextView name_text_view_nev=findViewById(R.id.editText_ZAM2);
        String FIO_OLD = name_text_view_old.getText().toString();
        String FIO_NEV = name_text_view_nev.getText().toString();

        Cursor query = db.rawQuery("SELECT * FROM students WHERE (FIO='" + FIO_OLD + "');", null);
        Cursor query2 = db.rawQuery("SELECT * FROM students WHERE (FIO='" + FIO_NEV + "');", null);
        if(query.getCount()==0)
        {
            TextView TV = findViewById(R.id.textView_ZAM);
            TV.setText("Запись не существует");
            db.close();
        }
        else {
            if (query2.getCount()==0) {
                db.execSQL("UPDATE students SET FIO='"+FIO_NEV+"' WHERE (FIO='" + FIO_OLD + "');");
                Cursor qquery = db.rawQuery("SELECT * FROM students WHERE (FIO='" + FIO_NEV + "');", null);

                if (qquery.getCount() > 0) {
                    TextView TV = findViewById(R.id.textView_ZAM);
                    TV.setText("Запись обновлена");
                }
                db.close();
            }
            else {
                TextView TV = findViewById(R.id.textView_DEL);
                TV.setText("Запись вже существует");

                db.close();
            }
        }
    }
}