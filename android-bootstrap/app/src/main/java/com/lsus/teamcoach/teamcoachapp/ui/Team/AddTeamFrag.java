package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

/**
 * Created by Don on 3/18/2015.
 */
public class AddTeamFrag extends DialogFragment implements View.OnClickListener {

    private SafeAsyncTask<Boolean> authenticationTask;
    private Team team;
    protected TeamsListFragment teamsListFragment;
    private boolean result = false;


    @Inject BootstrapService bootstrapService;

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
        if(btnAddTeamPositive.getId() == view.getId()) {
            team = new Team();
            team.setTeamName(etAddTeamName.getText().toString());
            team.setAgeGroups(sAddTeamGroup.getSelectedItem().toString());

            //TODO check to see if locally stored teams is null first.
            //TODO if not, get list of teams, add parseObject, push to parse.com
            Singleton singleton = Singleton.getInstance();
            ArrayList<Team> userTeams = singleton.getUserTeams();

            if(userTeams == null){
                userTeams = new ArrayList<Team>();
                singleton.setUserTeams(userTeams);
            }

            //ArrayList Example.
            JSONArray parseList = new JSONArray();
            for (Team team : userTeams){
                parseList.put(team);
            }

            //Saving team to Team class on Parse.com
            ParseObject teamToAdd = new ParseObject("Team");
            teamToAdd.put("teamName", etAddTeamName.getText().toString());
            teamToAdd.put("ageGroup", sAddTeamGroup.getSelectedItem().toString());
            teamToAdd.put("coach", ParseUser.getCurrentUser().getEmail());

            try {
                teamToAdd.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException ex) {
                        if (ex == null){
                            teamsListFragment.refresh();
                            result = true;
                        }
                        else{
                            result = false;
                            Log.e("", ex.getLocalizedMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Saving team locally in list.
            userTeams.add(team);
            singleton.setUserTeams(userTeams);

            AddTeamFrag.this.dismiss();

        }
        if(btnAddTeamNegative.getId() == view.getId()){
            dismiss();
        }
    }

    public void setTeamsListFragment(TeamsListFragment teamsListFragment){
        this.teamsListFragment = teamsListFragment;
    }
}
