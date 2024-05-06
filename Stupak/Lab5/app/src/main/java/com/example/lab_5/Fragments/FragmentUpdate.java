package com.example.lab_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lab_5.R;

public class FragmentUpdate extends Fragment {

    public static FragmentUpdate newInstance(int page) {
        FragmentUpdate fragment = new FragmentUpdate();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentUpdate() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_update, container, false);
        TextView pageHeader = result.findViewById(R.id.fragUPDATE);
        String header = "Фрагмент UPDATE";
        pageHeader.setText(header);
        return result;
    }
}
