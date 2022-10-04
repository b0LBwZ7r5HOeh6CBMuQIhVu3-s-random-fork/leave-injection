package com.test.mod.event;

import java.util.Iterator;

import com.test.mod.Utils.Side;
import net.minecraft.client.Minecraft;
import com.test.mod.module.Module;
import com.test.mod.Client;
import com.test.mod.Utils.Connection;

public class Event
{
    public Event() {
        super();
    }
    
    public boolean onPacket(final Object packet,  Side side) {
        boolean suc = true;
        for (final Module module : Client.instance.moduleManager.getModules()) {
            if (module.isState()) {
                if (Minecraft.getMinecraft().theWorld == null) {
                    continue;
                }
                suc &= module.onPacket(packet, side);
            }
        }
        return suc;
    }
}
