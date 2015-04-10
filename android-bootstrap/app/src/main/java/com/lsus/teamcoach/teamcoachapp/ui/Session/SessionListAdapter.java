package com.lsus.teamcoach.teamcoachapp.ui.Session;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.ui.AlternatingColorListAdapter;

import java.util.List;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class SessionListAdapter extends AlternatingColorListAdapter<String> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public SessionListAdapter(final LayoutInflater inflater, final List<String> items,
                            final boolean selectable) {
        super(R.layout.session_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public SessionListAdapter(final LayoutInflater inflater, final List<String> items) {
        super(R.layout.session_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_name, R.id.tv_arrow};
    }

    @Override
    protected void update(final int position, final String item) {
        super.update(position, item);

        setText(0, item);
        setText(1, ">");
    }
}
