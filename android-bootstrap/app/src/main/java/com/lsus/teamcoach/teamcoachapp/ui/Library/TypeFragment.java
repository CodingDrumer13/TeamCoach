package com.lsus.teamcoach.teamcoachapp.ui.Library;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Created by TeamCoach on 4/14/2015.
 */
public class TypeFragment extends ItemListFragment<String> implements View.OnClickListener {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    private String library = "";
    private String age = "";
    private LibraryFragment parent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);
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
    public Loader<List<String>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<String>>(getActivity(), items) {

            @Override
            public List<String> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    if(library.equalsIgnoreCase("Drills")){
                        return getDrillTypes(age);
                    } else if(library.equalsIgnoreCase("Sessions")){
                        return getSessionTypes(age);
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(final List<String> items) {
        return new LibraryListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        if(library.equalsIgnoreCase("Drills")){
            String drillType = ((String) l.getItemAtPosition(position));

            DrillListFragment drillFragment = new DrillListFragment();
            drillFragment.setRetainInstance(true);
            drillFragment.setDrillData(age, drillType);
            drillFragment.setLibrary(library);
            drillFragment.setParent(parent);
            parent.replaceFragment(this, drillFragment);
        } else if(library.equalsIgnoreCase("Sessions")){
            String sessionType = ((String) l.getItemAtPosition(position));

            SessionListFragment sessionFragment = new SessionListFragment();
            sessionFragment.setRetainInstance(true);
            sessionFragment.setSessionData(age, sessionType);
            sessionFragment.setLibrary(library);
            sessionFragment.setParent(parent);
            parent.replaceFragment(this, sessionFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {

    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    private List<String> getDrillTypes(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Passing");
        menuItems.add("Finishing");
        menuItems.add("Technical");
        if (age.length() == 3) {
            menuItems.add("Goalkeeping");
        }
        return menuItems;
    }

    private List<String> getSessionTypes(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Fitness");
        menuItems.add("Technical");
        return menuItems;
    }

    public void setAge(String age){
        this.age = age;
    }

    public void setLibrary(String library){
        this.library = library;
    }

    public String getLibrary(){
        return library;
    }

    public void setParent(LibraryFragment parent){
        this.parent = parent;
    }
}