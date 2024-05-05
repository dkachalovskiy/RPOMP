package com.example.mynotes.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynotes.R;

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
        TextView pageHeader = result.findViewById(R.id.fragmentDel);
        String header = "FRAGMENT DELETE";
        pageHeader.setText(header);
        return result;
    }
}



