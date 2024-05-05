package com.example.lab1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {

    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    EditText name;
    TextView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        name = findViewById(R.id.create_nameTextEdit);
        errorView = findViewById(R.id.create_errorTextVie);

    }

    public void onPerformCreateClick(View view) {
        if (databaseHelper.insert(db, name.getText().toString())) {
            Intent intent = new Intent(this, TableViewActivity.class);
            startActivity(intent);
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }
}