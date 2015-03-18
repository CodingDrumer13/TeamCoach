package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Team;

import javax.inject.Inject;

/**
 * Created by TeamCoach on 3/3/2015.
 */
public class AdminFragment extends GenCarouselFragment<Team> {

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    View view;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.admin, container, false);
        return view;
    }

    @Override
    protected LogoutService getLogoutService() {
        return logoutService;
    }

}