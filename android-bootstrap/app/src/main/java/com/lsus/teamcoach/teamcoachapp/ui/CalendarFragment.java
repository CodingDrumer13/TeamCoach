package com.lsus.teamcoach.teamcoachapp.ui;

import android.content.Intent;
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
 * Created by Caroline on 3/25/2015.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewEvent) Button btnNewEvent;

    @Inject protected LogoutService logoutService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);
        Injector.inject(this);
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CalendarListFragment calListFragment = new CalendarListFragment();
        calListFragment.setRetainInstance(true);
        fragmentTransaction.replace(R.id.teamCalendarView, calListFragment);
        fragmentTransaction.commit();

        btnNewEvent.setOnClickListener(this);
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
        if(view.getId() == btnNewEvent.getId()){
            addEvent(view);
        }
    }

    //Only called from TeamListFragment
    public void addEvent(View v){
        Toast.makeText(this.getActivity(), "Add Event.", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        DialogFragment newFragment = new AddEventFrag();
        newFragment.show(ft, "dialog");
    }
}
