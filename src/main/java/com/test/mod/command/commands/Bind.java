package com.test.mod.command.commands;

import java.util.Iterator;
import com.test.mod.Utils.ChatUtils;
import org.lwjgl.input.Keyboard;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.command.Command;

public class Bind extends Command
{
    public Bind() {
        super();
    }
    
    @Override
    public String getName() {
        return "bind";
    }
    
    @Override
    public void execute(final String[] args) {
        try {
            if (args.length == 0) {
                Command.msg(this.getAll());
            }
            else if (args.length == 2) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    if (module.getName().equalsIgnoreCase(args[0])) {
                        module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                        ChatUtils.message(module.getName() + " key changed to §9" + Keyboard.getKeyName(module.getKey()));
                    }
                }
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDesc() {
        return "Sets binds for modules.";
    }
    
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
    
    @Override
    public String getSyntax() {
        return ".bind <module> <key>";
    }
}
