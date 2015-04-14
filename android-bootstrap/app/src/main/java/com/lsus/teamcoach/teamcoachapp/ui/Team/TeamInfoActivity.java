package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import javax.inject.Inject;

import butterknife.InjectView;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.TEAM;



/**
 * Created by Don on 4/13/2015.
 */
public class TeamInfoActivity extends BootstrapActivity implements View.OnClickListener{


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
    @InjectView(R.id.et_Team_Age_Group)
    EditText etTeamAgeGroup;
    @InjectView(R.id.btn_Team_Info_Edit)
    Button btnTeamEdit;
    @InjectView(R.id.btn_Team_info_Submit)
    Button btnTeamSubmit;
    @InjectView(R.id.btn_Team_Info_Delete)
    Button btnTeamDelete;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.team_info_activity);

        btnTeamEdit.setOnClickListener(this);
        btnTeamEdit.setOnClickListener(this);

        setTitle("Team");

        if (getIntent() != null && getIntent().getExtras() != null) {
            team = (Team) getIntent().getExtras().getSerializable(TEAM);
        }

        tvTeamName.setText(String.format("%s", team.getTeamName()));
        tvTeamAgeGroup.setText(String.format("%s", team.getAgeGroup()));
        btnTeamEdit.setVisibility(View.VISIBLE);



        Singleton singleton = Singleton.getInstance();
//        if(team.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
//            btnEdit.setVisibility(View.VISIBLE);
//        }
//
//        if(singleton.getCurrentUser().getRole().equalsIgnoreCase("Admin")){
//            timesUsed.setVisibility(View.VISIBLE);
//            timesUsedNum.setText(String.format("%s", drill.getTimesUsed()));
//            timesUsedNum.setVisibility(View.VISIBLE);
//            btnEdit.setVisibility(View.VISIBLE);
//        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void setTeamListFragment(TeamsListFragment teamsListFragment){
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
    }

    private boolean isValid(){
        //TODO make checks here!

        if(etTeamName.getText().toString().equalsIgnoreCase("")){
            return false;
        }

        if(etTeamAgeGroup.getText().toString().equalsIgnoreCase("")){
            return false;
        }

        return true;
    }

    private void onEdit() {
        tvTeamName.setVisibility(View.GONE);
        tvTeamAgeGroup.setVisibility(View.GONE);

        etTeamName.setVisibility(View.VISIBLE);
        etTeamAgeGroup.setVisibility(View.VISIBLE);

        btnTeamEdit.setVisibility(View.GONE);
        btnTeamSubmit.setVisibility(View.VISIBLE);

        btnTeamSubmit.setVisibility(View.VISIBLE);
    }

    private void onSubmit() {
        if(isValid()){
            tvTeamName.setVisibility(View.VISIBLE);
            tvTeamAgeGroup.setVisibility(View.VISIBLE);

            etTeamName.setVisibility(View.GONE);
            etTeamAgeGroup.setVisibility(View.GONE);

            btnTeamSubmit.setVisibility(View.GONE);
            btnTeamEdit.setVisibility(View.VISIBLE);

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.update(team);

                    return true;
                }
            };
            authenticationTask.execute();
        }
    }

}