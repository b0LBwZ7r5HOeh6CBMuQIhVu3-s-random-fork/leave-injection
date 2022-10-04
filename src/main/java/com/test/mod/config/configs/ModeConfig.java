package com.test.mod.config.configs;

import java.util.Iterator;
import com.test.mod.settings.ModeSetting;
import com.test.mod.settings.Setting;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Tools;
import com.test.mod.config.ConfigManager;

public class ModeConfig
{
    private static ConfigManager configManager;
    
    public ModeConfig() {
        super();
    }
    
    static {
        ModeConfig.configManager = new ConfigManager(Tools.getConfigPath(), "Mode.txt");
    }
    
    public static void loadState() {
        try {
            for (final String s : ModeConfig.configManager.read()) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    if (!module.getSettings().isEmpty()) {
                        for (final Setting setting : module.getSettings()) {
                            final String name = s.split(":")[0];
                            final String mod = s.split(":")[1];
                            final String value = s.split(":")[2];
                            if (setting.getName().equalsIgnoreCase(name) && ((ModeSetting)setting).getParent().getName().equalsIgnoreCase(mod)) {
                                ((ModeSetting)setting).setCurrent(value);
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
            ModeConfig.configManager.clear();
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (!module.getSettings().isEmpty()) {
                    for (final Setting setting : module.getSettings()) {
                        if (setting instanceof ModeSetting) {
                            final String line = setting.getName() + ":" + module.getName() + ":" + ((ModeSetting)setting).getCurrent();
                            ModeConfig.configManager.write(line);
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
