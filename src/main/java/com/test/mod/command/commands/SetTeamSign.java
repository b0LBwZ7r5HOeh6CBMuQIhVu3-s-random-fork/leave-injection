package com.test.mod.command.commands;

import com.test.mod.Utils.ChatUtils;
import com.test.mod.command.Command;

public class SetTeamSign extends Command
{
    public static String teamsign;
    
    public SetTeamSign() {
        super();
    }
    
    @Override
    public String getName() {
        return "setteamsign";
    }
    
    @Override
    public void execute(final String[] args) {
        SetTeamSign.teamsign = args[0];
        ChatUtils.message("New TeamSign: " + SetTeamSign.teamsign);
    }
    
    @Override
    public String getDesc() {
        return "set team sign for Team TabList Mode";
    }
    
    @Override
    public String getSyntax() {
        return ".setteamsign <name>";
    }
}
