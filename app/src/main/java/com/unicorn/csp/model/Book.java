package com.unicorn.csp.model;

import java.io.Serializable;


public class Book implements Serializable {

    private Integer id;

    private String name;

    private String picture;

    private String ebook;

    private String ebookFilename;

    private String summary;

    public Book(Integer id, String name, String picture, String ebook, String ebookFilename,String summary) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.ebook = ebook;
        this.ebookFilename = ebookFilename;
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEbook() {
        return ebook;
    }

    public void setEbook(String ebook) {
        this.ebook = ebook;
    }

    public String getEbookFilename() {
        return ebookFilename;
    }

    public void setEbookFilename(String ebookFilename) {
        this.ebookFilename = ebookFilename;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

