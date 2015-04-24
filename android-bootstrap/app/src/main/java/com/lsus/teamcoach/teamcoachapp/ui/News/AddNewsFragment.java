package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/24/2015.
 */
public class AddNewsFragment extends Fragment{

    @InjectView(R.id.et_add_news_title) EditText et_add_news_title;
    @InjectView(R.id.et_add_news_message) EditText et_add_news_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_news_fragment, container, false);
        Injector.inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);
    }
}
