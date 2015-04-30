package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.News;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.AlternatingColorListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NewsListAdapter extends AlternatingColorListAdapter<News> {

    protected Singleton singleton = Singleton.getInstance();

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
                R.id.tv_date, R.id.tv_new_team_name};
    }

    @Override
    protected void update(final int position, final News item) {
        super.update(position, item);

        Log.d("Timestamp", item.getTimestamp());
        try {
            // Full Date Format
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

            //Date format
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");


            // Date of the news submission
            Date date;
            date = formatter.parse(item.getTimestamp());
            String sNewSubmissionDate = dateFormat.format(date);
//            Date newSubmissionDate = dateFormat.parse(sNewSubmissionDate);


            //Today's date
            Date currentDate = new Date();
            String sTodaysDate = dateFormat.format(currentDate);
//            Date todaysDate = dateFormat.parse(s2);



            if(sNewSubmissionDate.equalsIgnoreCase(sTodaysDate)) {
                //Dates are before today
                //show date
                DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                setText(2, timeFormat.format(date));
            }
            else{
                //Date are after today
                //show time
                setText(2, sNewSubmissionDate);
            }

        } catch (ParseException e) {
            Log.d("Date Error", "");
        }

        setText(0, item.getTitle());
        setText(1, item.getContent());
        ArrayList<Team> teams = singleton.getUserTeams();
        for(Team team: teams){
            if(team.getObjectId().equals(item.getTeamId())){
                setText(3, team.getTeamName());
            }
        }


    }

}
