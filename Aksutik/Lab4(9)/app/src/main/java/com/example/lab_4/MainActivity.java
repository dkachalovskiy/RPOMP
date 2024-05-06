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

    private void fillData() {

        arr_goods.add(new Good(1, " " + "Хлеб  ", false, 1.5));
        arr_goods.add(new Good(2, " " + "Батон  ", false, 1.6));
        arr_goods.add(new Good(3," " + "Десяток яиц  ", false, 3.5));
        arr_goods.add(new Good(4," " + "Кефир  ", false, 2.7));
        arr_goods.add(new Good(5," " + "Сметана  ", false, 2.56));
        arr_goods.add(new Good(6," " + "Творог  ", false, 2.13));
        arr_goods.add(new Good(7," " + "Сырок плавленный  ", false, 1.06));
        arr_goods.add(new Good(8," " + "Печенье овсяное ", false, 3.8));
        arr_goods.add(new Good(9, " " + "Молоко  ", false, 1.9));
        arr_goods.add(new Good(10, " " + "Масло сливочное  ", false, 4.2));
        arr_goods.add(new Good(11, " " + "Сахар  ", false, 2.0));
        arr_goods.add(new Good(12, " " + "Чай  ", false, 2.5));
        arr_goods.add(new Good(13, " " + "Кофе  ", false, 5.0));
        arr_goods.add(new Good(14, " " + "Рис  ", false, 2.3));
        arr_goods.add(new Good(15, " " + "Макароны  ", false, 1.8));
        arr_goods.add(new Good(16, " " + "Салями  ", false, 3.99));
        arr_goods.add(new Good(17, " " + "Огурцы  ", false, 1.49));
        arr_goods.add(new Good(18, " " + "Помидоры  ", false, 1.69));
        arr_goods.add(new Good(19, " " + "Картофель  ", false, 1.99));
        arr_goods.add(new Good(20, " " + "Лук  ", false, 1.79));
    }

    @Override
    public void onDataChanged() {
        int size = goodsAdapter.getCheckedGoods().size();
        tv_count.setText("Количество товаров: " + size + "");

    }
}
