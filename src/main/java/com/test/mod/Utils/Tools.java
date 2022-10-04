package com.test.mod.Utils;

import net.java.games.input.Mouse;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import java.nio.ByteBuffer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class Tools
{
    private static Field timerField;
    private static Field mouseButton;
    private static Field mouseButtonState;
    private static Field mouseButtons;
    private static Minecraft mc;
    
    public Tools() {
        super();
    }
    
    static {
        Tools.timerField = null;
        Tools.mouseButton = null;
        Tools.mouseButtonState = null;
        Tools.mouseButtons = null;
        Tools.mc = Minecraft.getMinecraft();
    }
    
    public static boolean nullCheck() {
        return Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null;
    }
    
    public static boolean isBlocking() {
        return Tools.mc.thePlayer.isUsingItem() || Tools.mc.thePlayer.isBlocking();
    }
    
    public static void setMouseButtonState(final int mouseButton, final boolean held) {
        if (Tools.mouseButton != null && Tools.mouseButtonState != null && Tools.mouseButtons != null) {
            final MouseEvent m = new MouseEvent();
            try {
                Tools.mouseButton.setAccessible(true);
                Tools.mouseButton.set(m, mouseButton);
                Tools.mouseButtonState.setAccessible(true);
                Tools.mouseButtonState.set(m, held);
                MinecraftForge.EVENT_BUS.post((Event)m);
                Tools.mouseButtons.setAccessible(true);
                final ByteBuffer bf = (ByteBuffer)Tools.mouseButtons.get(null);
                Tools.mouseButtons.setAccessible(false);
                bf.put(mouseButton, (byte)(held ? 1 : 0));
            }
            catch (IllegalAccessException ex) {}
        }
    }
    
    public static boolean currentScreenMinecraft() {
        return Tools.mc.currentScreen == null;
    }
    
    public static int getCurrentPlayerSlot() {
        return Tools.mc.thePlayer.inventory.currentItem;
    }
    
    public static boolean isPlayerHoldingWeapon() {
        if (Tools.mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        }
        final Item item = Tools.mc.thePlayer.getCurrentEquippedItem().getItem();
        return item instanceof ItemSword || item instanceof ItemAxe;
    }
    
    public static String getFontPath() {
        return getLocalPath() + "\\Fair\\Fonts";
    }
    
    public static String getConfigPath() {
        return getLocalPath() + "\\Leave_Lite";
    }
    
    public static boolean isPlayerInGame() {
        return Tools.mc.thePlayer != null && Tools.mc.theWorld != null;
    }
    
    public static boolean isMoving() {
        return !Tools.mc.thePlayer.isCollidedHorizontally && !Tools.mc.thePlayer.isSneaking() && (Tools.mc.thePlayer.movementInput.moveForward != 0.0f || Tools.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isOnGround(final double height) {
        return !Tools.mc.theWorld.getCollidingBoundingBoxes((Entity)Tools.mc.thePlayer, Tools.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static void hotkeyToSlot(final int slot) {
        if (!isPlayerInGame()) {
            return;
        }
        Tools.mc.thePlayer.inventory.currentItem = slot;
    }
    
    public static void su() {
        try {
            Tools.timerField = Minecraft.class.getDeclaredField("timer");
        }
        catch (Exception var4) {
            try {
                Tools.timerField = Minecraft.class.getDeclaredField("timer");
            }
            catch (Exception ex) {}
        }
        if (Tools.timerField != null) {
            Tools.timerField.setAccessible(true);
        }
        try {
            Tools.mouseButton = MouseEvent.class.getDeclaredField("button");
            Tools.mouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
            Tools.mouseButtons = Mouse.class.getDeclaredField("buttons");
        }
        catch (Exception ex2) {}
    }
    
    public static boolean isHyp() {
        if (!isPlayerInGame()) {
            return false;
        }
        try {
            return !Tools.mc.isSingleplayer() && Tools.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }
    
    public static boolean isHycraft() {
        if (!isPlayerInGame()) {
            return false;
        }
        try {
            return !Tools.mc.isSingleplayer() && Tools.mc.getCurrentServerData().serverIP.toLowerCase().contains("hycraft.com");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }
    
    public static String getLocalPath() {
        return System.getProperty("user.dir");
    }
    
    public static String getLogoPath() {
        return getLocalPath() + "\\Fair\\Logos";
    }
    
    public static boolean isSingleplayer() {
        return Tools.mc.isSingleplayer();
    }
}
