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

public class FragmentUpdate extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update, container, false);
    }
    ArrayList<MyNote> MyNotes;
    FragmentUpdate(ArrayList<MyNote> MyNotes)
    {
        this.MyNotes=MyNotes;
    }
    @Override
    public void onResume() {
        super.onResume();
        Button btnResume = getActivity().findViewById(R.id.buttonUpdate);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewIndex = getActivity().findViewById(R.id.textViewIndex);
                TextView textViewDescr = getActivity().findViewById(R.id.textViewDescription);
                SQLiteDatabase db = getActivity().openOrCreateDatabase("myApp.db", getActivity().MODE_PRIVATE, null);
                try {
                    int indexToUpdate=Integer.parseInt(textViewIndex.getText().toString());
                    String newDescr = textViewDescr.getText().toString();
                    String request = "UPDATE mynotes SET description = '"+newDescr+"' WHERE name = '"+MyNotes.get(indexToUpdate-1).name+"';";
                    db.execSQL(request);
                    MyNotes.clear();
                    Cursor query = db.rawQuery("SELECT * FROM mynotes;", null);
                    while (query.moveToNext()) {
                        MyNotes.add(new MyNote(query.getString(0), query.getString(1)));
                    }
                    query.close();
                    Toast toast = Toast.makeText(getActivity(),
                            "Успех!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (Exception exc) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Ошибка!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                db.close();
            }
        });
    }
}