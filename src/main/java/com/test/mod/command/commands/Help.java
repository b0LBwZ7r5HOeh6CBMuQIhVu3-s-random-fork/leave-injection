package com.test.mod.command.commands;

import java.util.Iterator;
import net.minecraft.util.EnumChatFormatting;
import com.test.mod.Client;
import com.test.mod.command.Command;

public class Help extends Command
{
    public Help() {
        super();
    }
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            for (final Command c : Client.instance.commandManager.getCommands()) {
                Command.msg(c.getSyntax() + " " + EnumChatFormatting.AQUA + "- " + c.getDesc());
            }
        }
    }
    
    @Override
    public String getDesc() {
        return "Gives you the syntax of all commands and what they do.";
    }
    
    @Override
    public String getSyntax() {
        return ".help";
    }
}
