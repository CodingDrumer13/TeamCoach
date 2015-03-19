package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.User;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class DrillInfoActivity extends BootstrapActivity{
    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_drill_name) protected TextView drillName;
    @InjectView(R.id.tv_drill_description) protected TextView drillDescription;
    @InjectView(R.id.tv_drill_rating) protected TextView drillRating;
    @InjectView(R.id.button_drill_edit) protected Button btnEdit;
    @InjectView(R.id.button_drill_submit) protected Button btnSubmit;

    private Drill drill;

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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
