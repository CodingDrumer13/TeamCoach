package com.lsus.teamcoach.teamcoachapp.ui;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.*;

import java.util.List;

/**
 * Created by Caroline on 3/11/2015.
 */
public class CalendarListAdapter extends AlternatingColorListAdapter<CalendarEvent> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public CalendarListAdapter(final LayoutInflater inflater, final List<CalendarEvent> items,
                            final boolean selectable) {
        super(R.layout.calendar_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CalendarListAdapter(final LayoutInflater inflater, final List<CalendarEvent> items) {
        super(R.layout.calendar_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_teamCalendar};
    }

    @Override
    protected void update(final int position, final CalendarEvent item) {
        super.update(position, item);

        //setText(0, item.getEventName());
    }
}
