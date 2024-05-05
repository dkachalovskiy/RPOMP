package com.example.mynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context mContext;
    private List<com.example.mynotes.Notes> mNotes;


    public NotesAdapter(Context context) {
        mContext = context;
        mNotes = new ArrayList<>();
    }

    public NotesAdapter(Context context, List<com.example.mynotes.Notes> notes) {
        mContext = context;
        mNotes = notes;
    }

    public void setNotes(List<com.example.mynotes.Notes> notes) {
        mNotes = notes;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.note, parent, false);
        }

        TextView textViewNoteNumber = convertView.findViewById(R.id.noteID);
        TextView textViewNoteDescription = convertView.findViewById(R.id.noteDescription);

        com.example.mynotes.Notes note = mNotes.get(position);
        textViewNoteNumber.setText(String.valueOf(note.getNumber()));
        textViewNoteDescription.setText(note.getDescription());

        return convertView;
    }
}



