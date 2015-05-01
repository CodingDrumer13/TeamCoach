package com.lsus.teamcoach.teamcoachapp.core;

import com.parse.ParseFile;

import java.io.Serializable;

/**
 * Created by TeamCoach on 3/17/2015.
 */
public class Drill implements Serializable {

    protected String objectId;
    protected String groupId;
    protected boolean isGroup;
    protected String drillName;
    protected String drillType;
    protected String drillAge;
    protected String drillDescription;
    protected float drillRating;
    protected int numberOfRatings;
    protected int timesUsed;
    protected boolean hasPicture;
    protected String creator;

    public Drill(){}

    public Drill(String groupId, String drillName, String drillType, String drillAge, String drillDescription, String creator){
        this.groupId = groupId;
        this.drillName = drillName;
        this.drillType = drillType;
        this.drillAge = drillAge;
        this.drillDescription = drillDescription;
        this.drillRating = 0;
        this.timesUsed = 0;
        this.numberOfRatings = 0;
        this.creator = creator;
    }

    public void setObjectId(String objectId) { this.objectId = objectId; }

    public String getObjectId() { return objectId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupId() { return groupId; }

    public void setIsGroup(boolean isGroup) { this.isGroup = isGroup; }

    public boolean getIsGroup() { return isGroup; }

    public void setDrillName(String drillName) { this.drillName = drillName; }

    public String getDrillName(){ return drillName; }

    public void setDrillType(String drillType){ this.drillType = drillType; }

    public String getDrillType(){ return drillType; }

    public void setDrillAge(String drillAge){ this.drillAge = drillAge; }

    public String getDrillAge(){ return drillAge; }

    public void setDrillDescription(String drillDescription){ this.drillDescription = drillDescription; }

    public String getDrillDescription(){ return drillDescription; }

    public void setDrillRating(float drillRating){ this.drillRating = drillRating; }

    public float getDrillRating(){ return drillRating; }

    public void setNumberOfRatings(int numberOfRatings){ this.numberOfRatings = numberOfRatings; }

    public int getNumberOfRatings(){ return numberOfRatings; }

    public void setTimesUsed(int timesUsed){ this.timesUsed = timesUsed; }

    public int getTimesUsed(){ return timesUsed; }

    public void setHasPicture(boolean hasPicture) { this.hasPicture = hasPicture; }

    public boolean getHasPicture() { return hasPicture; }

//    public void setDrillPicture(ParseFile drillPicture) { this.drillPicture = drillPicture; }
//
//    public ParseFile getDrillPicture() { return drillPicture; }

    public void setCreator(String creator) { this.creator = creator; }

    public String getCreator(){ return creator; }

}
