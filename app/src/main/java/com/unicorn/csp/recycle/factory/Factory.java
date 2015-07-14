package com.unicorn.csp.recycle.factory;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.render.New1Render;
import com.unicorn.csp.recycle.render.New2Render;

import me.alexrs.recyclerviewrenderers.interfaces.RendererFactory;
import me.alexrs.recyclerviewrenderers.renderer.Renderer;


public class Factory implements RendererFactory {

    @Override
    public Renderer getRenderer(int id) {
        switch (id) {
            case R.layout.item_new_type1:
                return new New1Render(id);
            case R.layout.item_new_type2:
                return new New2Render(id);
        }
        return null;
    }

}
