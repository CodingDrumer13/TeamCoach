package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Caroline on 4/21/2015.
 */
public class CalendarEvent implements Serializable, Comparable<CalendarEvent> {

    protected String eventName, eventType;
    protected String eventDate;
    protected String eventStartTime, eventEndTime;
    protected String objectId, creator, sessionId;
    protected String eventTeam, eventTeamAge;
    protected ArrayList<Drill> eventSession;


    public CalendarEvent(){}

    public CalendarEvent(String eventName, String eventDate, String eventStartTime,
                         String eventEndTime, String eventType){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventType = eventType;
    }

    public ArrayList<Drill> getEventSession(){ return eventSession;}

    public void setEventSession(ArrayList<Drill> eventSession) {this.eventSession = eventSession; }

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

    public String getEventTeam() {return eventTeam;}

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {return sessionId;}

    public void setEventTeam(String eventTeam) {this.eventTeam = eventTeam;}

    public void setEventTeamAge(String eventTeamAge) {this.eventTeamAge = eventTeamAge;}

    public String getEventTeamAge() {return eventTeamAge;}

    public String getCreator() {return creator;}

    public void setCreator(String creator) {this.creator = creator;}

    public int getStartHour()
    {
        String[] time = getEventStartTime().split(":");
        return Integer.parseInt(time[0]);
    }

    public int getStartMinute()
    {
        String[] time = getEventStartTime().split(":");
        return Integer.parseInt(time[1].substring(0,2));
    }

    public int getEndHour()
    {
        String[] time = getEventEndTime().split(":");
        return Integer.parseInt(time[0]);
    }

    public int getEndMinute()
    {
        String[] time = getEventEndTime().split(":");
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
        int origHour = this.getStartHour();
        int origMinute = this.getStartMinute();

        Calendar origDate = new GregorianCalendar(origYear, origMonth, origDay, origHour, origMinute);

        int toYear = this.getYear();
        int toMonth = this.getMonth();
        int toDay = this.getDay();
        int toHour = this.getStartHour();
        int toMinute = this.getStartMinute();

        Calendar toDate = new GregorianCalendar(toYear, toMonth, toDay, toHour, toMinute);

        return origDate.compareTo(toDate);

    }



}
