package com.test.mod.module.modules.combat;

import com.test.mod.Utils.Side;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import com.test.mod.Utils.ChatUtils;
import net.minecraft.network.play.server.S0BPacketAnimation;
import com.test.mod.Utils.Connection;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals extends Module
{
    public ModeSetting mode;
    private IntegerSetting hurtTimeValue;
    private IntegerSetting delayValue;
    public static EnableSetting debug;
    TimerUtils timerUtils;
    int target1;
    
    public Criticals() {
        super("Criticals", 0, ModuleType.Combat, false);
        this.mode = new ModeSetting("Mode", "Visual", Arrays.<String>asList("AutoJump", "Packet", "NewPacket", "NcpPacket", "Hop", "TPHop", "Jump", "LowJump", "Visual"), this);
        this.hurtTimeValue = new IntegerSetting("HurtTime", 0.0, 0.0, 0.0, 1);
        this.delayValue = new IntegerSetting("Delay", 0.0, 0.0, 0.0, 1);
        this.timerUtils = new TimerUtils();
        this.target1 = 0;
        this.add(this.mode, this.hurtTimeValue, this.delayValue, Criticals.debug);
    }
    
    static {
        Criticals.debug = new EnableSetting("DeBug", true);
    }
    
    @Override
    public boolean onPacket(final Object packet, final Side side) {
        if (Criticals.debug.getEnable() && packet instanceof S0BPacketAnimation && ((S0BPacketAnimation)packet).getAnimationType() == 4 && ((S0BPacketAnimation)packet).getEntityID() == this.target1) {
            ChatUtils.message("TriggerCritical");
        }
        return true;
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
            this.target1 = event.target.getEntityId();
            if (!Criticals.mc.thePlayer.onGround || Criticals.mc.thePlayer.isOnLadder() || Criticals.mc.thePlayer.isInWater() || Criticals.mc.thePlayer.isInLava() || Criticals.mc.thePlayer.ridingEntity != null || ((EntityLivingBase)event.target).hurtTime > this.hurtTimeValue.getCurrent() || !this.timerUtils.isDelay((long)this.delayValue.getCurrent())) {
                return;
            }
            final double x = Criticals.mc.thePlayer.posX;
            final double y = Criticals.mc.thePlayer.posY;
            final double z = Criticals.mc.thePlayer.posZ;
            final String lowerCase = this.mode.getCurrent().toLowerCase();
            int n = -1;
            switch (lowerCase.hashCode()) {
                case 1439392349:
                    if (lowerCase.equals("autojump")) {
                        n = 0;
                        break;
                    }
                    break;
                case -995865464:
                    if (lowerCase.equals("packet")) {
                        n = 1;
                        break;
                    }
                    break;
                case 216546856:
                    if (lowerCase.equals("newpacket")) {
                        n = 2;
                        break;
                    }
                    break;
                case -891664989:
                    if (lowerCase.equals("ncppacket")) {
                        n = 3;
                        break;
                    }
                    break;
                case 103497:
                    if (lowerCase.equals("hop")) {
                        n = 4;
                        break;
                    }
                    break;
                case 110568525:
                    if (lowerCase.equals("tphop")) {
                        n = 5;
                        break;
                    }
                    break;
                case 3273774:
                    if (lowerCase.equals("jump")) {
                        n = 6;
                        break;
                    }
                    break;
                case 357158274:
                    if (lowerCase.equals("lowjump")) {
                        n = 7;
                        break;
                    }
                    break;
                case -816216256:
                    if (lowerCase.equals("visual")) {
                        n = 8;
                        break;
                    }
                    break;
            }
            switch (n) {
                case 0:
                    Criticals.mc.thePlayer.jump();
                case 1:
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0, z, true));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.035941369E-314, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                case 2:
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.4438856165E-314, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.5915002246E-314, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 4.11137107E-315, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.5915002246E-314, z, false));
                case 3:
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.612716801E-314, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1496582884E-314, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.7811120855E-314, z, false));
                case 4:
                    Criticals.mc.thePlayer.motionY = 1.273197475E-314;
                    Criticals.mc.thePlayer.fallDistance = 0.1f;
                    Criticals.mc.thePlayer.onGround = false;
                case 5:
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.941588215E-315, z, false));
                    Criticals.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.941588215E-315, z, false));
                    Criticals.mc.thePlayer.setPosition(x, y + 5.941588215E-315, z);
                case 6:
                    Criticals.mc.thePlayer.motionY = 1.4429571377E-314;
                case 7:
                    Criticals.mc.thePlayer.motionY = 2.54639495E-315;
                case 8:
                    Criticals.mc.thePlayer.onCriticalHit(event.target);
                    Criticals.mc.thePlayer.onEnchantmentCritical(event.target);
                    break;
            }
            this.timerUtils.reset();
        }
    }
}
