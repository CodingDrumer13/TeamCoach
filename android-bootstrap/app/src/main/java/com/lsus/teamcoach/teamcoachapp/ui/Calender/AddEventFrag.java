package com.lsus.teamcoach.teamcoachapp.ui.Calender;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.*;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Caroline on 3/25/2015.
 */
public class AddEventFrag extends DialogFragment implements View.OnClickListener {

    private SafeAsyncTask<Boolean> authenticationTask;
    private CalendarEvent event;
    private Singleton singleton = Singleton.getInstance();

    //Date and time picker dialog boxes
    private DatePickerDialog startDateDialog;
    private DatePickerDialog endDateDialog;
    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;

    private CalendarFragment parent;
    private CalendarListFragment calListFragment;

    private SimpleDateFormat dateFormatter;

    // Process to get Current Date
    private final Calendar c = Calendar.getInstance();
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);
    private int mHour = c.get(Calendar.HOUR_OF_DAY);
    private int mMinute = c.get(Calendar.MINUTE);

    private boolean result = false;



    @Inject BootstrapService bootstrapService;

    @InjectView(R.id.et_eventTitle) EditText et_eventTitle;
    @InjectView(R.id.spin_eventType) Spinner spin_eventType;
    @InjectView(R.id.btnCancelCreateEvent) Button btnCancelCreateEvent;
    @InjectView(R.id.btnCreateEvent) Button btnCreateEvent;

    @InjectView(R.id.btnDate) Button btnDate;
    @InjectView(R.id.btnStartTime) Button btnStartTime;
    @InjectView(R.id.btnEndTime) Button btnEndTime;

    @InjectView(R.id.et_EventStartDate) EditText et_EventStartDate;
    @InjectView(R.id.et_EventStartTime) EditText et_EventStartTime;
    @InjectView(R.id.et_EventEndTime) EditText et_EventEndTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Event");

        View view = inflater.inflate(R.layout.add_event_dialog_fragment, container, false);
        Injector.inject(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        //Edit texts are not editable, must use date or time picker
        et_EventStartDate.setKeyListener(null);
        et_EventStartTime.setKeyListener(null);
        et_EventEndTime.setKeyListener(null);

        btnCancelCreateEvent.setOnClickListener(this);
        btnCreateEvent.setOnClickListener(this);
        et_EventStartDate.setOnClickListener(this);
        et_EventStartTime.setOnClickListener(this);
        et_EventEndTime.setOnClickListener(this);

        btnDate.setOnClickListener(this);
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);

        //Set labels for event type drop down list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                                R.array.event_type_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spin_eventType.setAdapter(adapter);

        //Set default values for start date, start time, and end time
        String month, day;
        if((mMonth+ 1) < 10){ //Add leading zeroes to months less than 10
            month = "0" + String.valueOf(mMonth+1);}
        else {
            month = String.valueOf(mMonth+1);}
        if(mDay < 10){ //Add leading zeroes to days less than 10
            day = "0" + String.valueOf(mDay);}
        else {
            day = String.valueOf(mDay);}

        et_EventStartDate.setText(month + "-" + day + "-" + mYear);

            String minuteString;
            if(mMinute < 10){
                minuteString = "0" + mMinute;}
            else {
                minuteString = String.valueOf(mMinute);}
            String am_pm;
            String hour;
            if(mHour >= 12){ //Times after 12 noon
                am_pm = "PM";
                if(mHour == 12){
                    hour = String.valueOf(mHour);}
                else {
                    hour = String.valueOf(mHour - 12);}}
            else if(mHour == 0) { //Midnight
                am_pm = "AM";
                hour = String.valueOf(mHour + 12);
            }
            else {
                am_pm = "AM";
                hour = String.valueOf(mHour);}

        et_EventStartTime.setText(hour + ":" + minuteString + " " + am_pm);

        //Set end time default to 1 1/2 hours after current time
        String endHour = String.valueOf(Integer.parseInt(hour)+1);
        String endMinute;
        if(mMinute > 29){
            endMinute = String.valueOf((mMinute + 30) - 60);
            if (((mMinute + 30) - 60) < 10)
               endMinute = "0" + endMinute;
            endHour = String.valueOf(Integer.parseInt(hour)+2);
        }
        else
            endMinute = String.valueOf(mMinute + 30);

        et_EventEndTime.setText(endHour + ":" + endMinute + " " + am_pm);

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
        if(btnCreateEvent.getId() == view.getId()) {
            event = new CalendarEvent();
            event.setEventName(et_eventTitle.getText().toString());
            event.setEventType(spin_eventType.getSelectedItem().toString());

            event.setEventDate(et_EventStartDate.getText().toString());
            event.setEventStartTime(et_EventStartTime.getText().toString());
            event.setEventEndTime(et_EventEndTime.getText().toString());

            User user = singleton.getCurrentUser();

            event.setCreator(user.getEmail());

            ArrayList<CalendarEvent> events = singleton.getUserEvents();

            //ArrayList Example.
            JSONArray parseList = new JSONArray();
            for (CalendarEvent e : events){
                parseList.put(e);
            }

            //Saving team to Team class on Parse.com
            ParseObject eventToAdd = new ParseObject("Event");
            eventToAdd.put("eventName", et_eventTitle.getText().toString());
            eventToAdd.put("eventDate", et_EventStartDate.getText().toString());
            eventToAdd.put("eventStartTime", et_EventStartTime.getText().toString());
            eventToAdd.put("eventEndTime", et_EventEndTime.getText().toString());
            eventToAdd.put("eventType", spin_eventType.getSelectedItem().toString());
            eventToAdd.put("creator", ParseUser.getCurrentUser().getEmail());

            try {
                eventToAdd.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException ex) {
                        if (ex == null){
                            //calListFragment.refresh();
                            refreshParent();
                            result = true;
                        }
                        else{
                            result = false;
                            Log.e("", ex.getLocalizedMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Saving team locally in list.
            events.add(event);
            user.setEvents(events);


            refreshParent();
            AddEventFrag.this.dismiss();

        }

            if (view == btnDate) {

//                // Process to get Current Date
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);

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
                                et_EventStartDate.setText(month + "-"
                                        + day + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
            if (view == btnStartTime) {

//                // Process to get Current Time
//                final Calendar c = Calendar.getInstance();
//                mHour = c.get(Calendar.HOUR_OF_DAY);
//                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                String minuteString;
                                if(minute < 10){
                                    minuteString = "0" + minute;}
                                else
                                    minuteString = String.valueOf(minute);
                                String am_pm;
                                String hour;
                                if(hourOfDay >= 12){ //Times after 12 noon
                                    am_pm = "PM";
                                    if(hourOfDay == 12){
                                        hour = String.valueOf(hourOfDay);}
                                    else {
                                        hour = String.valueOf(hourOfDay - 12);}}
                                else if(hourOfDay == 0) { //Midnight
                                    am_pm = "AM";
                                    hour = String.valueOf(hourOfDay + 12);
                                }
                                else {
                                    am_pm = "AM";
                                    hour = String.valueOf(hourOfDay);}
                                et_EventStartTime.setText(hour + ":" + minuteString + " " + am_pm);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
            if (view == btnEndTime) {

                // Process to get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                String minuteString;
                                if(minute < 10){
                                    minuteString = "0" + minute;}
                                else
                                    minuteString = String.valueOf(minute);
                                String am_pm;
                                String hour;
                                if(hourOfDay >= 12){ //12 noon and later
                                    am_pm = "PM";
                                    if(hourOfDay == 12){
                                        hour = String.valueOf(hourOfDay);}
                                    else {
                                        hour = String.valueOf(hourOfDay - 12);}}
                                else if(hourOfDay == 0) { //12 Midnight
                                    am_pm = "AM";
                                    hour = String.valueOf(hourOfDay + 12);
                                }
                                else {
                                    am_pm = "AM";
                                    hour = String.valueOf(hourOfDay);}
                                et_EventEndTime.setText(hour + ":" + minuteString + " " + am_pm);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }

        if(btnCancelCreateEvent.getId() == view.getId()) {
            dismiss();
        }
    }

    public void setParent(CalendarFragment parent) { this.parent = parent; }

    private void refreshParent(){
        parent.refreshLists();
    }
}


