package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.DrillPictureObject;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionInfoActivity;
import com.lsus.teamcoach.teamcoachapp.ui.MainActivity;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_INFO_PARENT;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class DrillInfoActivity extends BootstrapActivity implements RatingBar.OnRatingBarChangeListener{
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
    @InjectView(R.id.button_drill_edit) protected Button btnEdit;
    @InjectView(R.id.button_drill_submit) protected Button btnSubmit;
    @InjectView(R.id.button_drill_remove) protected Button btnRemove;
    @InjectView(R.id.tv_drill_times_used) protected TextView timesUsed;
    @InjectView(R.id.tv_drill_times_used_num) protected TextView timesUsedNum;
    @InjectView(R.id.iv_drillImage) protected ImageView drillPicture;

    private Drill drill;
    private float userRating;
    private ParseFile picture;
    private SafeAsyncTask<Boolean> authenticationTask;
    private String parent;
    ArrayList<Drill> group;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drill_info_activity);

        setTitle(R.string.title_drill_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            drill = (Drill) getIntent().getExtras().getSerializable(DRILL);
            parent = (String) getIntent().getExtras().getSerializable(DRILL_INFO_PARENT);
        }

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
                        picture = list.get(0).getParseFile("picture");

                        picture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                Bitmap bmp = null;
                                try {
                                    bmp = BitmapFactory.decodeByteArray(picture.getData(), 0, picture.getData().length);
                                    drillPicture.setVisibility(View.VISIBLE);
                                    drillPicture.setImageBitmap(bmp);
                                } catch (ParseException e1) {}

                            }
                        });
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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            // This is the home button in the top left corner of the screen.
            case android.R.id.home:
                // Don't call finish! Because activity could have been started by an
                // outside activity and the home button would not operated as expected!
                if(parent.equalsIgnoreCase("Session")) {
                    final Intent sessionIntent = new Intent(this, SessionInfoActivity.class);
                    sessionIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(sessionIntent);
                    return true;
                } else {
                    final Intent homeIntent = new Intent(this, MainActivity.class);
                    homeIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(homeIntent);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        if(view.getId() == btnEdit.getId()) {
            //The Edit button has been clicked
            onEdit();
        }else if(view.getId() == btnSubmit.getId()){
            //The Submit button has been clicked
            onSubmit();
        }else if(view.getId() == btnRemove.getId()){
            //The Remove button has been clicked
            onRemove();
        }else if(view.getId() == ratingSubmit.getId()){
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

            //TODO Handle if drill is the same, no need to update.
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
                            protected void onFinally() throws RuntimeException {

                            }
                        };
                        authenticationTask.execute();

                    }
                };
                authenticationTask.execute();
            }
        }
    }

    private void onRemove(){
        if(!drill.getIsGroup()) {
            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.remove(drill);

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
                    Toaster.showLong(DrillInfoActivity.this, "Removing drills, please wait.");

                    for (Drill drill : group) {
                        drill = checkDifferences(drill);
                    }

                    authenticationTask = new SafeAsyncTask<Boolean>() {
                        public Boolean call() throws Exception {

                            for (final Drill drill : group) {
                                bootstrapService.remove(drill);
                            }

                            return true;
                        }

                        @Override
                        protected void onFinally() throws RuntimeException {
                            DrillInfoActivity.this.finish();
                        }
                    };
                    authenticationTask.execute();

                }
            };
            authenticationTask.execute();
        }
    }

    private void submitRating(){
        Toaster.showShort(this, "Submitting rating");


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

                    Toaster.showShort(DrillInfoActivity.this, "Rating submitted!");
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

                            Toaster.showShort(DrillInfoActivity.this, "Rating submitted!");
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
