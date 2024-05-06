package com.example.notes42;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentShow extends Fragment {

    public FragmentShow(ArrayList<MyNote> MyNotes)
    {
       this.Notes=MyNotes;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }
    ArrayList<MyNote> Notes = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = new ListAdapter(requireActivity(), Notes);
        ListView myListView = getActivity().findViewById(R.id.myListView);
        myListView.setAdapter(listAdapter);
    }
}