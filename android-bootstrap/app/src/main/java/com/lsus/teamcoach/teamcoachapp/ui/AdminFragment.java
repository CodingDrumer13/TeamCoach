package com.lsus.teamcoach.teamcoachapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsus.teamcoach.teamcoachapp.R;

/**
 * Created by TeamCoach on 3/3/2015.
 */
public class AdminFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.admin, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}