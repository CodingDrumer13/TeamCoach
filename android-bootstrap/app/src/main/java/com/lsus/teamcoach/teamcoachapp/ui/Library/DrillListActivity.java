package com.lsus.teamcoach.teamcoachapp.ui.Library;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
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

    private String drillAge;
    private String drillType;
    private DrillListFragment childFragment;

    @InjectView(R.id.tv_drill_list_title) protected TextView drillListTitle;
    @InjectView(R.id.btnListNewDrill) protected Button btnNewDrill;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            drillAge = (String) getIntent().getExtras().getSerializable(DRILL_AGE);
            drillType = (String) getIntent().getExtras().getSerializable(DRILL_TYPE);
        }

        setContentView(R.layout.drill_list_activity);

        setTitle(R.string.title_drill_list);

        drillListTitle.setText(drillAge + " " + drillType + " drills:");

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        DrillListFragment drillListFragment = new DrillListFragment();
        drillListFragment.setRetainInstance(true);
        drillListFragment.setDrillData(drillAge, drillType);

        fragmentManager.beginTransaction()
                .replace(R.id.drill_List_Fragment_Holder, drillListFragment)
                .commit();

        childFragment = drillListFragment;
        btnNewDrill.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnNewDrill.getId()){
            addDrill(view);
        }
    }

    //Only called from TeamListFragment
    public void addDrill(View v){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        AddDrillDialogFragment newFragment = new AddDrillDialogFragment();
        newFragment.setAgeSelected(true);
        newFragment.setAge(drillAge);
        newFragment.setTypeSelected(true);
        newFragment.setType(drillType);
        newFragment.setDrillListFragment(childFragment);
        newFragment.show(ft, "dialog");
    }
}
