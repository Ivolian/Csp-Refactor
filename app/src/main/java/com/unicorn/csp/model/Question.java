package com.unicorn.csp.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Question implements ParentObject ,Serializable {

    private List<Object> answerList;

    private String content;

    private String name;

    private Date eventTime;

    private String id;

    public Question() {
    }

    public Question(String content, String name, Date eventTime, String id) {
        this.content = content;
        this.name = name;
        this.eventTime = eventTime;
        this.id = id;
    }

//

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

    @Override
    public List<Object> getChildObjectList() {
        return answerList;
    }

    @Override
    public void setChildObjectList(List<Object> childObjectList) {
        answerList = childObjectList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
