package com.test.mod.config.configs;

import java.util.Iterator;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.Setting;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Tools;
import com.test.mod.config.ConfigManager;

public class EnableConfig
{
    private static ConfigManager configManager;
    
    public EnableConfig() {
        super();
    }
    
    static {
        EnableConfig.configManager = new ConfigManager(Tools.getConfigPath(), "Enable.txt");
    }
    
    public static void loadState() {
        try {
            for (final String s : EnableConfig.configManager.read()) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    if (!module.getSettings().isEmpty()) {
                        for (final Setting setting : module.getSettings()) {
                            final String name = s.split(":")[0];
                            final boolean enable = false;
                            final String mod = s.split(":")[1];
                            final boolean toggled = Boolean.parseBoolean(s.split(":")[2]);
                            if (setting.getName().equalsIgnoreCase(name) && module.getName().equalsIgnoreCase(mod)) {
                                ((EnableSetting)setting).setEnable(toggled);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveState() {
        try {
            EnableConfig.configManager.clear();
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (!module.getSettings().isEmpty()) {
                    for (final Setting setting : module.getSettings()) {
                        if (setting instanceof EnableSetting) {
                            final String line = setting.getName() + ":" + module.getName() + ":" + ((EnableSetting)setting).getEnable();
                            EnableConfig.configManager.write(line);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
