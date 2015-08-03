package com.unicorn.csp.model;

import java.io.Serializable;


public class Book implements Serializable {

    private Integer id2;

    private String name;

    private String picture;

    private String ebook;

    private String ebookFilename;

    private String summary;

    private String id;

    public Book(Integer id2, String name, String picture, String ebook, String ebookFilename, String summary, String id) {
        this.id2 = id2;
        this.name = name;
        this.picture = picture;
        this.ebook = ebook;
        this.ebookFilename = ebookFilename;
        this.summary = summary;
        this.id = id;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

