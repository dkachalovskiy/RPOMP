package com.example.lab1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DeleteActivity extends AppCompatActivity {
    CustomDbHelper databaseHelper;
    SQLiteDatabase db;
    EditText id;
    TextView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        databaseHelper = new CustomDbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        id = findViewById(R.id.delete_idTextEdit);
        errorView = findViewById(R.id.delete_errorTextView);
    }

    public void onPerformDeleteClick(View view) {
        if (databaseHelper.delete(db, id.getText().toString())) {
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