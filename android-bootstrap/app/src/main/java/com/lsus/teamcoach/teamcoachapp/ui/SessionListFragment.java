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
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class SessionListFragment extends ItemListFragment<Session> {

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
                        .inflate(R.layout.drill_type_list_label, null));
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
    public void onResume(){
        this.refresh();
        super.onResume();
    }

    @Override
    public Loader<List<Session>> onCreateLoader(final int id, final Bundle args) {
        final List<Session> initialItems = items;
        return new ThrowableLoader<List<Session>>(getActivity(), items) {

            @Override
            public List<Session> loadData() throws Exception {
                if (getActivity() != null) {
                    Singleton singleton = Singleton.getInstance();
                    serviceProvider.getService(getActivity());
                    return Collections.emptyList();
                    //return serviceProvider.getService(getActivity()).getDrills(age, type);
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Session> createAdapter(final List<Session> items) {
        return new SessionListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Session item = ((Session) l.getItemAtPosition(position));

        Toaster.showShort(this.getActivity(), item.getName());
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_drills;
    }
}
