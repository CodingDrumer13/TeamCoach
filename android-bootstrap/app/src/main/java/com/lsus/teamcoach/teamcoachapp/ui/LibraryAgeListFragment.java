package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.AgeGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;

/**
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryAgeListFragment extends ItemListFragment<String> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_library_list_header) protected TextView listHeader;

    private boolean typeSelected = false;
    private String age = "";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.library_list_age_item_labels, null));
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
    public Loader<List<String>> onCreateLoader(final int id, final Bundle args) {
        final List<String> initialItems = items;
        return new ThrowableLoader<List<String>>(getActivity(), items) {

            @Override
            public List<String> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());

                    if(!typeSelected){
                        return getAgeGroups();
                    } else {
                        return getMenuItems(age);
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(final List<String> items) {
        return new LibraryAgeListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {


        if(!typeSelected){
            final String age = ((String) l.getItemAtPosition(position));
            this.age = age;

            //TODO Change the fragment header when changing screens.
            if(listHeader != null){
                listHeader.setText(age + ": " + R.string.drill_type_column + " Drills");
            }


            typeSelected = true;
        }
        else{
            final String drillType = ((String) l.getItemAtPosition(position));

            if(!drillType.equalsIgnoreCase("Back")){
                Intent drillIntent = new Intent(new Intent(getActivity(), DrillListActivity.class));
                drillIntent.putExtra(DRILL_AGE, age);
                drillIntent.putExtra(DRILL_TYPE, drillType);
                startActivity(drillIntent);
            }

            typeSelected = false;
        }

        this.refresh();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    /**
     * Gets the list of all age groups. THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    private List<String> getAgeGroups() {
        List<String> ages = new ArrayList<String>();
        AgeGroup age;
        for(int i = 3; i < 19; i++){
            ages.add("U" + i);
        }
        return ages;
    }

    private List<String> getMenuItems(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("BACK");
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Passing");
        menuItems.add("Shooting");
        if (age.length() == 3){
            menuItems.add("Goalkeeping");
        }
        return menuItems;
    }
}
