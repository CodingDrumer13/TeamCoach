package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TeamCoach on 3/31/2015.
 */
public class Session implements Serializable {

    protected String objectId;
    protected String name;
    protected String ageGroup;
    protected String type;
    protected ArrayList<Drill> drillList;
    protected float rating;
    protected boolean isPublic;
    protected String creator;

    public Session(String name, ArrayList<Drill> drillList, float rating, boolean isPublic, String creator){
        this.name = name;
        this.drillList = drillList;
        this.rating = rating;
        this.isPublic = isPublic;
        this.creator = creator;
    }

    public String getObjectId(){ return objectId; }

    public void setObjectId(String objectId){ this.objectId = objectId; }

    public String getName(){ return name; }

    public void setName(String name) { this.name = name; }

    public String getAgeGroup() { return ageGroup; }

    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public ArrayList<Drill> getDrillList(){ return drillList; }

    public void setDrillList(ArrayList<Drill> drillList){ this.drillList = drillList; }

    public float getRating(){ return rating; }

    public void setRating(float rating){ this.rating = rating; }

    public boolean getIsPublic(){ return isPublic; }

    public void setIsPublic(boolean isPublic){ this.isPublic = isPublic; }

    public String getCreator(){ return creator; }

    public void setCreator(String creator){ this.creator = creator; }
}
