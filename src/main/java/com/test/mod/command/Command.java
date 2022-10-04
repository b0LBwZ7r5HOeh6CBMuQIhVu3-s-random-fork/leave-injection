package com.test.mod.command;

import com.test.mod.Utils.ChatUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public abstract class Command
{
    public Command() {
        super();
    }
    
    public abstract String getName();
    
    public abstract void execute(final String[] p0);
    
    public abstract String getDesc();
    
    public void normal(final String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(msg));
    }
    
    public static void msg(final String msg) {
        ChatUtils.report(msg);
    }
    
    public abstract String getSyntax();
    
    public String getName1() {
        return this.getName();
    }
    
    public String getHelp() {
        return null;
    }
    
    public String getCmd() {
        return this.getName();
    }
}
