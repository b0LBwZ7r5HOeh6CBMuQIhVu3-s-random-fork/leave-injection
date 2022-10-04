package com.test.mod.module.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import com.test.mod.Utils.Tools;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.nio.ByteBuffer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Mouse;
import net.minecraftforge.client.event.MouseEvent;
import com.test.mod.settings.Setting;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.IntegerSetting;
import java.lang.reflect.Field;
import java.util.Random;
import com.test.mod.module.Module;

public class AutoClicker extends Module
{
    private Random random;
    private long lastClick;
    private long hold;
    private double speed;
    private double holdLength;
    private double min;
    private double max;
    private boolean hasSelectedBlock;
    private static Field FmouseButton;
    private static Field FmouseButtons;
    private static Field FmouseButtonState;
    public boolean isDone;
    public int timer;
    private IntegerSetting maxcps;
    private IntegerSetting mincps;
    private double jitteryaw;
    private double jitterpitch;
    private EnableSetting jitter;
    private EnableSetting BlockHit;
    private EnableSetting BreakBlocks;
    private EnableSetting cpsdBypass;
    private long leftHold;
    private long rightHold;
    
    public AutoClicker() {
        super("AutoClicker", 0, ModuleType.Combat, false);
        this.isDone = true;
        this.maxcps = new IntegerSetting("MaxCPS", 0.0, 1.0, 0.0, 1);
        this.mincps = new IntegerSetting("MinCPS", 0.0, 1.0, 0.0, 1);
        this.jitteryaw = 0.0;
        this.jitterpitch = 0.0;
        this.jitter = new EnableSetting("Jitter", false);
        this.BlockHit = new EnableSetting("Block_Hit", false);
        this.BreakBlocks = new EnableSetting("Break_Blocks", true);
        this.cpsdBypass = new EnableSetting("Cps_Bypass", false);
        this.add(this.maxcps, this.mincps, this.jitter, this.BlockHit, this.BreakBlocks, this.cpsdBypass);
    }
    
