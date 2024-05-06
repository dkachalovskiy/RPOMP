package com.example.lab_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectedGoodsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Good> arr_checked_goods_adapter;
    private LayoutInflater layoutInflater;

    public SelectedGoodsAdapter(Context context, ArrayList<Good> arr_checked_goods_adapter) {
        this.context = context;
        this.arr_checked_goods_adapter = arr_checked_goods_adapter;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arr_checked_goods_adapter.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_checked_goods_adapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_good, null, false);
        }
        Good good_temp = arr_checked_goods_adapter.get(position);
        TextView tv_goodId = view.findViewById(R.id.tv_goodId);
        tv_goodId.setText(Integer.toString(good_temp.getId()));
        TextView tv_goodName = view.findViewById(R.id.tv_goodName);
        tv_goodName.setText(good_temp.getName());
        TextView tv_goodCost = view.findViewById(R.id.tv_goodCost);
        tv_goodCost.setText(Double.toString(good_temp.getCost()));
        CheckBox cb_good = view.findViewById(R.id.cb_good);
        cb_good.setVisibility(View.GONE); // Прячем чекбокс
        return view;
    }
}
