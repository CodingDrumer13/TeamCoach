package com.lsus.teamcoach.teamcoachapp.core;

import java.util.Comparator;

/**
 * Created by Caroline on 4/27/2015.
 */
public class CalendarComparator implements Comparator<CalendarEvent> {

    @Override
    public int compare(CalendarEvent ev1, CalendarEvent ev2) {
        int year1 = ev1.getYear();
        int year2 = ev2.getYear();

        int month1 = ev1.getMonth();
        int month2 = ev2.getMonth();

        int day1 = ev1.getDay();
        int day2 = ev2.getDay();

        int hour1 = ev1.getHour();
        int hour2 = ev2.getHour();

        int minute1 = ev1.getMinute();
        int minute2 = ev2.getMinute();

        if (year1 < year2)
            return -1;
        else if(month1 < month2)
            return -1;
        else if(day1 < day2)
            return -1;
        else if(hour1 < hour2)
            return -1;
        else if(minute1 < minute2)
            return -1;
        else if (minute1 == minute2)
            return 0;
        else
            return 1;
    }

}

