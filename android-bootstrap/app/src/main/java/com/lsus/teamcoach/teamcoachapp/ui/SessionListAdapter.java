package com.lsus.teamcoach.teamcoachapp.ui;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Session;

import java.util.List;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class SessionListAdapter extends AlternatingColorListAdapter<Session> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public SessionListAdapter(final LayoutInflater inflater, final List<Session> items,
                            final boolean selectable) {
        super(R.layout.session_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public SessionListAdapter(final LayoutInflater inflater, final List<Session> items) {
        super(R.layout.session_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_drillType};
    }

    @Override
    protected void update(final int position, final Session item) {
        super.update(position, item);

        setText(0, item.getName());
    }
}
