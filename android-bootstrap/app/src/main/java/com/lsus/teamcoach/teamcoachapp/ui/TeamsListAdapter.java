package com.lsus.teamcoach.teamcoachapp.ui;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Team;

import java.util.List;

/**
 * Created by Don on 3/11/2015.
 */
public class TeamsListAdapter extends AlternatingColorListAdapter<Team> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public TeamsListAdapter(final LayoutInflater inflater, final List<Team> items,
                               final boolean selectable) {
        super(R.layout.teams_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public TeamsListAdapter(final LayoutInflater inflater, final List<Team> items) {
        super(R.layout.teams_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_teamName};
    }

    @Override
    protected void update(final int position, final Team item) {
        super.update(position, item);

        setText(0, item.getTeamName());
    }
}
