package com.unicorn.csp.recycle.factory;

import com.unicorn.csp.recycle.render.GeneralRender;

import me.alexrs.recyclerviewrenderers.interfaces.RendererFactory;
import me.alexrs.recyclerviewrenderers.renderer.Renderer;


public class Factory implements RendererFactory {

    @Override
    public Renderer getRenderer(int id) {

        return new GeneralRender(id);
    }

}
