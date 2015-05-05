package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SelectListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionInfoFragment;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 4/21/2015.
 */
public class CalDrillSelectorDialogFrag extends DialogFragment implements View.OnClickListener{

    @InjectView(R.id.btnCancelSelectDrill) protected Button btnCancelSelectDrill;

    CalendarInfoFragment parent;
    private String age;
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select Drill");

        View view = inflater.inflate(R.layout.select_drill_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        CalendarSelectListFragment selectListFragment = new CalendarSelectListFragment();
        selectListFragment.setRetainInstance(true);
        selectListFragment.setParent(parent);
        selectListFragment.setContainer(this);
        selectListFragment.setDrillData(age, type);
        fragmentTransaction.replace(R.id.selector_container, selectListFragment);
        fragmentTransaction.commit();

        btnCancelSelectDrill.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(btnCancelSelectDrill.getId() == v.getId()){
            dismiss();
        }
    }

    public void setParent(CalendarInfoFragment parent) { this.parent = parent;}

    public CalendarInfoFragment getParent() { return parent; }

    public void setAge(String age) { this.age = age; }

    public void setType(String type) { this.type = type; }
}
