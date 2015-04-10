package com.lsus.teamcoach.teamcoachapp.ui.Session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.DrillListActivity;
import com.lsus.teamcoach.teamcoachapp.ui.ThrowableLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;

/**
 * Created by TeamCoach on 3/12/2015.
 */
public class SessionListFragment extends ItemListFragment<String> implements View.OnClickListener{

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_session_list_header) protected TextView listHeader;

    private boolean ageSelected = false;
    private Button backButton;
    private String age = "";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
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
    protected LogoutService getLogoutService() { return logoutService; }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<String>> onCreateLoader(final int id, final Bundle args) {
        final List<String> initialItems = items;

        return new ThrowableLoader<List<String>>(getActivity(), items) {
            @Override
            public List<String> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    if(!ageSelected){
                        return getAgeGroups();
                    } else {
                        return getMenuItems(age);
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(final List<String> items) {
        return new SessionListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        if(!ageSelected){
            final String age = ((String) l.getItemAtPosition(position));
            this.age = age;

            backButton.setVisibility(View.VISIBLE);


            //TODO Change the fragment header when changing screens.
            if(listHeader != null){
                listHeader.setText(age + ": " + R.string.drill_type_column + " Sessions");
            }

            ageSelected = true;

        }
        else{
            final String drillType = ((String) l.getItemAtPosition(position));

            if(listHeader != null){
                listHeader.setText(R.string.age_group_column);
            }
            Toaster.showShort(getActivity(), "Showing session details.");
            //TODO Changed to add new session Intent.
            //Intent drillIntent = new Intent(new Intent(getActivity(), DrillListActivity.class));
            //drillIntent.putExtra(DRILL_AGE, age);
            //drillIntent.putExtra(DRILL_TYPE, drillType);
            //startActivity(drillIntent);
        }
        this.refresh();
    }

    @Override
    public void onResume(){
        //this.refresh();
        super.onResume();
    }

    public void onClick(View view) {

    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_drills;
    }

    /**
     * Gets the list of all age groups. THIS NEEDS TO BE UPDATED SO IT IS NOT HARD CODED???
     * @return
     */
    private List<String> getAgeGroups() {
        List<String> ages = new ArrayList<String>();
        for(int i = 4; i < 19; i++){
            ages.add("U" + i);
        }
        return ages;
    }

    /**
     * Returns the session types.
     *
     * @param age
     * @return
     */

    private List<String> getMenuItems(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Finishing");
        menuItems.add("Technical");
        return menuItems;
    }

    public boolean getAgeSelected(){
        return ageSelected;
    }

    public String getAge(){
        return age;
    }

    public void setBackButton(Button backButton){
        this.backButton = backButton;
    }

    public void backClicked(){
        ageSelected = false;
        backButton.setVisibility(View.GONE);
        this.refresh();
    }
}
