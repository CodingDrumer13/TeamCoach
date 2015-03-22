package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class AddDrillDialogFragment extends DialogFragment implements View.OnClickListener {

    @InjectView(R.id.btnCancelAddDrill) protected Button btnCancelAddDrill;
    @InjectView(R.id.btnAddDrill) protected Button btnAddDrill;
    @InjectView(R.id.etAddDrillAgeGroup) protected EditText etAgeGroup;
    @InjectView(R.id.etAddDrillType) protected EditText etType;

    private boolean ageSelected;
    private String age;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Drill");

        View view = inflater.inflate(R.layout.add_drill_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnCancelAddDrill.setOnClickListener(this);
        btnAddDrill.setOnClickListener(this);

        if(ageSelected){
            Toaster.showShort(this.getActivity(), age);
            System.out.println("The selected age is: " + age);
            etAgeGroup.setText(String.format("%s", age));
        }
    }

    @Override
    public void onClick(View view) {
        if(btnAddDrill.getId() == view.getId()) {

        }
        if(btnCancelAddDrill.getId() == view.getId()){
            dismiss();
        }
    }

    public void setAgeSelected(Boolean ageSelected){
        this.ageSelected = ageSelected;
    }

    public void setAge(String age){
        this.age = age;
    }
}
