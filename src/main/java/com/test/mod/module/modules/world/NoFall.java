package com.test.mod.module.modules.world;

import com.test.mod.Utils.Side;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import com.test.mod.Utils.BlockUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.lang.reflect.Field;
import net.minecraft.network.play.client.C03PacketPlayer;
import com.test.mod.Utils.Connection;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.entity.Entity;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import java.lang.reflect.Method;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import com.test.mod.Client;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;

public class NoFall extends Module
{
    private boolean aac5doFlag;
    private boolean aac5Check;
    private int aac5Timer;
    private boolean isDmgFalling;
    private final ModeSetting Mode;
    private EnableSetting VOID;
    private IntegerSetting delay;

    public static Object invoke(Object target, String methodName, String obfName, Class[] methodArgs, Object[] args) {
        Class clazz = target.getClass();
        Method method = ReflectionHelper.findMethod(clazz, target, Client.isObfuscate ? new String[]{obfName} : new String[]{methodName}, methodArgs);
        method.setAccessible(true);

        try {
            return method.invoke(target, args);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
    
    public NoFall() {
        super("NoFall", 0, ModuleType.World, false);
        this.aac5doFlag = false;
        this.aac5Check = false;
        this.aac5Timer = 0;
        this.isDmgFalling = false;
        this.Mode = new ModeSetting("Mode", "AAC", Arrays.<String>asList("AAC", "Simple", "AAC2", "AAC5", "AAC5.0.4", "MLG"), this);
        this.VOID = new EnableSetting("VOID", true);
        this.delay = new IntegerSetting("MLGDelay", 0.0, 1.0, 0.0, 0);
        this.getSettings().add(this.Mode);
        this.getSettings().add(this.VOID);
        this.getSettings().add(this.delay);
    }

    
    private boolean isBlockUnderJudge() {
        for (int offset = 0; offset < NoFall.mc.thePlayer.posY + NoFall.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = NoFall.mc.thePlayer.getEntityBoundingBox().offset(0.0, (double)(-offset), 0.0);
            if (!NoFall.mc.theWorld.getCollidingBoundingBoxes((Entity)NoFall.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private void swapToWaterBucket(final int blockSlot) {
        NoFall.mc.thePlayer.inventory.currentItem = blockSlot;
        NoFall.mc.thePlayer.sendQueue.getNetworkManager().sendPacket((Packet)new C09PacketHeldItemChange(blockSlot));
    }
    
    private int getSlotWaterBucket() {
        for (int i = 0; i < 8; ++i) {
            if (NoFall.mc.thePlayer.inventory.mainInventory[i] != null && NoFall.mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater")) {
                return i;
            }
        }
        return -1;
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("MLG")) {
            if (NoFall.mc.thePlayer.fallDistance > 4.0f && this.getSlotWaterBucket() != -1 && this.isMLGNeeded()) {
                NoFall.mc.thePlayer.rotationPitch = 90.0f;
                this.swapToWaterBucket(this.getSlotWaterBucket());
            }
            if (NoFall.mc.thePlayer.fallDistance > 4.0f && this.isMLGNeeded() && !NoFall.mc.thePlayer.isOnLadder() && NoFall.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1.0, NoFall.mc.thePlayer.posZ);
                this.placeWater(pos, EnumFacing.UP);
                if (NoFall.mc.thePlayer.getHeldItem().getItem() == Items.bucket) {
                    Thread thr = new Thread(() -> {
                        try {
                            Thread.sleep((long)this.delay.getCurrent());
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        NoFall.rightClickMouse(mc);
                    });
                    thr.start();
                }
                NoFall.mc.thePlayer.fallDistance = 0.0f;
            }
        }
    }
    
    @Override
    public boolean onPacket(final Object packet, Side side) {
        if (side == Side.OUT && packet instanceof C03PacketPlayer) {
            final C03PacketPlayer p = (C03PacketPlayer)packet;
            if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
                final Field field = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "onGround", "onGround" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setBoolean(p, true);
                }
                catch (Exception ex) {}
            }
            else if (this.Mode.getCurrent().equalsIgnoreCase("AAC5.0.4") && this.isDmgFalling) {
                final Field field = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "onGround", "onGround" });
                final Field fx = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "x", "x" });
                final Field fy = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "y", "y" });
                final Field fz = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "z", "z" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!fx.isAccessible()) {
                        fx.setAccessible(true);
                    }
                    if (!fy.isAccessible()) {
                        fy.setAccessible(true);
                    }
                    if (!fz.isAccessible()) {
                        fz.setAccessible(true);
                    }
                    if (field.getBoolean(p) && NoFall.mc.thePlayer.onGround) {
                        this.isDmgFalling = false;
                        field.setBoolean(p, true);
                        NoFall.mc.thePlayer.onGround = false;
                        final double y = fy.getDouble(p);
                        final double x = fx.getDouble(p);
                        final double z = fx.getDouble(p);
                        fy.setDouble(p, y + 1.0);
                        this.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 7.605232915E-315, z, false));
                        this.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.0, z, true));
                    }
                }
                catch (Exception ex2) {}
            }
        }
        return true;
    }
    
    @Override
    public void onEnable() {
        this.aac5Check = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.isDmgFalling = false;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.aac5Check = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.isDmgFalling = false;
        super.onDisable();
    }
    
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("Simple") && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            this.sendPacket((Packet)new C03PacketPlayer(true));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC2") && NoFall.mc.thePlayer.ticksExisted == 1 && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            final C03PacketPlayer.C04PacketPlayerPosition p = new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, 0.0, NoFall.mc.thePlayer.posZ, true);
            NoFall.mc.thePlayer.sendQueue.addToSendQueue((Packet)p);
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC5")) {
            double offsetYs = 0.0;
            this.aac5Check = false;
            while (NoFall.mc.thePlayer.motionY - 0.0 < offsetYs) {
                final BlockPos blockPos = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + offsetYs, NoFall.mc.thePlayer.posZ);
                final Block block = BlockUtils.getBlock(blockPos);
                final AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox((World)NoFall.mc.theWorld, blockPos, BlockUtils.getState(blockPos));
                if (axisAlignedBB != null) {
                    offsetYs = 4.24399158E-315;
                    this.aac5Check = true;
                }
                offsetYs -= 0.0;
            }
            if (NoFall.mc.thePlayer.onGround) {
                NoFall.mc.thePlayer.fallDistance = -2.0f;
                this.aac5Check = false;
            }
            if (this.aac5Timer > 0) {
                --this.aac5Timer;
            }
            if (this.aac5Check && NoFall.mc.thePlayer.fallDistance > 0.0 && !NoFall.mc.thePlayer.onGround) {
                this.aac5doFlag = true;
                this.aac5Timer = 18;
            }
            else if (this.aac5Timer < 2) {
                this.aac5doFlag = false;
            }
            if (this.aac5doFlag) {
                if (NoFall.mc.thePlayer.onGround) {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 0.0, NoFall.mc.thePlayer.posZ, true));
                }
                else {
                    NoFall.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 1.4429571377E-314, NoFall.mc.thePlayer.posZ, true));
                }
            }
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC5.0.4") && NoFall.mc.thePlayer.fallDistance > 3.0f) {
            this.isDmgFalling = true;
        }
    }
    
    private boolean isMLGNeeded() {
        if (NoFall.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE || NoFall.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR || NoFall.mc.thePlayer.capabilities.isFlying || NoFall.mc.thePlayer.capabilities.allowFlying) {
            return false;
        }
        final Minecraft mc = NoFall.mc;
        for (double y = Minecraft.getMinecraft().thePlayer.posY; y > 0.0; --y) {
            final Minecraft mc2 = NoFall.mc;
            final double posX = Minecraft.getMinecraft().thePlayer.posX;
            final double n = y;
            final Minecraft mc3 = NoFall.mc;
            final Block block = BlockUtils.getBlock(new BlockPos(posX, n, Minecraft.getMinecraft().thePlayer.posZ));
            if (block.getMaterial() == Material.water) {
                return false;
            }
            if (block.getMaterial() != Material.air) {
                return true;
            }
            if (y < 0.0) {
                break;
            }
        }
        return true;
    }
    
    private void placeWater(final BlockPos pos, final EnumFacing facing) {
        final ItemStack heldItem = NoFall.mc.thePlayer.inventory.getCurrentItem();
        NoFall.mc.playerController.onPlayerRightClick(NoFall.mc.thePlayer, NoFall.mc.theWorld, NoFall.mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3(pos.getX() + 0.0, pos.getY() + 1.0, pos.getZ() + 0.0));
        if (heldItem != null) {
            NoFall.mc.playerController.sendUseItem((EntityPlayer)NoFall.mc.thePlayer, (World)NoFall.mc.theWorld, heldItem);
            NoFall.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
        }
    }
    
    public static void rightClickMouse(final Minecraft mc) {
        invoke(mc, "rightClickMouse", "rightClickMouse", new Class[0], new Object[0]);
    }
    
    public void sendPacket(final Packet packet) {
        NoFall.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
