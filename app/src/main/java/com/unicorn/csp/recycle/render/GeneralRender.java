package com.unicorn.csp.recycle.render;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.viewholder.News1ViewHolder;
import com.unicorn.csp.recycle.viewholder.News2ViewHolder;

import me.alexrs.recyclerviewrenderers.renderer.Renderer;
import me.alexrs.recyclerviewrenderers.viewholder.RenderViewHolder;


public class GeneralRender extends Renderer {

    public GeneralRender(int id) {
        super(id);
    }

    @Override
    public RenderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(id, viewGroup, false);

        switch (id){
            case R.layout.item_new_type1:
                return new News1ViewHolder(itemView);
            case R.layout.item_new_type2:
                return new News2ViewHolder(itemView);
        }

        return new News1ViewHolder(itemView);
    }
}
