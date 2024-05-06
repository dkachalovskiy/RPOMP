package com.example.minishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    // Объявляем переменную productList как поле класса MainActivity
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView totalItemsCountView = findViewById(R.id.total_items_count_view); // Найдите элемент по идентификатору

        TextView totalItemsTextView = findViewById(R.id.total_items_text_view);
        ListView listView = findViewById(R.id.product_list_view);
        Button showCartButton = findViewById(R.id.show_cart_button);


        // Инициализируем productList
        productList = new ArrayList<>();

        // Добавляем 20 товаров в список
        for (int i = 1; i <= 20; i++) {
            Product product = new Product();
            product.setId(i);
            product.setName("Товар " + i);
            product.setPrice(i * 10.0);
            product.setChecked(false);
            productList.add(product);
        }

        ProductAdapter adapter = new ProductAdapter(this, productList);
        listView.setAdapter(adapter);

        ArrayList<Product> finalProductList = productList;
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product selectedProduct = finalProductList.get(position);
            selectedProduct.setChecked(!selectedProduct.isChecked());
            adapter.notifyDataSetChanged();

            int selectedItemsCount = 0;
            for (Product product : productList) {
                if (product.isChecked()) {
                    selectedItemsCount++;
                }
            }
            totalItemsCountView.setText(String.valueOf(selectedItemsCount));
        });

        showCartButton.setOnClickListener(v -> showCart());
    }


    private void showCart() {
        ArrayList<Product> selectedProducts = new ArrayList<>();

        // Используем переменную productList, объявленную как поле класса MainActivity
        for (Product product : productList) {
            if (product.isChecked()) {
                selectedProducts.add(product);
            }
        }

        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        // Помещаем выбранные товары в Intent
        intent.putParcelableArrayListExtra("selectedProducts", selectedProducts);
        startActivity(intent);
    }
}
