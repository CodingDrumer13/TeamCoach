package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.AddDrillDialogFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListFragment;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_TYPE;

/**
 * Created by TeamCoach on 4/9/2015.
 */
public class SessionListActivity extends BootstrapActivity implements View.OnClickListener {

    private String sessionAge;
    private String sessionType;
    private SessionListFragment childFragment;

    @InjectView(R.id.tv_session_list_title) protected TextView sessionListTitle;
    @InjectView(R.id.btnListNewSession) protected Button btnNewSession;

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            sessionAge = (String) getIntent().getExtras().getSerializable(SESSION_AGE);
            sessionType = (String) getIntent().getExtras().getSerializable(SESSION_TYPE);
        }

        setContentView(R.layout.session_list_activity);

        setTitle(R.string.title_session_list);

        sessionListTitle.setText(sessionAge + " " + sessionType + " sessions:");

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        SessionListFragment sessionListFragment = new SessionListFragment();
        sessionListFragment.setRetainInstance(true);
        sessionListFragment.setSessionData(sessionAge, sessionType);

        fragmentManager.beginTransaction()
                .replace(R.id.session_List_Fragment_Holder, sessionListFragment)
                .commit();

        childFragment = sessionListFragment;
        btnNewSession.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnNewSession.getId()){
            addDrill(view);
        }
    }

    //Only called from TeamListFragment
    public void addDrill(View v){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        AddSessionDialogFragment newFragment = new AddSessionDialogFragment();
        newFragment.setAgeSelected(true);
        newFragment.setAge(sessionAge);
        newFragment.setTypeSelected(true);
        newFragment.setType(sessionType);
        newFragment.setSessionListFragment(childFragment);
        newFragment.show(ft, "dialog");
    }
}
