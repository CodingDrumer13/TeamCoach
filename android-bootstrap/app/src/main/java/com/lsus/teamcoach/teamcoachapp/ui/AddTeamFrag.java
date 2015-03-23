package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Team;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 3/18/2015.
 */
public class AddTeamFrag extends DialogFragment implements View.OnClickListener {

    @Inject protected BootstrapServiceProvider serviceProvider;

    @InjectView(R.id.etAddTeamName) EditText etAddTeamName;
    @InjectView(R.id.sAddTeamAgeGroup) Spinner sAddTeamGroup;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sAddTeamGroup.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if(btnAddTeamPositive.getId() == view.getId()) {
            Team team = new Team();
            team.setTeamName(etAddTeamName.getText().toString());
            team.setAgeGroups(sAddTeamGroup.getSelectedItem().toString());

//            serviceProvider.getService(this.getActivity()).


        }
        if(btnAddTeamNegative.getId() == view.getId()){
            dismiss();
        }
    }
}
