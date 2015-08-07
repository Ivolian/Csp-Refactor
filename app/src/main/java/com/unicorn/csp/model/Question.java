package com.unicorn.csp.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.Date;
import java.util.List;


public class Question implements ParentObject {

    private List<Object> answerList;

    private String content;

    private String name;

    private Date eventTime;

    public Question() {
    }

    public Question( String content, String name, Date eventTime) {
        this.content = content;
        this.name = name;
        this.eventTime = eventTime;
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
}