    static {
        try {
            AutoClicker.FmouseButton = MouseEvent.class.getDeclaredField("button");
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            AutoClicker.FmouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            AutoClicker.FmouseButtons = Mouse.class.getDeclaredField("buttons");
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    private long getDelay() {
        return (long)((int)this.maxcps.getCurrent() + this.random.nextDouble() * ((int)this.mincps.getCurrent() - (int)this.maxcps.getCurrent()));
    }
    
    private boolean getIfSelectingBlock(boolean state) {
        final EntityPlayerSP playerSP;
        final MovingObjectPosition objectPosition;
        final World w;
        final BlockPos bpos;
        if (AutoClicker.mc.getRenderViewEntity() != null && (playerSP = AutoClicker.mc.thePlayer) != null && playerSP instanceof EntityPlayerSP && (objectPosition = AutoClicker.mc.getRenderViewEntity().rayTrace(4.24399158E-315, 1.0f)) != null && (w = AutoClicker.mc.thePlayer.worldObj) != null && objectPosition.hitVec != null && (bpos = new BlockPos(objectPosition.hitVec.xCoord, objectPosition.hitVec.yCoord, objectPosition.hitVec.zCoord)) != null) {
            final Material material = w.getBlockState(bpos).getBlock().getMaterial();
            if (w.getBlockState(bpos).getBlock() != null && material != null && objectPosition.typeOfHit != null && AutoClicker.mc.objectMouseOver != null) {
                if (AutoClicker.mc.objectMouseOver.entityHit != null) {
                    state = false;
                }
                else if (objectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                    state = true;
                }
                else if (objectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                    state = false;
                }
            }
        }
        return state;
    }
    
    public static void setMouseButtonState(final int mouseButton, final boolean state) {
        final MouseEvent e = new MouseEvent();
        AutoClicker.FmouseButton.setAccessible(true);
        try {
            AutoClicker.FmouseButton.set(e, mouseButton);
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        AutoClicker.FmouseButton.setAccessible(false);
        AutoClicker.FmouseButtonState.setAccessible(true);
        try {
            AutoClicker.FmouseButtonState.set(e, state);
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        AutoClicker.FmouseButtonState.setAccessible(false);
        MinecraftForge.EVENT_BUS.post((Event)e);
        try {
            AutoClicker.FmouseButtons.setAccessible(true);
            final ByteBuffer buffer = (ByteBuffer)AutoClicker.FmouseButtons.get(null);
            AutoClicker.FmouseButtons.setAccessible(false);
            buffer.put(mouseButton, (byte)(state ? 1 : 0));
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    @Override
    public void onEnable() {
        this.random = new Random();
        this.updateVals();
    }
    
    @Override
    public void onDisable() {
        this.isDone = true;
        super.onDisable();
    }
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.click();
    }
    
    private void updateVals() {
        try {
            final Field LF = Minecraft.class.getDeclaredField("leftClickCounter");
            LF.setAccessible(true);
            LF.set(Minecraft.getMinecraft(), 0);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        this.min = this.mincps.getCurrent();
        this.max = this.maxcps.getCurrent();
        if (this.min >= this.max) {
            this.max = this.min + 1.0;
        }
        this.speed = 1.0 / ThreadLocalRandom.current().nextDouble(this.min - 1.273197475E-314, this.max);
        this.holdLength = this.speed / ThreadLocalRandom.current().nextDouble(this.min, this.max);
        this.isDone = true;
        this.timer = 0;
    }
    
    private boolean canByp() {
        return AutoClicker.FmouseButtonState != null && AutoClicker.FmouseButton != null && AutoClicker.FmouseButtons != null && this.cpsdBypass.getEnable();
    }
    
    public void clickMouse() {
        AutoClicker.mc.thePlayer.swingItem();
        if (AutoClicker.mc.objectMouseOver != null) {
            switch (AutoClicker.mc.objectMouseOver.typeOfHit) {
                case ENTITY:
                    AutoClicker.mc.playerController.attackEntity((EntityPlayer)AutoClicker.mc.thePlayer, AutoClicker.mc.objectMouseOver.entityHit);
                    break;
                case BLOCK: {
                    final BlockPos blockpos = AutoClicker.mc.objectMouseOver.getBlockPos();
                    if (AutoClicker.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        AutoClicker.mc.playerController.clickBlock(blockpos, AutoClicker.mc.objectMouseOver.sideHit);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void click() {
        if (Tools.nullCheck()) {
            return;
        }
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (AutoClicker.mc.currentScreen == null) {
            if (this.BreakBlocks.getEnable() && this.getIfSelectingBlock(this.hasSelectedBlock)) {
                return;
            }
            final double speedLeft1 = 1.0 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.mincps.getCurrent() - 1.273197475E-314, this.maxcps.getCurrent());
            final double leftHoldLength = speedLeft1 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.mincps.getCurrent() - 5.941588215E-315, this.maxcps.getCurrent());
            if (Mouse.isButtonDown(0)) {
                if (this.jitter.getEnable()) {
                    final double a = 1.697596633E-314;
                    if (this.random.nextBoolean()) {
                        final EntityPlayerSP entityPlayer = AutoClicker.mc.thePlayer;
                        entityPlayer.rotationYaw += this.random.nextFloat() * a;
                    }
                    else {
                        final EntityPlayerSP entityPlayer = AutoClicker.mc.thePlayer;
                        entityPlayer.rotationYaw -= this.random.nextFloat() * a;
                    }
                    if (this.random.nextBoolean()) {
                        final EntityPlayerSP entityPlayer = AutoClicker.mc.thePlayer;
                        entityPlayer.rotationPitch += this.random.nextFloat() * a * 1.697596633E-314;
                    }
                    else {
                        final EntityPlayerSP entityPlayer = AutoClicker.mc.thePlayer;
                        entityPlayer.rotationPitch -= this.random.nextFloat() * a * 1.697596633E-314;
                    }
                }
                final double speedLeft2 = 1.0 / ThreadLocalRandom.current().nextDouble(this.mincps.getCurrent() - 1.273197475E-314, this.maxcps.getCurrent());
                if (System.currentTimeMillis() - this.lastClick > speedLeft2 * 0.0) {
                    this.lastClick = System.currentTimeMillis();
                    if (this.leftHold < this.lastClick) {
                        this.leftHold = this.lastClick;
                    }
                    final int key = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
                    KeyBinding.setKeyBindState(key, true);
                    KeyBinding.onTick(key);
                    Tools.setMouseButtonState(0, true);
                }
                else if (System.currentTimeMillis() - this.leftHold > leftHoldLength * 0.0) {
                    KeyBinding.setKeyBindState(AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode(), false);
                    Tools.setMouseButtonState(0, false);
                }
            }
        }
    }
    
    public void jitter(final Random rand) {
        if (rand.nextBoolean()) {
            if (rand.nextBoolean()) {
                final EntityPlayerSP thePlayer = AutoClicker.mc.thePlayer;
                thePlayer.rotationPitch -= (float)(rand.nextFloat() * 4.24399158E-315);
            }
            else {
                final EntityPlayerSP thePlayer2 = AutoClicker.mc.thePlayer;
                thePlayer2.rotationPitch += (float)(rand.nextFloat() * 4.24399158E-315);
            }
        }
        else if (rand.nextBoolean()) {
            final EntityPlayerSP thePlayer3 = AutoClicker.mc.thePlayer;
            thePlayer3.rotationYaw -= (float)(rand.nextFloat() * 4.24399158E-315);
        }
        else {
            final EntityPlayerSP thePlayer4 = AutoClicker.mc.thePlayer;
            thePlayer4.rotationYaw += (float)(rand.nextFloat() * 4.24399158E-315);
        }
    }
}
