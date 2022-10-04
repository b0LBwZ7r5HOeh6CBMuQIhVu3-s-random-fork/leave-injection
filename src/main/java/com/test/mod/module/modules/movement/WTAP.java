package com.test.mod.module.modules.movement;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.concurrent.ThreadLocalRandom;
import org.lwjgl.input.Mouse;
import net.minecraft.entity.Entity;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.EnableSetting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import com.test.mod.module.Module;

public class WTAP extends Module
{
    public static double comboLasts;
    private static final HashMap<EntityPlayer, Long> newEnt;
    public static boolean comboing;
    public static boolean hitCoolDown;
    public static boolean alreadyHit;
    public static int hitTimeout;
    public static int hitsWaited;
    private EnableSetting onlyPlayers;
    private IntegerSetting minActionTicks;
    private IntegerSetting minOnceEvery;
    private IntegerSetting range;
    
    public WTAP() {
        super("WTAP", 0, ModuleType.Movement, false);
        this.onlyPlayers = new EnableSetting("PlayersOnly", true);
        this.minActionTicks = new IntegerSetting("Delay", 0.0, 1.0, 0.0, 1);
        this.minOnceEvery = new IntegerSetting("Hits", 1.0, 1.0, 0.0, 1);
        this.range = new IntegerSetting("Range", 0.0, 0.0, 0.0, 1);
        this.getSettings().add(this.onlyPlayers);
        this.getSettings().add(this.minActionTicks);
        this.getSettings().add(this.minOnceEvery);
        this.getSettings().add(this.range);
    }
    
    static {
        newEnt = new HashMap<EntityPlayer, Long>();
    }
    @SubscribeEvent
    public void onTick(final TickEvent.RenderTickEvent e) {
        if (Tools.nullCheck()) {
            return;
        }
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (!WTAP.comboing) {
            if (WTAP.mc.objectMouseOver != null && WTAP.mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
                final Entity target = WTAP.mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    return;
                }
                if (WTAP.mc.thePlayer.getDistanceToEntity(target) <= this.range.getCurrent()) {
                    if (target.hurtResistantTime >= 10) {
                        if (this.onlyPlayers.getEnable() && !(target instanceof EntityPlayer)) {
                            return;
                        }
                        if (WTAP.hitCoolDown && !WTAP.alreadyHit) {
                            ++WTAP.hitsWaited;
                            if (WTAP.hitsWaited < WTAP.hitTimeout) {
                                WTAP.alreadyHit = true;
                                return;
                            }
                            WTAP.hitCoolDown = false;
                            WTAP.hitsWaited = 0;
                        }
                        if (!WTAP.alreadyHit) {
                            WTAP.hitTimeout = (int)this.minOnceEvery.getCurrent();
                        }
                        else {
                            WTAP.hitTimeout = ThreadLocalRandom.current().nextInt((int)this.minOnceEvery.getCurrent(), (int)this.minOnceEvery.getCurrent());
                        }
                        WTAP.hitCoolDown = true;
                        WTAP.hitsWaited = 0;
                        WTAP.comboLasts = ThreadLocalRandom.current().nextDouble(this.minActionTicks.getCurrent(), this.minActionTicks.getCurrent() + 5.941588215E-315) + System.currentTimeMillis();
                        WTAP.comboing = true;
                        startCombo();
                        WTAP.alreadyHit = true;
                    }
                }
                else {
                    if (WTAP.alreadyHit) {}
                    WTAP.alreadyHit = false;
                }
            }
            return;
        }
        if (System.currentTimeMillis() >= WTAP.comboLasts) {
            WTAP.comboing = false;
            finishCombo();
        }
    }
    
    private static void startCombo() {
        if (Keyboard.isKeyDown(WTAP.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTAP.mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(WTAP.mc.gameSettings.keyBindForward.getKeyCode());
        }
    }
    
    private static void finishCombo() {
        if (Keyboard.isKeyDown(WTAP.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTAP.mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }
    
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (event.entity instanceof EntityPlayer && event.entity != WTAP.mc.thePlayer) {
            WTAP.newEnt.put((EntityPlayer)event.entity, System.currentTimeMillis());
        }
    }
}
