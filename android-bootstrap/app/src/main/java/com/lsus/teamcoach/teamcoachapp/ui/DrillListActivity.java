package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.News;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.NEWS_ITEM;

/**
 * Created by TeamCoach on 3/16/2015.
 */
public class DrillListActivity extends BootstrapActivity implements View.OnClickListener {

    private String age;
    private String drillType;

    @InjectView(R.id.tv_drill_placeholder) protected TextView placeholderTitle;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            age = (String) getIntent().getExtras().getSerializable(DRILL_AGE);
            drillType = (String) getIntent().getExtras().getSerializable(DRILL_TYPE);
        }

        setContentView(R.layout.drill_list_activity);

        setTitle(R.string.title_drill_list);

        placeholderTitle.setText("Currently Displaying " + drillType + " drills for " + age);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

    }
}
