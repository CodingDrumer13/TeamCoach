package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.AgeGroup;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

/**
 * Created by Don on 3/7/2015
 */
public class TeamsListFragment extends ItemListFragment<Team> {

    @Inject protected LogoutService logoutService;
    @Inject protected BootstrapService bootstrapService;

    @InjectView(R.id.btnNewTeam) protected Button btnNewTeam;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_teams);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.team_menu_list_item_labels, null));

        getListAdapter().addFooter(btnNewTeam);
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
    public Loader<List<Team>> onCreateLoader(final int id, final Bundle args) {
        final List<Team> initialItems = items;
        return new ThrowableLoader<List<Team>>(getActivity(), items) {

            @Override
            public List<Team> loadData() throws Exception {
                if (getActivity() != null) {
                    return getTeamItems();
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

        Toaster.showLong(this.getActivity(), "You clicked: " + item);


        //---------------------------------------------------------------------------------------
        //Original code.
        //---------------------------------------------------------------------------------------
//        final CheckIn checkIn = ((CheckIn) l.getItemAtPosition(position));
//
//        final String uri = String.format("geo:%s,%s?q=%s",
//                checkIn.getLocation().getLatitude(),
//                checkIn.getLocation().getLongitude(),
//                checkIn.getName());
//
//        // Show a chooser that allows the user to decide how to display this data, in this case, map data.
//        startActivity(Intent.createChooser(
//                new Intent(Intent.ACTION_VIEW, Uri.parse(uri)), getString(R.string.choose))

        //Initalize Fragement for adding a team


//        );
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_teams;
    }

    /**
     * Gets the list of all the coaches teams . THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    public List<Team> getTeamItems() {
        Singleton singleton = Singleton.getInstance();
        User user = singleton.getCurrentUser();

        ArrayList<Team> menuItems = new ArrayList<Team>();
        //add a selection for adding a new team

        if(!user.getTeams().isEmpty() && user.getTeams() != null)
            menuItems = (user.getTeams());

        return menuItems;
    }
}
