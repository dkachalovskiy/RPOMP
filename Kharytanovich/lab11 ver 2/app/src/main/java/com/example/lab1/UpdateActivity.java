package com.example.lab1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class UpdateActivity extends AppCompatActivity {
    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    String oldLastName;
    String oldName;
    String oldMiddleName;
    EditText lastname;
    EditText name;
    EditText middleName;
    TextView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        lastname = findViewById(R.id.update_lastnameTextEdit);
        name = findViewById(R.id.update_nameTextEdit);
        middleName = findViewById(R.id.update_middleNameTextEdit);
        errorView = findViewById(R.id.update_errorTextView);

        oldLastName = getIntent().getStringExtra("oldLastName");
        oldName = getIntent().getStringExtra("oldName");
        oldMiddleName = getIntent().getStringExtra("oldMiddleName");

        lastname.setText(oldLastName);
        name.setText(oldName);
        middleName.setText(oldMiddleName);
    }

    public void onPerformUpdateClick(View view) {
        if (databaseHelper.update(db, oldLastName, lastname.getText().toString(), name.getText().toString(), middleName.getText().toString())) {
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