package com.example.minishop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CartProductAdapter extends ArrayAdapter<Product> {

    public CartProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Получаем текущий товар
        Product product = getItem(position);

        // Проверяем, существует ли View для элемента списка. Если нет, создаем новое
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Находим TextView в макете элемента списка и устанавливаем имя товара
        TextView itemNameTextView = convertView.findViewById(android.R.id.text1);
        if (product != null) {
            itemNameTextView.setText(product.getName());
        }

        return convertView;
    }
}
