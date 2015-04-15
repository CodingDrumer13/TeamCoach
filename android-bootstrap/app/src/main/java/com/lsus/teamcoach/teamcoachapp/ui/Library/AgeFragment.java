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
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Created by TeamCoach on 4/14/2015.
 */
public class AgeFragment extends ItemListFragment<String>{

    @Inject
    protected BootstrapServiceProvider serviceProvider;
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
                    return getAgeGroups();
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
        age = ((String) l.getItemAtPosition(position));

        TypeFragment typeFragment = new TypeFragment();
        typeFragment.setRetainInstance(true);
        typeFragment.setAge(age);
        typeFragment.setLibrary(library);
        typeFragment.setParent(parent);
        parent.replaceFragment(this, typeFragment);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
    }

    private List<String> getAgeGroups() {
        List<String> ages = new ArrayList<String>();
        for(int i = 4; i < 19; i++){
            ages.add("U" + i);
        }
        return ages;
    }

    public String getAge(){
        return age;
    }

    public void setLibrary(String library){
        this.library = library;
    }

    public void setParent(LibraryFragment parent){
        this.parent = parent;
    }
}