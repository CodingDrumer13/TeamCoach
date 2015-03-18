package com.lsus.teamcoach.teamcoachapp.ui;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;

import java.util.List;

/**
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryListAdapter extends AlternatingColorListAdapter<String> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public LibraryListAdapter(final LayoutInflater inflater, final List<String> items,
                              final boolean selectable) {
        super(R.layout.library_list_age_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public LibraryListAdapter(final LayoutInflater inflater, final List<String> items) {
        super(R.layout.library_list_age_item, inflater, items);
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
