package com.lsus.teamcoach.teamcoachapp.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.*;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.*;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.*;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

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

    private SimpleDateFormat dateFormatter;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;



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

            event.setStartDate(et_EventStartDate.getText().toString());
            event.setStartTime(et_EventStartTime.getText().toString());
            event.setEndTime(et_EventEndTime.getText().toString());

            User user = singleton.getCurrentUser();
            ArrayList<CalendarEvent> events = singleton.getUserEvents();

            //Saving team locally in list.
            events.add(event);
            user.setEvents(events);


            AddEventFrag.this.dismiss();

        }

            if (view == btnDate) {

                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                et_EventStartDate.setText((monthOfYear + 1) + "-"
                                        + dayOfMonth + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
            if (view == btnStartTime) {

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
                                if(hourOfDay >= 12){
                                    am_pm = "PM";
                                    hour = String.valueOf(hourOfDay - 12);}
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
                                if(hourOfDay >= 12){
                                    am_pm = "PM";
                                    hour = String.valueOf(hourOfDay - 12);}
                                else {
                                    am_pm = "AM";
                                    hour = String.valueOf(hourOfDay);}
                                et_EventEndTime.setText(hour + ":" + minuteString + " " + am_pm);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }

        if(btnCancelCreateEvent.getId() == view.getId()){
            dismiss();
        }


    }
}


