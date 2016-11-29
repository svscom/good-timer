package com.svs.goodtimer;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentSetTimers fragmentSetTimers;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        fragmentSetTimers = (FragmentSetTimers) fragmentManager.findFragmentByTag("fragmentSetTimersTAG");
        if (fragmentSetTimers == null)
            fragmentManager.beginTransaction().add(R.id.activity_main, new FragmentSetTimers(), "fragmentSetTimersTAG").commit();
    }

    ArrayList<ItemListOfActualTimers> fillDataToListOfActualTimers() {
        ArrayList<ItemListOfActualTimers> list = new ArrayList<>();
        list.add(new ItemListOfActualTimers(0, 10, 0));
        list.add(new ItemListOfActualTimers(0, 30, 0));
        list.add(new ItemListOfActualTimers(0, 10, 0, "Макароны"));
        list.add(new ItemListOfActualTimers(0, 5, 0, "Чайник"));
        list.add(new ItemListOfActualTimers(1, 23, 0, "Хз что это"));
        return list;
    }
}
