package com.unicorn.csp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class News implements Parcelable {

    private String title;

    private Date time;

    private String data;

    private int commentCount;

    private String picture;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeLong(time != null ? time.getTime() : -1);
        dest.writeString(this.data);
        dest.writeInt(this.commentCount);
        dest.writeString(this.picture);
    }

    public News() {
    }

    protected News(Parcel in) {
        this.title = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.data = in.readString();
        this.commentCount = in.readInt();
        this.picture = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public News(String title, Date time, String data, int commentCount, String picture) {
        this.title = title;
        this.time = time;
        this.data = data;
        this.commentCount = commentCount;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public static Creator<News> getCREATOR() {
        return CREATOR;
    }
}
