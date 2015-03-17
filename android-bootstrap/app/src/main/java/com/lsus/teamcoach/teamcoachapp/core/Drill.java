package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;

/**
 * Created by TeamCoach on 3/17/2015.
 */
public class Drill implements Serializable {

    protected String objectId;
    protected String drillName;
    protected String drillType;
    protected String drillAge;
    protected String drillDescription;
    protected int drillRating;

    public Drill(String objectId, String drillName, String drillType, String drillAge, String drillDescription, int drillRating){
        this.objectId = objectId;
        this.drillName = drillName;
        this.drillType = drillType;
        this.drillAge = drillAge;
        this.drillDescription = drillDescription;
        this.drillRating = drillRating;
    }

    public void setDrillName(String drillName) { this.drillName = drillName; }

    public String getDrillName(){ return drillName; }

    public void setDrillType(String drillType){ this.drillType = drillType; }

    public String getDrillType(){ return drillType; }

    public void setDrillAge(String drillAge){ this.drillAge = drillAge; }

    public String getDrillAge(){ return drillAge; }

    public void setDrillDescription(String drillDescription){ this.drillDescription = drillDescription; }

    public String getDrillDescription(){ return drillDescription; }

    public void setDrillRating(int drillRating){ this.drillRating = drillRating; }

    public int getDrillRating(){ return drillRating; }
}
