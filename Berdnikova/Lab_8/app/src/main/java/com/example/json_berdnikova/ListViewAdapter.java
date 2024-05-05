package com.example.json_berdnikova;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private int resource;
    private ArrayList<JSONObject> items;

    public ListViewAdapter(Context context, int resource, ArrayList<JSONObject> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }


        JSONObject currentItem = items.get(position);
        String name = "";
        try {
            name = currentItem.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        textViewName.setText(name);
        return convertView;
    }

    public void clearData() {
        items.clear();
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<JSONObject> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}
