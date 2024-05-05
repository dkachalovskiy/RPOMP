package com.example.mynotes_berdnikova.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mynotes_berdnikova.NotesAdapter;
import com.example.mynotes_berdnikova.R;



public class FragmentShow extends Fragment {
    private ListView listVNotes;
    private NotesAdapter noteAdapter;

    public static FragmentShow newInstance(int page) {
        FragmentShow fragment = new FragmentShow();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentShow() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_show, container, false);

        listVNotes = result.findViewById(R.id.listViewNotes);
        TextView pageHeader = result.findViewById(R.id.fragmentShow);
        String header = "FRAGMENT SHOW";
        pageHeader.setText(header);
        return result;
    }
}
