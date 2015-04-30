package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillInfoActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListAdapter;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.Collections;
import java.util.List;

import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_INFO_PARENT;

/**
 * Created by TeamCoach on 4/21/2015.
 */
public class SessionDrillListFragment extends ItemListFragment<Drill> {

    private SessionInfoFragment parent;
    private List<Drill> drillList;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        this.setHasOptionsMenu(false);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);
    }

    @Override
    protected void configureList(final Activity activity, final ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<Drill>> onCreateLoader(final int id, final Bundle args) {
        final List<Drill> initialItems = items;

        return new ThrowableLoader<List<Drill>>(getActivity(), items) {
            @Override
            public List<Drill> loadData() throws Exception {
                if (getActivity() != null) {
                    return drillList;
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Drill> createAdapter(final List<Drill> items) {
        return new DrillListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Drill item = ((Drill) l.getItemAtPosition(position));
        if(parent.isEditClicked()){
            AlertDialog.Builder removeDrill = new AlertDialog.Builder(this.getActivity());
            removeDrill.setTitle("Remove Drill");
            removeDrill.setMessage("Are you sure you want to remove this drill?");
            removeDrill.setCancelable(true);
            removeDrill.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drillList.remove(item);
                    SessionDrillListFragment.this.parent.refreshList();
                    dialog.cancel();
                }
            });

            removeDrill.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog remove = removeDrill.create();
            remove.show();
        } else {
            Intent drillInfoIntent = new Intent(getActivity(), DrillInfoActivity.class);
            drillInfoIntent.putExtra(DRILL, item);
            drillInfoIntent.putExtra(DRILL_INFO_PARENT, "Session");
            startActivity(drillInfoIntent);
        }
    }

    @Override
    public void onResume(){
        this.refresh();
        super.onResume();
    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_drills;
    }

    public void setDrillList(List<Drill> drillList) { this.drillList = drillList; }

    public void setParent(SessionInfoFragment parent){ this.parent = parent; }
}