package com.test.mod.Utils;


import net.minecraft.client.Minecraft;
import net.minecraft.util.*;
import net.minecraft.init.*;

public class BlockData
{
    public static Minecraft mc = Minecraft.getMinecraft();
    public final BlockPos position;
    public final EnumFacing face;

    public BlockData(final BlockPos add, final EnumFacing up) {
        this.position = add;
        this.face = up;
    }

    public static BlockData getBlockData(final BlockPos pos) {
        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }
}
