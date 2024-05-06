package ru.startandroid.develop.intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_CONTACT = 1;
    private String selectedPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Button callButton = findViewById(R.id.call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText number = findViewById(R.id.editTextNumber);
                String phoneNumber = number.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    String toDial = "tel: " + number.getText().toString();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));
                }
                else {
                    Toast.makeText(ContactActivity.this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button smsButton = findViewById(R.id.sms_button);
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText number = findViewById(R.id.editTextNumber);
                String phoneNumber = number.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    String message = "Привет, это тестовое сообщение!";
                    Uri smsUri = Uri.parse("smsto:" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                    intent.putExtra("sms_body", message);
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactActivity.this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button returnButton = findViewById(R.id.returnButtonContact);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Закрываем текущую активность (ContactActivity)
            }
        });

    }
}