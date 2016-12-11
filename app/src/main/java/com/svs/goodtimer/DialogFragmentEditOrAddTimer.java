package com.svs.goodtimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by Виталий on 11.12.2016.
 */

public class DialogFragmentEditOrAddTimer extends DialogFragment implements View.OnClickListener {
    Button btnSaveOrAdd;
    NumberPicker npHours;
    NumberPicker npMinutes;
    NumberPicker npSeconds;
    EditText description;
    int itemPositionInList;

    ItemListOfActualTimers item;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup dialogView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.header_lv_actual_timers, null, false);
        btnSaveOrAdd = (Button) dialogView.findViewById(R.id.buttonStartTimer);
        btnSaveOrAdd.setOnClickListener(this);
        btnSaveOrAdd.setText("Сохранить");
        dialogView.findViewById(R.id.switchAddInActualTimers).setVisibility(View.GONE);
        dialogView.findViewById(R.id.textViewHeaderOfListActualTimers).setVisibility(View.GONE);
        dialogView.findViewById(R.id.dividerSetTimerContextAndHeaderOfList).setVisibility(View.GONE);
        Bundle args = getArguments();
        itemPositionInList = args.getInt("Item position");
        item = ItemListOfActualTimers.getItemFromString(args.getString("ItemForEdit"));
        npHours = (NumberPicker) dialogView.findViewById(R.id.numberPickerHours);
        npHours.setMinValue(0);
        npHours.setMaxValue(48);
        npHours.setValue(item.getHours());
        npMinutes = (NumberPicker) dialogView.findViewById(R.id.numberPickerMinutes);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npMinutes.setValue(item.getMinutes());
        npSeconds = (NumberPicker) dialogView.findViewById(R.id.numberPickerSeconds);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);
        npSeconds.setValue(item.getSeconds());
        description = (EditText) dialogView.findViewById(R.id.editTextDescription);
        description.setText(item.getDescription());

        builder.setIcon(android.R.drawable.ic_menu_edit);
        builder.setTitle(args.getString("Dialog action"));
        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStartTimer:
                editTimer();
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
}
