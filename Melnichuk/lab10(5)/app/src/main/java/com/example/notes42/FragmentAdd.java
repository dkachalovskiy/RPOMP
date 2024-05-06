package com.example.notes42;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentAdd extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }
    Button btnAdd;
    TextView descrInput;
    TextView nameInput;
    ArrayList<MyNote> Notes = new ArrayList<>();
    public FragmentAdd(ArrayList<MyNote> MyNotes)
    {
        this.Notes=MyNotes;
    }
    @Override
    public void onResume() {
        super.onResume();

        descrInput=getActivity().findViewById(R.id.DescriptionField);
        nameInput=getActivity().findViewById(R.id.NameField);

        btnAdd=getActivity().findViewById(R.id.btnDel);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase db = getActivity().openOrCreateDatabase("myApp.db", getActivity().MODE_PRIVATE, null);
                    String request = "INSERT OR IGNORE INTO mynotes VALUES ('" + nameInput.getText().toString() + "', '" + descrInput.getText().toString() + "');";
                    db.execSQL(request);
                    Notes.clear();
                    Cursor query = db.rawQuery("SELECT * FROM mynotes;", null);
                    while (query.moveToNext()) {
                        Notes.add(new MyNote(query.getString(0), query.getString(1)));
                    }
                    query.close();
                    db.close();
                    Toast toast = Toast.makeText(getActivity(),
                            "Успех!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (Exception ex) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Ошибка!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}