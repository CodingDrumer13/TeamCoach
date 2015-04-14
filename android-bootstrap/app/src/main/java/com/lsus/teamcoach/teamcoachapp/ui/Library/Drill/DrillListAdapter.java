package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.ui.AlternatingColorListAdapter;

import java.util.List;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class DrillListAdapter extends AlternatingColorListAdapter<Drill> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public DrillListAdapter(final LayoutInflater inflater, final List<Drill> items,
                            final boolean selectable) {
        super(R.layout.drill_type_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public DrillListAdapter(final LayoutInflater inflater, final List<Drill> items) {
        super(R.layout.drill_type_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_drillType};
    }

    @Override
    protected void update(final int position, final Drill item) {
        super.update(position, item);

        setText(0, item.getDrillName());
    }
}
