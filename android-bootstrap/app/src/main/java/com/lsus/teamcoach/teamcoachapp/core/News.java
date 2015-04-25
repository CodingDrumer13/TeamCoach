package com.lsus.teamcoach.teamcoachapp.core;

import java.io.Serializable;

public class News implements Serializable {

    private static final long serialVersionUID = -6641292855569752036L;

    private String title;
    private String content;
    private String objectId;
    private String creator;
    private String createdAt;
    private String teamId;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
