package com.example.minishopberdnikova;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    ArrayList<Good> goods;
    GoodsAdapter goodsAdapter;
    ListView listViewMain;
    TextView footerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        goods = (ArrayList<Good>) intent.getSerializableExtra("goods");

        footerTextView = (TextView) findViewById(R.id.footerTextView);
        goodsAdapter = new GoodsAdapter(this, goods, footerTextView);

        listViewMain = (ListView) findViewById(R.id.listViewMain);
        listViewMain.setAdapter(goodsAdapter);

    }

    public void backToSelect(View view) {
        onBackPressed();
    }


}




