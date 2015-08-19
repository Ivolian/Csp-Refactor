package com.unicorn.csp.model;

import java.util.Date;


public class Comment {

    private String courtName;

    private String username;

    private Date eventTime;

    private String content;

    public Comment(String courtName,String username, Date eventTime, String content) {
        this.courtName = courtName;
        this.username = username;
        this.eventTime = eventTime;
        this.content = content;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
