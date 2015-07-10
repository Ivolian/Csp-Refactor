package com.unicorn.csp.activity;

import com.negusoft.greenmatter.MatPalette;
import com.negusoft.greenmatter.activity.MatActivity;
import com.unicorn.csp.greenmatter.ColorOverrider;


public class ColorActivity extends MatActivity {

    @Override
    public MatPalette overridePalette(MatPalette palette) {

        return ColorOverrider.getInstance(this).applyOverride(palette);
    }

}
