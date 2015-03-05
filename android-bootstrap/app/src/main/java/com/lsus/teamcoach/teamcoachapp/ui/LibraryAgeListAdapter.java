package com.lsus.teamcoach.teamcoachapp.ui;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.AgeGroup;
import com.lsus.teamcoach.teamcoachapp.core.CheckIn;

import java.util.List;

/**
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryAgeListAdapter extends AlternatingColorListAdapter<AgeGroup> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public LibraryAgeListAdapter(final LayoutInflater inflater, final List<AgeGroup> items,
                               final boolean selectable) {
        super(R.layout.library_list_age_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public LibraryAgeListAdapter(final LayoutInflater inflater, final List<AgeGroup> items) {
        super(R.layout.library_list_age_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_name, R.id.tv_arrow};
    }

    @Override
    protected void update(final int position, final AgeGroup item) {
        super.update(position, item);

        setText(0, item.getAge());
        setText(1, ">");
    }
}
