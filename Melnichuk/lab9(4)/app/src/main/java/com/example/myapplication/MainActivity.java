package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView2);

        // Создаем адаптер
        fillData();
        boxAdapter = new BoxAdapter(this, products);

        // Настраиваем список
        ListView lvMain = findViewById(R.id.lvSecond);
        lvMain.setAdapter(boxAdapter);

        updateTotalCount();
    }

    // Генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 2; i++) {
            products.add(new Product("Кефир", i * 3,
                    R.drawable.kefir, false));
            products.add(new Product("Сгущенка", i * 5,
                    R.drawable.mers, false));
            products.add(new Product("Курица", i * 4,
                    R.drawable.kurica, false));
            products.add(new Product("Яйца", i * 9,
                    R.drawable.eggs, false));
            products.add(new Product("Кофе", i * 35,
                    R.drawable.cofe, false));
            products.add(new Product("Макароны", i * 1,
                    R.drawable.makoroni, false));
            products.add(new Product("Хлеб", i * 3,
                    R.drawable.hleb, false));
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

    public void onCheckboxClicked(View view) {
        CheckBox cb = (CheckBox) view;
        int position = (int) cb.getTag();
        Product product = products.get(position);

        product.box = cb.isChecked();
    }
    private void updateTotalCount() {
        int count = 0;
        for (Product product : products) {
            if (product.box) {
                count += product.quantity;
            }
        }
        totalCount = count;
        tv.setText(Integer.toString(totalCount));
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
                updateTotalCount();
            }
        };



    }
}