package com.lsus.teamcoach.teamcoachapp.ui.Library;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;

/**
 * Created by TeamCoach on 3/16/2015.
 */

public class DrillListActivity extends BootstrapActivity implements View.OnClickListener {

    private String age;
    private String drillType;

    @InjectView(R.id.tv_drill_list_title) protected TextView drillListTitle;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            age = (String) getIntent().getExtras().getSerializable(DRILL_AGE);
            drillType = (String) getIntent().getExtras().getSerializable(DRILL_TYPE);
        }

        setContentView(R.layout.drill_list_activity);

        setTitle(R.string.title_drill_list);

        drillListTitle.setText(age + " " + drillType + " drills:");

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        DrillListFragment drillListFragment = new DrillListFragment();
        drillListFragment.setRetainInstance(true);
        drillListFragment.setDrillData(age, drillType);

        fragmentManager.beginTransaction()
                .replace(R.id.drill_List_Fragment_Holder, drillListFragment)
                .commit();


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

    }
}
