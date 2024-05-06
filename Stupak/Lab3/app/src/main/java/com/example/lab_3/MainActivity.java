package com.example.lab_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DataLoader.OnDataLoadedListener {

    private ArrayAdapter<String> adapter;
    private ListView listView;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(position);
                    User user = parseUser(jsonObject);
                    // Создайте Intent для перехода к UserDetailActivity
                    Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                    // Передайте данные о пользователе в UserDetailActivity через Intent
                    intent.putExtra("selectedUser", user);
                    // Запустите активность UserDetailActivity
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchData(View view) {
        DataLoader dataLoader = new DataLoader(this);
        // Замените URL на ваш URL-адрес сервера, который возвращает JSON данные
        dataLoader.execute("https://jsonplaceholder.typicode.com/users");
    }

    @Override
    public void onDataLoaded(String jsonData) {
        try {
            jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String itemName = jsonObject.getString("name");
                adapter.add(itemName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private User parseUser(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setId(jsonObject.getInt("id"));
        user.setName(jsonObject.getString("name"));
        user.setUsername(jsonObject.getString("username"));
        user.setEmail(jsonObject.getString("email"));
        return user;
    }
}
