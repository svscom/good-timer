package com.svs.goodtimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;

/**
 * Created by Виталий on 11.12.2016.
 */

public class DialogFragmentEditOrAddTimer extends DialogFragment implements View.OnClickListener,
        View.OnTouchListener {
    String dialogAction;

    Button btnSaveOrAdd;
    NumberPicker npHours;
    NumberPicker npMinutes;
    NumberPicker npSeconds;
    EditText description;
    int itemPositionInList;

    ItemListOfActualTimers item;

    ViewGroup dialogView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onActivityCreated savedInstanceState not null");
            onViewStateRestored(savedInstanceState);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        dialogAction = args.getString("Dialog action");

        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialogView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.header_lv_actual_timers, null, false);
        btnSaveOrAdd = (Button) dialogView.findViewById(R.id.buttonStartTimer);
        btnSaveOrAdd.setOnClickListener(this);
        dialogView.findViewById(R.id.switchAddInActualTimers).setVisibility(View.GONE);
        dialogView.findViewById(R.id.textViewHeaderOfListActualTimers).setVisibility(View.GONE);
        dialogView.findViewById(R.id.dividerSetTimerContextAndHeaderOfList).setVisibility(View.GONE);
        dialogView.findViewById(R.id.imageButtonAddTimerToList).setVisibility(View.GONE);
        npHours = (NumberPicker) dialogView.findViewById(R.id.numberPickerHours);
        npHours.setMinValue(0);
        npHours.setMaxValue(48);
        npHours.setOnTouchListener(this);
        npMinutes = (NumberPicker) dialogView.findViewById(R.id.numberPickerMinutes);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npMinutes.setOnTouchListener(this);
        npSeconds = (NumberPicker) dialogView.findViewById(R.id.numberPickerSeconds);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
        npSeconds.setOnTouchListener(this);
        description = (EditText) dialogView.findViewById(R.id.editTextDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if ("Редактирование".equals(dialogAction)) {
            item = ItemListOfActualTimers.getItemFromString(args.getString("ItemForEdit"));
            itemPositionInList = args.getInt("Item position");
            btnSaveOrAdd.setText("Сохранить");
            npHours.setValue(item.getHours());
            npMinutes.setValue(item.getMinutes());
            npSeconds.setValue(item.getSeconds());
            description.setText(item.getDescription());
            builder.setIcon(android.R.drawable.ic_menu_edit);
        } else {
            builder.setIcon(R.drawable.ic_add_timer_black_24dp);
            btnSaveOrAdd.setText("Добавить");
        }
        builder.setTitle(dialogAction);
        //добавляем dialogview в созданный программно scrollview
        scrollView.addView(dialogView);
        builder.setView(scrollView);
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onCreateDialog");
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStartTimer:
                if ("Редактирование".equals(dialogAction)) editTimer();
                else addNewTimer();
                dismiss();
                break;
        }
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onClick");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void editTimer() {
        item.setHours(npHours.getValue());
        item.setMinutes(npMinutes.getValue());
        item.setSeconds(npSeconds.getValue());
        item.setDescription(description.getText().toString().trim());
        item = ItemListOfActualTimers.getItemFromString(item.toString());
        ((FragmentSetTimers) getFragmentManager().findFragmentByTag("fragmentSetTimersTAG")).editItemInListOfActualTimers(item, itemPositionInList);
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer editTimer");
    }

    private void addNewTimer() {
        item = new ItemListOfActualTimers(npHours.getValue(), npMinutes.getValue(), npSeconds.getValue(), description.getText().toString().trim());
        ((FragmentSetTimers) getFragmentManager().findFragmentByTag("fragmentSetTimersTAG")).addItemInListOfActualTimers(item);
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer addNewTimer");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("Hours", npHours.getValue());
        outState.putInt("Minutes", npMinutes.getValue());
        outState.putInt("Seconds", npSeconds.getValue());
        outState.putString("Description", description.getText().toString().trim());
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        npHours.setValue(savedInstanceState != null ? savedInstanceState.getInt("Hours") : 0);
        npMinutes.setValue(savedInstanceState != null ? savedInstanceState.getInt("Minutes") : 0);
        npSeconds.setValue(savedInstanceState != null ? savedInstanceState.getInt("Seconds") : 0);
        description.setText(savedInstanceState != null ? savedInstanceState.getString("Description") : null);
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onViewStateRestored");
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

        Log.d(MainActivity.logTag, "DialogFragmentEditOrAddTimer onTouch");
        return true;
    }
}
