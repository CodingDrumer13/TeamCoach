package com.lsus.teamcoach.teamcoachapp.ui.Library;

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
import com.lsus.teamcoach.teamcoachapp.ui.Framework.ItemListFragment;
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
 * Created by TeamCoach on 3/4/2015.
 */
public class LibraryListFragment extends ItemListFragment<String> implements View.OnClickListener{

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_library_list_header) protected TextView listHeader;

    private boolean ageSelected = false;
    private boolean librarySelected = false;
    private String library = "";
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
    protected LogoutService getLogoutService() {
        return logoutService;
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<String>> onCreateLoader(final int id, final Bundle args) {
        return new ThrowableLoader<List<String>>(getActivity(), items) {

            @Override
            public List<String> loadData() throws Exception {
                if (getActivity() != null) {
                    serviceProvider.getService(getActivity());
                    if(!librarySelected){
                        return getLibraries();
                    }else{
                        if(!ageSelected){
                            return getAgeGroups();
                        } else {
                            if(library.equalsIgnoreCase("Drills")){
                               return getDrillTypes(age);
                            } else{
                                return getSessionTypes(age);
                            }

                        }
                    }
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<String> createAdapter(final List<String> items) {
        return new LibraryListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        if(!librarySelected){
            library = ((String) l.getItemAtPosition(position));
            librarySelected = true;
        }else {
            if (!ageSelected) {
                final String age = ((String) l.getItemAtPosition(position));
                this.age = age;

                backButton.setVisibility(View.VISIBLE);


                //TODO Change the fragment header when changing screens.
                if (listHeader != null) {
                    listHeader.setText(age + ": " + R.string.drill_type_column + " Drills");
                }

                ageSelected = true;

            } else {
                if(library.equalsIgnoreCase("Drills")) {
                    final String drillType = ((String) l.getItemAtPosition(position));

                    if (listHeader != null) {
                        listHeader.setText(R.string.age_group_column);
                    }

                    Intent drillIntent = new Intent(new Intent(getActivity(), DrillListActivity.class));
                    drillIntent.putExtra(DRILL_AGE, age);
                    drillIntent.putExtra(DRILL_TYPE, drillType);
                    startActivity(drillIntent);
                } else {
                    final String sessionType = ((String) l.getItemAtPosition(position));

                    if (listHeader != null) {
                        listHeader.setText(R.string.age_group_column);
                    }

                    Toaster.showShort(getActivity(), "Selected a " + age + " " + sessionType);
                    //TODO handle sessions here.
                }
            }
        }
        this.refresh();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void onClick(View view) {

    }

    @Override
    protected int getErrorMessage(final Exception exception) {
        return R.string.error_loading_checkins;
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

    private List<String> getLibraries(){
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Drills");
        menuItems.add("Sessions");
        return menuItems;
    }

    private List<String> getDrillTypes(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Passing");
        menuItems.add("Finishing");
        menuItems.add("Technical");
        if (age.length() == 3){
            menuItems.add("Goalkeeping");
        }
        return menuItems;
    }

    private List<String> getSessionTypes(String age) {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add("Defending");
        menuItems.add("Attacking");
        menuItems.add("Fitness");
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
        if(librarySelected && ageSelected) {
            ageSelected = false;
        } else if(librarySelected && !ageSelected){
            librarySelected = false;
            backButton.setVisibility(View.GONE);
        } else {
            librarySelected = false;
            ageSelected = false;
            backButton.setVisibility(View.GONE);
        }
        this.refresh();
    }
}
