package com.test.mod;

import com.test.mod.run.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Forge_Mod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    
    public Forge_Mod() {
        new Launch();
    }

    @SubscribeEvent
    public void init(final FMLInitializationEvent event) {
//        new Launch();
    }
}
