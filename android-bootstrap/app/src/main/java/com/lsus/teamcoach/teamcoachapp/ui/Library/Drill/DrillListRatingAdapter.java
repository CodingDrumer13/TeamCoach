package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.AlternatingColorListAdapter;

import java.util.List;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class DrillListRatingAdapter extends AlternatingColorListAdapter<Drill> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public DrillListRatingAdapter(final LayoutInflater inflater, final List<Drill> items,
                                  final boolean selectable) {
        super(R.layout.drill_type_list_rating_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public DrillListRatingAdapter(final LayoutInflater inflater, final List<Drill> items) {
        super(R.layout.drill_type_list_rating_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_drillType, R.id.drillRatingBar};
    }

    @Override
    protected void update(final int position, final Drill item) {
        super.update(position, item);

        setText(0, item.getDrillName());
        ((RatingBar)view(1)).setRating(item.getDrillRating());
    }
}
