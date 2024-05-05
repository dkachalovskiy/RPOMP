package com.example.lab1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;

public class VievBase extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE , null);
        //db.execSQL("DROP TABLE students;");

        DBHelper dbh = new DBHelper(getBaseContext());
        SQLiteDatabase db = dbh.getWritableDatabase();



        TableLayout tableLayout = new TableLayout( this);
        Cursor query = db.rawQuery("SELECT * FROM students;", null);
        for(;query.moveToNext();){
            String name = query.getString(0);
            String time = query.getString(1);
            String ID = query.getString(2);

            TableRow tableRow1 = new TableRow(this);

            TextView textView1 = new TextView(this);
            textView1.setText(name);
            textView1.setPadding(0,0,80,0);
            TextView textView2 = new TextView(this);
            textView2.setText(time);
            textView2.setPadding(0,0,80,0);
            TextView textView3 = new TextView(this);
            textView3.setText(ID);
            textView3.setPadding(0,0,40,0);


            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView temp =(TextView) v;
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("aaa",temp.getText());
                    clipboard.setPrimaryClip(clip);
                }
            });
            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView temp =(TextView) v;
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("aaa",temp.getText());
                    clipboard.setPrimaryClip(clip);
                }
            });

            tableRow1.addView(textView1, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            tableRow1.addView(textView2, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            tableRow1.addView(textView3, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            tableRow1.setPadding(0,100,0,10);

            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
            horizontalScrollView.addView(tableRow1);
            tableLayout.addView(horizontalScrollView);
        }
        ScrollView space = new ScrollView( this);

        space.addView(tableLayout);
        space.setPadding(35,45,0,45);
        setContentView(space);

        db.close();
    }
}
////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////
class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "app.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String[] FIO = { "Иванов Иван Иванович", "Марьина Марья Мармоковна", "Петренко Петр Петрович",
                "Антонов Антон Антонович", "Дашкевич Даша Дариевна", "Жук Иван Федорович",
                "Борисов Борис Борисович", "Костюшко Костя Константинович", "Игривый Игорь Игоревич",
                "Педальный Педро Педрович", "Ничингер Кирилл Сергеевич", "Качаловский Даниил Сергеевич",
                "Шварцнеггер Арнольд Сталонович","Кирпичный Богдан Артемович","Угледарский Гор Осирисович",
                "Гномский Гугл Айпадович"};

        ContentValues cv = new ContentValues();

        db.execSQL("CREATE TABLE IF NOT EXISTS students (\n" +
                "\t\"FIO\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"TIME\"\tTEXT NOT NULL,\n" +
                "\t\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");");

        // заполняем ее
        for (int i = 0; i < FIO.length; i++) {
            cv.clear();
            cv.put("FIO", FIO[i]);
            cv.put("TIME", new Date().toString());
            db.insert("students", null, cv);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        super.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS students (\n" +
                "\t\"FIO\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"TIME\"\tTEXT NOT NULL,\n" +
                "\t\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");");
        return super.getWritableDatabase();
    }
}
