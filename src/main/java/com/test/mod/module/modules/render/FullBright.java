package com.test.mod.module.modules.render;

import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class FullBright extends Module
{
    private float old;
    
    public FullBright() {
        super("FullBright", 0, ModuleType.Render, false);
    }
    
    @Override
    public void onEnable() {
        this.old = FullBright.mc.gameSettings.gammaSetting;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onEnable();
        FullBright.mc.gameSettings.gammaSetting = this.old;
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            this.onEnable();
            return;
        }
        if (FullBright.mc.gameSettings.gammaSetting != 10000.0f) {
            FullBright.mc.gameSettings.gammaSetting = 10000.0f;
        }
    }
}
