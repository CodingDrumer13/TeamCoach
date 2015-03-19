package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class LibraryFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btnNewDrill) Button btnNewDrill;

    @Inject protected LogoutService logoutService;


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
        fragmentTransaction.replace(R.id.library_container, libraryListFragment);
        fragmentTransaction.commit();

        btnNewDrill.setOnClickListener(this);
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
        if(view.getId() == btnNewDrill.getId()){
            addDrill(view);
        }
    }

    //Only called from TeamListFragment
    public void addDrill(View v){
        Toaster.showShort(this.getActivity(), "Add Drill Clicked.");
    }
}
