package com.test.mod.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.GuiChat;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class InvMove extends Module
{
    public InvMove() {
        super("InvMove", 0, ModuleType.Movement, false);
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (InvMove.mc.currentScreen != null && !(InvMove.mc.currentScreen instanceof GuiChat)) {
            final KeyBinding[] key = { InvMove.mc.gameSettings.keyBindForward, InvMove.mc.gameSettings.keyBindBack, InvMove.mc.gameSettings.keyBindLeft, InvMove.mc.gameSettings.keyBindRight, InvMove.mc.gameSettings.keyBindSprint, InvMove.mc.gameSettings.keyBindJump };
            KeyBinding[] array;
            for (int lengths = (array = key).length, i = 0; i < lengths; ++i) {
                final KeyBinding b = array[i];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
            }
        }
    }
}
