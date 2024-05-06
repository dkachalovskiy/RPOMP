package com.example.mynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private ArrayList<Note> notes;
    private Context context;

    public void clear() {
        notes.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Note> newNotes) {
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }
    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_note, null);
        }

        TextView textViewNote = view.findViewById(R.id.text_view_note);
        Note note = notes.get(position);
        textViewNote.setText(String.format("%d. %s", note.getId(), note.getDescription()));

        return view;
    }
}


