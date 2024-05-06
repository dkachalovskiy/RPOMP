package com.example.rpo_lab2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private Button uploadDataBut;
    private Button exitBut;
    private EditText dataSize;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        uploadDataBut = (Button) findViewById(R.id.uploadDataButton);
        exitBut = (Button) findViewById(R.id.exit);
        dataSize = (EditText) findViewById(R.id.dataSize);

        uploadDataBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String numberDataSize = String.valueOf(dataSize.getText());
                    Exception NumberFormatException = null;
                    if (Integer.parseInt(numberDataSize) > 100 || Integer.parseInt(numberDataSize) < 0) throw NumberFormatException;

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("datasize", numberDataSize);
                    startActivity(intent);
                } catch (Exception e) {
                    errorHandler(e.getMessage(), "Error");
                }
            }
        });

        exitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
    }

    private void errorHandler(String message, String title){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(StartActivity.this);
        alertbox.setTitle(title);
        alertbox.setMessage(message);
        alertbox.setCancelable(true);

        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        AlertDialog alertbox2 = alertbox.create();
        alertbox2.show();
    }
}
