package com.test.mod.module.modules.combat;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ContainerChest;
import com.test.mod.settings.Setting;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.module.Module;

public class ChestStealer extends Module
{
    private TimerUtils timer;
    public int ticks;
    public IntegerSetting Delay;
    private EnableSetting autoClose;
    
    public ChestStealer() {
        super("ChestStealer", 0, ModuleType.Combat, false);
        this.timer = new TimerUtils();
        this.Delay = new IntegerSetting("Delay", 0.0, 0.0, 0.0, 1);
        this.autoClose = new EnableSetting("AutoClose", true);
        this.add(this.Delay, this.autoClose);
    }
    
    private boolean isEmpty() {
        if (ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
            final ContainerChest container = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                final ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() != null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean empty(final Container container) {
        boolean voll = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                voll = false;
            }
        }
        return voll;
    }
    
    private boolean isContainerEmpty(final Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 35; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        final Minecraft mc = ChestStealer.mc;
        if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest && !(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GuiContainerCreative)) {
            final ContainerChest container = (ContainerChest)mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null) {
                    if (this.timer.isDelayComplete(this.Delay.getCurrent() + (double)new Random().nextInt(100))) {
                        final PlayerControllerMP playerController = mc.playerController;
                        final int windowId = container.windowId;
                        final int slotId = i;
                        final int mouseButtonClicked = 0;
                        final int mode = 1;
                        playerController.windowClick(windowId, slotId, 0, 1, (EntityPlayer)mc.thePlayer);
                        this.timer.reset();
                    }
                }
                else if (this.empty((Container)container) && this.autoClose.getEnable()) {
                    mc.thePlayer.closeScreen();
                }
            }
            ++this.ticks;
        }
    }
}
