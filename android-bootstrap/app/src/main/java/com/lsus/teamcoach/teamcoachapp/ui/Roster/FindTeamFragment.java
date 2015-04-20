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

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault.UserListFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/19/2015.
 */
public class FindTeamFragment extends Fragment implements View.OnClickListener
{
    private RosterFragment parentFragment;

    @InjectView(R.id.etFindTeamCoachName) EditText etCoachName;
    @InjectView(R.id.btnFindTeamTeamSearch) Button btnTeamSearch;


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

        btnTeamSearch.setOnClickListener(this);
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
        if(v.getId() == btnTeamSearch.getId()){
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserListFragment userListFragment = new UserListFragment();
            userListFragment.setRetainInstance(true);
            userListFragment.setParentFragment(this);
            fragmentTransaction.replace(R.id.flListView, userListFragment);
            fragmentTransaction.commit();
        }
    }

    public void setParentFragment(RosterFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

}

