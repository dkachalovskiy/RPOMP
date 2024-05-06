package com.example.lab_4;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.popup, null);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setLayout(width, height);
        }

        getDialog().getWindow().findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox checkBox = (CheckBox)getDialog().getWindow().findViewById(R.id.checkBox);

                if(checkBox.isChecked()) {
                    SharedPreferences settings = getActivity().getSharedPreferences("settings2", MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putBoolean("popup", false);
                    prefEditor.apply();
                }
                Dialog.this.dismiss();
            }
        });
    }
}
