package com.test.mod.module.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SuperKnockback extends Module
{
    public ModeSetting mode;
    private IntegerSetting delayValue;
    private IntegerSetting hurtTimeValue;
    TimerUtils timerUtils;
    
    public SuperKnockback() {
        super("SuperKnockBack", 0, ModuleType.Combat, false);
        this.mode = new ModeSetting("Mode", "Packet", Arrays.<String>asList("ExtraPacket", "WTap", "Packet"), this);
        this.delayValue = new IntegerSetting("Delay", 0.0, 0.0, 0.0, 1);
        this.hurtTimeValue = new IntegerSetting("HurtTime", 0.0, 0.0, 0.0, 1);
        this.timerUtils = new TimerUtils();
        this.add(this.mode, this.delayValue, this.hurtTimeValue);
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
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.target instanceof EntityLivingBase) {
            if (((EntityLivingBase)event.target).hurtTime > this.hurtTimeValue.getCurrent() || !this.timerUtils.isDelay((long)this.delayValue.getCurrent())) {
                return;
            }
            final String lowerCase = this.mode.getCurrent().toLowerCase();
            int n = -1;
            switch (lowerCase.hashCode()) {
                case -2117036904:
                    if (lowerCase.equals("extrapacket")) {
                        n = 0;
                        break;
                    }
                    break;
                case 3659724:
                    if (lowerCase.equals("wtap")) {
                        n = 1;
                        break;
                    }
                    break;
                case -995865464:
                    if (lowerCase.equals("packet")) {
                        n = 2;
                        break;
                    }
                    break;
            }
            switch (n) {
                case 0:
                    if (SuperKnockback.mc.thePlayer.isSprinting()) {
                        SuperKnockback.mc.thePlayer.setSprinting(true);
                    }
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                case 1:
                    if (SuperKnockback.mc.thePlayer.isSprinting()) {
                        SuperKnockback.mc.thePlayer.setSprinting(false);
                    }
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                case 2:
                    if (SuperKnockback.mc.thePlayer.isSprinting()) {
                        SuperKnockback.mc.thePlayer.setSprinting(true);
                    }
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKnockback.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
            }
            this.timerUtils.reset();
        }
    }
}
