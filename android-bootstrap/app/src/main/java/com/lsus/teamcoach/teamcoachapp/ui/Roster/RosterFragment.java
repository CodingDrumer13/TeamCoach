package com.lsus.teamcoach.teamcoachapp.ui.Roster;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Team.AddTeamFrag;
import com.lsus.teamcoach.teamcoachapp.ui.Team.TeamsListFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

public class RosterFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.btnAddTeam) Button btnAddTeam;

    protected RosterListFragment rosterListFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.roster, container, false);
        Injector.inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        rosterListFragment = new RosterListFragment();
        rosterListFragment.setRetainInstance(true);
        rosterListFragment.setParentFragment(this);
        fragmentTransaction.replace(R.id.roster_content, rosterListFragment);
        fragmentTransaction.commit();

        btnAddTeam.setOnClickListener(this);
    }


    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        getActivity().dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        getActivity().showDialog(0);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnAddTeam.getId()){
            addTeam(view);
        }
    }

    //Only called from TeamListFragment
    public void addTeam(View v){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        FindTeamFragment findTeamFragment = new FindTeamFragment();
        findTeamFragment.setParentFragment(this);
        ft.replace(rosterListFragment.getId(), findTeamFragment);
        ft.addToBackStack("findTeamFragment");
        ft.commit();

        hideButton();

    }

    public void showButton(){
        btnAddTeam.setVisibility(View.VISIBLE);
    }

    public void hideButton(){
        btnAddTeam.setVisibility(View.GONE);
    }
}