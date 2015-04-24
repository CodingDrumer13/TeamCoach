package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lsus.teamcoach.teamcoachapp.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/24/2015.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {

    protected NewsListFragment newsListFragment;
    protected AddNewsFragment addNewsFragment;

    @InjectView(R.id.btn_add_news) Button btn_add_news;

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btn_add_news.setOnClickListener(this);

        newsListFragment = new NewsListFragment();
        newsListFragment.setRetainInstance(true);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.news_framelayout, newsListFragment).commit();

    }

    public void onClick(View view) {

        addNewsFragment = new AddNewsFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(newsListFragment.getId(), addNewsFragment).commit();
    }
}
