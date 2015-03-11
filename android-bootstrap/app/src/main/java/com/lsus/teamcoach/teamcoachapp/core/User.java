package com.lsus.teamcoach.teamcoachapp.core;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = -7495897652017488896L;

    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected String phone;
    protected String objectId;
    protected String sessionToken;
    protected String gravatarId;
    protected String avatarUrl;
    protected String alias;
    protected String role;
    protected String email;
    protected ArrayList<String> teams;

    public User(String userUsername, String userPassword, String userAlias, String userRole, String userEmail, String userFirstName, String userLastName){
        this.firstName = userFirstName;
        this.lastName = userLastName;
        this.username = userUsername;
        this.password = userPassword;
        this.alias = userAlias;
        this.role = userRole;
        this.email = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
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

    public ArrayList<String> getTeams() { return teams;  }

    public void setTeams(ArrayList<String> teams) { this.teams = teams; }

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
}
