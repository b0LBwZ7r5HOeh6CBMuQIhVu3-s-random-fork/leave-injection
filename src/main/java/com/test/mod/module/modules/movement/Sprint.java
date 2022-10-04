package com.test.mod.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class Sprint extends Module
{
    public Sprint() {
        super("Sprint", 0, ModuleType.Movement, false);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    @SubscribeEvent
    public void onUpdate(final TickEvent.PlayerTickEvent event) {
        if (!Sprint.mc.thePlayer.isCollidedHorizontally && Sprint.mc.thePlayer.moveForward > 0.0f) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}
