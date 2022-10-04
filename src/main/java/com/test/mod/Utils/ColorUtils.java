package com.test.mod.Utils;

import java.awt.Color;

public class ColorUtils
{
    public ColorUtils() {
        super();
    }
    
    public static int color(final float r, final float g, final float b, final float a) {
        return new Color(r, g, b, a).getRGB();
    }
    
    public static int color(final int r, final int g, final int b, final int a) {
        return new Color(r, g, b, a).getRGB();
    }
    
    public static int getColor(final int a, final int r, final int g, final int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }
    
    public static int getColor(final int r, final int g, final int b) {
        return 0xFF000000 | r << 16 | g << 8 | b;
    }
    
    public static Color rainbow() {
        final long offset = 3567587327L;
        final float fade = 1.0f;
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
}
