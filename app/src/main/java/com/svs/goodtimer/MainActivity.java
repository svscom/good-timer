package com.svs.goodtimer;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentSetTimers fragmentSetTimers;
    private FragmentManager fragmentManager;
    static String logTag = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(logTag, "MainActivity onCreate");

        fragmentManager = getSupportFragmentManager();

        fragmentSetTimers = (FragmentSetTimers) fragmentManager.findFragmentByTag("fragmentSetTimersTAG");
        if (fragmentSetTimers == null)
            fragmentManager.beginTransaction().add(R.id.activity_main, new FragmentSetTimers(), "fragmentSetTimersTAG").commit();
    }
}
