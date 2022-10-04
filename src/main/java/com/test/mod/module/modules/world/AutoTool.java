package com.test.mod.module.modules.world;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import com.test.mod.Utils.Tools;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class AutoTool extends Module
{
    public static int previousSlot;
    public static boolean justFinishedMining;
    public static boolean mining;
    
    public AutoTool() {
        super("AutoTool", 0, ModuleType.World, false);
    }
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (Mouse.isButtonDown(0)) {
            final BlockPos lookingAtBlock = AutoTool.mc.objectMouseOver.getBlockPos();
            if (lookingAtBlock != null) {
                final Block stateBlock = AutoTool.mc.theWorld.getBlockState(lookingAtBlock).getBlock();
                if (stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {
                    if (!AutoTool.mining) {
                        AutoTool.previousSlot = Tools.getCurrentPlayerSlot();
                        AutoTool.mining = true;
                    }
                    int index = -1;
                    double speed = 1.0;
                    for (int slot = 0; slot <= 8; ++slot) {
                        final ItemStack itemInSlot = AutoTool.mc.thePlayer.inventory.getStackInSlot(slot);
                        if (itemInSlot != null && itemInSlot.getItem() instanceof ItemTool) {
                            final BlockPos p = AutoTool.mc.objectMouseOver.getBlockPos();
                            final Block bl = AutoTool.mc.theWorld.getBlockState(p).getBlock();
                            if (itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                        else if (itemInSlot != null && itemInSlot.getItem() instanceof ItemShears) {
                            final BlockPos p = AutoTool.mc.objectMouseOver.getBlockPos();
                            final Block bl = AutoTool.mc.theWorld.getBlockState(p).getBlock();
                            if (itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                    }
                    if (index != -1 && speed > 1.273197475E-314) {
                        if (speed != 0.0) {
                            Tools.hotkeyToSlot(index);
                        }
                    }
                }
            }
        }
        else {
            if (AutoTool.mining) {
                Tools.hotkeyToSlot(AutoTool.previousSlot);
            }
            AutoTool.justFinishedMining = false;
            AutoTool.mining = false;
        }
    }
}
