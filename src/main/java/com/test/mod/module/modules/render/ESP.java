package com.test.mod.module.modules.render;

import java.util.Iterator;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import com.test.mod.Utils.HUDUtils;
import com.test.mod.module.modules.other.Teams;
import net.minecraft.entity.Entity;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.ModeSetting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ESP extends Module
{
    private static final HashMap<EntityPlayer, Long> newEnt;
    private final ModeSetting Mode;
    private EnableSetting Players;
    private EnableSetting Mobs;
    private EnableSetting Animals;
    private EnableSetting Inv;
    
    public ESP() {
        super("ESP", 0, ModuleType.Render, false);
        this.Mode = new ModeSetting("Mode", "2D", Arrays.<String>asList("2D", "Arrow", "Box", "Health", "Ring", "Shaded"), this);
        this.Players = new EnableSetting("Players", true);
        this.Mobs = new EnableSetting("Mobs", false);
        this.Animals = new EnableSetting("Animals", false);
        this.Inv = new EnableSetting("Invisible", false);
        this.add(this.Mode, this.Players, this.Mobs, this.Animals, this.Inv);
    }
    
    static {
        newEnt = new HashMap<EntityPlayer, Long>();
    }
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        final int rgb = Color.RED.getRGB();
        for (final Entity en : ESP.mc.theWorld.loadedEntityList) {
            if (en != ESP.mc.thePlayer && !en.isDead && (this.Inv.getEnable() || !en.isInvisible())) {
                if (en instanceof EntityPlayer && this.Players.getEnable()) {
                    if (!Teams.isTeam(en)) {
                        if (this.Mode.getCurrent().equals("2D")) {
                            HUDUtils.ee(en, 3, 0.0, 1.0, Color.GREEN.getRGB(), true);
                        }
                        if (this.Mode.getCurrent().equals("Arrow")) {
                            HUDUtils.ee(en, 5, 0.0, 1.0, Color.GREEN.getRGB(), true);
                        }
                        if (this.Mode.getCurrent().equals("Box")) {
                            HUDUtils.ee(en, 1, 0.0, 1.0, Color.GREEN.getRGB(), false);
                        }
                        if (this.Mode.getCurrent().equals("Health")) {
                            HUDUtils.ee(en, 4, 0.0, 1.0, Color.GREEN.getRGB(), true);
                        }
                        if (this.Mode.getCurrent().equals("Ring")) {
                            HUDUtils.ee(en, 6, 0.0, 1.0, Color.GREEN.getRGB(), true);
                        }
                        if (this.Mode.getCurrent().equals("Shaded")) {
                            HUDUtils.ee(en, 2, 0.0, 1.0, Color.GREEN.getRGB(), true);
                        }
                        if (this.Mode.getCurrent().equals("CSGO")) {
                            HUDUtils.CSGO(en, true);
                        }
                    }
                    else {
                        if (this.Mode.getCurrent().equals("2D")) {
                            HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
                        }
                        if (this.Mode.getCurrent().equals("Arrow")) {
                            HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
                        }
                        if (this.Mode.getCurrent().equals("Box")) {
                            HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
                        }
                        if (this.Mode.getCurrent().equals("Health")) {
                            HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
                        }
                        if (this.Mode.getCurrent().equals("Ring")) {
                            HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
                        }
                        if (this.Mode.getCurrent().equals("Shaded")) {
                            HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
                        }
                        if (this.Mode.getCurrent().equals("CSGO")) {
                            HUDUtils.CSGO(en, false);
                        }
                    }
                }
                if (en instanceof EntityAnimal && this.Animals.getEnable()) {
                    if (this.Mode.getCurrent().equals("2D")) {
                        HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
                    }
                    if (this.Mode.getCurrent().equals("Arrow")) {
                        HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
                    }
                    if (this.Mode.getCurrent().equals("Box")) {
                        HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
                    }
                    if (this.Mode.getCurrent().equals("Health")) {
                        HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
                    }
                    if (this.Mode.getCurrent().equals("Ring")) {
                        HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
                    }
                    if (this.Mode.getCurrent().equals("Shaded")) {
                        HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
                    }
                    if (this.Mode.getCurrent().equals("CSGO")) {
                        HUDUtils.CSGO(en, false);
                    }
                }
                if (!(en instanceof EntityMob) || !this.Mobs.getEnable()) {
                    continue;
                }
                if (this.Mode.getCurrent().equals("2D")) {
                    HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent().equals("Arrow")) {
                    HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent().equals("Box")) {
                    HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
                }
                if (this.Mode.getCurrent().equals("Health")) {
                    HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent().equals("Ring")) {
                    HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent().equals("Shaded")) {
                    HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
                }
                if (!this.Mode.getCurrent().equals("CSGO")) {
                    continue;
                }
                HUDUtils.CSGO(en, false);
            }
        }
    }
}
