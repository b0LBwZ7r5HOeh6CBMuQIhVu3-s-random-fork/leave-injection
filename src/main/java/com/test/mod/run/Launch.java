package com.test.mod.run;

import com.test.mod.Client;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "nmsl",version = "1")
public class Launch
{
    public Launch() {
        Client.instance.run();
    }


    // 入口
    public static void Load(final String a, final String b) {
        new Launch();
    }
}
