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

            arr_goods.add(new Good(1," " + "Bread ", false, 3.6));
            arr_goods.add(new Good(2," " + "Milk ", false, 3.2));
            arr_goods.add(new Good(3," " + "Eggs ", false, 3.7));
            arr_goods.add(new Good(4," " + "Butter ", false, 4.5));
            arr_goods.add(new Good(5," " + "Cereal ", false, 5.9));
            arr_goods.add(new Good(6," " + "Cheese ", false, 1.4));
            arr_goods.add(new Good(7," " + "Juice ", false, 3.4));
            arr_goods.add(new Good(8," " + "Butter ", false, 1.8));
            arr_goods.add(new Good(9," " + "Yogurt ", false, 1));
            arr_goods.add(new Good(10," " + "Sausage ", false, 2.9));
            arr_goods.add(new Good(11," " + "Mayo ", false, 3.3));
            arr_goods.add(new Good(12," " + "Ketchup ", false, 3.8));

    }

    @Override
    public void onDataChanged() {
        int size = goodsAdapter.getCheckedGoods().size();
        tv_count.setText("Count of goods = " + size + "");

    }
}
