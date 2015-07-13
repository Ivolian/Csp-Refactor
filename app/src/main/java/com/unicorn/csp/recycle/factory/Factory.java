/*
 * Copyright (C) 2014 Alejandro Rodriguez Salamanca.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unicorn.csp.recycle.factory;

import com.unicorn.csp.R;
import com.unicorn.csp.recycle.renderers.ItemRender;

import me.alexrs.recyclerviewrenderers.interfaces.RendererFactory;
import me.alexrs.recyclerviewrenderers.renderer.Renderer;

/**
 * @author Alejandro Rodriguez <https://github.com/Alexrs95>
 */
public class Factory implements RendererFactory {

    @Override
    public Renderer getRenderer(int id) {
        switch (id) {
            case R.layout.item_view:
                return new ItemRender(id);
        }
        return null;
    }
}
