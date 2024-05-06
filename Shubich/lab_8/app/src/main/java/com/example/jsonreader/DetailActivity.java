package com.example.jsonreader;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Получение переданных данных JSON из Intent
        String jsonData = getIntent().getStringExtra("jsonData");

        if (jsonData != null && !jsonData.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                // Здесь отобразите подробную информацию из jsonObject на экране, например:
                String email = jsonObject.getString("email");
                String phone = jsonObject.getString("phone");
                String website = jsonObject.getString("website");
                String username = jsonObject.getString("username");

                TextView textViewName = findViewById(R.id.textViewUserName);
                TextView textViewPhone = findViewById(R.id.textViewPhoneName);
                TextView textViewEmail = findViewById(R.id.textViewEmailName);
                TextView textViewWebsite = findViewById(R.id.textWebSiteViewName);

                textViewName.setText(username);
                textViewPhone.setText(phone);
                textViewEmail.setText(email);
                textViewWebsite.setText(website);
                // Продолжайте аналогичным образом для других данных JSON
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Если данные JSON отсутствуют или пусты, вы можете вывести сообщение об ошибке или выполнить другие действия
            Toast.makeText(this, "Данные JSON отсутствуют или пусты", Toast.LENGTH_SHORT).show();
        }

    }
}