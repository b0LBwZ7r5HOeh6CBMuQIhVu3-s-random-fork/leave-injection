package com.test.mod.command;

import java.util.Iterator;
import net.minecraft.util.EnumChatFormatting;
import java.util.Arrays;
import com.test.mod.command.commands.Toggle;
import com.test.mod.command.commands.Help;
import com.test.mod.command.commands.Bind;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;

public class CommandManager
{
    public NetHandlerPlayClient sendQueue;
    private static CommandManager me;
    private List<Command> commands;
    private String prefix;
    
    public CommandManager() {
        super();
        this.commands = new ArrayList<Command>();
        this.prefix = ".";
        this.add(new Bind());
        this.add(new Help());
        this.add(new Toggle());
    }
    
    static {
        CommandManager.me = new CommandManager();
    }
    
    public void add(final Command command) {
        this.commands.add(command);
    }
    
    public static CommandManager get() {
        return CommandManager.me;
    }
    
    public boolean execute(String text) {
        if (!text.startsWith(this.prefix)) {
            return false;
        }
        text = text.substring(1);
        final String[] arguments = text.split(" ");
        final String ranCmd = arguments[0];
        for (final Command cmd : this.commands) {
            if (cmd.getName().equalsIgnoreCase(arguments[0])) {
                final String[] args = (String[])Arrays.<String>copyOfRange(arguments, 1, arguments.length);
                final String[] args2 = text.split(" ");
                cmd.execute(args);
                return true;
            }
        }
        Command.msg("The command" + EnumChatFormatting.AQUA + ranCmd + EnumChatFormatting.GREEN + " has not been found!");
        return false;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
}
