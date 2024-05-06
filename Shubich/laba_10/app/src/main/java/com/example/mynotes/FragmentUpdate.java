package com.example.mynotes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class FragmentUpdate extends Fragment {

    private EditText editTextNoteNumber;
    private EditText editTextNewDescription;
    private Button buttonUpdate;
    private NoteDatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        // Инициализация элементов управления
        editTextNoteNumber = view.findViewById(R.id.edit_text_note_number);
        editTextNewDescription = view.findViewById(R.id.edit_text_new_description);
        buttonUpdate = view.findViewById(R.id.button_update);

        // Инициализация базы данных
        databaseHelper = new NoteDatabaseHelper(getContext());

        // Обработка нажатия кнопки "Update"
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNoteInDatabase();
            }
        });

        return view;
    }

    // Метод для обновления заметки в базе данных
    private void updateNoteInDatabase() {
        // Получить номер заметки из поля EditText
        String noteNumberString = editTextNoteNumber.getText().toString().trim();
        int noteNumber = Integer.parseInt(noteNumberString);
        // Получить новое описание заметки из поля EditText
        String newDescription = editTextNewDescription.getText().toString().trim();
        // Обновить описание заметки в базе данных
        databaseHelper.updateNoteDescription(noteNumber, newDescription);
        editTextNoteNumber.getText().clear();
        editTextNewDescription.getText().clear();

    }
}
