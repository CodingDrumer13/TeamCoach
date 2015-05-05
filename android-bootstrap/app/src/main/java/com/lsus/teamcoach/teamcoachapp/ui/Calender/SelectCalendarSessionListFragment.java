package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.ui.Calender.AddCalSessionFrag;
import com.lsus.teamcoach.teamcoachapp.ui.Calender.CalendarInfoFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListRatingAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListRatingAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Caroline on 5/1/2015.
 */
public class SelectCalendarSessionListFragment extends ItemListFragment<Session> {

    @Inject
    protected BootstrapServiceProvider serviceProvider;
    @Inject
    protected LogoutService logoutService;

    private String age;
    private String type;
    private String library;
    private CalendarInfoFragment parent;
    private AddCalSessionFrag container;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        this.setHasOptionsMenu(false);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        this.refresh();
        super.onResume();
    }

    @Override
    public Loader<List<Session>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<Session>>(getActivity(), items) {

            @Override
            public List<Session> loadData() throws Exception {
                if (getActivity() != null) {
                    return serviceProvider.getService(getActivity()).getPublicSessions(age, type);
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Session> createAdapter(final List<Session> items) {
        return new SessionListRatingAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        Session session = ((Session) l.getItemAtPosition(position));
        parent.setSessionToAdd(session);
        parent.initList();
        container.dismiss();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_sessions;
    }

    public void setSessionData(String age, String type) {
        this.age = age;
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }


    public void setParent(CalendarInfoFragment calParent) {this.parent = calParent;}

    public void setContainer(AddCalSessionFrag container) { this.container = container; }

    public void setType(String type) {this.type = type;}
}