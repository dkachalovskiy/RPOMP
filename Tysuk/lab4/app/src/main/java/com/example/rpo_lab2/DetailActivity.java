package com.example.rpo_lab2;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Button backBut;
    private Button updateBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backBut = (Button) findViewById(R.id.back);
        updateBut = (Button) findViewById(R.id.update);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");

        TextView idTextView = findViewById(R.id.idtxt);
        TextView titleTextView = findViewById(R.id.titletxt);
        TextView bodyTextView = findViewById(R.id.bodytxt);

        idTextView.setText("Id:"+ id +"\n");
        titleTextView.setText("Title:-"+ title +"\n");
        bodyTextView.setText("Body:-"+body);

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(DetailActivity.this, MainActivity.class);

                String titleValue = String.valueOf(titleTextView.getText());
                String bodyValue = String.valueOf(bodyTextView.getText());

                intent2.putExtra("idvalue", id);
                intent2.putExtra("titlevalue", titleValue);
                intent2.putExtra("bodyvalue", bodyValue);

                setResult(Activity.RESULT_OK, intent2);
                finish();
            }
        });

        updateBut.setOnClickListener(view ->  {
                Intent intent2 = new Intent(DetailActivity.this, MainActivity.class);

                String titleValue = String.valueOf(titleTextView.getText());
                String bodyValue = String.valueOf(bodyTextView.getText());

                intent2.putExtra("idvalue", id);
                intent2.putExtra("titlevalue", titleValue);
                intent2.putExtra("bodyvalue", bodyValue);

                setResult(Activity.RESULT_OK, intent2);
                finish();
        });
    }
}
