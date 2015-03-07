package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Don on 3/7/2015.
 */
public class Team implements Serializable {

//    private static final long serialVersionUID = 0;

    protected String teamName;
    protected ArrayList<User> Players;
    protected String objectId;

    public Team(String teamName, String teamObjectId, ArrayList<User> Players){
        this.teamName = teamName;
        this.objectId = teamObjectId;
        this.Players = Players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<User> getPlayers() {
        return Players;
    }

    public void setPlayers(ArrayList<User> players) {
        Players = players;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }





}
