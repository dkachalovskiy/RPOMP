package com.example.mynotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class FragmentDel extends Fragment {

    private EditText editTextNoteNumber;
    private Button buttonDelete;
    private NoteDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_del, container, false);

        // Инициализация элементов управления
        editTextNoteNumber = view.findViewById(R.id.edit_text_note_number);
        buttonDelete = view.findViewById(R.id.button_delete);

        // Инициализация базы данных
        databaseHelper = new NoteDatabaseHelper(getContext());

        // Обработка нажатия кнопки "Delete"
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoteFromDatabase();
            }
        });

        return view;
    }

    // Метод для удаления заметки из базы данных
    private void deleteNoteFromDatabase() {

        String noteNumberString = editTextNoteNumber.getText().toString().trim();
        if (!noteNumberString.isEmpty()) {

            int noteNumber = Integer.parseInt(noteNumberString);

            databaseHelper.deleteNote(noteNumber);
            editTextNoteNumber.getText().clear();

        }
    }

}
