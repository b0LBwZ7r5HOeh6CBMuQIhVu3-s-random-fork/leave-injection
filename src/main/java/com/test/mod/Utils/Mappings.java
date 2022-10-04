package com.test.mod.Utils;

import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class Mappings
{
    public static String timer;
    public static String anti;
    public static String isInWeb;
    public static String registerReloadListener;
    public static String session;
    public static String yaw;
    public static String pitch;
    public static String rightClickDelayTimer;
    public static String getPlayerInfo;
    public static String playerTextures;
    public static String currentGameType;
    public static String connection;
    public static String blockHitDelay;
    public static String curBlockDamageMP;
    public static String isHittingBlock;
    public static String onUpdateWalkingPlayer;
    public static final Field delayTimer;
    public static final Field running;
    public static final Field pressed;
    public static final Field theShaderGroup;
    public static final Field listShaders;
    
    public Mappings() {
        super();
    }
    
    static {
        Mappings.timer = (isMCP() ? "timer" : "timer");
        Mappings.anti = (isMCP() ? "MovementInput" : "movementInput");
        Mappings.isInWeb = (isMCP() ? "isInWeb" : "isInWeb");
        Mappings.registerReloadListener = (isMCP() ? "registerReloadListener" : "registerReloadListener");
        Mappings.session = (isNotObfuscated() ? "session" : "session");
        Mappings.yaw = (isNotObfuscated() ? "yaw" : "yaw");
        Mappings.pitch = (isNotObfuscated() ? "pitch" : "pitch");
        Mappings.rightClickDelayTimer = (isNotObfuscated() ? "rightClickDelayTimer" : "rightClickDelayTimer");
        Mappings.getPlayerInfo = (isNotObfuscated() ? "getPlayerInfo" : "getPlayerInfo");
        Mappings.playerTextures = (isNotObfuscated() ? "playerTextures" : "field_187107_a");
        Mappings.currentGameType = (isNotObfuscated() ? "currentGameType" : "currentGameType");
        Mappings.connection = (isNotObfuscated() ? "connection" : "netClientHandler");
        Mappings.blockHitDelay = (isNotObfuscated() ? "blockHitDelay" : "blockHitDelay");
        Mappings.curBlockDamageMP = (isNotObfuscated() ? "curBlockDamageMP" : "curBlockDamageMP");
        Mappings.isHittingBlock = (isNotObfuscated() ? "isHittingBlock" : "isHittingBlock");
        Mappings.onUpdateWalkingPlayer = (isNotObfuscated() ? "onUpdateWalkingPlayer" : "onUpdateWalkingPlayer");
        delayTimer = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "rightClickDelayTimer", "rightClickDelayTimer" });
        running = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "running", "running" });
        pressed = ReflectionHelper.findField((Class)KeyBinding.class, new String[] { "pressed", "pressed" });
        theShaderGroup = ReflectionHelper.findField((Class)EntityRenderer.class, new String[] { "theShaderGroup", "theShaderGroup" });
        listShaders = ReflectionHelper.findField((Class)ShaderGroup.class, new String[] { "listShaders", "listShaders" });
    }
    
    private static boolean isMCP() {
        try {
            return ReflectionHelper.findField((Class)Minecraft.class, new String[] { "theMinecraft" }) != null;
        }
        catch (Exception var1) {
            return false;
        }
    }
    
    public static boolean isNotObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
