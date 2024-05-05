package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

class Product implements Serializable {

    String name;
    int price;
    int image;
    boolean box;
    int quantity;

    Product(String _describe, int _price, int _image, boolean _box) {
        name = _describe;
        price = _price;
        image = _image;
        box = _box;
        quantity = 1; // Устанавливаем начальное количество товаров в 1
    }
}

public class MainActivity extends Activity {

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> favProducts = new ArrayList<>();
    BoxAdapter boxAdapter;
    int totalCount = 0; // Переменная для общего количества товаров
    TextView tv;



    /** Called when the activity is first created. */
    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView2);
        tv.setText(Integer.toString(totalCount));

        // Создаем адаптер
        fillData();
        boxAdapter = new BoxAdapter(this, products);

        // Настраиваем список
        ListView lvMain = findViewById(R.id.lvSecond);
        lvMain.setAdapter(boxAdapter);

        for (int i = 0; i < boxAdapter.getCount(); i++) {
            Product product = boxAdapter.getProduct(i);
            product.box = true; // Установка состояния чекбокса в true
            //product.box = false; // Установка состояния чекбокса в true
           // boxAdapter.notifyDataSetChanged(); // Обновление адаптера
            //onCheckboxClicked(lvMain.getChildAt(i)); // Вызов обработчика
        }

        // Вычисляем общее количество товаров
        /*for (Product product : products) {
            totalCount += product.quantity;
        }*/
       // tv.setText(Integer.toString(totalCount));
    }

    // Генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 1; i++) {
            products.add(new Product("Cыр SVEZA", i * 3,
                    R.drawable.a1, false));
            products.add(new Product("Сгущенка Рогачев", i * 5,
                    R.drawable.mers, false));
            products.add(new Product("Вареники с вишней", i * 4,
                    R.drawable.haval, false));
            products.add(new Product("Сыр российский", i * 9,
                    R.drawable.bomba, false));
            products.add(new Product("Кофе Lavazza", i * 35,
                    R.drawable.skazka, false));
            products.add(new Product("Йогурт Danon", i * 1,
                    R.drawable.danon, false));
            products.add(new Product("Exponenta", i * 3,
                    R.drawable.exp, false));
            products.add(new Product("Молоко", i * 2,
                    R.drawable.moloko, false));
        }
    }

    public void clickOpenFav(View view) {
        favProducts.clear();
        Intent intent = new Intent(this, SecondActivity.class);
        for (Product p : boxAdapter.getBox()) {
            if (p.box) {
                favProducts.add(p);
            }
        }
        intent.putExtra("list", favProducts);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void onCheckboxClicked(View view) {

        CheckBox checkbox = view.findViewById(R.id.cbBox);

        // Устанавливаем слушатель изменения состояния для каждого чекбокса
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    totalCount++; // Увеличиваем счетчик при активации чекбокса
                } else {
                    totalCount--; // Уменьшаем счетчик при деактивации чекбокса
                }
                // Здесь можно выполнить дополнительные действия, основанные на изменении состояния чекбокса
                tv.setText(Integer.toString(totalCount));

            }
        });

        // CheckBox cb = (CheckBox) view;
        int position = (int) checkbox.getTag();
        Product product = products.get(position);

       /* if (cb.isChecked()) {
            totalCount += product.quantity;
        } else {
            totalCount -= product.quantity;
        }
        totalCount++;*/


        product.box = checkbox.isChecked();
    }

    class BoxAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<Product> objects;

        BoxAdapter(Context context, ArrayList<Product> products) {
            ctx = context;
            objects = products;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.item, parent, false);
            }

            Product p = getProduct(position);

            ((TextView) view.findViewById(R.id.tvDescr)).setText(p.name);
            ((TextView) view.findViewById(R.id.tvPrice)).setText("Price: " + p.price);
            ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

            CheckBox cbBuy = view.findViewById(R.id.cbBox);
            cbBuy.setOnCheckedChangeListener(myCheckChangeList);
            cbBuy.setTag(position);
            cbBuy.setChecked(p.box);

            return view;
        }

        Product getProduct(int position) {
            return ((Product) getItem(position));
        }

        ArrayList<Product> getBox()
        {
            ArrayList<Product> box = new ArrayList<>();
            for (Product p : objects) {
                if (p.box)
                    box.add(p);
            }
            return box;
        }

        CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                getProduct((Integer) buttonView.getTag()).box = isChecked;
            }
        };
    }
}