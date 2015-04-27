package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Caroline on 4/21/2015.
 */
public class CalendarEvent implements Serializable, Comparable<CalendarEvent> {

    protected String eventName, eventType;
    protected String startDate, endDate;
    protected String startTime, endTime;

    private final int BEFORE = -1;
    private final int EQUAL = 0;
    private final int AFTER = 1;


    public CalendarEvent(){}

    public CalendarEvent(String eventName, String startDate, String startTime,
                         String endTime, String eventType){
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = startTime;
        //this.endDate = endDate;
        this.endTime = endTime;
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartDate(String date){this.startDate = date;}

    public String getStartDate() {return startDate;}

    //public void setEndDate(String date){this.endDate = date;}

    //public String getEndDate() {return endDate;}

    public void setStartTime(String time) {this.startTime = time;}

    public String getStartTime() {return startTime;}

    public void setEndTime(String time) {this.endTime = time;}

    public String getEndTime() {return endTime;}

    public String getTimeSpan() {String span = startTime + " - " + endTime;
                                    return span;}

    public String getEventType() { return eventType; }

    public void setEventType(String type) { this.eventType = type; }

    public int getHour()
    {
        String[] time = getStartTime().split(":");
        return Integer.parseInt(time[0]);
    }

    public int getMinute()
    {
        String[] time = getStartTime().split(":");
        return Integer.parseInt(time[1].substring(0,2));
    }

    public int getMonth()
    {
        String[] date = getStartDate().split("-");
        return Integer.parseInt(date[0]);
    }

    public int getDay()
    {
        String[] date = getStartDate().split("-");
        return Integer.parseInt(date[1]);
    }

    public int getYear()
    {
        String[] date = getStartDate().split("-");
        return Integer.parseInt(date[2]);
    }

    public int compareTo(CalendarEvent c) {
        int origYear = this.getYear();
        int origMonth = this.getMonth();
        int origDay = this.getDay();
        int origHour = this.getHour();
        int origMinute = this.getMinute();

        Calendar origDate = new GregorianCalendar(origYear, origMonth, origDay, origHour, origMinute);

        int toYear = this.getYear();
        int toMonth = this.getMonth();
        int toDay = this.getDay();
        int toHour = this.getHour();
        int toMinute = this.getMinute();

        Calendar toDate = new GregorianCalendar(toYear, toMonth, toDay, toHour, toMinute);

        return origDate.compareTo(toDate);

    }

    public GregorianCalendar toDateFormat()
    {
        int origYear = this.getYear();
        int origMonth = this.getMonth();
        int origDay = this.getDay();
        int origHour = this.getHour();
        int origMinute = this.getMinute();

        GregorianCalendar origDate = new GregorianCalendar(origYear, origMonth, origDay, origHour, origMinute);

        return origDate;
    }



}
