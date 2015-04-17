package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 3/16/2015.
 */
public class TeamsFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewTeam) Button btnNewTeam;

    @Inject protected LogoutService logoutService;

    protected TeamsListFragment teamsListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teams, container, false);
        Injector.inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        teamsListFragment = new TeamsListFragment();
        teamsListFragment.setRetainInstance(true);
        teamsListFragment.setParentFragment(this);
        fragmentTransaction.replace(R.id.content, teamsListFragment);
        fragmentTransaction.commit();

        btnNewTeam.setOnClickListener(this);
    }

    protected LogoutService getLogoutService() {
        return logoutService;
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
    public void onClick(View view) {
        if(view.getId() == btnNewTeam.getId()){
            addTeam(view);
        }
    }

    //Only called from TeamListFragment
    public void addTeam(View v){
        Toast.makeText(this.getActivity(), "Add Team Method Called.", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        AddTeamFrag newFragment = new AddTeamFrag();
        newFragment.setTeamsListFragment(teamsListFragment);
        newFragment.show(ft, "dialog");

    }
}
