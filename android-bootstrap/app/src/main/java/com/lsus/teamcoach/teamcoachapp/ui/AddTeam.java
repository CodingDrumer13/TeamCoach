package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 3/18/2015.
 */
public class AddTeam extends DialogFragment implements View.OnClickListener {

    @InjectView(R.id.btnAddTeamNegative) Button btnAddTeamNegative;
    @InjectView(R.id.btnAddTeamPositive) Button btnAddTeamPositive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Team");

        View view = inflater.inflate(R.layout.add_team_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnAddTeamNegative.setOnClickListener(this);
        btnAddTeamPositive.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(btnAddTeamPositive.getId() == view.getId()) {

        }
        if(btnAddTeamNegative.getId() == view.getId()){
            dismiss();
        }
    }
}
