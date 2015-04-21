package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;

import javax.inject.Inject;

/**
 * Created by TeamCoach on 4/14/2015.
 */
public class AddDrillFragment extends Fragment implements View.OnClickListener{

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin, container, false);
        return view;
    }

    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onClick(View v) {

    }
}
