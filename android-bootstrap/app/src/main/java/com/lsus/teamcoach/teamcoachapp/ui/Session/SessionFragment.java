package com.lsus.teamcoach.teamcoachapp.ui.Session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 4/9/2015.
 */
public class SessionFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewSession) protected Button btnNewSession;
    @InjectView(R.id.btnSessionBack) protected Button btnSessionBack;

    @Inject protected LogoutService logoutService;

    SessionListFragment sessionListFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.session_fragment, container, false);
        Injector.inject(this);
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        sessionListFragment = new SessionListFragment();
        sessionListFragment.setRetainInstance(true);
        sessionListFragment.setBackButton(btnSessionBack);
        fragmentTransaction.replace(R.id.session_container, sessionListFragment);
        fragmentTransaction.commit();

        btnSessionBack.setOnClickListener(this);
        btnNewSession.setOnClickListener(this);
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
        if(view.getId() == btnNewSession.getId()){
            addSession(view);
        }
        if(view.getId() == btnSessionBack.getId()){
            sessionListFragment.backClicked();
        }
    }

    //Only called from TeamListFragment
    public void addSession(View v){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        AddSessionDialogFragment newFragment = new AddSessionDialogFragment();
        newFragment.setAgeSelected(sessionListFragment.getAgeSelected());
        newFragment.setAge(sessionListFragment.getAge());
        newFragment.show(ft, "dialog");
    }
}
