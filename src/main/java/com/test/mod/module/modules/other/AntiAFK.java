package com.test.mod.module.modules.other;

import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.module.Module;

public class AntiAFK extends Module
{
    private TimerUtils timerUtils;
    
    public AntiAFK() {
        super("AntiAFK", 0, ModuleType.Other, false);
        this.timerUtils = new TimerUtils();
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (this.timerUtils.isDelayComplete(0.0)) {
            AntiAFK.mc.thePlayer.jump();
            this.timerUtils.reset();
        }
    }
}
