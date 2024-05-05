package com.example.lab1_2;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VievBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SQLiteDatabase dbb = SQLiteDatabase.openOrCreateDatabase("data/auf.db",null);
        SQLiteDatabase db = getBaseContext()
                .openOrCreateDatabase("/data/user/0/com.example.lab1/databases/app.db",
                        MODE_PRIVATE , null);
        //db.execSQL("DROP TABLE govno;");
        db.getPath();
        db.execSQL("CREATE TABLE IF NOT EXISTS govno (\n" +
                "\t\"FIO\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"TIME\"\tTEXT NOT NULL,\n" +
                "\t\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");");

        db.execSQL("INSERT OR IGNORE INTO govno (\"FIO\",\"TIME\") VALUES ('Tom Smith', '23'), ('John Dow', '31');");

        TableLayout tableLayout = new TableLayout( this);
        Cursor query = db.rawQuery("SELECT * FROM govno;", null);
        for(;query.moveToNext();){
            String name = query.getString(0);
            String time = query.getString(1);
            String ID = query.getString(2);
            TableRow tableRow1 = new TableRow(this);
            TextView textView1 = new TextView(this);
            textView1.setText(name);
            TextView textView2 = new TextView(this);
            textView2.setText(time);
            TextView textView3 = new TextView(this);
            textView3.setText(ID);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView temp =(TextView) v;
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("aaa",temp.getText());
                    clipboard.setPrimaryClip(clip);
                }
            });
            tableRow1.addView(textView1, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            tableRow1.addView(textView2, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            tableRow1.addView(textView3, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            tableRow1.setPadding(0,10,0,10);
            tableLayout.addView(tableRow1);
        }
        ScrollView space = new ScrollView( this);
        space.addView(tableLayout);
        space.setPadding(35,45,0,45);
        setContentView(space);

        db.close();
    }
}