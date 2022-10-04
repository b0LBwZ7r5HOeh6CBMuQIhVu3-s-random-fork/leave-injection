package com.test.mod.module.modules.other;

import net.minecraft.entity.Entity;
import java.util.Iterator;
import java.util.Collection;
import com.test.mod.command.commands.SetTeamSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.test.mod.Utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import com.test.mod.Client;
import net.minecraft.entity.EntityLivingBase;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Teams extends Module
{
    public ModeSetting mode;
    
    public Teams() {
        super("Teams", 0, ModuleType.Other, false);
        this.mode = new ModeSetting("Mode", "ArmorColor", Arrays.<String>asList("Base", "ArmorColor", "NameColor", "TabList"), this);
        this.add(this.mode);
    }
    @SubscribeEvent
    public static boolean isTeam(EntityLivingBase entity) {
        Module teams = Client.instance.moduleManager.getModule("Teams");
        if (teams.isState() && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (teams.isToggledMode("Base") && player.getTeam() != null && Teams.mc.thePlayer.getTeam() != null && player.getTeam().isSameTeam(Teams.mc.thePlayer.getTeam())) {
                return false;
            }
            if (teams.isToggledMode("ArmorColor") && !Utils.checkEnemyColor(player)) {
                return false;
            }
            if (teams.isToggledMode("NameColor") && !Utils.checkEnemyNameColor((EntityLivingBase)player)) {
                return false;
            }
            if (teams.isToggledMode("TabList")) {
                Collection<NetworkPlayerInfo> list = Teams.mc.thePlayer.sendQueue.getPlayerInfoMap();
                for (NetworkPlayerInfo networkplayerinfo : list) {
                    if (entity.getName().equals(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkplayerinfo))) {
                        return Teams.mc.ingameGUI.getTabList().getPlayerName(networkplayerinfo).contains(SetTeamSign.teamsign);
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean isTeam(Entity entity) {
        Module teams = Client.instance.moduleManager.getModule("Teams");
        if (teams.isState() && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (teams.isToggledMode("Base") && player.getTeam() != null && Teams.mc.thePlayer.getTeam() != null && player.getTeam().isSameTeam(Teams.mc.thePlayer.getTeam())) {
                return false;
            }
            if (teams.isToggledMode("ArmorColor") && !Utils.checkEnemyColor(player)) {
                return false;
            }
            if (teams.isToggledMode("NameColor") && !Utils.checkEnemyNameColor((EntityLivingBase)player)) {
                return false;
            }
            if (teams.isToggledMode("TabList")) {
                Collection<NetworkPlayerInfo> list = Teams.mc.thePlayer.sendQueue.getPlayerInfoMap();
                for (NetworkPlayerInfo networkplayerinfo : list) {
                    if (entity.getName().equals(Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(networkplayerinfo))) {
                        return Teams.mc.ingameGUI.getTabList().getPlayerName(networkplayerinfo).contains(SetTeamSign.teamsign);
                    }
                }
            }
        }
        return true;
    }
}
