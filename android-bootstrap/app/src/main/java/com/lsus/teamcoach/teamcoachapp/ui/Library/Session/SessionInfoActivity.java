package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_ID;

/**
 * Created by TeamCoach on 4/13/2015.
 */
public class SessionInfoActivity extends BootstrapActivity implements View.OnClickListener{
    @Inject protected BootstrapService bootstrapService;

    @InjectView(R.id.button_session_edit) protected Button btnEdit;
    @InjectView(R.id.button_session_submit) protected Button btnSubmit;
    @InjectView(R.id.button_session_remove) protected Button btnRemove;


    private Session session;

    private SessionInfoFragment sessionInfoFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.session_info_activity);

        setTitle(R.string.title_session_info);

        if (getIntent() != null && getIntent().getExtras() != null) {
            session = (Session) getIntent().getExtras().getSerializable(SESSION);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            session.setObjectId(getIntent().getExtras().getSerializable(SESSION_ID).toString());
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        sessionInfoFragment = new SessionInfoFragment();
        sessionInfoFragment.setRetainInstance(true);
        sessionInfoFragment.setParent(this);
        sessionInfoFragment.setSession(session);
        sessionInfoFragment.setButtons(btnEdit, btnRemove, btnSubmit);

        fragmentTransaction.replace(R.id.session_activity_container, sessionInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View view) {
        if(view.getId() == btnEdit.getId()){
            //The Edit button has been clicked
            sessionInfoFragment.setEditClicked(true);
            sessionInfoFragment.onEdit();
        }else if(view.getId() == btnSubmit.getId()){
            //The Submit button has been clicked
            sessionInfoFragment.setEditClicked(false);
            sessionInfoFragment.onSubmit();
        }else if(view.getId() == btnRemove.getId()){
            //The Remove button has been clicked
            sessionInfoFragment.onRemove();
        }
    }
}
