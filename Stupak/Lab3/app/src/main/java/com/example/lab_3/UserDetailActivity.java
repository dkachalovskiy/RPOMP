package com.example.lab_3;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Получите данные о пользователе из Intent
        User selectedUser = getIntent().getParcelableExtra("selectedUser");

        // Отобразите данные о пользователе в макете
        displayUserInfo(selectedUser);
    }

    private void displayUserInfo(User user) {
        // Отобразите данные о пользователе в TextView
        TextView userInfoTextView = findViewById(R.id.userInfoTextView);
        String userInfo = "ID: " + user.getId() + "\n" +
                "Имя: " + user.getName() + "\n" +
                "Имя пользователя: " + user.getUsername() + "\n" +
                "Email: " + user.getEmail() + "\n";
        userInfoTextView.setText(userInfo);
    }
}
