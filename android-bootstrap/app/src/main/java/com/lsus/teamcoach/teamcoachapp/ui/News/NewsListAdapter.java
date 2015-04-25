package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.util.Log;
import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.News;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.AlternatingColorListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsListAdapter extends AlternatingColorListAdapter<News> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items,
                           final boolean selectable) {
        super(R.layout.news_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items) {
        super(R.layout.news_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_title, R.id.tv_summary,
                R.id.tv_date};
    }

    @Override
    protected void update(final int position, final News item) {
        super.update(position, item);

//        Date date;
//        Log.d("Timestamp", item.getTimestamp());
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:MM:SS.MMM");
//        try {
//            date = formatter.parse(item.getTimestamp());
//
//
//        } catch (ParseException e) {
//            Log.d("Date Error", "");
//        }

        setText(0, item.getTitle());
        setText(1, item.getContent());
        setText(2, item.getTimestamp());


    }
}
