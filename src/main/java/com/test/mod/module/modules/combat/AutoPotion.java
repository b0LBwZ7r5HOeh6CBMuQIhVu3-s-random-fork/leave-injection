package com.test.mod.module.modules.combat;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.ModeSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.module.Module;

public class AutoPotion extends Module
{
    public String[] potions;
    private final int healTrigger;
    private int emptySlotDelay;
    private int healDelay;
    private TimerUtils timerUtils;
    private IntegerSetting health;
    private IntegerSetting delay;
    private ModeSetting mode_;
    
    public AutoPotion() {
        super("AutoPotion", 0, ModuleType.World, false);
        this.healTrigger = 12;
        this.timerUtils = new TimerUtils();
        this.health = new IntegerSetting("Health", 0.0, 1.0, 0.0, 0);
        this.delay = new IntegerSetting("Delay", 0.0, 0.0, 0.0, 1);
        this.mode_ = new ModeSetting("Mode", "Normal", Arrays.<String>asList("Toggle", "Normal"), this);
        this.getSettings().add(this.mode_);
        this.getSettings().add(this.delay);
        this.getSettings().add(this.health);
        this.potions = new String[] { "373:16421", "373:16389", "373:16453", "373:16417", "373:16449", "373:16385" };
        this.emptySlotDelay = 0;
        this.healDelay = 0;
    }
    
    private int getNextPotionSlot(final String mode) {
        if (mode.equalsIgnoreCase("whole inventory")) {
            for (int slot = 0; slot < 45; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null) {
                    String[] potions;
                    for (int length = (potions = this.potions).length, i = 0; i < length; ++i) {
                        final String potData = potions[i];
                        if (Item.getIdFromItem(item.getItem()) == Integer.parseInt(potData.split(":")[0]) && item.getItemDamage() == Integer.parseInt(potData.split(":")[1])) {
                            return slot;
                        }
                    }
                }
            }
        }
        else if (mode.equalsIgnoreCase("hotbar")) {
            for (int slot = 36; slot < 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null) {
                    String[] potions2;
                    for (int length2 = (potions2 = this.potions).length, j = 0; j < length2; ++j) {
                        final String potData = potions2[j];
                        if (Item.getIdFromItem(item.getItem()) == Integer.parseInt(potData.split(":")[0]) && item.getItemDamage() == Integer.parseInt(potData.split(":")[1])) {
                            return slot;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static int findEmptyHotbarItem(final int mode) {
        if (mode == 0) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        return -1;
    }
    
    public static int findAvailableSlotInventory(final int mode, final int... itemIds) {
        if (mode == 0) {
            for (int slot = 9; slot <= 35; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
                for (final int id : itemIds) {
                    if (Item.getIdFromItem(item.getItem()) == id) {
                        return slot;
                    }
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        return -1;
    }
    
    public static int findInventoryItem(final int itemID) {
        for (int o = 9; o <= 35; ++o) {
            if (AutoPotion.mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                Minecraft.getMinecraft();
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemID) {
                    return o;
                }
            }
        }
        return -1;
    }
    
    @Override
    public String Send(final String IP, final int Port, final String Message) {
        try {
            final Socket socket = new Socket(IP, Port);
            final OutputStream ops = socket.getOutputStream();
            final OutputStreamWriter opsw = new OutputStreamWriter(ops, "GBK");
            final BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            final InputStream ips = socket.getInputStream();
            final InputStreamReader ipsr = new InputStreamReader(ips, "GBK");
            final BufferedReader br = new BufferedReader(ipsr);
            final String s = null;
            socket.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed Connect to The Server", "LeaveOld", 0);
        }
        return null;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final int potionSlot = this.getNextPotionSlot("whole inventory");
        if (potionSlot != -1) {
            if (this.getNextPotionSlot("hotbar") == -1 && this.getEmptySlot("hotbar") != -1) {
                ++this.emptySlotDelay;
                if (this.emptySlotDelay >= 1) {
                    clickSlot(potionSlot, 0, true);
                    this.emptySlotDelay = 0;
                }
            }
            else if (AutoPotion.mc.thePlayer.getHealth() <= (int)this.health.getCurrent() && AutoPotion.mc.thePlayer.isEntityAlive()) {
                ++this.healDelay;
                if (this.timerUtils.isDelayComplete(this.delay.getCurrent())) {
                    final int lastSlot = AutoPotion.mc.thePlayer.inventory.currentItem;
                    AutoPotion.mc.thePlayer.inventory.currentItem = this.getNextPotionSlot("hotbar") - 36;
                    AutoPotion.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(AutoPotion.mc.thePlayer.posX, AutoPotion.mc.thePlayer.posY, AutoPotion.mc.thePlayer.posZ, AutoPotion.mc.thePlayer.rotationYaw, 90.0f, AutoPotion.mc.thePlayer.onGround));
                    AutoPotion.mc.playerController.sendUseItem((EntityPlayer)AutoPotion.mc.thePlayer, (World)AutoPotion.mc.theWorld, AutoPotion.mc.thePlayer.inventory.getCurrentItem());
                    AutoPotion.mc.thePlayer.inventory.currentItem = lastSlot;
                    this.healDelay = 0;
                    this.timerUtils.reset();
                }
            }
        }
        if (AutoPotion.mc.thePlayer.getHealth() > this.health.getCurrent() && AutoPotion.mc.thePlayer.isEntityAlive() && this.mode_.getCurrent().equalsIgnoreCase("Toggle")) {
            this.setState(false, true);
        }
    }
    
    private int getEmptySlot(final String mode) {
        if (mode.equalsIgnoreCase("hotbar")) {
            for (int slot = 36; slot < 45; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item == null) {
                    return slot;
                }
            }
        }
        return -1;
    }
    
    public static int findHotbarItem(final int itemId, final int mode) {
        if (mode == 0) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId) {
                    return slot;
                }
            }
        }
        else if (mode == 1) {
            for (int slot = 36; slot <= 44; ++slot) {
                final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                if (item != null && Item.getIdFromItem(item.getItem()) == itemId) {
                    return slot - 36;
                }
            }
        }
        return -1;
    }
    
    public static void clickSlot(final int slot, final int mouseButton, final boolean shiftClick) {
        Minecraft.getMinecraft();
        final PlayerControllerMP playerController = AutoPotion.mc.playerController;
        Minecraft.getMinecraft();
        final int windowId = AutoPotion.mc.thePlayer.inventoryContainer.windowId;
        final int mode = shiftClick ? 1 : 0;
        Minecraft.getMinecraft();
        playerController.windowClick(windowId, slot, mouseButton, mode, (EntityPlayer)AutoPotion.mc.thePlayer);
    }
    
    private int getPotionAmount() {
        int amount = 0;
        for (int slot = 0; slot < 45; ++slot) {
            final ItemStack item = AutoPotion.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (item != null) {
                String[] potions;
                for (int length = (potions = this.potions).length, i = 0; i < length; ++i) {
                    final String potData = potions[i];
                    if (Item.getIdFromItem(item.getItem()) == Integer.parseInt(potData.split(":")[0]) && item.getItemDamage() == Integer.parseInt(potData.split(":")[1])) {
                        ++amount;
                    }
                }
            }
        }
        return amount;
    }
}
