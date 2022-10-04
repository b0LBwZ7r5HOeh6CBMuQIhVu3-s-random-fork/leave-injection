package com.test.mod.module.modules.render;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderPlayerEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.Utils.Nameplate;
import java.util.Queue;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Nametags extends Module
{
    private Queue<Nameplate> tags;
    
    public Nametags() {
        super("Nametags", 0, ModuleType.Render, false);
    }
    @SubscribeEvent
    public void onPreRender(final RenderPlayerEvent.Pre event) {
        double v = 4.24399158E-315;
        final Scoreboard sb = event.entityPlayer.getWorldScoreboard();
        final ScoreObjective sbObj = sb.getObjectiveInDisplaySlot(2);
        if (sbObj != null && !event.entityPlayer.getDisplayNameString().equals(Minecraft.getMinecraft().thePlayer.getDisplayNameString()) && event.entityPlayer.getDistanceSqToEntity((Entity)Minecraft.getMinecraft().thePlayer) < 0.0) {
            v *= 0.0;
        }
        if (!event.entityPlayer.getDisplayName().equals(Minecraft.getMinecraft().thePlayer.getDisplayName())) {
            final Nameplate np = new Nameplate(event.entityPlayer.getDisplayNameString(), event.x, event.y, event.z, event.entityLiving);
            np.renderNewPlate(new Color(100, 100, 100));
        }
    }
}
