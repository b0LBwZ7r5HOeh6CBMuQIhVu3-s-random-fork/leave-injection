package com.test.mod.module.modules.world;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.settings.Setting;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.module.Module;

public class Regen extends Module
{
    public IntegerSetting health;
    public IntegerSetting speed;
    
    public Regen() {
        super("Regen", 0, ModuleType.Combat, false);
        this.health = new IntegerSetting("Health", 0.0, 0.0, 0.0, 0);
        this.speed = new IntegerSetting("Speed", 0.0, 0.0, 0.0, 0);
        this.add(this.health, this.speed);
    }
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Regen.mc.thePlayer.capabilities.isCreativeMode || Regen.mc.thePlayer.getHealth() == 0.0f) {
            return;
        }
        if (Regen.mc.thePlayer.getFoodStats().getFoodLevel() < 18) {
            return;
        }
        if (Regen.mc.thePlayer.getHealth() >= Regen.mc.thePlayer.getMaxHealth()) {
            return;
        }
        if (Regen.mc.thePlayer.getHealth() <= this.health.getCurrent()) {
            for (int i = 0; i < (int)this.speed.getCurrent(); ++i) {
                Regen.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer());
            }
        }
    }
}
