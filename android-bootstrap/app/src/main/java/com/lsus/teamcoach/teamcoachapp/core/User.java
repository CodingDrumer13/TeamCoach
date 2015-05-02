package com.lsus.teamcoach.teamcoachapp.core;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable {

    private static final long serialVersionUID = -7495897652017488896L;

    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected String phonenumber;
    protected String objectId;
    protected String sessionToken;
    protected String gravatarId;
    protected String avatarUrl;
    protected String alias;
    protected String role;
    protected String email;
    protected String createdAt;
    protected ArrayList<Team> teams;
    protected ArrayList<CalendarEvent> events;
    protected ArrayList<Session> sessions;
    protected String team;
    protected ArrayList<Drill> drills;

    public User(String userUsername, String userPassword, String userAlias, String userRole, String userEmail, String userFirstName, String userLastName, String userphoneNumber){
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.username = userUsername;
        this.password = userPassword;
        this.alias = userAlias;
        this.role = userRole;
        this.email = userEmail;
        this.phonenumber = userphoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(final String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public void setPassword(final String password) {this.password = password;}

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){ return firstName +" "+ lastName; }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {this.alias = alias;}

    public String getEmail(){return email; }

    public void setEmail(final String email){this.email = email; }

    public String getRole() {return role;}

    public void setRole(final String role) {this.role = role; }

    public String getGravatarId() {
        return gravatarId;
    }

    public ArrayList<Team> getTeams() {
        if(teams == null) {
            teams = new ArrayList<Team>();
        }
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) { this.teams = teams; }

    public ArrayList<CalendarEvent> getEvents() {
        if(events == null) {
            events = new ArrayList<CalendarEvent>();
        }
        return events;
    }


    public void setEvents(ArrayList<CalendarEvent> events) {
//        //this.events = events;
//        ArrayList<CalendarEvent> tempEvents = new ArrayList<CalendarEvent>();
//        for(int i = 0; i < events.size(); i++)
//        {
//          int next = i + 1;
//          for(int k = 0; next < events.size(); next++) {
//              if (events.get(k).compareTo(events.get(next)) < 1) //If dates are out of order
//              {
//                  CalendarEvent e = events.get(next);
//                  tempEvents.add(e); //Add second event to sorted list
//                  //events.remove(e); //Remove sorted item from original list
//              }
//              else
//              {
//                  tempEvents.add(events.get(k)); //Add first event to sorted list
//                  //events.remove(events.get(k)); //Remove sorted item from original list
//              }
//          }
//       }

        Collections.sort(events, new CalendarComparator());
        this.events = events;

    }

    public ArrayList<Session> getSessions(){ return sessions; }

    public void setSessions(ArrayList<Session> sessions){ this.sessions = sessions; }

    public void setDrills(ArrayList<Drill> drills) { this.drills = drills; }

    public ArrayList<Drill> getDrills() { return drills; }

    public String getCreatedAt(){
        return createdAt;
    }

    public String getAvatarUrl() {
        if (TextUtils.isEmpty(avatarUrl)) {
            String gravatarId = getGravatarId();
            if (TextUtils.isEmpty(gravatarId))
                gravatarId = GravatarUtils.getHash(getUsername());
            avatarUrl = getAvatarUrl(gravatarId);
        }
        return avatarUrl;
    }

    private String getAvatarUrl(String id) {
        if (!TextUtils.isEmpty(id))
            return "https://secure.gravatar.com/avatar/" + id + "?d=404";
        else
            return null;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
