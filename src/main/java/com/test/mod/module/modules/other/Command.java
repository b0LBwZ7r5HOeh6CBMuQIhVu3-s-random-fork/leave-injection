package com.test.mod.module.modules.other;

import java.lang.reflect.Field;
import com.test.mod.Client;
import com.test.mod.Utils.Side;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.network.play.client.C01PacketChatMessage;
import com.test.mod.Utils.Connection;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class Command extends Module
{
    public Command() {
        super("Command", 0, ModuleType.Other, true);
    }
    
    @Override
    public boolean onPacket(Object packet, Side side) {
        boolean send = true;
        if (side == Side.OUT && packet instanceof C01PacketChatMessage) {
            Field field = ReflectionHelper.findField((Class)C01PacketChatMessage.class, new String[] { "message", "message" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                C01PacketChatMessage p = (C01PacketChatMessage)packet;
                if (p.getMessage().subSequence(0, 1).equals(".")) {
                    send = false;
                    Client.instance.commandManager.execute(p.getMessage());
                    return send;
                }
                send = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return send;
    }
}
