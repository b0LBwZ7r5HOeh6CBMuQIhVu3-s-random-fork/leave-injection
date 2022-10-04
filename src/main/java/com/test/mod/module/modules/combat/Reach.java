package com.test.mod.module.modules.combat;

import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.test.mod.settings.Setting;
import com.test.mod.module.ModuleType;
import java.util.Random;
import com.test.mod.settings.EnableSetting;
import com.test.mod.settings.IntegerSetting;
import com.test.mod.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Reach extends Module
{
    public static IntegerSetting maxRange;
    public static IntegerSetting minRange;
    private static EnableSetting weapon_only;
    private static EnableSetting moving_only;
    private static EnableSetting sprint_only;
    private static EnableSetting hit_through_blocks;
    private static Random rand;
    
    public Reach() {
        super("Reach", 0, ModuleType.Combat, false);
        this.add(Reach.maxRange, Reach.minRange, Reach.weapon_only, Reach.moving_only, Reach.sprint_only, Reach.hit_through_blocks);
    }
    
    static {
        Reach.maxRange = new IntegerSetting("Max_Range", 0.0, 0.0, 0.0, 1);
        Reach.minRange = new IntegerSetting("Min_Range", 0.0, 0.0, 0.0, 1);
        Reach.weapon_only = new EnableSetting("weapon_only", false);
        Reach.moving_only = new EnableSetting("moving_only", false);
        Reach.sprint_only = new EnableSetting("sprint_only", false);
        Reach.hit_through_blocks = new EnableSetting("ThroughBlocks", false);
    }
    
    public static Object[] doReach(final double reachValue, final double AABB, final float cwc) {
        final Entity target = Reach.mc.getRenderViewEntity();
        Entity entity = null;
        if (target == null || Reach.mc.theWorld == null) {
            return null;
        }
        Reach.mc.mcProfiler.startSection("pick");
        final Vec3 targetEyes = target.getPositionEyes(0.0f);
        final Vec3 targetLook = target.getLook(0.0f);
        final Vec3 targetVector = targetEyes.addVector(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue);
        Vec3 targetVec = null;
        final List targetHitbox = Reach.mc.theWorld.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().addCoord(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue).expand(1.0, 1.0, 1.0));
        double reaching = reachValue;
        for (int i = 0; i < targetHitbox.size(); ++i) {
            final Entity targetEntity = (Entity)targetHitbox.get(i);
            if (targetEntity.canBeCollidedWith()) {
                final float targetCollisionBorderSize = targetEntity.getCollisionBorderSize();
                AxisAlignedBB targetAABB = targetEntity.getEntityBoundingBox().expand((double)targetCollisionBorderSize, (double)targetCollisionBorderSize, (double)targetCollisionBorderSize);
                targetAABB = targetAABB.expand(AABB, AABB, AABB);
                final MovingObjectPosition tagetPosition = targetAABB.calculateIntercept(targetEyes, targetVector);
                if (targetAABB.isVecInside(targetEyes)) {
                    if (0.0 < reaching || reaching == 0.0) {
                        entity = targetEntity;
                        targetVec = ((tagetPosition == null) ? targetEyes : tagetPosition.hitVec);
                        reaching = 0.0;
                    }
                }
                else if (tagetPosition != null) {
                    final double targetHitVec = targetEyes.distanceTo(tagetPosition.hitVec);
                    if (targetHitVec < reaching || reaching == 0.0) {
                        final boolean canRiderInteract = false;
                        if (targetEntity == target.ridingEntity) {
                            if (reaching == 0.0) {
                                entity = targetEntity;
                                targetVec = tagetPosition.hitVec;
                            }
                        }
                        else {
                            entity = targetEntity;
                            targetVec = tagetPosition.hitVec;
                            reaching = targetHitVec;
                        }
                    }
                }
            }
        }
        if (reaching < reachValue && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        Reach.mc.mcProfiler.endSection();
        if (entity == null || targetVec == null) {
            return null;
        }
        return new Object[] { entity, targetVec };
    }
    @SubscribeEvent
    public void onMove(final MouseEvent ev) {
        if (Reach.weapon_only.getEnable()) {
            if (Reach.mc.thePlayer.getCurrentEquippedItem() == null) {
                return;
            }
            if (!(Reach.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) && !(Reach.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                return;
            }
        }
        if (Reach.moving_only.getEnable() && Reach.mc.thePlayer.moveForward == 0.0 && Reach.mc.thePlayer.moveStrafing == 0.0) {
            return;
        }
        if (Reach.sprint_only.getEnable() && !Reach.mc.thePlayer.isSprinting()) {
            return;
        }
        if (!Reach.hit_through_blocks.getEnable() && Reach.mc.objectMouseOver != null) {
            final BlockPos blocksReach = Reach.mc.objectMouseOver.getBlockPos();
            if (blocksReach != null && Reach.mc.theWorld.getBlockState(blocksReach).getBlock() != Blocks.air) {
                return;
            }
        }
        final double Reach = com.test.mod.module.modules.combat.Reach.minRange.getCurrent();
        final Object[] reachs = doReach(Reach, 0.0, 0.0f);
        if (reachs == null) {
            return;
        }
        com.test.mod.module.modules.combat.Reach.mc.objectMouseOver = new MovingObjectPosition((Entity)reachs[0], (Vec3)reachs[1]);
        com.test.mod.module.modules.combat.Reach.mc.pointedEntity = (Entity)reachs[0];
    }
}
