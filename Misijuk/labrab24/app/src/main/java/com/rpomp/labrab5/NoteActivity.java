package com.rpomp.labrab5;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> notes;
    private int noteNum = 4;
    private long selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        // Настройка панели инструментов
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация списка заметок
        notes = new ArrayList<>();
        notes.add("Заметка 1");
        notes.add("Заметка 2");
        notes.add("Заметка 3");

        // Настройка адаптера для списка
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        // Настройка списка
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = id;
                String selectedNote = notes.get(position);
                Toast.makeText(NoteActivity.this, "Выбрана заметка: " + selectedNote, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Настройка меню
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка выбора пунктов меню
        int itemId = item.getItemId();
        if (itemId == R.id.menu_add_note) {
            addNote();
            return true;
        } else if (itemId == R.id.menu_delete_note) {
            deleteNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        // Логика добавления новой заметки
        String newNote = "Заметка " + noteNum++;
        notes.add(newNote);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Добавлена новая заметка", Toast.LENGTH_SHORT).show();
    }

    private void deleteNote() {
        // Логика удаления выбранной заметки
        //int selectedPosition = listView.getCheckedItemPosition();
        if (selectedPosition >= 0/*!= ListView.INVALID_POSITION*/) {
            String deletedNote = notes.get((int) selectedPosition);
            notes.remove(deletedNote);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Удалена заметка: " + deletedNote, Toast.LENGTH_SHORT).show();
            selectedPosition = -1;
        } else {
            Toast.makeText(this, "Выберите заметку для удаления", Toast.LENGTH_SHORT).show();
        }
    }
}