package com.example.lab_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnChangeListener {

    // добавляем новые переменные класса
    private ArrayList<Good> arr_checked_goods = new ArrayList<Good>();

    private LayoutInflater layoutInflater;
    private View view_header, view_footer;
    private Button btnShow;
    private TextView tv_count;

    private ListView listView;
    private ArrayList<Good> arr_goods = new ArrayList<Good>();
    private final int SIZE_OF_ARR = 12;
    private GoodsAdapter goodsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        createMyListView();
    }
    @Override
    public void onClick(View view) {
        arr_checked_goods = goodsAdapter.getCheckedGoods();
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putParcelableArrayListExtra("MyList", arr_checked_goods);
        startActivity(intent);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
    }
    private void createMyListView() {
        fillData();
        goodsAdapter = new GoodsAdapter(this, arr_goods, this);
        layoutInflater = LayoutInflater.from(this);
        view_header = layoutInflater.inflate(R.layout.header_mygoods, null);
        view_footer = layoutInflater.inflate(R.layout.footer_mygoods, null);
        btnShow = (Button) view_footer.findViewById(R.id.btnShow);
        btnShow.setOnClickListener(this);
        tv_count = (TextView) view_footer.findViewById(R.id.tv_count);
        listView.addHeaderView(view_header);
        listView.addFooterView(view_footer);
        listView.setAdapter(goodsAdapter);
    }
    private void fillData(){

            arr_goods.add(new Good(1," " + "Bread ", false, 1.5));
            arr_goods.add(new Good(2," " + "Milk ", false, 1.5));
            arr_goods.add(new Good(3," " + "Eggs ", false, 2.5));
            arr_goods.add(new Good(4," " + "Rice ", false, 1.75));
            arr_goods.add(new Good(5," " + "Apples ", false, 5));
            arr_goods.add(new Good(6," " + "Cereal ", false, 3));
            arr_goods.add(new Good(7," " + "Butter ", false, 3.5));
            arr_goods.add(new Good(8," " + "Olive oil ", false, 9));
            arr_goods.add(new Good(9," " + "Bananas ", false, 4));
            arr_goods.add(new Good(10," " + "Soup ", false, 2));
            arr_goods.add(new Good(11," " + "Yogurt ", false, 2.30));
            arr_goods.add(new Good(12," " + "Crisps ", false, 4.5));

    }

    @Override
    public void onDataChanged() {
        int size = goodsAdapter.getCheckedGoods().size();
        tv_count.setText("Count of goods = " + size + "");

    }
}
