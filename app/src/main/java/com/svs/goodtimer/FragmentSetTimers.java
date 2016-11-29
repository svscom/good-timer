package com.svs.goodtimer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;

/**
 * Created by Виталий on 26.11.2016.
 */

public class FragmentSetTimers extends Fragment implements View.OnTouchListener {
    ListView listViewOfActualTimers;
    View header;
    NumberPicker numberPickerHours, numberPickerMinutes, numberPickerSeconds;
    EditText etDescription;
    Switch switchAddInActualList;
    Button btnStartTimer;

    AdapterForListOfActualTimers adapterForListOfActualTimers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(MainActivity.logTag, "FragmentSetTimers onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(MainActivity.logTag, "FragmentSetTimers onCreateView");
        header = inflater.inflate(R.layout.header_lv_actual_timers, null, false);
        return inflater.inflate(R.layout.fragment_set_timers, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(MainActivity.logTag, "FragmentSetTimers onStart");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapterForListOfActualTimers = new AdapterForListOfActualTimers(getContext(),
                ((MainActivity) getActivity()).fillDataToListOfActualTimers()); // добавить метод в активити

        listViewOfActualTimers = (ListView) getActivity().findViewById(R.id.lvActualTimers);
        listViewOfActualTimers.addHeaderView(header, null, false);

        numberPickerHours = (NumberPicker) header.findViewById(R.id.numberPickerHours);
        numberPickerMinutes = (NumberPicker) header.findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = (NumberPicker) header.findViewById(R.id.numberPickerSeconds);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(48);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);
        numberPickerHours.setOnTouchListener(this);
        numberPickerMinutes.setOnTouchListener(this);
        numberPickerSeconds.setOnTouchListener(this);

        etDescription = (EditText) header.findViewById(R.id.editTextDescription);
        switchAddInActualList = (Switch) header.findViewById(R.id.switchAddInActualTimers);
        btnStartTimer = (Button) header.findViewById(R.id.buttonStartTimer);

        listViewOfActualTimers.setAdapter(adapterForListOfActualTimers);
        Log.d(MainActivity.logTag, "FragmentSetTimers onActivityCreated");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {  //пока здесь только фича, которая вроде как помогает правильно взаимодействовать
        // с numberPicker'ами, когда они расположены внутри Scroll элемента(не уверен насчёт ListView, нужно проверить!)
        if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }

        v.onTouchEvent(event);
        Log.d(MainActivity.logTag, "FragmentSetTimers onTouch");
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("numberPickerHours", numberPickerHours.getValue());
        outState.putInt("numberPickerMinutes", numberPickerMinutes.getValue());
        outState.putInt("numberPickerSeconds", numberPickerSeconds.getValue());
        outState.putString("etDescription", String.valueOf(etDescription.getText()));
        outState.putBoolean("switchAddInActualList", switchAddInActualList.isChecked());
        Log.d(MainActivity.logTag, "FragmentSetTimers onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        numberPickerHours.setValue(savedInstanceState != null ? savedInstanceState.getInt("numberPickerHours") : 0);
        numberPickerMinutes.setValue(savedInstanceState != null ? savedInstanceState.getInt("numberPickerMinutes") : 0);
        numberPickerSeconds.setValue(savedInstanceState != null ? savedInstanceState.getInt("numberPickerSeconds") : 0);
        etDescription.setText(savedInstanceState != null ? savedInstanceState.getString("etDescription") : null);
        switchAddInActualList.setChecked(savedInstanceState != null && savedInstanceState.getBoolean("switchAddInActualList"));
        Log.d(MainActivity.logTag, "FragmentSetTimers onViewStateRestored");
    }
}