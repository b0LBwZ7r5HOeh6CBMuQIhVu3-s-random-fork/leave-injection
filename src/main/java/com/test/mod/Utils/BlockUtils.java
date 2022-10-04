package com.test.mod.Utils;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.Vec3;
import java.util.LinkedList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;

public final class BlockUtils
{
    public static Minecraft mc;
    
    public BlockUtils() {
        super();
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtils.mc.theWorld.getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i = BlockUtils.mc.thePlayer.posY; i > 0.0; --i) {
            final Block block = getBlock(new BlockPos(BlockUtils.mc.thePlayer.posX, i, BlockUtils.mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
                distance = i;
                break;
            }
            if (i < 0.0) {
                break;
            }
        }
        final double distancetofall = BlockUtils.mc.thePlayer.posY - distance - 1.0;
        return distancetofall;
    }
    
    public static void faceBlockClientHorizontally(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.0 - BlockUtils.mc.thePlayer.posX;
        final double diffZ = blockPos.getZ() + 0.0 - BlockUtils.mc.thePlayer.posZ;
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 0.0 / 6.984873503E-315) - 90.0f;
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
    }
    
    public static float getPlayerBlockDistance(final BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public static float getPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        final float xDiff = (float)(BlockUtils.mc.thePlayer.posX - posX);
        final float yDiff = (float)(BlockUtils.mc.thePlayer.posY - posY);
        final float zDiff = (float)(BlockUtils.mc.thePlayer.posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }
    
    public static LinkedList<BlockPos> findBlocksNearEntity(final EntityLivingBase entity, final int blockId, final int blockMeta, final int distance) {
        final LinkedList blocks = new LinkedList();
        for (int x = (int)BlockUtils.mc.thePlayer.posX - distance; x <= (int)BlockUtils.mc.thePlayer.posX + distance; ++x) {
            for (int z = (int)BlockUtils.mc.thePlayer.posZ - distance; z <= (int)BlockUtils.mc.thePlayer.posZ + distance; ++z) {
                for (int height = BlockUtils.mc.theWorld.getHeight(), y = 0; y <= height; ++y) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    final IBlockState blockState = BlockUtils.mc.theWorld.getBlockState(blockPos);
                    if (blockId == -1 || blockMeta == -1) {
                        blocks.add(blockPos);
                    }
                    else {
                        final int id = Block.getIdFromBlock(blockState.getBlock());
                        final int meta = blockState.getBlock().getMetaFromState(blockState);
                        if (id == blockId && meta == blockMeta) {
                            blocks.add(blockPos);
                        }
                    }
                }
            }
        }
        return (LinkedList<BlockPos>)blocks;
    }
    
    public static void breakBlocksPacketSpam(final Iterable<BlockPos> blocks) {
        final Vec3 eyesPos = Utils.getEyesPos();
        final NetHandlerPlayClient connection = BlockUtils.mc.thePlayer.sendQueue;
        for (final BlockPos pos : blocks) {
            final Vec3 posVec = new Vec3((Vec3i)pos).addVector(0.0, 0.0, 0.0);
            final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
            for (final EnumFacing side : EnumFacing.values()) {
                final Vec3 hitVec = posVec.add(new Vec3(side.getDirectionVec()));
                if (eyesPos.squareDistanceTo(hitVec) < distanceSqPosVec) {
                    connection.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
                    connection.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
                    break;
                }
            }
        }
    }
    
    public static float getHorizontalPlayerBlockDistance(final BlockPos blockPos) {
        final float xDiff = (float)(BlockUtils.mc.thePlayer.posX - blockPos.getX());
        final float zDiff = (float)(BlockUtils.mc.thePlayer.posZ - blockPos.getZ());
        return MathHelper.sqrt_double((double)((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f)));
    }
    
    public static BlockPos getHypixelBlockpos(final String str) {
        int val = 89;
        if (str != null && str.length() > 1) {
            final char[] chs = str.toCharArray();
            for (int lenght = chs.length, i = 0; i < lenght; ++i) {
                val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
            }
            val /= str.length();
        }
        return new BlockPos(val, -val % 255, val);
    }
    
    public static boolean isInLiquid() {
        if (BlockUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int)BlockUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static void faceBlockClient(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.0 - BlockUtils.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.0 - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.0 - BlockUtils.mc.thePlayer.posZ;
        final double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 0.0 / 6.984873503E-315));
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
        BlockUtils.mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch);
    }
    
    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getBlock().getMaterial();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static boolean isBlockMaterial(final BlockPos blockPos, final Block block) {
        return getBlock(blockPos) == Blocks.air;
    }
    
    public static boolean isBlockMaterial(final BlockPos blockPos, final Material material) {
        return getState(blockPos).getBlock().getMaterial() == material;
    }
    
    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3((Vec3i)pos).addVector(0.0, 0.0, 0.0)) < eyesPos.squareDistanceTo(new Vec3((Vec3i)neighbor).addVector(0.0, 0.0, 0.0))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3((Vec3i)neighbor).addVector(0.0, 0.0, 0.0).add(new Vec3(side2.getDirectionVec()));
                    if (eyesPos.squareDistanceTo(hitVec) <= 0.0) {
                        faceVectorPacket(hitVec);
                    }
                }
            }
        }
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement());
        Utils.swingMainHand();
        return true;
    }
    
    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return MathHelper.sqrt_double((double)((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f)));
    }
    
    public static void faceVectorPacket(final Vec3 vec) {
        final double diffX = vec.xCoord - BlockUtils.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - BlockUtils.mc.thePlayer.posZ;
        final double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }
    
    public static void faceBlockPacket(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.0 - BlockUtils.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.0 - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.0 - BlockUtils.mc.thePlayer.posZ;
        final double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 0.0 / 6.984873503E-315));
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }
    
    public static float getHardness(final BlockPos pos) {
        return getState(pos).getBlock().getPlayerRelativeBlockHardness((EntityPlayer)BlockUtils.mc.thePlayer, (World)BlockUtils.mc.theWorld, pos);
    }
    
    public static boolean placeBlockSimple(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (getBlock(neighbor).canCollideCheck(getState(neighbor), false)) {
                final Vec3 hitVec = new Vec3((Vec3i)neighbor).addVector(0.0, 0.0, 0.0).add(new Vec3(side2.getDirectionVec()));
                if (eyesPos.squareDistanceTo(hitVec) <= 0.0) {
                    return true;
                }
            }
        }
        return false;
    }
}
