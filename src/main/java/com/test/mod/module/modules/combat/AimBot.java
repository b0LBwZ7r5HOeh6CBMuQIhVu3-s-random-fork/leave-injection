package com.test.mod.module.modules.combat;

import java.util.Iterator;

import com.test.mod.Utils.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import com.test.mod.Utils.Tools;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.Utils.Utils;
import net.minecraft.entity.Entity;
import com.test.mod.module.modules.other.Teams;
import net.minecraft.entity.item.EntityArmorStand;
import com.test.mod.settings.Setting;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import com.test.mod.module.ModuleType;
import net.minecraft.entity.EntityLivingBase;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.ModeSetting;
import com.test.mod.module.Module;

public class AimBot extends Module
{
    public ModeSetting priority;
    public EnableSetting walls;
    public EnableSetting clickAim;
    public EnableSetting Click;
    public static IntegerSetting yaw;
    public static IntegerSetting pitch;
    public IntegerSetting range;
    public IntegerSetting FOV;
    public IntegerSetting horizontal;
    public IntegerSetting vertical;
    public EntityLivingBase target;
    
    public AimBot() {
        super("AimBot", 0, ModuleType.Combat, false);
        this.priority = new ModeSetting("Priority", "Closest", Arrays.<String>asList("Closest", "Health"), this);
        this.walls = new EnableSetting("ThroughWalls", false);
        this.clickAim = new EnableSetting("ClickAim", false);
        this.Click = new EnableSetting("ClickAim", true);
        this.range = new IntegerSetting("Range", 1.697596633E-314, 1.273197475E-314, 0.0, 1);
        this.FOV = new IntegerSetting("FOV", 0.0, 1.0, 0.0, 0);
        this.add(this.priority, this.walls, this.clickAim, AimBot.yaw, AimBot.pitch, this.range, this.FOV, this.Click);
    }
    
    static {
        AimBot.yaw = new IntegerSetting("Yaw", 0.0, 0.0, 0.0, 0);
        AimBot.pitch = new IntegerSetting("Pitch", 0.0, 0.0, 0.0, 0);
    }
    
    public boolean check(EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && entity != AimBot.mc.thePlayer && !entity.isDead && isInAttackFOV(entity, (int)this.FOV.getCurrent()) && isInAttackRange(entity, (float)this.range.getCurrent()) && Teams.isTeam(entity) && this.isPriority(entity) && (this.walls.getEnable() || AimBot.mc.thePlayer.canEntityBeSeen(entity));
    }
    

    
    @Override
    public void onDisable() {
        this.target = null;
        super.onDisable();
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Tools.nullCheck()) {
            return;
        }
        this.updateTarget();
        if (AimBot.mc.thePlayer.getHeldItem() == null || AimBot.mc.thePlayer.getHeldItem().getItem() == null) {
            return;
        }
        Item heldItem = AimBot.mc.thePlayer.getHeldItem().getItem();
        if (this.Click.getEnable() && !Mouse.isButtonDown(0)) {
            return;
        }
        Utils.assistFaceEntity(this.target, (float)AimBot.yaw.getCurrent(), (float)AimBot.pitch.getCurrent());
        this.target = null;
    }
    
    public static boolean isInAttackRange(EntityLivingBase entity, float range) {
        return entity.getDistanceToEntity(AimBot.mc.thePlayer) <= range;
    }
    
    boolean isPriority(EntityLivingBase entity) {
        return (this.priority.getCurrent().equals("Closest") && isClosest(entity, this.target)) || (this.priority.getCurrent().equals("Health") && isLowHealth(entity, this.target));
    }
    
    public static boolean isLowHealth(EntityLivingBase entity, EntityLivingBase entityPriority) {
        return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
    }
    
    public static boolean isInAttackFOV(EntityLivingBase entity, int fov) {
        return Utils.getDistanceFromMouse(entity) <= fov;
    }
    
    void updateTarget() {
        for (Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            EntityLivingBase entity = (EntityLivingBase) object;
            if (!this.check(entity)) {
                continue;
            }
            this.target = entity;
        }
    }
    
    private int randomNumber() {
        return -1 + (int)(Math.random() * 0.0);
    }
    
    private EntityLivingBase getBestEntity() {
        CopyOnWriteArrayList<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
        for (Entity o : mc.theWorld.loadedEntityList) {
            EntityLivingBase ent;
            if (!(o instanceof EntityLivingBase) || !(ent = (EntityLivingBase) o).isEntityAlive() || !(ent instanceof EntityPlayer) || !((double)ent.getDistanceToEntity(AimBot.mc.thePlayer) < this.range.getCurrent()) || !this.fovCheck(ent)) continue;
            loaded.add(ent);
        }
        if (loaded.isEmpty()) {
            return null;
        }
        try {
            loaded.sort((o1, o2) -> {
                float[] rot1 = Utils.getRotations(o1);
                float[] rot2 = Utils.getRotations(o2);
                return (int)(Utils.getDistanceBetweenAngles((float)AimBot.mc.thePlayer.rotationYaw, (float)rot1[0]) + Utils.getDistanceBetweenAngles((float)AimBot.mc.thePlayer.rotationPitch, (float)rot1[1]) - (Utils.getDistanceBetweenAngles((float)AimBot.mc.thePlayer.rotationYaw, (float)rot2[0]) + Utils.getDistanceBetweenAngles((float)AimBot.mc.thePlayer.rotationPitch, (float)rot2[1])));
            });
        }
        catch (Exception e) {
            ChatUtils.error((Object)("Exception with TM: " + e.getMessage()));
        }
        return loaded.get(0);
    }
    
    private boolean fovCheck(EntityLivingBase ent) {
        float[] rotations = Utils.getRotations(ent);
        float dist = AimBot.mc.thePlayer.getDistanceToEntity(ent);
        if (dist == 0.0f) {
            dist = 1.0f;
        }
        float yawDist = Utils.getDistanceBetweenAngles(AimBot.mc.thePlayer.rotationYaw, rotations[0]);
        float pitchDist = Utils.getDistanceBetweenAngles(AimBot.mc.thePlayer.rotationPitch, rotations[1]);
        float fovYaw = (float)AimBot.yaw.getCurrent() * 3.0f / dist;
        float fovPitch = (float)AimBot.pitch.getCurrent() * 3.0f / dist;
        return yawDist < fovYaw && pitchDist < fovPitch;
    }
    
    public static boolean isClosest(EntityLivingBase entity, EntityLivingBase entityPriority) {
        return entityPriority == null || AimBot.mc.thePlayer.getDistanceToEntity(entity) < AimBot.mc.thePlayer.getDistanceSqToEntity(entityPriority);
    }
}
