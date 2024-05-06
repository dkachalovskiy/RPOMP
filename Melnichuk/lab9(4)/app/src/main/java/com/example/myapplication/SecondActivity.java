package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ListView;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    BoxAdapter boxAdapter;
    ArrayList<Product> FavProducts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ArrayList<Product> arrayFromIntent = (ArrayList<Product>) getIntent().getSerializableExtra("list");

        boxAdapter = new BoxAdapter(this, arrayFromIntent);

        // настраиваем список
        ListView lvMainlvSecond = (ListView) findViewById(R.id.listViewFav);
        lvMainlvSecond.setAdapter(boxAdapter);
        delBoxes();
    }
    CheckBox cb;
    void delBoxes()
    {
        cb = (CheckBox)findViewById(R.id.cbBox);
        if (cb!=null)
        {

        }
        for (Product p : boxAdapter.getBox()) {

        }
    }
}
