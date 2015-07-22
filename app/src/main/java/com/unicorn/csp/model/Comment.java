package com.unicorn.csp.model;

import java.util.Date;


public class Comment {

    private String username;

    private Date eventTime;

    private String words;

    public Comment(String username, Date eventTime, String words) {
        this.username = username;
        this.eventTime = eventTime;
        this.words = words;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
