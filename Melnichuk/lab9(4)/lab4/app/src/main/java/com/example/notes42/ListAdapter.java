package com.example.notes42;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    ArrayList<MyNote> myNotes;
    Context ctx;
    public ListAdapter(@NonNull Context context, ArrayList<MyNote> myNotes) {
        this.myNotes=myNotes;
        this.ctx=context;
    }

    @Override
    public int getCount() {
        return myNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return myNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; //???
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(ctx).inflate(R.layout.node,parent,false);
        TextView name = convertView.findViewById(R.id.name);
        TextView description = convertView.findViewById(R.id.description);
        int index=position+1;
        name.setText(index+") " + myNotes.get(position).name);
        description.setText(myNotes.get(position).description);
        return convertView;
    }
}
