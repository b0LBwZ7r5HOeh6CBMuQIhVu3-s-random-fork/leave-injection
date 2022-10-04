package com.test.mod.module.modules.world;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;
import com.test.mod.module.Module;

public class FastPlace extends Module
{
    Field c;
    public Minecraft mc;
    private IntegerSetting Dalay;
    
    public FastPlace() {
        super("FastPlace", 0, ModuleType.World, false);
        this.c = null;
        this.mc = Minecraft.getMinecraft();
        this.Dalay = new IntegerSetting("Dalay", 0.0, 0.0, 0.0, 0);
        this.getSettings().add(this.Dalay);
    }
    
    @Override
    public void onEnable() {
        try {
            this.c = this.mc.getClass().getDeclaredField("rightClickDelayTimer");
        }
        catch (Exception d) {
            try {
                this.c = this.mc.getClass().getDeclaredField("rightClickDelayTimer");
            }
            catch (Exception ex) {}
        }
        if (this.c != null) {
            this.c.setAccessible(true);
        }
    }
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (e.phase == TickEvent.Phase.END && Tools.isPlayerInGame() && this.mc.inGameHasFocus && this.c != null) {
            final ItemStack item = this.mc.thePlayer.getHeldItem();
            if (item == null || !(item.getItem() instanceof ItemBlock)) {
                return;
            }
            try {
                final int c1 = (int)this.Dalay.getCurrent();
                if (c1 == 0) {
                    this.c.set(this.mc, 0);
                }
                else {
                    if (c1 == 4) {
                        return;
                    }
                    final int d = this.c.getInt(this.mc);
                    if (d == 4) {
                        this.c.set(this.mc, c1);
                    }
                }
            }
            catch (IllegalAccessException ex) {}
        }
    }
}
