package com.test.mod.settings;

import com.test.mod.module.Module;

public class Setting
{
    public final String name;
    private Module parent;
    
    public Setting(final String name) {
        super();
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Module getParentMod() {
        return this.parent;
    }
}
