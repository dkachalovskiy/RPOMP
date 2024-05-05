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
    EditText lastname;
    EditText name;
    EditText middleName;
    TextView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        lastname = findViewById(R.id.create_lastnameTextEdit);
        name = findViewById(R.id.create_nameTextEdit);
        middleName = findViewById(R.id.create_middleNameTextEdit);
        errorView = findViewById(R.id.create_errorTextVie);

    }

    public void onPerformCreateClick(View view) {
        if (databaseHelper.insert(db, lastname.getText().toString(), name.getText().toString(), middleName.getText().toString())) {
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