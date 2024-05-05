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
    String oldName;
    EditText name;
    TextView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        name = findViewById(R.id.update_nameTextEdit);
        errorView = findViewById(R.id.update_errorTextView);

        oldName = getIntent().getStringExtra("oldName");
        name.setText(oldName);
    }

    public void onPerformUpdateClick(View view) {
        if (databaseHelper.update(db, oldName, name.getText().toString())) {
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