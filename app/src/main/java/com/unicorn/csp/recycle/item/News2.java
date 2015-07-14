package com.unicorn.csp.recycle.item;

import com.unicorn.csp.R;

import java.util.Date;

import me.alexrs.recyclerviewrenderers.interfaces.Renderable;


// 数据原型和布局绑定
public class News2 implements Renderable {

    @Override
    public int getRenderableId() {
        return R.layout.item_new_type2;
    }


    private String title;

    private Date time;

    private int commentCount;

    public News2(String title, Date time, int commentCount) {
        this.title = title;
        this.time = time;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
