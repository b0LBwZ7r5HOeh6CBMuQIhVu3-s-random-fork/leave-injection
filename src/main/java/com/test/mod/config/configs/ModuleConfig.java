package com.test.mod.config.configs;

import java.util.Iterator;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Tools;
import com.test.mod.config.ConfigManager;

public class ModuleConfig
{
    private static ConfigManager configManager;
    
    public ModuleConfig() {
        super();
    }
    
    static {
        ModuleConfig.configManager = new ConfigManager(Tools.getConfigPath(), "modules.txt");
    }
    
    public static void loadModules() {
        try {
            for (final String s : ModuleConfig.configManager.read()) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    final String name = s.split(":")[0];
                    final boolean toggled = Boolean.parseBoolean(s.split(":")[1]);
                    if (module.getName().equalsIgnoreCase(name) && module.getName() != "ClickGui") {
                        module.setState(toggled, false);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveModules() {
        try {
            ModuleConfig.configManager.clear();
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (module.getName() != "ClickGUI") {
                    final String line = module.getName() + ":" + String.valueOf(module.isState());
                    ModuleConfig.configManager.write(line);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
