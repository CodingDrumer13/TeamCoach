package com.lsus.teamcoach.teamcoachapp.core;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by TeamCoach on 4/28/2015.
 */
@ParseClassName("DrillPicture")
public class DrillPictureObject extends ParseObject {

    public DrillPictureObject(){}

    public String getObjectId() { return getString("objectId"); }

    public void setDrillId(String id) { put("drillId", id); }

    public String getDrillId() { return getString("drillId"); }

    public void setDrillPicture(ParseFile drillPicture) { put("picture", drillPicture); }

    public ParseFile getDrillPicture() { return getParseFile("picture"); }
}
