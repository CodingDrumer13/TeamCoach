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
    protected String eventDate;
    protected String eventStartTime, eventEndTime;
    protected String objectId, creator;

    private final int BEFORE = -1;
    private final int EQUAL = 0;
    private final int AFTER = 1;


    public CalendarEvent(){}

    public CalendarEvent(String eventName, String eventDate, String eventStartTime,
                         String eventEndTime, String eventType){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String date){this.eventDate = date;}

    public String getEventDate() {return eventDate;}

    public void setEventStartTime(String time) {this.eventStartTime = time;}

    public String getEventStartTime() {return eventStartTime;}

    public void setEventEndTime(String time) {this.eventEndTime = time;}

    public String getEventEndTime() {return eventEndTime;}

    public String getTimeSpan() {String span = getEventStartTime() + " - " + getEventEndTime();
                                    return span;}

    public String getEventType() { return eventType; }

    public void setEventType(String type) { this.eventType = type; }

    public String getObjectId() { return objectId; }

    public void setObjectId(String objectId) { this.objectId = objectId; }

    public String getCreator() {return creator;}

    public void setCreator(String creator) {this.creator = creator;}

    public int getHour()
    {
        String[] time = getEventStartTime().split(":");
        return Integer.parseInt(time[0]);
    }

    public int getMinute()
    {
        String[] time = getEventStartTime().split(":");
        return Integer.parseInt(time[1].substring(0,2));
    }

    public int getMonth()
    {
        String[] date = getEventDate().split("-");
        return Integer.parseInt(date[0]);
    }

    public int getDay()
    {
        String[] date = getEventDate().split("-");
        return Integer.parseInt(date[1]);
    }

    public int getYear()
    {
        String[] date = getEventDate().split("-");
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
