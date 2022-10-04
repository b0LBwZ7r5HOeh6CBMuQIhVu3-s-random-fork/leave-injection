package com.test.mod.module.modules.movement;

import net.minecraft.client.Minecraft;
import com.test.mod.Utils.ReflectionUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class Scaffold extends Module
{
    public Scaffold() {
        super("Scaffold", 0, ModuleType.Movement, false);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.Eagle();
    }
    
    void Eagle() {
        try {
            if (Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock && !Scaffold.mc.gameSettings.keyBindJump.isPressed()) {
                final BlockPos bp = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                if (Scaffold.mc.theWorld.getBlockState(bp).getBlock() == Blocks.air && !Keyboard.isKeyDown(17)) {
                    ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, true);
                }
                else {
                    ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, false);
                }
            }
        }
        catch (Exception ex) {}
    }
}
