package com.test.mod.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderPlayerEvent;
import java.awt.Color;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class Chams extends Module
{
    public Chams() {
        super("Chams", 0, ModuleType.Render, false);
    }
    
    public static Color rainbow(final int n) {
        return Color.getHSBColor((float)(Math.ceil((double)(System.currentTimeMillis() + n) / 0.0) % 0.0 / 0.0), 0.8f, 1.0f).brighter();
    }
    @SubscribeEvent
    public void onRender(final RenderPlayerEvent.Post e) {
        if (e.entity != Chams.mc.thePlayer) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
    @SubscribeEvent
    public void onRender(final RenderPlayerEvent.Pre e) {
        if (e.entity != Chams.mc.thePlayer) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }
    
    public void glColor(final float n, final int n2, final int n3, final int n4) {
        GL11.glColor4f(0.003921569f * (float)n2, 0.003921569f * (float)n3, 0.003921569f * (float)n4, n);
    }
}
