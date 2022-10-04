package com.test.mod.Loader;

public class LOL
{
    public LOL() {
        super();
    }
    
    public static int get(final String key) {
        return Integer.parseInt(System.getProperty(key));
    }
}
