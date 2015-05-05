package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TeamCoach on 3/31/2015.
 */
public class Session implements Serializable {

    protected String objectId;
    protected String groupId;
    protected boolean isGroup;
    protected String name;
    protected String ageGroup;
    protected String sessionType;
    protected ArrayList<Drill> drillList;
    protected float sessionRating;
    protected int numberOfRatings;
    protected int timesUsed;
    protected boolean isPublic;
    protected String creator;

    public Session(String groupId, String name, String sessionType, String ageGroup, Boolean isPublic, String creator){
        this.groupId = groupId;
        this.name = name;
        this.sessionType = sessionType;
        this.ageGroup = ageGroup;
        this.isPublic = isPublic;
        this.creator = creator;
    }

    public String getObjectId(){ return objectId; }

    public void setObjectId(String objectId){ this.objectId = objectId; }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public boolean getIsGroup() { return isGroup; }

    public void setIsGroup(boolean isGroup) { this.isGroup = isGroup; }

    public String getName(){ return name; }

    public void setName(String name) { this.name = name; }

    public String getAgeGroup() { return ageGroup; }

    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public String getSessionType() { return sessionType; }

    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public ArrayList<Drill> getDrillList(){ return drillList; }

    public void setDrillList(ArrayList<Drill> drillList){ this.drillList = drillList; }

    public float getSessionRating(){ return sessionRating; }

    public void setSessionRating(float sessionRating){ this.sessionRating = sessionRating; }

    public int getNumberOfRatings() { return numberOfRatings; }

    public void setNumberOfRatings(int numberOfRatings) { this.numberOfRatings = numberOfRatings; }

    public int getTimesUsed() { return timesUsed; }

    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }

    public boolean getIsPublic(){ return isPublic; }

    public void setIsPublic(boolean isPublic){ this.isPublic = isPublic; }

    public String getCreator(){ return creator; }

    public void setCreator(String creator){ this.creator = creator; }
}
