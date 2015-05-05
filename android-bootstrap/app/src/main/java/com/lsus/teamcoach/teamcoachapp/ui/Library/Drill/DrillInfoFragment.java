package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.AddSessionDialogFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 4/30/2015.
 */
public class DrillInfoFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{


    @Inject protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_drill_name) protected TextView drillName;
    @InjectView(R.id.et_drill_name) protected EditText editName;
    @InjectView(R.id.tv_drill_age) protected TextView drillAge;
    @InjectView(R.id.tv_drill_type) protected TextView drillType;
    @InjectView(R.id.tv_drill_description) protected TextView drillDescription;
    @InjectView(R.id.et_drill_description) protected EditText editDescription;
    @InjectView(R.id.drillRatingBar) protected RatingBar drillRating;
    @InjectView(R.id.tv_rating_bar_number) protected TextView ratingBarNumber;
    @InjectView(R.id.tv_rating_bar_rating) protected TextView ratingBarRating;
    @InjectView(R.id.button_rating_submit) protected Button ratingSubmit;
    @InjectView(R.id.tv_drill_times_used) protected TextView timesUsed;
    @InjectView(R.id.tv_drill_times_used_num) protected TextView timesUsedNum;
    @InjectView(R.id.iv_drillImage) protected ImageView drillPicture;

    private Drill drill;
    private float userRating;
    private ParseFile picture;
    private String pictureId;
    private SafeAsyncTask<Boolean> authenticationTask;
    private ArrayList<Drill> group;
    private View view;
    private Bitmap pictureBitmap;

    private DrillInfoActivity parent;

    private Button btnEdit;
    private Button btnRemove;
    private Button btnSubmit;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.drill_info_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        /**
         * If the drill has a picture, It retrieves the picture, retrieves the data for the picture,
         * then sets up the picture to be displayed.
         */
        if(drill.getHasPicture()){

            ParseQuery<ParseObject> query = new ParseQuery("DrillPicture");
            query.whereEqualTo("drillId", drill.getGroupId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e == null){
                        if(list.size() != 0) {
                            picture = list.get(0).getParseFile("picture");
                            pictureId = list.get(0).getObjectId();

                            picture.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    Bitmap bmp = null;
                                    try {
                                        bmp = BitmapFactory.decodeByteArray(picture.getData(), 0, picture.getData().length);
                                        pictureBitmap = bmp;
                                        drillPicture.setVisibility(View.VISIBLE);
                                        drillPicture.setImageBitmap(bmp);
                                        //TODO Enlarge image on click.
                                        //drillPicture.setOnClickListener(DrillInfoFragment.this);
                                    } catch (ParseException e1) {}

                                }
                            });
                        } else {
                            Toaster.showShort(DrillInfoFragment.this.getActivity(), "This drill's picture has been removed.");
                        }
                    }else {}
                }
            });
        }



        //TODO make drill age and type editable.
        drillName.setText(String.format("%s", drill.getDrillName()));
        drillAge.setText(String.format("%s", drill.getDrillAge()));
        drillType.setText(String.format("%s", drill.getDrillType()));
        drillDescription.setText(String.format("%s", drill.getDrillDescription()));
        drillRating.setRating(drill.getDrillRating());
        ratingBarRating.setText("(" + String.format("%.1f",drill.getDrillRating()) + " out of 5.0)");
        ratingBarNumber.setText(drill.getNumberOfRatings() + " user ratings");


        /**
         * Checks to see if the user is the creator of the drill.
         */
        Singleton singleton = Singleton.getInstance();
        if(drill.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
            btnEdit.setVisibility(View.VISIBLE);
        }

        ratingSubmit.setOnClickListener(this);

        /**
         * If the user is not the creator, make rating the drill possible
         */
        if(!drill.getCreator().equalsIgnoreCase(singleton.getCurrentUser().getEmail())){
            drillRating.setOnRatingBarChangeListener(this);
        } else {
            drillRating.setIsIndicator(true);
        }

        /**
         * Checks to see if the user is an Admin
         */
        if(singleton.getCurrentUser().getRole().equalsIgnoreCase("Admin")){
            timesUsed.setVisibility(View.VISIBLE);
            timesUsedNum.setText(String.format("%s", drill.getTimesUsed()));
            timesUsedNum.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        if(view.getId() == ratingSubmit.getId()){
            submitRating();
        }
        if(view.getId() == drillPicture.getId()){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ViewPictureDialogFragment newFragment = new ViewPictureDialogFragment();
            newFragment.setPictureBitmap(pictureBitmap);
            newFragment.show(ft, "dialog");
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

    public void onSubmit() {
        System.out.println("Submit working.");
        if(isValid()){
            System.out.println("Drill is valid.");
            drillName.setVisibility(View.VISIBLE);
            drillDescription.setVisibility(View.VISIBLE);

            editName.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);

            drillName.setText(editName.getText());
            drillDescription.setText(editDescription.getText());

            btnSubmit.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);

            //TODO Handle if drill is the same, no need to update.
            drill = checkDifferences(drill);

            if(!drill.getIsGroup()) {
                System.out.println("Drill is not a part of group.");
                authenticationTask = new SafeAsyncTask<Boolean>() {
                    public Boolean call() throws Exception {

                        System.out.println("Attempting update. " + drill.getObjectId());
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
                        Toaster.showLong(DrillInfoFragment.this.getActivity(), "Updating drills, please wait.");

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
                            protected void onFinally() throws RuntimeException {
                                authenticationTask = null;
                            }
                        };
                        authenticationTask.execute();

                    }
                };
                authenticationTask.execute();
            }
        }
    }

    public void onRemove(){
        if(!drill.getIsGroup()) {
            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.remove(drill);
                    if(drill.getHasPicture()){
                        bootstrapService.removePicture(pictureId);
                    }
                    return true;
                }
            };
            authenticationTask.execute();
            DrillInfoFragment.this.close();
        } else {
            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {
                    group = new ArrayList<Drill>();
                    group.addAll(bootstrapService.getGroupDrills(drill.getGroupId()));

                    return true;
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    Toaster.showLong(DrillInfoFragment.this.getActivity(), "Removing drills, please wait.");

                    for (Drill drill : group) {
                        drill = checkDifferences(drill);
                    }

                    authenticationTask = new SafeAsyncTask<Boolean>() {
                        public Boolean call() throws Exception {

                            for (final Drill drill : group) {
                                bootstrapService.remove(drill);
                            }

                            if(drill.getHasPicture()){
                                bootstrapService.removePicture(pictureId);
                            }

                            return true;
                        }
                    };
                    authenticationTask.execute();
                    DrillInfoFragment.this.close();
                }
            };
            authenticationTask.execute();

        }
    }

    private void submitRating(){
        Toaster.showShort(this.getActivity(), "Submitting rating");


        //Sets the new rating for the drill.
        float currentDrillRating = drill.getDrillRating();
        int numRatings = drill.getNumberOfRatings();
        int newNumRatings = numRatings + 1;
        float newRating = ((currentDrillRating * numRatings) + userRating) / newNumRatings;

        drill.setDrillRating(newRating);
        drill.setNumberOfRatings(newNumRatings);

        if(!drill.getIsGroup()) {
            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.update(drill);

                    return true;
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    ratingSubmit.setVisibility(View.GONE);

                    ratingBarRating.setText("(" + String.format("%.2f",drill.getDrillRating()) + " out of 5.0)");
                    ratingBarNumber.setText(drill.getNumberOfRatings() + " user ratings");

                    Toaster.showShort(DrillInfoFragment.this.getActivity(), "Rating submitted!");
                    authenticationTask = null;
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
                        protected void onFinally() throws RuntimeException {
                            ratingSubmit.setVisibility(View.GONE);

                            ratingBarRating.setText("(" + String.format("%.2f",drill.getDrillRating()) + " out of 5.0)");
                            ratingBarNumber.setText(drill.getNumberOfRatings() + " user ratings");

                            Toaster.showShort(DrillInfoFragment.this.getActivity(), "Rating submitted!");
                        }
                    };
                    authenticationTask.execute();

                }
            };
            authenticationTask.execute();

        }
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

        /**
         * Checks to see if the drill rating has changed.
         */
        if(drill.getDrillRating() != this.drill.getDrillRating()){
            drill.setDrillRating(this.drill.getDrillRating());
        }

        /**
         * Checks to see if the number of ratings of the drill has changed.
         */
        if(drill.getNumberOfRatings() != this.drill.getNumberOfRatings()){
            drill.setNumberOfRatings(this.drill.getNumberOfRatings());
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
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(drillDescription.getText().toString().equalsIgnoreCase("")){
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        return true;
    }

    public void setDrill(Drill drill) { this.drill = drill; }

    public void setButtons(Button btnEdit, Button btnRemove, Button btnSubmit){
        this.btnEdit = btnEdit;
        this.btnRemove = btnRemove;
        this.btnSubmit = btnSubmit;
    }

    public void setParent(DrillInfoActivity parent) { this.parent = parent; }

    private void close(){
        parent.finish();
    }
}
