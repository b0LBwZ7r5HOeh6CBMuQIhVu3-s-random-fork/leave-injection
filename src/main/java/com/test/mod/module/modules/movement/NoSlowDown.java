package com.test.mod.module.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.Utils.NoSlowDownUtil;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.Utils.TimerUtils;
import net.minecraft.util.MovementInput;
import com.test.mod.module.Module;

public class NoSlowDown extends Module
{
    MovementInput origmi;
    TimerUtils timer;
    private final ModeSetting Mode;
    private IntegerSetting percent;
    
    public NoSlowDown() {
        super("NoSlowDown", 0, ModuleType.Movement, false);
        this.timer = new TimerUtils();
        this.Mode = new ModeSetting("Mode", "Vanilla", Arrays.<String>asList("Basic", "Vanilla", "NCP", "AAC5", "AAC", "Custom"), this);
        this.percent = new IntegerSetting("percent", 0.0, 0.0, 0.0, 1);
        this.getSettings().add(this.Mode);
    }
    
    @Override
    public void onEnable() {
        this.origmi = NoSlowDown.mc.thePlayer.movementInput;
        if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
            NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.Mode.getCurrent().equalsIgnoreCase("Basic")) {
            if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
                NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
            }
            final NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
            move.setNSD(false);
        }
        NoSlowDown.mc.thePlayer.movementInput = this.origmi;
        super.onDisable();
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
            this.origmi = NoSlowDown.mc.thePlayer.movementInput;
            NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
        }
        if ((NoSlowDown.mc.thePlayer.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) || (NoSlowDown.mc.gameSettings.keyBindSneak.isKeyDown() && NoSlowDown.mc.gameSettings.keyBindUseItem.isKeyDown())) {
            final NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
            move.setNSD(true);
        }
        if (NoSlowDown.mc.thePlayer.isUsingItem() && isMoving() && isOnGround(1.4429571377E-314) && this.Mode.getCurrent().equalsIgnoreCase("NCP")) {
            final double x = NoSlowDown.mc.thePlayer.posX;
            final double y = NoSlowDown.mc.thePlayer.posY;
            final double z = NoSlowDown.mc.thePlayer.posZ;
            NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("Custom")) {
            try {
                if (NoSlowDown.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
                    final EntityPlayerSP thePlayer = NoSlowDown.mc.thePlayer;
                    thePlayer.motionX *= 0.0;
                    final EntityPlayerSP thePlayer2 = NoSlowDown.mc.thePlayer;
                    thePlayer2.motionZ *= 0.0;
                }
                if (NoSlowDown.mc.thePlayer.isUsingItem()) {
                    final EntityPlayerSP thePlayer3 = NoSlowDown.mc.thePlayer;
                    thePlayer3.motionX *= this.percent.getCurrent() / 0.0;
                    final EntityPlayerSP thePlayer4 = NoSlowDown.mc.thePlayer;
                    thePlayer4.motionZ *= this.percent.getCurrent() / 0.0;
                }
            }
            catch (NullPointerException ex) {}
        }
        if (NoSlowDown.mc.thePlayer.isUsingItem() && isMoving() && isOnGround(1.4429571377E-314) && this.Mode.getCurrent().equalsIgnoreCase("NCP")) {
            final double x = NoSlowDown.mc.thePlayer.posX;
            final double y = NoSlowDown.mc.thePlayer.posY;
            final double z = NoSlowDown.mc.thePlayer.posZ;
            NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("Basic")) {
            if ((NoSlowDown.mc.thePlayer.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) || (NoSlowDown.mc.gameSettings.keyBindSneak.isKeyDown() && NoSlowDown.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
                    NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
                }
                final NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
                move.setNSD(true);
                if (event.phase == TickEvent.Phase.START) {
                    if (NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && isMoving()) {
                        this.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                }
                else if (event.phase == TickEvent.Phase.END && NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && isMoving()) {
                    this.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
                }
            }
            if (this.Mode.getCurrent().equalsIgnoreCase("AAC") && NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && isMoving()) {
                if (event.phase == TickEvent.Phase.START) {
                    if (NoSlowDown.mc.thePlayer.onGround || isOnGround(0.0)) {
                        this.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                }
                else if (event.phase == TickEvent.Phase.END && this.timer.isDelayComplete(0.0)) {
                    this.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
                    this.timer.reset();
                }
            }
        }
    }
    @SubscribeEvent
    public void onPlayerTicks(final TickEvent.PlayerTickEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
            if (NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && isMoving()) {
                this.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
            }
        }
        else if (this.Mode.getCurrent().equalsIgnoreCase("Hypixel")) {
            if (NoSlowDown.mc.thePlayer.isBlocking() && isMoving() && isOnGround(1.4429571377E-314)) {
                NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        else if (this.Mode.getCurrent().equalsIgnoreCase("AAC5") && (NoSlowDown.mc.thePlayer.isUsingItem() || NoSlowDown.mc.thePlayer.isBlocking())) {
            final BlockPos pos = new BlockPos(-1, -1, -1);
            this.sendPacket((Packet)new C08PacketPlayerBlockPlacement(pos, 255, NoSlowDown.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
        }
    }
    
    public static boolean isMoving() {
        return !NoSlowDown.mc.thePlayer.isCollidedHorizontally && !NoSlowDown.mc.thePlayer.isSneaking() && (NoSlowDown.mc.thePlayer.movementInput.moveForward != 0.0f || NoSlowDown.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isOnGround(final double height) {
        return !NoSlowDown.mc.theWorld.getCollidingBoundingBoxes((Entity)NoSlowDown.mc.thePlayer, NoSlowDown.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public void sendPacket(final Packet packet) {
        NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
