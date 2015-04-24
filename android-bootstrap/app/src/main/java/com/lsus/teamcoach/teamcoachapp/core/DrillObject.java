package com.lsus.teamcoach.teamcoachapp.core;

import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by TeamCoach on 4/23/2015.
 */
public class DrillObject extends ParseObject {

    public DrillObject(String groupId, String drillName, String drillType, String drillAge, String drillDescription, String creator){
        setGroupId(groupId);
        setDrillName(drillName);
        setDrillType(drillType);
        setDrillAge(drillAge);
        setDrillDescription(drillDescription);
        setCreator(creator);
        setDrillRating(0);
        setNumberOfRatings(0);
        setTimesUsed(0);
    }

    public String getObjectId() { return getString("objectId"); }

    public void setGroupId(String groupId) { put("groupId", groupId); }

    public String getGroupId() { return getString("objectId"); }

    public void setIsGroup(boolean isGroup) { put("isGroup", isGroup); }

    public boolean getIsGroup() { return getBoolean("isGrouop"); }

    public void setDrillName(String drillName) { put("drillName", drillName); }

    public String getDrillName(){ return getString("drillName"); }

    public void setDrillType(String drillType){ put("drillType", drillType); }

    public String getDrillType(){ return getString("drillType"); }

    public void setDrillAge(String drillAge){ put("drillAge", drillAge); }

    public String getDrillAge(){ return getString("drillAge"); }

    public void setDrillDescription(String drillDescription){ put("drillDescription", drillDescription); }

    public String getDrillDescription(){ return getString("drillDescription"); }

    public void setDrillRating(int drillRating){ put("drillRating", drillRating); }

    public int getDrillRating(){ return getInt("drillRating"); }

    public void setNumberOfRatings(int numberOfRatings){ put("numberOfRatings", numberOfRatings); }

    public int getNumberOfRatings(){ return getInt("numberOfRatings"); }

    public void setTimesUsed(int timesUsed){ put("timesUsed", timesUsed); }

    public int getTimesUsed(){ return getInt("timesUsed"); }

    public void setDrillPicture(ParseFile drillPicture) { put("drillPicture", drillPicture); }

    public ParseFile getDrillPicture() { return getParseFile("drillPicture"); }

    public void setCreator(String creator) { put("creator", creator); }

    public String getCreator(){ return getString("creator"); }
}
