package com.svs.goodtimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Виталий on 28.11.2016.
 */

class AdapterForListOfActualTimers extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ItemListOfActualTimers> itemsListOfActualTimers;

    AdapterForListOfActualTimers(Context context, ArrayList<ItemListOfActualTimers> objects) {
        this.context = context;
        this.itemsListOfActualTimers = objects;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemsListOfActualTimers.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsListOfActualTimers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemListOfActualTimers item = (ItemListOfActualTimers) getItem(position);

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_listview_actual_timers, parent, false);
        }

        // заполняем View в пункте списка данными
        if (item.getDescription() != null && !item.getDescription().equals("")) {
            view.findViewById(R.id.textViewListActualTimersTimerDescription).setVisibility(View.VISIBLE);
            view.findViewById(R.id.viewListActualTimersHorizontalDivider).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.textViewListActualTimersTimerDescription)).setText(item.getDescription());
            ((TextView) view.findViewById(R.id.textViewListActualTimersTimerValue)).setText(item.getTimeInString());
        }
        else {
            view.findViewById(R.id.textViewListActualTimersTimerDescription).setVisibility(View.GONE);
            view.findViewById(R.id.viewListActualTimersHorizontalDivider).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.textViewListActualTimersTimerValue)).setText(item.getTimeInString());
        }

        return view;
    }
}
