package com.example.mynotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentShow extends Fragment {
    private ListView listViewNotes;
    private NoteAdapter noteAdapter;
    private NoteDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);

        listViewNotes = view.findViewById(R.id.list_view_notes);
        databaseHelper = new NoteDatabaseHelper(getContext());
        ArrayList<Note> notes = databaseHelper.getAllNotes();
        noteAdapter = new NoteAdapter(getContext(), notes);
        listViewNotes.setAdapter(noteAdapter);

        // Находим кнопку и добавляем обработчик нажатия
        Button buttonRefresh = view.findViewById(R.id.button_refresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNoteList();
            }
        });

        return view;
    }

    // Добавьте метод для обновления списка заметок
    public void refreshNoteList() {
        ArrayList<Note> updatedNotes = databaseHelper.getAllNotes();
        noteAdapter.clear(); // Очищаем текущий список заметок
        noteAdapter.addAll(updatedNotes); // Добавляем обновленный список заметок
        noteAdapter.notifyDataSetChanged(); // Уведомляем адаптер об изменениях
    }
}

