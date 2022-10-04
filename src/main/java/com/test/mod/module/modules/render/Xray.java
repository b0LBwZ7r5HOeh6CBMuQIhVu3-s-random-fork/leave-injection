package com.test.mod.module.modules.render;

import com.test.mod.Utils.HUDUtils;
import java.awt.Color;
import java.util.Iterator;
import java.util.Collection;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.test.mod.Utils.Tools;
import java.util.TimerTask;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.ArrayList;
import com.test.mod.module.ModuleType;
import com.test.mod.Utils.nBlockPos;
import com.test.mod.Utils.TimerUtils;
import com.test.mod.settings.IntegerSetting;
import net.minecraft.util.BlockPos;
import java.util.List;
import java.util.Timer;
import com.test.mod.settings.EnableSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Xray extends Module
{
    public static EnableSetting spawner;
    public static EnableSetting coal;
    public static EnableSetting iron;
    public static EnableSetting lapis;
    public static EnableSetting emerald;
    public static EnableSetting redstone;
    public static EnableSetting gold;
    public static EnableSetting diammond;
    private Timer t;
    private static List<BlockPos> ren;
    private final long per;
    public static IntegerSetting r;
    private final TimerUtils refresh;
    public nBlockPos pos;
    public static List<BlockPos> toRender;
    
    public Xray() {
        super("Xray", 0, ModuleType.Render, false);
        this.per = 200L;
        this.refresh = new TimerUtils();
        this.pos = new nBlockPos();
        this.getSettings().add(Xray.spawner);
        this.getSettings().add(Xray.coal);
        this.getSettings().add(Xray.iron);
        this.getSettings().add(Xray.lapis);
        this.getSettings().add(Xray.emerald);
        this.getSettings().add(Xray.redstone);
        this.getSettings().add(Xray.gold);
        this.getSettings().add(Xray.diammond);
        this.getSettings().add(Xray.r);
    }
    
    static {
        Xray.spawner = new EnableSetting("Spawner", true);
        Xray.coal = new EnableSetting("Coal", true);
        Xray.iron = new EnableSetting("Iron", true);
        Xray.lapis = new EnableSetting("Lapis", true);
        Xray.emerald = new EnableSetting("Emerald", true);
        Xray.redstone = new EnableSetting("Redstone", true);
        Xray.gold = new EnableSetting("Gold", true);
        Xray.diammond = new EnableSetting("Diammond", true);
        Xray.r = new IntegerSetting("Range", 0.0, 0.0, 0.0, 1);
        Xray.toRender = new ArrayList<BlockPos>();
    }
    
    static /* synthetic */ List access$000(final Xray x0) {
        return x0.ren;
    }
    
    private int[] c(final Block b) {
        int red = 0;
        int green = 0;
        int blue = 0;
        if (b.equals(Blocks.iron_ore)) {
            red = 255;
            green = 255;
            blue = 255;
        }
        else if (b.equals(Blocks.gold_ore)) {
            red = 255;
            green = 255;
        }
        else if (b.equals(Blocks.diamond_ore)) {
            green = 220;
            blue = 255;
        }
        else if (b.equals(Blocks.emerald_ore)) {
            red = 35;
            green = 255;
        }
        else if (b.equals(Blocks.lapis_ore)) {
            green = 50;
            blue = 255;
        }
        else if (b.equals(Blocks.redstone_ore)) {
            red = 255;
        }
        else if (b.equals(Blocks.mob_spawner)) {
            red = 30;
            blue = 135;
        }
        return new int[] { red, green, blue };
    }
    
    private TimerTask t() {
        return new TimerTask() {

            @Override
            public void run() {
                Xray.ren.clear();
                int y;
                for (int ra = y = (int)Xray.r.getCurrent(); y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            if (Tools.isPlayerInGame()) {
                                final BlockPos p = new BlockPos(Module.mc.thePlayer.posX + x, Module.mc.thePlayer.posY + y, Module.mc.thePlayer.posZ + z);
                                final Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                                if ((Xray.iron.getEnable() && bl.equals(Blocks.iron_ore)) || (Xray.gold.getEnable() && bl.equals(Blocks.gold_ore)) || (Xray.diammond.getEnable() && bl.equals(Blocks.diamond_ore)) || (Xray.emerald.getEnable() && bl.equals(Blocks.emerald_ore)) || (Xray.lapis.getEnable() && bl.equals(Blocks.lapis_ore)) || (Xray.redstone.getEnable() && bl.equals(Blocks.redstone_ore)) || (Xray.coal.getEnable() && bl.equals(Blocks.coal_ore)) || (Xray.spawner.getEnable() && bl.equals(Blocks.mob_spawner))) {
                                    Xray.ren.add(p);
                                }
                            }
                        }
                    }
                }
            }
        };
    }
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (Tools.isPlayerInGame() && !this.ren.isEmpty()) {
            final List<BlockPos> tRen = new ArrayList(this.ren);
            for (final BlockPos p : tRen) {
                this.dr(p);
            }
        }
    }
    
    private void dr(final BlockPos p) {
        final int[] rgb = this.c(Xray.mc.theWorld.getBlockState(p).getBlock());
        if (rgb[0] + rgb[1] + rgb[2] != 0) {
            HUDUtils.re(p, new Color(rgb[0], rgb[1], rgb[2]).getRGB(), true);
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.ren = new ArrayList<BlockPos>();
        (this.t = new Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }
    }
}
