package com.example.lab_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lab_5.R;

public class FragmentAdd extends Fragment {

    public static FragmentAdd newInstance(int page) {
        FragmentAdd fragment = new FragmentAdd();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAdd() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_add, container, false);
        TextView pageHeader = result.findViewById(R.id.fragADD);
        String header = "Fragment ADD(Тусюк)";
        pageHeader.setText(header);
        return result;
    }
}
