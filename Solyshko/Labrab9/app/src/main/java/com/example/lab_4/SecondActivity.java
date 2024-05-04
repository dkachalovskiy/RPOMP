package com.example.lab_4;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private ListView listViewSelectedGoods;
    private SelectedGoodsAdapter selectedGoodsAdapter;
    private ArrayList<Good> selectedGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        // Получаем список выбранных товаров из Intent
        selectedGoodsList = getIntent().getParcelableArrayListExtra("MyList");

        // Инициализируем ListView
        listViewSelectedGoods = findViewById(R.id.listview_selected_goods);

        // Создаем и устанавливаем адаптер
        selectedGoodsAdapter = new SelectedGoodsAdapter(this, selectedGoodsList);
        listViewSelectedGoods.setAdapter(selectedGoodsAdapter);
    }
}
