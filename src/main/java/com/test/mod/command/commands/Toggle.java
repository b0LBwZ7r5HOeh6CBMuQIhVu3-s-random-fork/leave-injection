package com.test.mod.command.commands;

import com.test.mod.module.Module;
import net.minecraft.util.EnumChatFormatting;
import com.test.mod.Client;
import com.test.mod.command.Command;

public class Toggle extends Command
{
    public Toggle() {
        super();
    }
    
    @Override
    public String getName() {
        return "t";
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            Command.msg(this.getAll());
        }
        else {
            final String module = args[0];
            final Module mod = Client.instance.moduleManager.getModule(module);
            if (mod == null) {
                Command.msg(EnumChatFormatting.LIGHT_PURPLE + "The requested module was not found!");
            }
            else {
                Client.instance.moduleManager.getModule(module).toggleModule(true);
                Command.msg(String.format(EnumChatFormatting.DARK_AQUA + "%s " + EnumChatFormatting.AQUA + "has been %s", Client.instance.moduleManager.getModule(module).getName(), Client.instance.moduleManager.getModule(module).isState() ? (EnumChatFormatting.GREEN + "enabled") : (EnumChatFormatting.LIGHT_PURPLE + "disabled.")));
            }
        }
    }
    
    @Override
    public String getDesc() {
        return "Toggles modules.";
    }
    
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
    
    @Override
    public String getSyntax() {
        return ".t";
    }
}
