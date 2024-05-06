package com.example.minishop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.minishop.Product;
import com.example.minishop.R;

import java.util.ArrayList;

    public class ProductAdapter extends BaseAdapter {
        private final ArrayList<Product> productList;
        private final Context context;

        public ProductAdapter(Context context, ArrayList<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
                holder = new ViewHolder();
                holder.productIdTextView = convertView.findViewById(R.id.product_id_text_view);
                holder.productNameTextView = convertView.findViewById(R.id.product_name_text_view);
                holder.productPriceTextView = convertView.findViewById(R.id.product_price_text_view);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Product product = productList.get(position);
            holder.productIdTextView.setText("ID: " + product.getId());
            holder.productNameTextView.setText("Наименование: " + product.getName());
            holder.productPriceTextView.setText("Цена: $" + product.getPrice());

            int backgroundColor = product.isChecked() ? ContextCompat.getColor(context, R.color.selected_background_color) : Color.TRANSPARENT;
            convertView.setBackgroundColor(backgroundColor);

            return convertView;
        }

        static class ViewHolder {
            TextView productIdTextView;
            TextView productNameTextView;
            TextView productPriceTextView;
        }
    }


