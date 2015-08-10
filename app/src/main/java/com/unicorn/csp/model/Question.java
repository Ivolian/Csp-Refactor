package com.unicorn.csp.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Question implements ParentObject ,Serializable {

    private List<Object> answerList;

    private String id;

    private String content;

    private String name;

    private Date eventTime;


    public Question() {
    }

    public Question(String id, String content, String name, Date eventTime) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
