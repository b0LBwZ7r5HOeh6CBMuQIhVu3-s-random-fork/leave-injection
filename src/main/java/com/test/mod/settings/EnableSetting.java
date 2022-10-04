package com.test.mod.settings;

public class EnableSetting extends Setting
{
    private boolean enable;
    
    public EnableSetting(final String name, final boolean enable) {
        super(name);
        this.enable = enable;
    }
    
    public void setEnable(final boolean enable) {
        this.enable = enable;
    }
    
    public boolean getEnable() {
        return this.enable;
    }
}
