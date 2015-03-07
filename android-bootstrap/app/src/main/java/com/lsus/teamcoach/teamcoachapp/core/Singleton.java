package com.lsus.teamcoach.teamcoachapp.core;

/**
 * Created by Don on 3/7/2015.
 */
public class Singleton {
    private static Singleton mInstance = null;

    private User currentUser;
    private String authToken;

    private Singleton(){
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public String getToken(){
        return this.authToken;
    }

    public void setToken(String token){
        authToken = token;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }
}
