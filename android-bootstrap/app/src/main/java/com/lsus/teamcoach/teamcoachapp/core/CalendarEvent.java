package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Caroline on 4/21/2015.
 */
public class CalendarEvent implements Serializable {

    protected String eventName, eventType;
    protected String startDate, endDate;
    protected String startTime, endTime;


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
}
