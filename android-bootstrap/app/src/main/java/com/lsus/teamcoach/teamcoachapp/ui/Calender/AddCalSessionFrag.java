package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SelectListFragment;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Caroline on 5/1/2015.
 */
public class AddCalSessionFrag extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    @InjectView(R.id.btnCancelSelectDrill) protected Button btnCancelSelectDrill;
    @InjectView(R.id.spin_calsession_drilltype) protected Spinner spinSessionType;

    CalendarInfoFragment parent;
    private String age;
    private String type = "Defending";
    Singleton singleton = Singleton.getInstance();
    SelectCalendarSessionListFragment selectListFragment = new SelectCalendarSessionListFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Session to Event");

        View view = inflater.inflate(R.layout.add_calendar_session_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.session_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSessionType.setAdapter(adapter);

        spinSessionType.setOnItemSelectedListener(this);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        selectListFragment.setRetainInstance(true);
        selectListFragment.setParent(parent);
        selectListFragment.setContainer(this);
        selectListFragment.setSessionData(age, spinSessionType.getSelectedItem().toString());

        fragmentTransaction.replace(R.id.selector_container, selectListFragment);
        fragmentTransaction.commit();

        btnCancelSelectDrill.setOnClickListener(this);

        Toaster.showShort(this.getActivity(), "Age: " + age + " Type: " + type);
    }

    @Override
    public void onClick(View v) {
        if(btnCancelSelectDrill.getId() == v.getId()){
            dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == spinSessionType.getId()){
            type = spinner.getSelectedItem().toString();
            selectListFragment.setType(type);
            selectListFragment.refresh();
            Toaster.showShort(this.getActivity(), "Age: " + age + " Type: " + type);

//            FragmentManager fragmentManager = getChildFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            selectListFragment.setRetainInstance(true);
//            selectListFragment.setParent(this.parent);
//            selectListFragment.setContainer(this);
//
//            selectListFragment.setSessionData(age, spinSessionType.getSelectedItem().toString());
//
//            fragmentTransaction.replace(R.id.selector_container, selectListFragment);
//            fragmentTransaction.commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setParent(CalendarInfoFragment parent) { this.parent = parent;}

    public CalendarInfoFragment getParent() { return parent; }

    public void setAge(String age) { this.age = age; }

    public void setType(String type) { this.type = type; }
}
