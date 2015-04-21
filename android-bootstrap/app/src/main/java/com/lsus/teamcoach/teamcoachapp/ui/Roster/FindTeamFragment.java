package com.lsus.teamcoach.teamcoachapp.ui.Roster;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

/**
 * Created by Don on 4/19/2015.
 */
public class FindTeamFragment extends Fragment implements View.OnClickListener
{
    private RosterFragment parentFragment;
    private SafeAsyncTask<Boolean> authenticationTask;
    private Singleton singleton = Singleton.getInstance();

    @InjectView(R.id.etFindTeamTeamCode) EditText etTeamCode;
    @InjectView(R.id.btnFindTeamSubmit) Button btnTeamSubmit;

    @Inject protected BootstrapService bootstrapService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_team_fragment, container, false);
        Injector.inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        rosterListFragment = new RosterListFragment();
//        rosterListFragment.setRetainInstance(true);
//        rosterListFragment.setParentFragment(this);
//        fragmentTransaction.replace(R.id.roster_content, rosterListFragment);
//        fragmentTransaction.commit();
//

        btnTeamSubmit.setOnClickListener(this);
    }


    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        getActivity().dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        getActivity().showDialog(0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnTeamSubmit.getId()){
            showProgress();
            authenticationTask = new SafeAsyncTask<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Team team = bootstrapService.getTeam(etTeamCode.getText().toString());
                    User user = singleton.getCurrentUser();
                    ArrayList<Team> teams = new ArrayList<Team>();
                    teams.add(team);
                    user.setTeams(teams);
                    bootstrapService.update(user);
                    singleton.setCurrentUser(user);
                    return true;
                }
                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if (!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if (cause != null) {
                            Toaster.showLong(getActivity(), cause.getMessage());
                        }
                    }
                }

                @Override
                public void onSuccess(final Boolean authSuccess) {

                }

                @Override
                protected void onFinally() throws RuntimeException {
                    hideProgress();
                    authenticationTask = null;
                }
            };
            authenticationTask.execute();

            this.getFragmentManager().popBackStack();
        }
    }

    public void setParentFragment(RosterFragment parentFragment) {
        this.parentFragment = parentFragment;
    }
}

