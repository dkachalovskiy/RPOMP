package com.example.minishop;

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

        // footerTextView = (TextView) findViewById(R.id.footerTextView);
    }

    void fillData() {
        String[] foodItems = {"Water", "Coffee", "Tea", "Bread", "Milk", "Yogurt", "Ice Cream", "Dark Chocolate", "Milk Chocolate", "White Chocolate",
                "Chips", "Apple", "Banana", "Cucumber", "Tomato", "Candy", "Ketchup", "Meat", "Fish", "Package"};

        for (int i = 0; i < foodItems.length; i++) {

            goods.add(new Good(foodItems[i % foodItems.length], (i + 1) * 100, false));

        }
    }

    public void showResult(View v) {
        ArrayList<Good> resultProducts = goodsAdapter.getBox();

        footerTextView.setText("Checked products(Tupik): " + resultProducts.size());
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("goods", resultProducts);
        startActivity(intent);
    }



}

