package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Don on 3/7/2015
 * Modified by TeamCoach on 4/8/2015
 */
public class TeamsListFragment extends ItemListFragment<Team> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    /**
     * List items provided to
     */
    protected Singleton singleton = Singleton.getInstance();
    protected User user = singleton.getCurrentUser();
    protected ArrayList<Team> menuItems = null;
    private TeamsListFragment teamsListFragment = this;
    private int listSize = 0;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return 0;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(R.string.no_teams);
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
    public Loader<List<Team>> onCreateLoader(final int id, final Bundle args) {
        final List<Team> initialItems = items;
        return new ThrowableLoader<List<Team>>(getActivity(), items) {
            @Override
            public List<Team> loadData() throws Exception {

                List<Team> latest = null;

                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    latest = getTeamItems();
                    listSize = latest.size();
                }

                if (latest != null) {
                    return latest;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }
    @Override
    protected SingleTypeAdapter<Team> createAdapter(final List<Team> items) {
        return new TeamsListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Team item = ((Team) l.getItemAtPosition(position));

        Toaster.showShort(this.getActivity(), "You clicked: " + item);

    }

    @Override
    public void onLoadFinished(final Loader<List<Team>> loader, final List<Team> items) {
        super.onLoadFinished(loader, items);
    }

    /**
     * Gets the list of all the coaches teams . THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    //TODO move contents of this method to bootstrap service where I put the other todo!!!
    //TODO replace the contents with querying the singleton for the list of teams.
    public List<Team> getTeamItems() {

        //Sets up the query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("coach", ParseUser.getCurrentUser().getEmail());

        //Queries in background, ON SEPARATE THREAD SO NOT UP TO DATE WITH MAIN THREAD!
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> teamList, ParseException e) {
                ArrayList<Team> newList = new ArrayList<Team>();

                //Pulls each team from the ParseObject, creates a Team object and adds it to the list.
                for (ParseObject parseTeam : teamList) {
                    Team addTeam = new Team();
                    addTeam.setTeamName(parseTeam.getString("teamName"));
                    addTeam.setAgeGroups(parseTeam.getString("ageGroup"));
                    addTeam.setCoach(parseTeam.getString("coach"));

                    newList.add(addTeam);
                }

                //Saving Locally
                singleton.setUserTeams(newList);


                //Forces refresh if background thread is running behind.
                if(listSize != newList.size()) teamsListFragment.refresh();
            }
        });
        menuItems = singleton.getUserTeams();

        return menuItems;
    }

    //Removes the additional menu Items
    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu, final MenuInflater inflater) {
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

}
