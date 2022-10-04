package com.test.mod.config.configs;

import java.util.Iterator;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.Setting;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Tools;
import com.test.mod.config.ConfigManager;

public class IntegerConfig
{
    private static ConfigManager configManager;
    
    public IntegerConfig() {
        super();
    }
    
    static {
        IntegerConfig.configManager = new ConfigManager(Tools.getConfigPath(), "Integer.txt");
    }
    
    public static void loadState() {
        try {
            for (final String s : IntegerConfig.configManager.read()) {
                for (final Module module : Client.instance.moduleManager.getModules()) {
                    if (!module.getSettings().isEmpty()) {
                        for (final Setting setting : module.getSettings()) {
                            final String name = s.split(":")[0];
                            final String mod = s.split(":")[1];
                            final double value = Double.parseDouble(s.split(":")[2]);
                            if (setting.getName().equalsIgnoreCase(name) && module.getName().equalsIgnoreCase(mod)) {
                                ((IntegerSetting)setting).setCurrent(value);
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
            IntegerConfig.configManager.clear();
            for (final Module module : Client.instance.moduleManager.getModules()) {
                if (!module.getSettings().isEmpty()) {
                    for (final Setting setting : module.getSettings()) {
                        if (setting instanceof IntegerSetting) {
                            final String line = setting.getName() + ":" + module.getName() + ":" + ((IntegerSetting)setting).getCurrent();
                            IntegerConfig.configManager.write(line);
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
