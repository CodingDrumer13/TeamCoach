package com.lsus.teamcoach.teamcoachapp.ui.Calender;

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
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Caroline on 3/25/2015
 */
public class CalendarListFragment extends ItemListFragment<CalendarEvent> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    protected Singleton singleton = Singleton.getInstance();
    protected User user = singleton.getCurrentUser();

    private CalendarListAdapter adapter;

    /**
     * List items provided to
     */
    //protected List<CalendarEvent> items = Collections.emptyList();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("No Events");
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
       super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);


    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<CalendarEvent>> onCreateLoader(final int id, final Bundle args) {

        return new ThrowableLoader<List<CalendarEvent>>(getActivity(), items) {

            @Override
            public List<CalendarEvent> loadData() throws Exception {
                if (getActivity() != null) {
                    return serviceProvider.getService(getActivity()).getEvents(singleton.getCurrentUser().getEmail());
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<CalendarEvent> createAdapter(List<CalendarEvent> items) {
        return new CalendarListAdapter(getActivity().getLayoutInflater(), items);

    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final CalendarEvent item = ((CalendarEvent) l.getItemAtPosition(position));

        Toaster.showShort(this.getActivity(), "You clicked: " + item.getEventName());

    }

    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_calendar;
    }

    private List<CalendarEvent> getCalendarItems() {
        Singleton singleton = Singleton.getInstance();
        User user = singleton.getCurrentUser();

        ArrayList<CalendarEvent> menuItems =  user.getEvents();


        return menuItems;
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }


}
