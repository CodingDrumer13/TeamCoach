package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.CalendarEvent;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Caroline on 3/25/2015
 */
public class CalendarListFragment extends ListFragment {

    @Inject protected BootstrapServiceProvider serviceProvider;

    /**
     * List items provided to
     */
    protected List<CalendarEvent> items = Collections.emptyList();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);


    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(createAdapter(getCalendarItems()));
//        setEmptyText(R.string.no_teams);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //    @Override
    protected void configureList(final Activity activity, final ListView listView) {
//        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    public Loader<List<CalendarEvent>> onCreateLoader(final int id, final Bundle args) {

        final List<CalendarEvent> initialItems = items;
        return new ThrowableLoader<List<CalendarEvent>>(getActivity(), items) {

            @Override
            public List<CalendarEvent> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    return getCalendarItems();
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    //    @Override
    protected SingleTypeAdapter<CalendarEvent> createAdapter(final List<CalendarEvent> items) {
        return new CalendarListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final CalendarEvent item = ((CalendarEvent) l.getItemAtPosition(position));

        Toaster.showShort(this.getActivity(), "You clicked: " + item);

    }


    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_calendar;
    }


    /**
     * Gets the list of all the coaches teams . THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    public List<CalendarEvent> getCalendarItems() {
        Singleton singleton = Singleton.getInstance();
        User user = singleton.getCurrentUser();

        ArrayList<CalendarEvent> menuItems = new ArrayList<CalendarEvent>();

//        if(user.getTeams() == null) {
//            //Inform the User to add a team
////            Team team = new Team();
////            team.setTeamName("Name");
////            menuItems.add(team);
//        }else{
//            menuItems = (user.getTeams());
//        }
        CalendarEvent event = new CalendarEvent();
        event.setEventName("New Event");
        Date date = new Date();
        event.setDate(date);
        event.setEventType("Game");
        menuItems.add(event);

        return menuItems;
    }
}
