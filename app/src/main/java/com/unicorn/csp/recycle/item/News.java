package com.unicorn.csp.recycle.item;

import com.unicorn.csp.R;

import java.util.Date;

import me.alexrs.recyclerviewrenderers.interfaces.Renderable;


// 数据原型和布局绑定
public class News implements Renderable {

    @Override
    public int getRenderableId() {
        return R.layout.item_news;
    }

    private String title;

    private Date time;

    private String data;

    private int commentCount;

    public News(String title, Date time, String data, int commentCount) {
        this.title = title;
        this.time = time;
        this.data = data;
        this.commentCount = commentCount;
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

}
