package com.example.mynotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class FragmentAdd extends Fragment {

    private EditText editTextNote;
    private Button buttonAdd;
    private NoteDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Инициализация элементов управления
        editTextNote = view.findViewById(R.id.edit_text_note);
        buttonAdd = view.findViewById(R.id.button_add);

        // Инициализация базы данных
        databaseHelper = new NoteDatabaseHelper(getContext());

        // Обработка нажатия кнопки "Add"
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteToDatabase();
            }
        });

        return view;
    }

    // Метод для добавления заметки в базу данных
    private void addNoteToDatabase() {
        String description = editTextNote.getText().toString().trim();
        if (!description.isEmpty()) {
            databaseHelper.addNote(description);
            // Очистить поле EditText после добавления заметки
            editTextNote.getText().clear();



        }
    }
}
