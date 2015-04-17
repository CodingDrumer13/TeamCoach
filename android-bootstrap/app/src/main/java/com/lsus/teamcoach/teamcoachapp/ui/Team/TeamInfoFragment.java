package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/13/2015.
 */
public class TeamInfoFragment extends Fragment implements View.OnClickListener{

    private SafeAsyncTask<Boolean> authenticationTask;
    private Team team;
    protected TeamsListFragment teamsListFragment;

    @Inject protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_team_name)
    TextView tvTeamName;
    @InjectView(R.id.et_team_name)
    EditText etTeamName;
    @InjectView(R.id.tv_Team_Age_Group)
    TextView tvTeamAgeGroup;
    @InjectView(R.id.sp_Team_Age_Group)
    Spinner spTeamAgeGroup;
    @InjectView(R.id.btn_Team_Info_Edit)
    Button btnTeamEdit;
    @InjectView(R.id.btn_Team_info_Submit)
    Button btnTeamSubmit;
    @InjectView(R.id.btn_Team_Info_Delete)
    Button btnTeamDelete;
    @InjectView(R.id.btn_team_info_back)
    Button btnTeamBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_info_fragment, container, false);
        Injector.inject(this);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Views.inject(this, view);

        btnTeamBack.setOnClickListener(this);
        btnTeamDelete.setOnClickListener(this);
        btnTeamEdit.setOnClickListener(this);
        btnTeamSubmit.setOnClickListener(this);
        tvTeamName.setText(String.format("%s", team.getTeamName()));
        tvTeamAgeGroup.setText(String.format("%s", team.getAgeGroup()));
        btnTeamEdit.setVisibility(View.VISIBLE);
        btnTeamDelete.setVisibility(View.GONE);

    }

    public void setParentFragment(TeamsListFragment teamsListFragment){
        this.teamsListFragment = teamsListFragment;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnTeamEdit.getId()) {
            //The Edit button has been clicked
            onEdit();
        } else if (view.getId() == btnTeamSubmit.getId()) {
            //The Submit button has been clicked
            onSubmit();
        }
        else if (view.getId() == btnTeamDelete.getId()) {
            //The Submit button has been clicked
            onDelete();
        }else if (view.getId() == btnTeamBack.getId()) {
            this.getFragmentManager().popBackStack();
        }
    }

    private boolean isValid(){
        //TODO make checks here!

        if(etTeamName.getText().toString().equalsIgnoreCase("")){
            return false;
        }

        return true;
    }

    private void onEdit() {
        tvTeamName.setVisibility(View.GONE);
        tvTeamAgeGroup.setVisibility(View.GONE);

        etTeamName.setVisibility(View.VISIBLE);
        spTeamAgeGroup.setVisibility(View.VISIBLE);
        spTeamAgeGroup.setBackgroundColor(Color.BLACK);

        etTeamName.setText(tvTeamName.getText());

        //Sets up the values for the Age Groups
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spTeamAgeGroup.setAdapter(ageAdapter);

        spTeamAgeGroup.setSelection(getIndex(spTeamAgeGroup, tvTeamAgeGroup.getText().toString()));

        btnTeamEdit.setVisibility(View.GONE);
        btnTeamSubmit.setVisibility(View.VISIBLE);

        btnTeamDelete.setVisibility(View.VISIBLE);
    }

    private void onDelete(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.remove(team);

                return true;
            }

            @Override protected void onFinally() throws RuntimeException {
                teamsListFragment.refresh();
                authenticationTask=null;
            }
        };
        authenticationTask.execute();


        this.getFragmentManager().popBackStack();
    }

    private void onSubmit() {
        // Setting Editing information to a team
        team.setTeamName(etTeamName.getText().toString());
        team.setAgeGroups(spTeamAgeGroup.getSelectedItem().toString());

        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.update(team);

                return true;
            }
        };
        authenticationTask.execute();

        etTeamName.setVisibility(View.GONE);
        spTeamAgeGroup.setVisibility(View.GONE);
        btnTeamSubmit.setVisibility(View.GONE);

        tvTeamName.setVisibility(View.VISIBLE);
        tvTeamAgeGroup.setVisibility(View.VISIBLE);

        tvTeamName.setText(team.getTeamName());
        tvTeamAgeGroup.setText(team.getAgeGroup());

        btnTeamEdit.setVisibility(View.VISIBLE);
        btnTeamDelete.setVisibility(View.GONE);

    }

    private int getIndex(Spinner spinner, String item){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index = i;
            }
        }
        return index;
    }
}