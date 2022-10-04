package com.test.mod.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;

public class Fly extends Module
{
    private final ModeSetting Mode;
    private IntegerSetting speed;
    
    public Fly() {
        super("Fly", 0, ModuleType.Movement, false);
        this.Mode = new ModeSetting("Mode", "Dynamic", Arrays.<String>asList("Simple", "Dynamic"), this);
        this.speed = new IntegerSetting("Speed", 1.0, 1.273197475E-314, 0.0, 1);
        this.add(this.Mode, this.speed);
    }
    
    @Override
    public void onDisable() {
        if (Fly.mc.thePlayer.capabilities.isFlying) {
            Fly.mc.thePlayer.capabilities.isFlying = false;
        }
        super.onDisable();
    }
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.Mode.getCurrent().equals("Dynamic")) {
            if (!Tools.currentScreenMinecraft()) {
                return;
            }
            final EntityPlayerSP player = Fly.mc.thePlayer;
            final float flyspeed = (float)this.speed.getCurrent();
            player.jumpMovementFactor = 0.4f;
            player.motionX = 0.0;
            player.motionY = 0.0;
            player.motionZ = 0.0;
            final EntityPlayerSP entityPlayerSP = player;
            entityPlayerSP.jumpMovementFactor *= flyspeed * 3.0f;
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                final EntityPlayerSP entityPlayerSP2 = player;
                entityPlayerSP2.motionY += flyspeed;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final EntityPlayerSP entityPlayerSP3 = player;
                entityPlayerSP3.motionY -= flyspeed;
            }
        }
        else if (this.Mode.getCurrent().equals("Simple")) {
            final EntityPlayerSP player = Fly.mc.thePlayer;
            player.capabilities.isFlying = true;
        }
    }
}
