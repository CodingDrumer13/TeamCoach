package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionInfoFragment;
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
public class DrillInfoActivity extends BootstrapActivity implements View.OnClickListener{
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
    private DrillInfoFragment drillInfoFragment;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drill_info_activity);

        setTitle(R.string.title_drill_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            drill = (Drill) getIntent().getExtras().getSerializable(DRILL);
            parent = (String) getIntent().getExtras().getSerializable(DRILL_INFO_PARENT);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        drillInfoFragment = new DrillInfoFragment();
        drillInfoFragment.setRetainInstance(true);
        drillInfoFragment.setParent(this);
        drillInfoFragment.setDrill(drill);
        drillInfoFragment.setButtons(btnEdit, btnRemove, btnSubmit);

        fragmentTransaction.replace(R.id.drill_activity_container, drillInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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
            drillInfoFragment.onEdit();
        }else if(view.getId() == btnSubmit.getId()){
            //The Submit button has been clicked
            drillInfoFragment.onSubmit();
        }else if(view.getId() == btnRemove.getId()){
            //The Remove button has been clicked
            drillInfoFragment.onRemove();
        }
    }

    public void refreshParent(){

    }
}
