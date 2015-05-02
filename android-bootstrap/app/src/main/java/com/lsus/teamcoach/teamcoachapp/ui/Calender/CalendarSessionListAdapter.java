package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.AlternatingColorListAdapter;

import java.util.List;

/**
 * Created by Caroline on 5/1/2015.
 */
public class CalendarSessionListAdapter extends AlternatingColorListAdapter<Session> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public CalendarSessionListAdapter(final LayoutInflater inflater, final List<Session> items,
                                  final boolean selectable) {
        super(R.layout.session_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CalendarSessionListAdapter(final LayoutInflater inflater, final List<Session> items) {
        super(R.layout.session_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_session_name};
    }

    @Override
    protected void update(final int position, final Session item) {
        super.update(position, item);

        setText(0, item.getName());
    }
}
