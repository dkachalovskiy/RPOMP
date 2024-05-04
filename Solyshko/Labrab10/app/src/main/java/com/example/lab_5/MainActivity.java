package com.example.lab_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab_5.DB.DB;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        db.open();

        {
            ViewPager2 pager = findViewById(R.id.pager);

            TabLayout tabLayout = findViewById(R.id.tabLayout);

            FragmentStateAdapter pageAdapter = new MyAdapter(this);
            pager.setAdapter(pageAdapter);

            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            switch (position) {
                                case 0:
                                    tab.setText("Show");
                                    break;
                                case 1:
                                    tab.setText("Add");
                                    break;
                                case 2:
                                    tab.setText("Del");
                                    break;
                                case 3:
                                    tab.setText("Update");
                                    break;
                            }
                        }
                    });
            tabLayoutMediator.attach();
        }

    }

    @Override
    protected void onDestroy() {
        db.close();

        super.onDestroy();
    }


    public void onClickShow(View v) {
        Cursor cursor = db.getAllData();
        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex("id");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (idIndex >= 0 && descriptionIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String description = cursor.getString(descriptionIndex);

                Note note = new Note(id, description);
                notes.add(note);
            }
        }
        ListView listViewNotes = findViewById(R.id.listViewNotes);
        NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        listViewNotes.setAdapter(noteAdapter);

        cursor.close();
    }

    public void onClickAdd(View view) {

        EditText editTextDescription = findViewById(R.id.editTextDescription);

        String description = editTextDescription.getText().toString();

        db.addRec(description);

        editTextDescription.setText("");

        Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
    }

    public void onClickDel(View view) {
        EditText editTextNodeId = findViewById(R.id.editTextNoteId);
        int ID = Integer.parseInt(editTextNodeId.getText().toString());
        if(db.containRecById(ID)){
            db.delRec(ID);
            editTextNodeId.setText("");
            Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
        }else {
            editTextNodeId.setText("");
            Toast.makeText(this, "Такой записи не существует", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickUpDate(View view) {
        EditText editTextId = findViewById(R.id.editTextId);
        EditText editTextDescription = findViewById(R.id.editTextDescription);

        int id = Integer.parseInt(editTextId.getText().toString());
        String description = editTextDescription.getText().toString();

        if(db.containRecById(id)){
            db.update(id,description);
            Toast.makeText(this, "Запись обновлена", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Такой записи не существует", Toast.LENGTH_SHORT).show();
        }
        editTextId.setText("");
        editTextDescription.setText("");

    }
}