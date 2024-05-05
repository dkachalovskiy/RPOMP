package com.example.minishopberdnikova;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Good> goods = new ArrayList<Good>();
    GoodsAdapter goodsAdapter;
    TextView footerTextView;

    View header;
    View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillData();

        LayoutInflater inflater = getLayoutInflater();
        header = inflater.inflate(R.layout.header, null);
        footer = inflater.inflate(R.layout.footer, null);

        footerTextView = (TextView) findViewById(R.id.footerTextView);
        goodsAdapter = new GoodsAdapter(this, goods, footerTextView);

        ListView listViewMain = (ListView) findViewById(R.id.listViewMain);
        listViewMain.addHeaderView(header);
        listViewMain.addFooterView(footer);
        listViewMain.setAdapter(goodsAdapter);
    }

    void fillData() {
        String[] foodItems = {"Banana", "Apple", "Pomegranate", "Coconut", "Plum", "Lemon", "Orange", "Cabbage", "Carrot", "Pumpkin",
                "Corn", "Potato", "Cucumber", "Radish", "Mushrooms", "Pepper", "Tomato", "Peas", "Watermelon", "Melon"};

        for (int i = 0; i < foodItems.length; i++) {

                goods.add(new Good(foodItems[i % foodItems.length], (i + 1) * 100, false));
        }
    }

    public void showResult(View v) {
        ArrayList<Good> resultProducts = goodsAdapter.getBox();
        footerTextView.setText("Checked products(Berdnikova): " + resultProducts.size());
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("goods", resultProducts);
        startActivity(intent);
    }

}

