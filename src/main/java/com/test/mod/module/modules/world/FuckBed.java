package com.test.mod.module.modules.world;

import net.minecraft.block.Block;
import java.lang.reflect.Field;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.test.mod.Utils.HUDUtils;
import net.minecraft.client.renderer.GlStateManager;
import com.test.mod.Utils.Tools;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.ArrayList;
import com.test.mod.module.ModuleType;
import com.test.mod.settings.EnableSetting;
import java.util.List;
import com.test.mod.Utils.TimerUtils;
import net.minecraft.util.BlockPos;
import com.test.mod.module.Module;

public class FuckBed extends Module
{
    public static BlockPos blockBreaking;
    TimerUtils timer;
    List<BlockPos> beds;
    private EnableSetting Instant;
    
    public FuckBed() {
        super("FuckBed", 0, ModuleType.World, false);
        this.timer = new TimerUtils();
        this.beds = new ArrayList<BlockPos>();
        this.Instant = new EnableSetting("Instant", false);
        this.getSettings().add(this.Instant);
    }
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (FuckBed.blockBreaking != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            HUDUtils.drawBoundingBox(FuckBed.blockBreaking.getX() - FuckBed.mc.getRenderManager().viewerPosX + 0.0, (double)FuckBed.blockBreaking.getY() - FuckBed.mc.getRenderManager().viewerPosY, FuckBed.blockBreaking.getZ() - FuckBed.mc.getRenderManager().viewerPosZ + 0.0, 0.0, 0.0, 1.0f, 0.0f, 0.0f, 0.25f);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }
    
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        int y;
        for (int reach = y = 6; y >= -reach; --y) {
            for (int x = -reach; x <= reach; ++x) {
                for (int z = -reach; z <= reach; ++z) {
                    if (FuckBed.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    final BlockPos pos = new BlockPos(FuckBed.mc.thePlayer.posX + x, FuckBed.mc.thePlayer.posY + y, FuckBed.mc.thePlayer.posZ + z);
                    if (this.blockChecks(FuckBed.mc.theWorld.getBlockState(pos).getBlock()) && FuckBed.mc.thePlayer.getDistance(FuckBed.mc.thePlayer.posX + (double)x, FuckBed.mc.thePlayer.posY + (double)y, FuckBed.mc.thePlayer.posZ + (double)z) < FuckBed.mc.playerController.getBlockReachDistance() - 1.273197475E-314 && !this.beds.contains(pos)) {
                        this.beds.add(pos);
                    }
                }
            }
        }
        BlockPos closest = null;
        if (!this.beds.isEmpty()) {
            for (int i = 0; i < this.beds.size(); ++i) {
                final BlockPos bed = (BlockPos)this.beds.get(i);
                if (FuckBed.mc.thePlayer.getDistance((double)bed.getX(), (double)bed.getY(), (double)bed.getZ()) > FuckBed.mc.playerController.getBlockReachDistance() - 1.273197475E-314 || FuckBed.mc.theWorld.getBlockState(bed).getBlock() != Blocks.bed) {
                    this.beds.remove(i);
                }
                if (closest == null || (FuckBed.mc.thePlayer.getDistance((double)bed.getX(), (double)bed.getY(), (double)bed.getZ()) < FuckBed.mc.thePlayer.getDistance((double)closest.getX(), (double)closest.getY(), (double)closest.getZ()) && FuckBed.mc.thePlayer.ticksExisted % 50 == 0)) {
                    closest = bed;
                }
            }
        }
        if (closest != null) {
            final float[] rot = this.getRotations(closest, this.getClosestEnum(closest));
            FuckBed.blockBreaking = closest;
            return;
        }
        FuckBed.blockBreaking = null;
    }
    
    public float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.0 - FuckBed.mc.thePlayer.posX;
        final double z = block.getZ() + 0.0 - FuckBed.mc.thePlayer.posZ;
        final double d1 = FuckBed.mc.thePlayer.posY + FuckBed.mc.thePlayer.getEyeHeight() - (block.getY() + 0.0);
        final double d2 = (double)MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 0.0 / 6.984873503E-315);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (FuckBed.blockBreaking != null) {
            if (this.Instant.getEnable()) {
                FuckBed.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, FuckBed.blockBreaking, EnumFacing.DOWN));
                FuckBed.mc.thePlayer.swingItem();
                FuckBed.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, FuckBed.blockBreaking, EnumFacing.DOWN));
            }
            else {
                final Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "curBlockDamageMP", "curBlockDamageMP" });
                final Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "blockHitDelay", "blockHitDelay" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!blockdelay.isAccessible()) {
                        blockdelay.setAccessible(true);
                    }
                    if (field.getFloat(FuckBed.mc.playerController) > 1.0f) {
                        blockdelay.setInt(FuckBed.mc.playerController, 1);
                    }
                    FuckBed.mc.thePlayer.swingItem();
                    final EnumFacing direction = this.getClosestEnum(FuckBed.blockBreaking);
                    if (direction != null) {
                        FuckBed.mc.playerController.onPlayerDamageBlock(FuckBed.blockBreaking, direction);
                    }
                }
                catch (Exception ex) {}
            }
        }
    }
    
    private EnumFacing getClosestEnum(final BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        final float rotations = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations >= 45.0f && rotations <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        }
        else if ((rotations >= 135.0f && rotations <= 180.0f) || (rotations <= -135.0f && rotations >= -180.0f)) {
            closestEnum = EnumFacing.SOUTH;
        }
        else if (rotations <= -45.0f && rotations >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        }
        else if ((rotations >= -45.0f && rotations <= 0.0f) || (rotations <= 45.0f && rotations >= 0.0f)) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }
    
    private boolean blockChecks(final Block block) {
        return block == Blocks.bed;
    }
}
