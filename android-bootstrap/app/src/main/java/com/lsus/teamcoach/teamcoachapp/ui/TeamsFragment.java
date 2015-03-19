package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;

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

        TeamsListFragment teamsListFragment = new TeamsListFragment();
        teamsListFragment.setRetainInstance(true);
        fragmentTransaction.replace(R.id.teamslistView, teamsListFragment);
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
//
//        fm.beginTransaction();
//        Fragment fragment = new AddTeamFragment();
//        ft.replace(R.id.teams_root_view, fragment).commit();

        DialogFragment newFragment = new AddTeamFragment();
        newFragment.show(ft, "dialog");
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        // Create and show the dialog.
//        SomeDialog newFragment = new SomeDialog ();
//        newFragment.show(ft, "dialog");
    }
}
