package com.svs.goodtimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Виталий on 26.11.2016.
 */

public class FragmentSetTimers extends Fragment implements View.OnTouchListener, View.OnFocusChangeListener,
        View.OnClickListener, TextView.OnEditorActionListener, ListView.OnItemClickListener, AbsListView.MultiChoiceModeListener {
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
        listViewOfActualTimers.setMultiChoiceModeListener(this);

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
        etDescription.setOnEditorActionListener(this);
        switchAddInActualList = (Switch) header.findViewById(R.id.switchAddInActualTimers);
        btnStartTimer = (Button) header.findViewById(R.id.buttonStartTimer);
        btnStartTimer.setOnClickListener(this);

        listViewOfActualTimers.setAdapter(adapterForListOfActualTimers);
        listViewOfActualTimers.setOnItemClickListener(this);

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

        Log.d(MainActivity.logTag, "FragmentSetTimers onTouch");
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStartTimer) {
            Log.d(MainActivity.logTag, "Button startTimer onClick");
            if (switchAddInActualList.isChecked()) {
                addItemInListOfActualTimers(); //добавляем таймер в лист
                Log.d(MainActivity.logTag, "Button startTimer addInListOfActualTimers");
            }
            startNewTimer();
        }
    }

    private void startNewTimer() {
        //обнуляем значения numberPicker'ов, EditText'а и switch'a
        if (numberPickerHours.getValue() != numberPickerHours.getMinValue()) numberPickerHours.setValue(numberPickerHours.getMinValue());
        if (numberPickerMinutes.getValue() != numberPickerMinutes.getMinValue()) numberPickerMinutes.setValue(numberPickerMinutes.getMinValue());
        if (numberPickerSeconds.getValue() != numberPickerSeconds.getMinValue()) numberPickerSeconds.setValue(numberPickerSeconds.getMinValue());
        if (etDescription.getText().length() != 0) etDescription.getText().clear();
        if (switchAddInActualList.isChecked()) switchAddInActualList.setChecked(false);
        Log.d(MainActivity.logTag, "startNewTimer");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            v.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == listViewOfActualTimers.getId()) {
            Log.d(MainActivity.logTag, String.valueOf(v.getId()));
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

            View header = View.inflate(getContext(), R.layout.item_listview_actual_timers, null);

            ItemListOfActualTimers itemForHeader = (ItemListOfActualTimers) listViewOfActualTimers.getAdapter().getItem((acmi.position));
            if (itemForHeader.getDescription() == null || "".equals(itemForHeader.getDescription())) {
                header.findViewById(R.id.textViewListActualTimersTimerDescription).setVisibility(View.GONE);
                header.findViewById(R.id.viewListActualTimersHorizontalDivider).setVisibility(View.GONE);
            } else {
                header.findViewById(R.id.textViewListActualTimersTimerDescription).setVisibility(View.VISIBLE);
                header.findViewById(R.id.viewListActualTimersHorizontalDivider).setVisibility(View.VISIBLE);
                ((TextView) header.findViewById(R.id.textViewListActualTimersTimerDescription)).setText(itemForHeader.getDescription());
            }
            ((TextView) header.findViewById(R.id.textViewListActualTimersTimerValue)).setText(itemForHeader.getTimeInString());

            menu.setHeaderView(header);

            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_actual_timers, menu);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(MainActivity.logTag, "Бля" + String.valueOf(position) + id);
        registerForContextMenu(listViewOfActualTimers); // регистрируем для контекстного меню
        listViewOfActualTimers.showContextMenuForChild(view);
        unregisterForContextMenu(listViewOfActualTimers);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d(MainActivity.logTag, "Бля" + String.valueOf(info.position) + " " + info.id);
        switch (item.getItemId())
        {
            case R.id.context_menu_actual_timers_start_timer_ID:
                Toast.makeText(getContext(), "Таймер запущен", Toast.LENGTH_LONG).show();
                return true;
            case R.id.context_menu_actual_timers_select_ID:
                listViewOfActualTimers.setItemChecked(info.position, true);
                Toast.makeText(getContext(), "Выбрать", Toast.LENGTH_LONG).show();
                return true;
            case R.id.context_menu_actual_timers_edit_ID:
                Toast.makeText(getContext(), "Редактировать", Toast.LENGTH_LONG).show();
                return true;
            case R.id.context_menu_actual_timers_delete_ID:
                ItemListOfActualTimers itemForRemove = (ItemListOfActualTimers) listViewOfActualTimers.getAdapter().getItem(info.position);
                if (listOfActualTimers.contains(itemForRemove)) listOfActualTimers.remove(itemForRemove);
                sortListOfActualTimers();
                adapterForListOfActualTimers.notifyDataSetChanged();
                Toast.makeText(getContext(), "Таймер удалён из списка", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        Log.d(MainActivity.logTag, "FragmentSetTimers saveListOfActualTimers");
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
        sortListOfActualTimers();
        Log.d(MainActivity.logTag, "FragmentSetTimers loadListOfActualTimers");
    }

    void addItemInListOfActualTimers() {
        ItemListOfActualTimers newItem = new ItemListOfActualTimers(numberPickerHours.getValue(),
                numberPickerMinutes.getValue(), numberPickerSeconds.getValue(), etDescription.getText().toString().trim());
        if (listOfActualTimers.contains(newItem)) {
            Toast.makeText(getContext(), "Такой таймер уже есть в списке", Toast.LENGTH_LONG).show();
        } else if (newItem.getTimeInMillis() == 0) {
            Toast.makeText(getContext(), "\tУстановленное время действия таймера равно нулю.\n\tПожалуйста, установите не нулевое значение.", Toast.LENGTH_LONG).show();
        } else {
            listOfActualTimers.add(newItem);

            sortListOfActualTimers();
            adapterForListOfActualTimers.notifyDataSetChanged();
        }
        Log.d(MainActivity.logTag, "FragmentSetTimers addItemInListOfActualTimers");
    }

    void sortListOfActualTimers() {
        Collections.sort(listOfActualTimers, new Comparator<ItemListOfActualTimers>() {
            @Override
            public int compare(ItemListOfActualTimers lhs, ItemListOfActualTimers rhs) {
                if (((lhs.getDescription() != null && !lhs.getDescription().equals("")) &&
                        (rhs.getDescription() != null && !rhs.getDescription().equals(""))) ||
                ((lhs.getDescription() == null || lhs.getDescription().equals("")) &&
                        (rhs.getDescription() == null || rhs.getDescription().equals("")))) { //у обоих или есть или нет описания сравниваем по времени
                    return (lhs.getTimeInMillis() - rhs.getTimeInMillis() > 0 ? 1 : -1);
                }
                if (((lhs.getDescription() != null && !lhs.getDescription().equals("")) &&
                        (rhs.getDescription() == null || rhs.getDescription().equals("")))) { //у левого есть, у правого нет
                    return -1;
                }
                if (((lhs.getDescription() == null || lhs.getDescription().equals("")) &&
                        (rhs.getDescription() != null && !rhs.getDescription().equals("")))) { //у правого есть, у левого нет
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public void onDestroy() {
        saveListOfActualTimers();
        Log.d(MainActivity.logTag, "FragmentSetTimers onDestroy");
        super.onDestroy();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        Log.d(MainActivity.logTag, "position = " + position + ", checked = "
                + checked);
        final int checkedCount = listViewOfActualTimers.getCheckedItemCount();
        switch (checkedCount) {
            case 0:
                mode.setSubtitle(null);
                break;
            case 1:
                mode.setSubtitle("Выделено: 1");
                break;
            default:
                mode.setSubtitle("Выделено: " + checkedCount);
                break;
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.setTitle("Всего: " + listViewOfActualTimers.getAdapter().getCount());
        mode.getMenuInflater().inflate(R.menu.action_mode_menu_actual_timers, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                for (int i = 0; i < listViewOfActualTimers.getAdapter().getCount(); i++) {
                    if (!listViewOfActualTimers.isItemChecked(i)) listViewOfActualTimers.setItemChecked(i, true);
                }
                return true;
            case R.id.item2:
                Toast.makeText(getContext(), "Выбранные таймеры запущены", Toast.LENGTH_LONG).show();
                mode.finish();
                return true;
            case R.id.item3:
                SparseBooleanArray sbArray = listViewOfActualTimers.getCheckedItemPositions();
                int count = listViewOfActualTimers.getCheckedItemCount();
                ArrayList<Integer> checkedItemPositions = new ArrayList<>(count);
                for (int i = 0; i < sbArray.size(); i++) {
                    if (sbArray.get(sbArray.keyAt(i))) checkedItemPositions.add(sbArray.keyAt(i));
                }
                Collections.sort(checkedItemPositions);
                Collections.reverse(checkedItemPositions);
                for (int i = 0; i < checkedItemPositions.size(); i++) {
                    listOfActualTimers.remove(checkedItemPositions.get(i) - 1);
                }
                adapterForListOfActualTimers.notifyDataSetChanged();
                Toast.makeText(getContext(), "Таймеры удалёны из списка", Toast.LENGTH_LONG).show();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}