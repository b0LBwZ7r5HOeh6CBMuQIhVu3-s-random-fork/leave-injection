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
        delayTimer = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "rightClickDelayTimer", "field_71467_ac" });
        running = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "running", "field_72619_a" });
        pressed = ReflectionHelper.findField((Class)KeyBinding.class, new String[] { "pressed", "field_74513_e" });
        theShaderGroup = ReflectionHelper.findField((Class)EntityRenderer.class, new String[] { "theShaderGroup", "field_147707_d" });
        listShaders = ReflectionHelper.findField((Class)ShaderGroup.class, new String[] { "listShaders", "field_148031_d" });
        ReflectionUtil.delayTimer.setAccessible(true);
        ReflectionUtil.running.setAccessible(true);
        ReflectionUtil.pressed.setAccessible(true);
        ReflectionUtil.theShaderGroup.setAccessible(true);
        ReflectionUtil.listShaders.setAccessible(true);
    }
    
    public static void rightClickMouse() {
        try {
            final String s = Client.isObfuscate ? "func_147121_ag" : "rightClickMouse";
            final Minecraft mc = Minecraft.getMinecraft();
            final Class c = mc.getClass();
            final Method m = c.getDeclaredMethod(s, (Class[])new Class[0]);
            m.setAccessible(true);
            m.invoke(mc, new Object[0]);
        }
        catch (Exception ex) {}
    }
}
