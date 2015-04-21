package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.app.Activity;
import android.content.Intent;
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
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryFragment;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_ID;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class SessionListFragment extends ItemListFragment<Session> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    private String age;
    private String type;
    private String library;
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
    protected LogoutService getLogoutService() { return logoutService; }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<Session>> onCreateLoader(final int id, final Bundle args) {
        final List<Session> initialItems = items;

        return new ThrowableLoader<List<Session>>(getActivity(), items) {
            @Override
            public List<Session> loadData() throws Exception {
                if (getActivity() != null) {
                    //TODO Fix pulling only public drills, not working.
                    return serviceProvider.getService(getActivity()).getPublicSessions(age, type);
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

        Intent sessionInfoIntent = new Intent(getActivity(), SessionInfoActivity.class);
        sessionInfoIntent.putExtra(SESSION, item);
        sessionInfoIntent.putExtra(SESSION_ID, item.getObjectId());
        startActivity(sessionInfoIntent);
    }

    @Override
    public void onResume(){
        this.refresh();
        super.onResume();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_drills;
    }

    public void setSessionData(String age, String type){
        this.age = age;
        this.type = type;
    }

    public String getAge(){
        return age;
    }

    public String getType(){
        return type;
    }

    public void setLibrary(String library){
        this.library = library;
    }

    public String getLibrary(){
        return library;
    }

    public void setParent(LibraryFragment parent){ this.parent = parent; }
}
