package com.unicorn.csp.recycle.item;

import com.unicorn.csp.R;

import java.util.Date;

import me.alexrs.recyclerviewrenderers.interfaces.Renderable;


public class Comment implements Renderable {

    @Override
    public int getRenderableId() {
        return R.layout.item_comment;
    }

    private String account;

    private Date time;

    private String content;

    public Comment(String account, Date time, String content) {
        this.account = account;
        this.time = time;
        this.content = content;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
