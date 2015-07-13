package com.unicorn.csp.recycle.items;

import com.unicorn.csp.R;

import me.alexrs.recyclerviewrenderers.interfaces.Renderable;

/**
 * Created by Administrator on 2015/7/13.
 */
public class ItemBender implements Renderable {

    private String title;

    public ItemBender(String title) {
        this.title = title;
    }

    @Override
    public int getRenderableId() {
        return R.layout.item_view;
    }

    public String getTitle() {
        return title;
    }
}
