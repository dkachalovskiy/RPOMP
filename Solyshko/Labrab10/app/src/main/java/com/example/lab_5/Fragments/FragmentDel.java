package com.example.lab_5.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lab_5.R;

public class FragmentDel extends Fragment {

    public static FragmentDel newInstance(int page) {
        FragmentDel fragment = new FragmentDel();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDel() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_del, container, false);
        TextView pageHeader = result.findViewById(R.id.fragDEL);
        String header = "FRAGMENT DEL (Солышко Д.А)";
        pageHeader.setText(header);
        return result;
    }
}
