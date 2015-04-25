package com.lsus.teamcoach.teamcoachapp.ui.News;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.News;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Don on 4/24/2015.
 */
public class AddNewsFragment extends Fragment implements View.OnClickListener{

    protected Singleton singleton = Singleton.getInstance();
    private SafeAsyncTask<Boolean> authenticationTask;
    private News news;
    protected NewsListFragment newsListFragment;

    @Inject protected BootstrapService bootstrapService;

    @InjectView(R.id.et_add_news_title) EditText et_add_news_title;
    @InjectView(R.id.et_add_news_message) EditText et_add_news_message;
    @InjectView(R.id.btn_send_message) Button btn_send_message;
    @InjectView(R.id.sp_news_team) Spinner sp_news_team;
    private NewsFragment parent;

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

        btn_send_message.setOnClickListener(this);

        ArrayAdapter<Team> spinnerArrayAdapter = new ArrayAdapter<Team>(this.getActivity(), android.R.layout.simple_spinner_item, singleton.getUserTeams()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_news_team.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        showProgress();
        news = new News();
        news.setContent(et_add_news_message.getText().toString());
        news.setCreator(singleton.getCurrentUser().getObjectId());
        news.setTitle(et_add_news_title.getText().toString());
        Team team = (Team)sp_news_team.getSelectedItem();
        news.setTeamId(team.getObjectId());
//        Current time an date
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(new Date());
        news.setTimestamp(currentDateandTime);

        authenticationTask = new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                bootstrapService.addNews(news);
                return true;
            }
            @Override
            public void onSuccess(final Boolean authSuccess) {
                getFragmentManager().popBackStack();
                newsListFragment.refresh();
                parent.showButton();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }

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

    public void setParent(NewsFragment parent) {
        this.parent = parent;
    }

    public void setNewsListFragment(NewsListFragment newsListFragment) {
        this.newsListFragment = newsListFragment;
    }
}
