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

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FragmentDel extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_del, container, false);
    }
    ArrayList<MyNote> MyNotes;
    FragmentDel(ArrayList<MyNote> MyNotes)
    {
        this.MyNotes=MyNotes;
    }
    @Override
    public void onResume() {
        super.onResume();
        Button btnDel=getActivity().findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvToDel = getActivity().findViewById(R.id.TextViewNumberForDel);
                SQLiteDatabase db = getActivity().openOrCreateDatabase("myApp.db", getActivity().MODE_PRIVATE, null);
                try {
                    int indexToDelete=Integer.parseInt(tvToDel.getText().toString());
                    String request = "DELETE FROM mynotes WHERE name='" + MyNotes.get(indexToDelete-1).name + "';";
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