package com.example.minishop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class GoodsAdapter extends BaseAdapter{

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Good> goods;
    TextView footerTextView;

    GoodsAdapter(Context p_context, ArrayList<Good> p_goods, TextView footerTextView){
        context = p_context;
        goods = p_goods;
        this.footerTextView = footerTextView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Good> getSelectedGoods(){
        ArrayList<Good> selectedGoods = new ArrayList<>();
        for (Good g : goods) {
            if (g.checkbox) {
                selectedGoods.add(g);
            }
        }
        return selectedGoods;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Good getItem(int position){
        return goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = layoutInflater.inflate(R.layout.good, parent, false);

        Good g = getGood(position);

        ((TextView) view.findViewById(R.id.textViewDescription)).setText(g.name);
        ((TextView) view.findViewById(R.id.textViewPrice)).setText(g.price + "");

        CheckBox checkboxBuy = (CheckBox) view.findViewById(R.id.checkboxBox);
        checkboxBuy.setOnCheckedChangeListener(myCheckChangeList);

        checkboxBuy.setTag(position);
        checkboxBuy.setChecked(g.checkbox);

        return view;
    }

    Good getGood(int position) {
        return ((Good) getItem(position));
    }

    ArrayList<Good> getBox() {
        ArrayList<Good> box = new ArrayList<Good>();
        for (Good g : goods) {
            if (g.checkbox)
                box.add(g);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getGood((Integer) buttonView.getTag()).checkbox = isChecked;
            updateCounter();
        }};

    void updateCounter() {
        int count = getBox().size();
        footerTextView.setText("Checked goods: " + count);
    }

    void updateGoods(ArrayList<Good> newGoods) {
        goods.clear();
        goods.addAll(newGoods);
        notifyDataSetChanged();
    }

}


