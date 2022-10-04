package com.test.mod.module.modules.render;

import java.util.Iterator;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import java.util.Comparator;
import java.util.ArrayList;
import com.test.mod.Client;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import com.test.mod.settings.Setting;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Hud extends Module
{
    public static EnableSetting RainBow;
    
    public Hud() {
        super("Hud", 0, ModuleType.Render, true);
        this.add(Hud.RainBow);
    }
    
    static {
        Hud.RainBow = new EnableSetting("RainBow", false);
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        int rainbowTickc = 0;
        ScaledResolution s = new ScaledResolution(Hud.mc);
        int width = new ScaledResolution(Hud.mc).getScaledWidth();
        int height = new ScaledResolution(Hud.mc).getScaledHeight();
        int y = 1;
        Hud.mc.fontRendererObj.drawStringWithShadow(Client.instance.username + " " + Client.instance.qq, (float)(width - Hud.mc.fontRendererObj.getStringWidth(Client.instance.username + " " + Client.instance.qq)), (float)(height - Hud.mc.fontRendererObj.FONT_HEIGHT), -1);
        ArrayList<Module> enabledModules = new ArrayList();
        for (Module m : Client.instance.moduleManager.getModules()) {
            if (m.isState()) {
                enabledModules.add(m);
            }
        }
        enabledModules.sort(new Comparator<Module>() {
       
            
            @Override
            public int compare(Module o1, Module o2) {
                return Module.mc.fontRendererObj.getStringWidth(o2.getName()) - Module.mc.fontRendererObj.getStringWidth(o1.getName());
            }
            
//            @Override
//            public int compare(Object o, Object o2) {
//                return this.compare((Module)o, (Module)o2);
//            }
        });
        for (Module m : enabledModules) {
            if (m != null && m.isState()) {
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                Color rainbow = new Color(Color.HSBtoRGB((float)(Minecraft.getMinecraft().thePlayer.ticksExisted / 0.0 - Math.sin(rainbowTickc / 0.0 * 1.273197475E-314)) % 1.0f, 0.5f, 1.0f));
                int moduleWidth = Hud.mc.fontRendererObj.getStringWidth(m.getName());
                Hud.mc.fontRendererObj.drawStringWithShadow(m.getName(), (float)(width - moduleWidth - 1), (float)y, Hud.RainBow.getEnable() ? rainbow.getRGB() : -1);
                y += Hud.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }
}
