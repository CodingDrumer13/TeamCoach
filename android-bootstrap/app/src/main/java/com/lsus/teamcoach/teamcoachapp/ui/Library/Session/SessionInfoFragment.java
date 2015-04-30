package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 4/30/2015.
 */
public class SessionInfoFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    @Inject protected BootstrapService bootstrapService;

    @InjectView(R.id.tv_session_name) protected TextView sessionName;
    @InjectView(R.id.et_session_name) protected EditText editName;
    @InjectView(R.id.tv_session_age) protected TextView sessionAge;
    @InjectView(R.id.tv_session_type) protected TextView sessionType;
    @InjectView(R.id.button_addDrillList) protected Button addDrillList;
    @InjectView(R.id.sessionRatingBar) protected RatingBar sessionRating;
    @InjectView(R.id.tv_rating_bar_number) protected TextView ratingBarNumber;
    @InjectView(R.id.tv_rating_bar_rating) protected TextView ratingBarRating;
    @InjectView(R.id.button_rating_submit) protected Button ratingSubmit;
    @InjectView(R.id.session_fragment_container) protected FrameLayout sessionContainer;


    private Session session;
    private SafeAsyncTask<Boolean> authenticationTask;
    private boolean editClicked = false;
    private View view;
    private Button btnEdit;
    private Button btnSubmit;
    private Button btnRemove;
    private SessionInfoActivity parent;

    private float userRating;
    private SessionDrillListFragment drillListFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        drillListFragment = new SessionDrillListFragment();
        drillListFragment.setRetainInstance(true);
        drillListFragment.setParent(this);
        if(session.getDrillList() == null) session.setDrillList(new ArrayList<Drill>());
        drillListFragment.setDrillList(session.getDrillList());

        fragmentTransaction.replace(R.id.session_fragment_container, drillListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.session_info_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        addDrillList.setOnClickListener(this);
        ratingSubmit.setOnClickListener(this);

        sessionRating.setRating(session.getSessionRating());
        ratingBarRating.setText("(" + String.format("%.1f",session.getSessionRating()) + " out of 5.0)");
        ratingBarNumber.setText(session.getNumberOfRatings() + " user ratings");


        Singleton singleton = Singleton.getInstance();

        /**
         * If the user is not the creator, make rating the session possible
         */
        if(!session.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
            sessionRating.setOnRatingBarChangeListener(this);
        } else {
            sessionRating.setIsIndicator(true);
        }

        sessionName.setText(String.format("%s", session.getName()));
        sessionAge.setText(String.format("%s", session.getAgeGroup()));
        sessionType.setText(String.format("%s", session.getSessionType()));


        if (session.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())) {
            btnEdit.setVisibility(View.VISIBLE);
        }

        //USED FOR ADMIN PRIVILEGES
        if (singleton.getCurrentUser().getRole().equalsIgnoreCase("Admin")) {
            btnEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        if(view.getId() == addDrillList.getId()){
            addNewDrill();
        } else if(view.getId() == ratingSubmit.getId()){
            editClicked = false;
            submitRating();
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        ratingSubmit.setVisibility(View.VISIBLE);
        ratingBarRating.setVisibility(View.GONE);
        ratingBarNumber.setVisibility(View.GONE);
        userRating = ratingBar.getRating();
    }

    public void onEdit() {
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

    public void onSubmit() {
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

    public void onRemove(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.remove(session);

                return true;
            }
        };
        authenticationTask.execute();
        parent.finish();
    }

    private void addNewDrill(){
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        DrillSelectorDialogFragment newFragment = new DrillSelectorDialogFragment();
        newFragment.setAge(session.getAgeGroup());
        newFragment.setType(session.getSessionType());
        newFragment.setParent(this);
        newFragment.show(ft, "dialog");
    }

    private void submitRating(){
        Toaster.showShort(this.getActivity(), "Submitting rating");


        //Sets the new rating for the drill.
        float currentSessionRating = session.getSessionRating();
        int numRatings = session.getNumberOfRatings();
        int newNumRatings = numRatings + 1;
        float newRating = ((currentSessionRating * numRatings) + userRating) / newNumRatings;

        session.setSessionRating(newRating);
        session.setNumberOfRatings(newNumRatings);

        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.update(session);

                return true;
            }
        };
        authenticationTask.execute();
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
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
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
    }

    public void refreshList(){
        drillListFragment.refresh();
    }

    public boolean isEditClicked() { return editClicked; }

    public void setSession(Session session) { this.session = session; }

    public void setParent(SessionInfoActivity parent) { this.parent = parent; }

    public void setButtons(Button btnEdit, Button btnRemove, Button btnSubmit){
        this.btnEdit = btnEdit;
        this.btnRemove = btnRemove;
        this.btnSubmit = btnSubmit;
    }

    public void setEditClicked(boolean editClicked) { this.editClicked = editClicked; }
}
