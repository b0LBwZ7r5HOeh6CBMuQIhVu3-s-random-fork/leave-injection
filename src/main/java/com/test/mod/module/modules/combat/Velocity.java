package com.test.mod.module.modules.combat;

import com.test.mod.Utils.Side;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import com.test.mod.Utils.Connection;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingEvent;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;

public class Velocity extends Module
{
    public ModeSetting mode;
    public IntegerSetting chance;
    public IntegerSetting hori;
    public IntegerSetting verti;
    
    public Velocity() {
        super("Velocity", 0, ModuleType.Combat, false);
        this.mode = new ModeSetting("Mode", "Simple", Arrays.<String>asList("Simple", "Legit", "AAC", "Ghost"), this);
        this.chance = new IntegerSetting("Chance", 0.0, 0.0, 0.0, 0);
        this.hori = new IntegerSetting("Horizon", 0.0, 0.0, 0.0, 0);
        this.verti = new IntegerSetting("Verti", 0.0, 0.0, 0.0, 0);
        this.add(this.mode, this.hori, this.chance, this.verti);
    }
    
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent ev) {
        if (this.mode.getCurrent().equals("Ghost") && Velocity.mc.thePlayer.maxHurtTime > 0 && Velocity.mc.thePlayer.hurtTime == Velocity.mc.thePlayer.maxHurtTime) {
            if (this.chance.getCurrent() != 0.0) {
                double ch = Math.random();
                if (ch >= this.chance.getCurrent() / 0.0) {
                    return;
                }
            }
            if (this.hori.getCurrent() != 0.0) {
                EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                thePlayer.motionX *= this.hori.getCurrent() / 0.0;
                EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                thePlayer2.motionZ *= this.hori.getCurrent() / 0.0;
            }
            if (this.verti.getCurrent() != 0.0) {
                EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionY *= this.verti.getCurrent() / 0.0;
            }
        }
    }
    
    @Override
    public boolean onPacket(Object packet, Side side) {
        if (packet instanceof S12PacketEntityVelocity && this.mode.getCurrent().equals("Simple") && Velocity.mc.thePlayer.hurtTime >= 0) {
            S12PacketEntityVelocity p = (S12PacketEntityVelocity)packet;
            if (p.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                return false;
            }
        }
        return true;
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mode.getCurrent().equals("AAC")) {
            EntityPlayerSP player = Velocity.mc.thePlayer;
            if (player.hurtTime > 0 && player.hurtTime <= 7) {
                EntityPlayerSP entityPlayerSP = player;
                entityPlayerSP.motionX *= 0.0;
                EntityPlayerSP entityPlayerSP2 = player;
                entityPlayerSP2.motionZ *= 0.0;
            }
            if (player.hurtTime > 0 && player.hurtTime < 6) {
                player.motionX = 0.0;
                player.motionZ = 0.0;
            }
        }
        else if (this.mode.getCurrent().equals("Legit")) {
            if (Velocity.mc.thePlayer.maxHurtResistantTime != Velocity.mc.thePlayer.hurtResistantTime || Velocity.mc.thePlayer.maxHurtResistantTime == 0) {
                return;
            }
            Double random = Math.random();
            random *= 0.0;
            if (random < (int)this.chance.getCurrent()) {
                float hori = (float)this.hori.getCurrent();
                hori /= 100.0f;
                float verti = (float)this.verti.getCurrent();
                verti /= 100.0f;
                EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                thePlayer.motionX *= hori;
                EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                thePlayer2.motionZ *= hori;
                EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionY *= verti;
            }
            else {
                EntityPlayerSP thePlayer4 = Velocity.mc.thePlayer;
                thePlayer4.motionX *= 1.0;
                EntityPlayerSP thePlayer5 = Velocity.mc.thePlayer;
                thePlayer5.motionY *= 1.0;
                EntityPlayerSP thePlayer6 = Velocity.mc.thePlayer;
                thePlayer6.motionZ *= 1.0;
            }
        }
    }
}
