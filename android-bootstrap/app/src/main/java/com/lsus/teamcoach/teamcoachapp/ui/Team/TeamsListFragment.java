package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import com.lsus.teamcoach.teamcoachapp.ui.Team.TeamsListAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * Created by Don on 3/7/2015
 */
public class TeamsListFragment extends ItemListFragment<Team> {

    @Inject protected BootstrapServiceProvider serviceProvider;
//    @Inject protected LogoutService logoutService;
    @Inject protected BootstrapService bootstrapService;

    /**
     * List items provided to
     */
    protected Singleton singleton = Singleton.getInstance();
    protected User user = singleton.getCurrentUser();
    protected ArrayList<Team> menuItems = null;

    protected TeamsListFragment teamsListFragment;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
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

                try {
                    List<Team> latest = null;

                    if (getActivity() != null) {
                        serviceProvider.getService(getActivity());
                        latest = getTeamItems();
                    }

                    if (latest != null) {
                        return latest;
                    } else {
                        return Collections.emptyList();
                    }
                } catch (final OperationCanceledException e) {
                    final Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                    return initialItems;
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
    public List<Team> getTeamItems() {
        if(user.getTeams() == null) {
            //Inform the User to add a team
//            Team team = new Team();
//            team.setTeamName("Name");
//            menuItems.add(team);
            menuItems = null;
        }else{

            SafeAsyncTask<Boolean> authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    ArrayList<Team> teams = new ArrayList<Team>();
                    menuItems = new ArrayList<Team>();
                    for(Team team : user.getTeams()){
//                        Team fullTeam = serviceProvider.getService(getActivity()).getTeam(team.getObjectId());
                        Team fullTeam = bootstrapService.getTeam(team.getObjectId());

                        teams.add(fullTeam);
                    }
                    user.setTeams(teams);
                    menuItems = teams;

                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if(!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if(cause != null) {
//                            Toaster.showLong(TeamsListFragment.this, cause.getMessage());
                        }
                    }
                }

                @Override
                public void onSuccess(final Boolean authSuccess) {
//                    onAuthenticationResult(authSuccess);
                }

                @Override
                protected void onFinally() throws RuntimeException {
//                    hideProgress();
//                    authenticationTask = null;
                }
            };
            authenticationTask.execute();

        }

        return menuItems;
    }

    public void setTeamsListFragment(TeamsListFragment teamsListFragment){
        this.teamsListFragment = teamsListFragment;
    }

    //Removes the additional menu Items
    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu, final MenuInflater inflater) {
    }

}
