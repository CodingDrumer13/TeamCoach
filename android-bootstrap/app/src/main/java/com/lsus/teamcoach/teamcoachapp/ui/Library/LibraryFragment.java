package com.lsus.teamcoach.teamcoachapp.ui.Library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.AddDrillDialogFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListFragment;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class LibraryFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewButton) protected Button addButton;
    @InjectView(R.id.btnLibraryBack) protected Button backButton;
    @InjectView(R.id.btnLibraryHome) protected Button homeButton;

    @Inject protected LogoutService logoutService;

    private Fragment currentFragment;
    private boolean ageSelected = false;
    private boolean typeSelected = false;
    private String age;
    private String type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        Injector.inject(this);
        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LibraryListFragment libraryListFragment = new LibraryListFragment();
        libraryListFragment.setRetainInstance(true);
        libraryListFragment.setButtons(backButton, addButton, homeButton);
        libraryListFragment.setParent(this);
        currentFragment = libraryListFragment;
        fragmentTransaction.replace(R.id.library_container, libraryListFragment);
        fragmentTransaction.commit();

        backButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    protected LogoutService getLogoutService() {
        return logoutService;
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
        if(view.getId() == addButton.getId()){
            addDrill();
        }
        if(view.getId() == backButton.getId()){
            back();
        }
        if(view.getId() == homeButton.getId()){
            home();
        }
    }

    //Only called from TeamListFragment
    public void addDrill(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        AddDrillDialogFragment newFragment = new AddDrillDialogFragment();
        newFragment.setAgeSelected(ageSelected);
        newFragment.setTypeSelected(typeSelected);
        newFragment.setParent(currentFragment);
        if(ageSelected) newFragment.setAge(age);
        if(typeSelected) newFragment.setType(type);
        newFragment.show(ft, "dialog");
    }

    /**
     * Replaces the currently displaying fragment with a newFragment.
     *
     * @param oldFragment
     * @param newFragment
     */
    public void replaceFragment(Fragment oldFragment, Fragment newFragment){
        currentFragment = newFragment;

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(oldFragment.getId(), newFragment);
        fragmentTransaction.commit();
    }

    private void back(){
        if(currentFragment instanceof AgeFragment){
            ageSelected = false;
            typeSelected = false;

            LibraryListFragment libraryListFragment = new LibraryListFragment();
            libraryListFragment.setRetainInstance(true);
            libraryListFragment.setButtons(backButton, addButton, homeButton);
            libraryListFragment.setParent(this);
            replaceFragment(currentFragment, libraryListFragment);

        } else if(currentFragment instanceof TypeFragment){
            ageSelected = false;
            typeSelected = false;

            AgeFragment ageFragment = new AgeFragment();
            ageFragment.setRetainInstance(true);
            ageFragment.setLibrary(((TypeFragment) currentFragment).getLibrary());
            ageFragment.setParent(this);
            replaceFragment(currentFragment, ageFragment);

        } else if(currentFragment instanceof DrillListFragment){
            typeSelected = false;

            TypeFragment typeFragment = new TypeFragment();
            typeFragment.setRetainInstance(true);
            typeFragment.setAge(((DrillListFragment) currentFragment).getAge());
            typeFragment.setLibrary(((DrillListFragment) currentFragment).getLibrary());
            typeFragment.setParent(this);
            replaceFragment(currentFragment, typeFragment);

        }else if(currentFragment instanceof SessionListFragment){
            typeSelected = false;

            TypeFragment typeFragment = new TypeFragment();
            typeFragment.setRetainInstance(true);
            typeFragment.setAge(((SessionListFragment) currentFragment).getAge());
            typeFragment.setLibrary(((SessionListFragment) currentFragment).getLibrary());
            typeFragment.setParent(this);
            replaceFragment(currentFragment, typeFragment);
        }
    }

    private void home(){

        LibraryListFragment libraryListFragment = new LibraryListFragment();
        libraryListFragment.setRetainInstance(true);
        libraryListFragment.setButtons(backButton, addButton, homeButton);
        libraryListFragment.setParent(this);
        replaceFragment(currentFragment, libraryListFragment);

    }

    public void setAgeSelected(Boolean ageSelected){ this.ageSelected = ageSelected; }

    public void setTypeSelected(Boolean typeSelected) { this.typeSelected = typeSelected; }

    public void setAge(String age) { this.age = age; }

    public void setType(String type) { this.type = type; }
}
