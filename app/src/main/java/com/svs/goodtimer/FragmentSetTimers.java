package com.svs.goodtimer;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

/**
 * Created by Виталий on 26.11.2016.
 */

public class FragmentSetTimers extends Fragment implements View.OnTouchListener, View.OnFocusChangeListener,
        View.OnClickListener {
    ListView listViewOfActualTimers;
    View header;
    NumberPicker numberPickerHours, numberPickerMinutes, numberPickerSeconds;
    EditText etDescription;
    Switch switchAddInActualList;
    Button btnStartTimer;

    AdapterForListOfActualTimers adapterForListOfActualTimers;
    private ArrayList<ItemListOfActualTimers> listOfActualTimers;

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        loadListOfActualTimers();
        Log.d(MainActivity.logTag, "FragmentSetTimers onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(MainActivity.logTag, "FragmentSetTimers onCreateView");
        header = inflater.inflate(R.layout.header_lv_actual_timers, listViewOfActualTimers, false);
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

        adapterForListOfActualTimers = new AdapterForListOfActualTimers(getContext(), listOfActualTimers); // добавить метод в активити

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
        btnStartTimer.setOnClickListener(this);

        listViewOfActualTimers.setAdapter(adapterForListOfActualTimers);
        Log.d(MainActivity.logTag, "FragmentSetTimers onActivityCreated");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //фича, которая помогает правильно взаимодействовать с numberPicker'ами, когда они расположены внутри Scroll элемента
        if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
        }

        v.onTouchEvent(event);

        if (v.getId() != etDescription.getId() && (etDescription.hasFocus() || etDescription.hasFocusable())) etDescription.clearFocus();

        Log.d(MainActivity.logTag, "FragmentSetTimers onTouch");
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStartTimer) {}
    }

    void saveListOfActualTimers() {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.sizeListOfActualTimers), listOfActualTimers.size());
        StringBuilder stringBuilder;
        for (int i = 0; i < listOfActualTimers.size(); i++) {
            stringBuilder = new StringBuilder(getString(R.string.itemActualTimer));
            stringBuilder.append(i);
            editor.putString(stringBuilder.toString(), listOfActualTimers.get(i).toString());
        }
        editor.apply();
    }

    void loadListOfActualTimers() {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.firstRun), true)) {
            listOfActualTimers = new ArrayList<>(6);
            listOfActualTimers.add(new ItemListOfActualTimers(0, 10, 0));
            listOfActualTimers.add(new ItemListOfActualTimers(0, 30, 0));
            listOfActualTimers.add(new ItemListOfActualTimers(0, 10, 0, "Макарошки"));
            listOfActualTimers.add(new ItemListOfActualTimers(0, 5, 0, "Чайник"));
            listOfActualTimers.add(new ItemListOfActualTimers(1, 23, 0, "Хз что это"));
            listOfActualTimers.add(new ItemListOfActualTimers(1, 23, 17, "Секунды"));
            sharedPreferences.edit().putBoolean(getString(R.string.firstRun), false).apply();
        } else {
            int size = sharedPreferences.getInt(getString(R.string.sizeListOfActualTimers), 0);
            listOfActualTimers = new ArrayList<>(size);
            StringBuilder stringBuilder;
            for (int i = 0; i < size; i++) {
                stringBuilder = new StringBuilder(getString(R.string.itemActualTimer));
                stringBuilder.append(i);
                listOfActualTimers.add(ItemListOfActualTimers.getItemFromString(sharedPreferences.getString(stringBuilder.toString(), null)));
            }
        }
    }

    @Override
    public void onDestroy() {
        saveListOfActualTimers();
        super.onDestroy();
    }
}