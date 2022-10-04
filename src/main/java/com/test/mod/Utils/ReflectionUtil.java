package com.test.mod.Utils;

import java.lang.reflect.Method;
import com.test.mod.Client;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class ReflectionUtil
{
    public static final Field delayTimer;
    public static final Field running;
    public static final Field pressed;
    public static final Field theShaderGroup;
    public static final Field listShaders;
    
    public ReflectionUtil() {
        super();
    }
    
    static {
        delayTimer = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "rightClickDelayTimer", "rightClickDelayTimer" });
        running = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "running", "running" });
        pressed = ReflectionHelper.findField((Class)KeyBinding.class, new String[] { "pressed", "pressed" });
        theShaderGroup = ReflectionHelper.findField((Class)EntityRenderer.class, new String[] { "theShaderGroup", "theShaderGroup" });
        listShaders = ReflectionHelper.findField((Class)ShaderGroup.class, new String[] { "listShaders", "listShaders" });
        ReflectionUtil.delayTimer.setAccessible(true);
        ReflectionUtil.running.setAccessible(true);
        ReflectionUtil.pressed.setAccessible(true);
        ReflectionUtil.theShaderGroup.setAccessible(true);
        ReflectionUtil.listShaders.setAccessible(true);
    }
    
    public static void rightClickMouse() {
        try {
            final String s = Client.isObfuscate ? "rightClickMouse" : "rightClickMouse";
            final Minecraft mc = Minecraft.getMinecraft();
            final Class c = mc.getClass();
            final Method m = c.getDeclaredMethod(s, (Class[])new Class[0]);
            m.setAccessible(true);
            m.invoke(mc, new Object[0]);
        }
        catch (Exception ex) {}
    }
}
