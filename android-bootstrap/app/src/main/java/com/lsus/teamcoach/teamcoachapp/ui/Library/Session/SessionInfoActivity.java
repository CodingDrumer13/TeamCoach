package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_ID;

/**
 * Created by TeamCoach on 4/13/2015.
 */
public class SessionInfoActivity extends BootstrapActivity {
    @Inject protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_session_name) protected TextView sessionName;
    @InjectView(R.id.et_session_name) protected EditText editName;
    @InjectView(R.id.tv_session_age) protected TextView sessionAge;
    @InjectView(R.id.tv_session_type) protected TextView sessionType;
    @InjectView(R.id.tv_session_rating) protected TextView sessionRating;
    @InjectView(R.id.button_session_edit) protected Button btnEdit;
    @InjectView(R.id.button_session_submit) protected Button btnSubmit;
    @InjectView(R.id.button_session_remove) protected Button btnRemove;
    @InjectView(R.id.button_addDrillList) protected Button addDrillList;
    @InjectView(R.id.tv_session_times_used) protected TextView timesUsed;
    @InjectView(R.id.tv_session_times_used_num) protected TextView timesUsedNum;


    private Session session;
    private SafeAsyncTask<Boolean> authenticationTask;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.session_info_activity);

        setTitle(R.string.title_session_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            session = (Session) getIntent().getExtras().getSerializable(SESSION);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            session.setObjectId(getIntent().getExtras().getSerializable(SESSION_ID).toString());
        }


        Toaster.showLong(this, "Session ID: " + session.getObjectId());


        //TODO Handle age ranges here.
        if(session.getIsGroup()){
            if (getIntent() != null && getIntent().getExtras() != null) {
                session = (Session) getIntent().getExtras().getSerializable(SESSION);
            }
        }

        //TODO make session age and type editable.
        sessionName.setText(String.format("%s", session.getName()));
        sessionAge.setText(String.format("%s", session.getAgeGroup()));
        sessionType.setText(String.format("%s", session.getSessionType()));
        sessionRating.setText(String.format("%s", session.getSessionRating()));

        Singleton singleton = Singleton.getInstance();
        if(session.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
            btnEdit.setVisibility(View.VISIBLE);
        }

        //USED FOR ADMIN PRIVILEGES
        if(singleton.getCurrentUser().getRole().equalsIgnoreCase("Admin")){
            timesUsed.setVisibility(View.VISIBLE);
            timesUsedNum.setText(String.format("%s", session.getTimesUsed()));
            timesUsedNum.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View view) {
        if(view.getId() == btnEdit.getId())
        {
            //The Edit button has been clicked
            onEdit();
        }else if(view.getId() == btnSubmit.getId()){
            //The Submit button has been clicked
            onSubmit();
        }else if(view.getId() == btnRemove.getId()){
            //The Remove button has been clicked
            onRemove();
        }else if(view.getId() == addDrillList.getId()){
            addNewDrill();
        }
    }

    private void onEdit() {
        sessionName.setVisibility(View.GONE);
        //sessionDescription.setVisibility(View.GONE);

        editName.setVisibility(View.VISIBLE);
        //editDescription.setVisibility(View.VISIBLE);

        editName.setText(sessionName.getText());
        //editDescription.setText(sessionDescription.getText());

        btnEdit.setVisibility(View.GONE);
        addDrillList.setVisibility(View.VISIBLE);
        btnRemove.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void onSubmit() {
        if(isValid()){
            sessionName.setVisibility(View.VISIBLE);
            //sessionDescription.setVisibility(View.VISIBLE);

            editName.setVisibility(View.GONE);
            //editDescription.setVisibility(View.GONE);

            sessionName.setText(editName.getText());
            //sessionDescription.setText(editDescription.getText());

            addDrillList.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);

            session = checkDifferences(session);

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.update(session);

                    return true;
                }
            };
            authenticationTask.execute();
        }
    }

    private void onRemove(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.remove(session);

                return true;
            }
        };
        authenticationTask.execute();
        this.finish();
    }

    private void addNewDrill(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        DrillSelectorDialogFragment newFragment = new DrillSelectorDialogFragment();
        newFragment.setAge(session.getAgeGroup());
        newFragment.setType(session.getSessionType());
        newFragment.setParent(this);
        newFragment.show(ft, "dialog");
    }


    /**
     * Checks to see if session information has changed.
     *
     * @param session
     * @return
     */
    private Session checkDifferences(Session session) {
        /**
         * Checks to see if the Session Name has changed
         */
        if(!session.getName().equalsIgnoreCase(sessionName.getText().toString())){
            session.setName(sessionName.getText().toString());
        }

        //TODO finish other checks.

        return session;
    }

    /**
     * Validates that all fields are filled out and there are no errors.
     * @return
     */
    private boolean isValid(){
        //TODO make checks here!

        if(sessionName.getText().toString().equalsIgnoreCase("")){
            Toaster.showShort(this, "Please fill out all fields.");
            return false;
        }

//        if(sessionDescription.getText().toString().equalsIgnoreCase("")){
//            Toaster.showShort(this, "Please fill out all fields.");
//            return false;
//        }

        return true;
    }

    /**
     * Handles the drill that is picked from the selector dialog box
     * @param drill
     */
    public void setDrillToAdd(Drill drill){
        ArrayList<Drill> current = session.getDrillList();

        current.add(drill);

        session.setDrillList(current);
        String drillName = current.get(current.size() - 1).getDrillName();

        Toaster.showLong(this, "Added " + drillName + "\nList Size: " + current.size());
    }

}
