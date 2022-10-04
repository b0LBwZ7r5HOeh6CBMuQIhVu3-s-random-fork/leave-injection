package com.test.mod.settings;

import java.util.Iterator;
import java.util.List;
import com.test.mod.module.Module;

public class ModeSetting extends Setting
{
    private Module parent;
    private String current;
    private final List<String> modes;
    
    public ModeSetting(final String name, final String current, final List<String> modes, final Module parent) {
        super(name);
        this.current = current;
        this.modes = modes;
        this.parent = parent;
    }
    
    public Module getParent() {
        return this.parent;
    }
    
    public void setParent(final Module parent) {
        this.parent = parent;
    }
    
    public List<String> getModes() {
        return this.modes;
    }
    
    public String getCurrent() {
        return this.current;
    }
    
    public void setCurrent(final String current) {
        this.current = current;
    }
    
    public int getCurrentIndex() {
        int index = 0;
        for (final String mode : this.modes) {
            if (mode.equalsIgnoreCase(this.current)) {
                return index;
            }
            ++index;
        }
        return 0;
    }
}
