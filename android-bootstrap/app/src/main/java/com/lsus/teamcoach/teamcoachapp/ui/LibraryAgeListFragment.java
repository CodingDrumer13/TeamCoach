package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
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

/**
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryAgeListFragment extends ItemListFragment<AgeGroup> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

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
    public Loader<List<AgeGroup>> onCreateLoader(final int id, final Bundle args) {
        final List<AgeGroup> initialItems = items;
        return new ThrowableLoader<List<AgeGroup>>(getActivity(), items) {

            @Override
            public List<AgeGroup> loadData() throws Exception {
                if (getActivity() != null) {
                    return getAgeGroups();
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<AgeGroup> createAdapter(final List<AgeGroup> items) {
        return new LibraryAgeListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final AgeGroup age = ((AgeGroup) l.getItemAtPosition(position));

        Toaster.showLong(this.getActivity(), "The age group is: " + age.getAge());


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
//                        new Intent(Intent.ACTION_VIEW, Uri.parse(uri)), getString(R.string.choose))
//        );
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    /**
     * Gets the list of all age groups. THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    public List<AgeGroup> getAgeGroups() {
        List<AgeGroup> ages = new ArrayList<AgeGroup>();
        AgeGroup age;
        for(int i = 3; i < 19; i++){
            age = new AgeGroup();
            age.setAge("U" + i);
            ages.add(age);
        }
        return ages;
    }
}
