package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.Date;

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

    public String getHour()
    {
        String[] time = getStartTime().split(":");
        return time[0];
    }

    public int compareTo(CalendarEvent c) {
        int result;

        //Years 01-01-2015
        int compYear = Integer.parseInt(c.getStartDate().substring(6));
        int startYear = Integer.parseInt(this.getStartDate().substring(6));
        //Months
        int compMonth = Integer.parseInt(c.getStartDate().substring(3,5));
        int startMonth = Integer.parseInt(this.getStartDate().substring(3,5));
        //Days
        int compDay = Integer.parseInt(c.getStartDate().substring(0,2));
        int startDay = Integer.parseInt(this.getStartDate().substring(0,2));
        //Hours 12:12 PM
        int compHour = Integer.parseInt(c.getStartTime().substring(0,2));
        int startHour = Integer.parseInt(this.getStartTime().substring(0,2));
        //Minutes
        int compMinute = Integer.parseInt(c.getStartTime().substring(3,5));
        int startMinute = Integer.parseInt(this.getStartTime().substring(3,5));


        if(compYear > startYear){
            result = AFTER;}
        else if(compMonth > startMonth){ //Year is <= compYear
            result = AFTER;}
        else if (compDay > startDay){ //Month is <= compMonth
            result = AFTER;}
        else if (compDay == startDay){ //The dates are the same
            if(compHour > startHour){
                result = AFTER;}
            else { //Hour is <= compHour
                if (compMinute > startMinute){
                   result = AFTER; }
                else if (compMinute == startMinute) { //Dates and times are the same
                    result = EQUAL;}
                else
                    result = BEFORE;}}
        else
            result = BEFORE; //The date being compared to this comes before this

        return result;
    }
}
