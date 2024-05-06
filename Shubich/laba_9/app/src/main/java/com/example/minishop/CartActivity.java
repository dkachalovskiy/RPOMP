package com.example.minishop;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Получаем список выбранных товаров из Intent
        ArrayList<Product> selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        // Находим ListView в макете activity_cart.xml
        ListView cartListView = findViewById(R.id.cart_list_view);

        // Создаем адаптер для отображения выбранных товаров в ListView
        CartProductAdapter adapter = new CartProductAdapter(this, selectedProducts);

        // Устанавливаем адаптер для ListView
        cartListView.setAdapter(adapter);
    }

}
