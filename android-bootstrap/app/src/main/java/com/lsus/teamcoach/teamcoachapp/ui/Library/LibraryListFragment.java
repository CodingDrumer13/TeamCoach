package com.lsus.teamcoach.teamcoachapp.ui.Library;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryListFragment extends ItemListFragment<String> implements View.OnClickListener{

    private Button backButton;
    private Button addButton;
    private Button homeButton;
    private LibraryFragment parent;

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        parent.setHeaderVisibility(false);
        backButton.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
        homeButton.setVisibility(View.GONE);
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
                    return getLibraries();
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
        String library = ((String) l.getItemAtPosition(position));

        backButton.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        homeButton.setVisibility(View.VISIBLE);

        AgeFragment ageFragment = new AgeFragment();
        ageFragment.setRetainInstance(true);
        ageFragment.setLibrary(library);
        parent.setLibrary(library);
        ageFragment.setParent(parent);
        parent.replaceFragment(this, ageFragment);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void onClick(View view) {

    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    private List<String> getLibraries(){
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Sessions");
        menuItems.add("Drills");
        return menuItems;
    }

    public void setButtons(Button backButton, Button addButton, Button homeButton){
        this.addButton = addButton;
        this.backButton = backButton;
        this.homeButton = homeButton;
    }

    public void setParentFragment(LibraryFragment fragment){
        this.parent = fragment;
    }
}