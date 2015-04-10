package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.ui.Library.AddDrillDialogFragment;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 4/9/2015.
 */
public class SessionFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewDrill) protected Button btnNewDrill;

    @Inject protected LogoutService logoutService;

    SessionListFragment sessionListFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        Injector.inject(this);
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //sessionListFragment = new LibraryListFragment();
        //sessionListFragment.setRetainInstance(true);
        //sessionListFragment.setBackButton(btnLibraryBack);
        //fragmentTransaction.replace(R.id.library_container, sessionListFragment);
        //fragmentTransaction.commit();

        btnNewDrill.setOnClickListener(this);
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
        if(view.getId() == btnNewDrill.getId()){
            addDrill(view);
        }
    }

    //Only called from TeamListFragment
    public void addDrill(View v){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //AddDrillDialogFragment newFragment = new AddDrillDialogFragment();
        //newFragment.setAgeSelected(sessionListFragment.getAgeSelected());
        //newFragment.setAge(sessionListFragment.getAge());
        //newFragment.show(ft, "dialog");
    }
}
