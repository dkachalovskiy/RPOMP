package com.example.notes42;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<MyNote> myNotes = new ArrayList<>();
    private MyFragmentAdapter myFragmentAdapter;
    private Fragment FrShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewPagerAndTabLayout();
        GetDataFromDatabase();
    }

    public void GetDataFromDatabase()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("myApp.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mynotes (name TEXT, description TEXT, UNIQUE(name))");
        Cursor query = db.rawQuery("SELECT * FROM mynotes;", null);
        while(query.moveToNext()){
            myNotes.add(new MyNote(query.getString(0),query.getString(1)));
        }
        query.close();
        db.close();
    }
    void createViewPagerAndTabLayout()
    {
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.TabLayout);
        viewPager2 = findViewById(R.id.ViewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Ваши заметки"));
        tabLayout.addTab(tabLayout.newTab().setText("Добавить"));
        tabLayout.addTab(tabLayout.newTab().setText("Удалить"));
        tabLayout.addTab(tabLayout.newTab().setText("Изменить"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        myFragmentAdapter = new MyFragmentAdapter(fragmentManager,getLifecycle(),myNotes);
        viewPager2.setAdapter(myFragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}