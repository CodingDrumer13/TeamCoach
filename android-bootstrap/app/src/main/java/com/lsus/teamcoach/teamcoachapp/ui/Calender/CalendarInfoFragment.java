package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.CalendarEvent;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Caroline on 4/29/2015.
 */
public class CalendarInfoFragment extends Fragment implements View.OnClickListener {

 private SafeAsyncTask<Boolean> authenticationTask;
    private CalendarEvent event;
    protected CalendarListFragment calListFragment;
    protected Singleton singleton = Singleton.getInstance();

    @Inject
    protected BootstrapService bootstrapService;
    @Inject protected LogoutService logoutService;

    @InjectView(R.id.tv_event_name_info)
    TextView tvEventNameInfo;
    @InjectView(R.id.et_event_name_info)
    EditText etEventNameInfo;
    @InjectView(R.id.tv_event_team_info)
    TextView tvEventTeamInfo;
    @InjectView(R.id.spin_event_team_info)
    Spinner spinEventTeamInfo;
    @InjectView(R.id.tv_event_start_date_info)
    TextView tvEventDateInfo;
    @InjectView(R.id.tv_event_type_info)
    TextView tvEventTypeInfo;
    @InjectView(R.id.spin_event_type_info)
    Spinner spinEventTypeInfo;
    @InjectView(R.id.btnCalendarInfoEdit)
    Button btnCalendarInfoEdit;
    @InjectView(R.id.btnCalendarInfoSubmit)
    Button btnCalendarInfoSubmit;
    @InjectView(R.id.btnCalendarInfoDelete)
    Button btnCalendarInfoDelete;
    @InjectView(R.id.btnCalendarInfoBack)
    Button btnCalendarInfoBack;
    @InjectView(R.id.btnEventDateInfo)
    Button btnEventDateInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_info_fragment, container, false);
        Injector.inject(this);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Views.inject(this, view);

        CalendarFragment calFrag = (CalendarFragment)this.getParentFragment();
        calFrag.btnNewEvent.setVisibility(View.GONE);

        btnCalendarInfoBack.setOnClickListener(this);
        btnCalendarInfoDelete.setOnClickListener(this);
        btnCalendarInfoEdit.setOnClickListener(this);
        btnCalendarInfoSubmit.setOnClickListener(this);
        btnEventDateInfo.setOnClickListener(this);

        tvEventNameInfo.setText(String.format("%s", event.getEventName()));
        tvEventTeamInfo.setText(event.getEventTeam());
        tvEventDateInfo.setText(event.getEventDate());
        tvEventTypeInfo.setText(String.format("%s", event.getEventType()));

        btnCalendarInfoEdit.setVisibility(View.VISIBLE);
        btnCalendarInfoDelete.setVisibility(View.GONE);

        btnEventDateInfo.setVisibility(View.GONE);

        tvEventDateInfo.setKeyListener(null);

    }

    public void setParentFragment(CalendarListFragment calListFragment){
        this.calListFragment = calListFragment;
    }

    public void setEvent(CalendarEvent event){
        this.event = event;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnCalendarInfoEdit.getId()) {
            //The Edit button has been clicked
            onEdit();
        } else if (view.getId() == btnCalendarInfoSubmit.getId()) {
            //The Submit button has been clicked
            onSubmit();
        }
        else if (view.getId() == btnCalendarInfoDelete.getId()) {
            //The Delete button has been clicked
            onDelete();
        }else if (view.getId() == btnCalendarInfoBack.getId()) {
            this.getFragmentManager().popBackStack();
            CalendarFragment calFrag = (CalendarFragment)this.getParentFragment();
            calFrag.btnNewEvent.setVisibility(View.VISIBLE);
        }
        if(view.getId() == btnEventDateInfo.getId()) {
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String month, day;
                            if((monthOfYear + 1) < 10){ //Add leading zeroes to months less than 10
                                month = "0" + String.valueOf(monthOfYear+1);}
                            else {
                                month = String.valueOf(monthOfYear+1);}
                            if(dayOfMonth < 10){ //Add leading zeroes to days less than 10
                                day = "0" + String.valueOf(dayOfMonth);}
                            else {
                                day = String.valueOf(dayOfMonth);}
                            // Display Selected date in textbox
                            tvEventDateInfo.setText(month + "-"
                                    + day + "-" + year);

                        }
                    }, event.getYear(), (event.getMonth())-1, event.getDay());
            dpd.show();
        }

    }

    private boolean isValid(){

        if(etEventNameInfo.getText().toString().equalsIgnoreCase("")){
            return false;
        }

        return true;
    }

    private void onEdit() {
        tvEventNameInfo.setVisibility(View.GONE);
        tvEventTypeInfo.setVisibility(View.GONE);

        etEventNameInfo.setVisibility(View.VISIBLE);
        spinEventTypeInfo.setVisibility(View.VISIBLE);

        etEventNameInfo.setText(tvEventNameInfo.getText());

        tvEventTeamInfo.setVisibility(View.GONE);
        spinEventTeamInfo.setVisibility(View.VISIBLE);

        //Set adapter for team spinner
        ArrayAdapter<Team> spinnerArrayAdapter = new ArrayAdapter<Team>(this.getActivity(), R.layout.teamcoach_spinner_item, singleton.getUserTeams()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEventTeamInfo.setAdapter(spinnerArrayAdapter);

        //Sets up the values for the Event Types
        ArrayAdapter<CharSequence> eventTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.event_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinEventTypeInfo.setAdapter(eventTypeAdapter);

        spinEventTypeInfo.setSelection(getIndex(spinEventTypeInfo, tvEventTypeInfo.getText().toString()));

        btnEventDateInfo.setVisibility(View.VISIBLE);
        tvEventDateInfo.setVisibility(View.GONE);

        btnCalendarInfoEdit.setVisibility(View.GONE);
        btnCalendarInfoSubmit.setVisibility(View.VISIBLE);

        btnCalendarInfoDelete.setVisibility(View.VISIBLE);
    }

    private void onDelete(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.remove(event);

                return true;
            }
            // Should be on success
            @Override protected void onFinally() throws RuntimeException {
                calListFragment.refresh();
                authenticationTask=null;
            }
        };
        authenticationTask.execute();


        this.getFragmentManager().popBackStack();
    }

    private void onSubmit() {
        // Setting Editing information to a team
        event.setEventName(etEventNameInfo.getText().toString());
        event.setEventType(spinEventTypeInfo.getSelectedItem().toString());
        event.setEventDate(tvEventDateInfo.getText().toString());
        event.setEventTeam(spinEventTeamInfo.getSelectedItem().toString());

        Team team = (Team) spinEventTeamInfo.getSelectedItem();
        event.setTeamId(team.getObjectId());

        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {

                //Implement try/catch for update error
                bootstrapService.update(event);

                return true;
            }
        };
        authenticationTask.execute();

        etEventNameInfo.setVisibility(View.GONE);
        spinEventTypeInfo.setVisibility(View.GONE);
        spinEventTeamInfo.setVisibility(View.GONE);
        btnCalendarInfoSubmit.setVisibility(View.GONE);

        tvEventNameInfo.setVisibility(View.VISIBLE);
        tvEventTeamInfo.setVisibility(View.VISIBLE);
        tvEventTypeInfo.setVisibility(View.VISIBLE);

        btnEventDateInfo.setVisibility(View.GONE);
        tvEventDateInfo.setVisibility(View.VISIBLE);

        tvEventNameInfo.setText(event.getEventName());
        tvEventNameInfo.setText(event.getEventType());
        tvEventDateInfo.setText(event.getEventDate());

        btnCalendarInfoEdit.setVisibility(View.VISIBLE);
        btnCalendarInfoDelete.setVisibility(View.GONE);

    }

    private int getIndex(Spinner spinner, String item){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index = i;
            }
        }
        return index;
    }
}