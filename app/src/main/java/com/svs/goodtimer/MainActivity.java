package com.svs.goodtimer;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
