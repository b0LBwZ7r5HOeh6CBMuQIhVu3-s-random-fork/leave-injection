package com.test.mod.settings;

import com.test.mod.module.Module;

public class IntegerSetting extends Setting
{
    private final double min;
    private final double max;
    private double current;
    private Module parent;
    private int dou;
    
    public IntegerSetting(final String name, final double current, final double min, final double max, final int dou) {
        super(name);
        this.current = current;
        this.min = min;
        this.max = max;
        this.dou = dou;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getCurrent() {
        return this.current;
    }
    
    @Override
    public Module getParentMod() {
        return this.parent;
    }
    
    public void setCurrent(final double current) {
        this.current = current;
    }
    
    public void setDou(final int dou) {
        this.dou = dou;
    }
    
    public int getDou() {
        return this.dou;
    }
}
