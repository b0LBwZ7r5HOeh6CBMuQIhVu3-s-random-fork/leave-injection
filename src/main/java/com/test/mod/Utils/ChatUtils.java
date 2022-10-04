package com.test.mod.Utils;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils
{
    public ChatUtils() {
        super();
    }
    
    public static void message(final Object message) {
        component(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[LeaveOld]§7" + message));
    }
    
    public static void error(final Object message) {
        message("§8[§4ERROR§8]§c " + message);
    }
    
    public static void warning(final Object message) {
        message("§8[§eWARNING§8]§e " + message);
    }
    
    public static void component(final ChatComponentText component) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().ingameGUI.getChatGUI() == null) {
            return;
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("").appendSibling((IChatComponent)component));
    }
    
    public static void report(final String message) {
        message(EnumChatFormatting.GREEN + message);
    }
}
