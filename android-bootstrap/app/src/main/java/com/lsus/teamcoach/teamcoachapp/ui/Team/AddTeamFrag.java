package com.lsus.teamcoach.teamcoachapp.ui.Team;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
    private Singleton singleton = Singleton.getInstance();

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

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {
                    team = bootstrapService.setTeam(team);
                    User user = singleton.getCurrentUser();
                    if(user.getTeams() == null){
                        ArrayList<Team> teams = new ArrayList<Team>();
                        teams.add(team);
                        user.setTeams(teams);
                    }else{
                        user.getTeams().add(team);
                    }
                    bootstrapService.update(user);

                    singleton.setCurrentUser(user);

                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if (!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if (cause != null) {
                            Toaster.showLong(getActivity(), cause.getMessage());
                        }
                    }
                }

                @Override
                public void onSuccess(final Boolean authSuccess) {
                    AddTeamFrag.this.dismiss();
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    hideProgress();
                    authenticationTask = null;
                }
            };
            authenticationTask.execute();


        }
        if(btnAddTeamNegative.getId() == view.getId()){
            dismiss();
        }
    }
}
