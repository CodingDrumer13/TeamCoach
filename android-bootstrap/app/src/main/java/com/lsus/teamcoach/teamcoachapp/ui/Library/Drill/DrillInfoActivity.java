package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class DrillInfoActivity extends BootstrapActivity {
    @Inject protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_drill_name) protected TextView drillName;
    @InjectView(R.id.et_drill_name) protected EditText editName;
    @InjectView(R.id.tv_drill_description) protected TextView drillDescription;
    @InjectView(R.id.et_drill_description) protected EditText editDescription;
    @InjectView(R.id.tv_drill_rating) protected TextView drillRating;
    @InjectView(R.id.button_drill_edit) protected Button btnEdit;
    @InjectView(R.id.button_drill_submit) protected Button btnSubmit;
    @InjectView(R.id.button_drill_remove) protected Button btnRemove;
    @InjectView(R.id.tv_drill_times_used) protected TextView timesUsed;
    @InjectView(R.id.tv_drill_times_used_num) protected TextView timesUsedNum;

    private Drill drill;
    private SafeAsyncTask<Boolean> authenticationTask;
    private boolean finished;
    ArrayList<Drill> group;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drill_info_activity);

        setTitle(R.string.title_drill_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            drill = (Drill) getIntent().getExtras().getSerializable(DRILL);
        }

        drillName.setText(String.format("%s", drill.getDrillName()));
        drillDescription.setText(String.format("%s", drill.getDrillDescription()));
        drillRating.setText(String.format("%s", drill.getDrillRating()));

        Singleton singleton = Singleton.getInstance();
        if(drill.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
            btnEdit.setVisibility(View.VISIBLE);
        }

        if(singleton.getCurrentUser().getRole().equalsIgnoreCase("Admin")){
            timesUsed.setVisibility(View.VISIBLE);
            timesUsedNum.setText(String.format("%s", drill.getTimesUsed()));
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
        }
    }

    private void onEdit() {
        drillName.setVisibility(View.GONE);
        drillDescription.setVisibility(View.GONE);

        editName.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);

        editName.setText(drillName.getText());
        editDescription.setText(drillDescription.getText());

        btnEdit.setVisibility(View.GONE);
        btnRemove.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void onSubmit() {
        if(isValid()){
            drillName.setVisibility(View.VISIBLE);
            drillDescription.setVisibility(View.VISIBLE);

            editName.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);

            drillName.setText(editName.getText());
            drillDescription.setText(editDescription.getText());

            btnSubmit.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);

            drill = checkDifferences(drill);

            if(!drill.getIsGroup()) {
                authenticationTask = new SafeAsyncTask<Boolean>() {
                    public Boolean call() throws Exception {

                        //Implement try/catch for update error
                        bootstrapService.update(drill);

                        return true;
                    }
                };
                authenticationTask.execute();
            } else {


                authenticationTask = new SafeAsyncTask<Boolean>() {
                    public Boolean call() throws Exception {
                        group = new ArrayList<Drill>();
                        group.addAll(bootstrapService.getGroupDrills(drill.getGroupId()));

                        return true;
                    }

                    @Override
                    protected void onFinally() throws RuntimeException {
                        Toaster.showLong(DrillInfoActivity.this, "Updating drills, please wait.");

                        for (Drill drill : group){
                            drill = checkDifferences(drill);
                        }

                        authenticationTask = new SafeAsyncTask<Boolean>() {
                            public Boolean call() throws Exception {

                                for (final Drill drill : group) {
                                    bootstrapService.update(drill);
                                }

                                return true;
                            }

                            @Override
                            protected void onFinally() throws RuntimeException {}
                        };
                        authenticationTask.execute();

                    }
                };
                authenticationTask.execute();
            }
        }
    }

    private void onRemove(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.remove(drill);

                return true;
            }
        };
        authenticationTask.execute();
        this.finish();
    }


    /**
     * Checks to see if drill information has changed.
     *
     * @param drill
     * @return
     */
    private Drill checkDifferences(Drill drill) {
        /**
         * Checks to see if the Drill Name has changed
         */
        if(!drill.getDrillName().equalsIgnoreCase(drillName.getText().toString())){
            drill.setDrillName(drillName.getText().toString());
        }

        /**
         * Checks to see if the Drill description has changed.
         */
        if(!drill.getDrillDescription().equalsIgnoreCase(drillDescription.getText().toString())){
            drill.setDrillDescription(drillDescription.getText().toString());
        }

        return drill;
    }

    /**
     * Validates that all fields are filled out and there are no errors.
     * @return
     */
    private boolean isValid(){
        //TODO make checks here!

        if(drillName.getText().toString().equalsIgnoreCase("")){
            Toaster.showShort(this, "Please fill out all fields.");
            return false;
        }

        if(drillDescription.getText().toString().equalsIgnoreCase("")){
            Toaster.showShort(this, "Please fill out all fields.");
            return false;
        }

        return true;
    }
}
