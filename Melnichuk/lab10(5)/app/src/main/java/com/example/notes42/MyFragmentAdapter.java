package com.example.notes42;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MyFragmentAdapter extends FragmentStateAdapter {

    ArrayList<MyNote> Notes;
    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<MyNote> Notes) {
        super(fragmentManager, lifecycle);
        this.Notes= Notes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0: fragment = new FragmentShow(Notes);
            fragment.getView();
                break;
            case 1: fragment = new FragmentAdd(Notes);
                break;
            case 2:fragment = new FragmentDel(Notes);
                break;
            case 3:fragment = new FragmentUpdate(Notes);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
