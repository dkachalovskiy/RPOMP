package com.example.lab_5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter {
    private Context mContext;
    private List<Note> mNotes;


    public NoteAdapter(Context context) {
        mContext = context;
        mNotes = new ArrayList<>();
    }

    public NoteAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    public void setNotes(List<Note> notes) {
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
            convertView = inflater.inflate(R.layout.item_note, parent, false);
        }

        TextView textViewNoteNumber = convertView.findViewById(R.id.noteID);
        TextView textViewNoteDescription = convertView.findViewById(R.id.noteDescription);

        Note note = mNotes.get(position);
        textViewNoteNumber.setText(String.valueOf(note.getNumber()));
        textViewNoteDescription.setText(note.getDescription());

        return convertView;
    }
}