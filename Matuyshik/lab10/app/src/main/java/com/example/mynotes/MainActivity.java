package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mynotes.DataBase.DB;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

            FragmentStateAdapter pageAdapter = new MyFragmentPagerAdapter(this);
            pager.setAdapter(pageAdapter);

            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            switch (position) {
                                case 0:
                                    tab.setText("View");
                                    break;
                                case 1:
                                    tab.setText("Add");
                                    break;
                                case 2:
                                    tab.setText("Delete");
                                    break;
                                case 3:
                                    tab.setText("Change");
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
        List<Notes> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex("id");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (idIndex >= 0 && descriptionIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String description = cursor.getString(descriptionIndex);

                Notes note = new Notes(id, description);
                notes.add(note);
            }
        }
        ListView listViewNotes = findViewById(R.id.listViewNotes);
        NotesAdapter noteAdapter = new NotesAdapter(this, notes);
        listViewNotes.setAdapter(noteAdapter);

        cursor.close();
    }

    public void onClickAdd(View view) {

        EditText editTextDescription = findViewById(R.id.editTextDescription);
        String description = editTextDescription.getText().toString();

        db.addRec(description);
        editTextDescription.setText("");

        Toast.makeText(this, "Заметка добавлена в базу", Toast.LENGTH_SHORT).show();
    }

    public void onClickDelete(View view) {
        EditText editTextNodeId = findViewById(R.id.editTextId);
        int ID = Integer.parseInt(editTextNodeId.getText().toString());
        if(db.containRecById(ID)){
            db.delRec(ID);
            editTextNodeId.setText("");
            Toast.makeText(this, "Заметка удалена из базы", Toast.LENGTH_SHORT).show();
        }else {
            editTextNodeId.setText("");
            Toast.makeText(this, "Заметки не существует", Toast.LENGTH_SHORT).show();
        }
        // db.delAll();

    }

    public void onClickUpdate(View view) {
        EditText editTextId = findViewById(R.id.editTextId);
        EditText editTextDescription = findViewById(R.id.editTextDescription);

        int id = Integer.parseInt(editTextId.getText().toString());
        String description = editTextDescription.getText().toString();

        if(db.containRecById(id)){
            db.update(id,description);
            Toast.makeText(this, "Заметка изменена", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Заметки не существует", Toast.LENGTH_SHORT).show();
        }

        editTextId.setText("");
        editTextDescription.setText("");

    }
}

