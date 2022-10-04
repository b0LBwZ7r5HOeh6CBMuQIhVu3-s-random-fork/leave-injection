package com.test.mod.config.configs;

import java.util.Iterator;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Tools;
import com.test.mod.config.ConfigManager;

public class KeyBindConfig
{
    private static ConfigManager configManager;
    
    public KeyBindConfig() {
        super();
    }
    
    static {
        KeyBindConfig.configManager = new ConfigManager(Tools.getConfigPath(), "KeyBind.txt");
    }
    
    public static void loadKey() {
        try {
            for (final String s : KeyBindConfig.configManager.read()) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    final String name = s.split(":")[0];
                    final int key = Integer.parseInt(s.split(":")[1]);
                    if (module.getName().equalsIgnoreCase(name)) {
                        module.setKey(key);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveKey() {
        try {
            KeyBindConfig.configManager.clear();
            for (final Module module : Client.instance.moduleManager.getModules()) {
                final String line = module.getName() + ":" + module.getKey();
                KeyBindConfig.configManager.write(line);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
