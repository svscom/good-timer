package com.svs.goodtimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;

/**
 * Created by Виталий on 11.12.2016.
 */

public class DialogFragmentEditOrAddTimer extends DialogFragment implements View.OnClickListener {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) onViewStateRestored(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        dialogAction = args.getString("Dialog action");

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
        npMinutes = (NumberPicker) dialogView.findViewById(R.id.numberPickerMinutes);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npSeconds = (NumberPicker) dialogView.findViewById(R.id.numberPickerSeconds);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
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
        builder.setView(dialogView);
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
    }

    private void addNewTimer() {
        item = new ItemListOfActualTimers(npHours.getValue(), npMinutes.getValue(), npSeconds.getValue(), description.getText().toString().trim());
        ((FragmentSetTimers) getFragmentManager().findFragmentByTag("fragmentSetTimersTAG")).addItemInListOfActualTimers(item);
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
        super.onViewStateRestored(savedInstanceState);
        npHours.setValue(savedInstanceState != null ? savedInstanceState.getInt("Hours") : 0);
        npMinutes.setValue(savedInstanceState != null ? savedInstanceState.getInt("Minutes") : 0);
        npSeconds.setValue(savedInstanceState != null ? savedInstanceState.getInt("Seconds") : 0);
        description.setText(savedInstanceState != null ? savedInstanceState.getString("Description") : null);
        Log.d(MainActivity.logTag, "FragmentEditOrAddTimer onViewStateRestored");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        /*if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScrollView scroll = new ScrollView(getContext());
            scroll.addView(dialogView);
            getDialog().setContentView(scroll);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }*/
        super.onConfigurationChanged(newConfig);
    }
}
