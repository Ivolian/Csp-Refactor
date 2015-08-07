package com.unicorn.csp.model;

import java.io.Serializable;
import java.util.Date;


public class Answer implements Serializable {

    private String content;

    private String name;

    private Date eventTime;

    private String id;
    //


    public Answer(String content, String name, Date eventTime, String id) {
        this.content = content;
        this.name = name;
        this.eventTime = eventTime;
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
