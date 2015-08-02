package com.unicorn.csp.model;

import java.io.Serializable;


public class Book implements Serializable {

    private String id;

    private String name;

    private String picture;

    private String ebook;

    private String ebookFilename;

    public Book(String id, String name, String picture, String ebook, String ebookFilename) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.ebook = ebook;
        this.ebookFilename = ebookFilename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}

