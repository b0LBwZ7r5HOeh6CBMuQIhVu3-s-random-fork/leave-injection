package com.test.mod.Utils;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.potion.Potion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class MoveUtils
{
    private static Minecraft mc;
    
    public MoveUtils() {
        super();
    }
    
    static {
        MoveUtils.mc = Minecraft.getMinecraft();
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static float getDistanceToGround(final Entity e) {
        if (MoveUtils.mc.thePlayer.isCollidedVertically && MoveUtils.mc.thePlayer.onGround) {
            return 0.0f;
        }
        float a = (float)e.posY;
        while (a > 0.0f) {
            final int[] stairs = { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
            final int[] exemptIds = { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177 };
            final Block block = MoveUtils.mc.theWorld.getBlockState(new BlockPos(e.posX, (double)(a - 1.0f), e.posZ)).getBlock();
            if (!(block instanceof BlockAir)) {
                if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                    return ((float)(e.posY - a - 0.0) < 0.0f) ? 0.0f : ((float)(e.posY - a - 0.0));
                }
                int[] arrayOfInt1;
                for (int j = (arrayOfInt1 = stairs).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a - 1.0) < 0.0f) ? 0.0f : ((float)(e.posY - a - 1.0));
                    }
                }
                for (int j = (arrayOfInt1 = exemptIds).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a) < 0.0f) ? 0.0f : ((float)(e.posY - a));
                    }
                }
                return (float)(e.posY - a + block.getBlockBoundsMaxY() - 1.0);
            }
            else {
                --a;
            }
        }
        return 0.0f;
    }
    
    public static float[] getRotationsBlock(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.0 - MoveUtils.mc.thePlayer.posX + face.getFrontOffsetX() / 0.0;
        final double z = block.getZ() + 0.0 - MoveUtils.mc.thePlayer.posZ + face.getFrontOffsetZ() / 0.0;
        final double y = block.getY() + 0.0;
        final double d1 = MoveUtils.mc.thePlayer.posY + MoveUtils.mc.thePlayer.getEyeHeight() - y;
        final double d2 = (double)MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 0.0 / 6.984873503E-315);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public static void strafe(final double speed) {
        final float a = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f;
        final float l = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 4.712389f;
        final float r = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 2.3876104f;
        if (MoveUtils.mc.gameSettings.keyBindForward.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer = MoveUtils.mc.thePlayer;
                thePlayer.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP thePlayer2 = MoveUtils.mc.thePlayer;
                thePlayer2.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer3 = MoveUtils.mc.thePlayer;
                thePlayer3.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP thePlayer4 = MoveUtils.mc.thePlayer;
                thePlayer4.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP thePlayer5 = MoveUtils.mc.thePlayer;
                thePlayer5.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer6 = MoveUtils.mc.thePlayer;
                thePlayer6.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer7 = MoveUtils.mc.thePlayer;
                thePlayer7.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP thePlayer8 = MoveUtils.mc.thePlayer;
                thePlayer8.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer9 = MoveUtils.mc.thePlayer;
                thePlayer9.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP thePlayer10 = MoveUtils.mc.thePlayer;
                thePlayer10.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP thePlayer11 = MoveUtils.mc.thePlayer;
                thePlayer11.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer12 = MoveUtils.mc.thePlayer;
                thePlayer12.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer13 = MoveUtils.mc.thePlayer;
            thePlayer13.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP thePlayer14 = MoveUtils.mc.thePlayer;
            thePlayer14.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer15 = MoveUtils.mc.thePlayer;
            thePlayer15.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP thePlayer16 = MoveUtils.mc.thePlayer;
            thePlayer16.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static boolean isMoving() {
        return MoveUtils.mc.thePlayer != null && (MoveUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MoveUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isOnGround(final double height) {
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, MoveUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = MoveUtils.mc.thePlayer.capabilities.getWalkSpeed() * 8.997262156E-315;
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 1.273197475E-314 * (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 1.273197475E-314 * (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static void setMotion(final double speed) {
        double forward = (double)MoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)MoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MoveUtils.mc.thePlayer.motionX = 0.0;
            MoveUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = 0.0;
                }
            }
            MoveUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f)));
            MoveUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f)));
        }
    }
    
    public static double defaultSpeed() {
        double baseSpeed = 1.1441801305E-314;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 1.273197475E-314 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void setSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static void setSpeed(final double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = 0.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = 0.0;
        }
        final double mx = Math.cos(Math.toRadians((double)(yaw + 90.0f)));
        final double mz = Math.sin(Math.toRadians((double)(yaw + 90.0f)));
        MoveUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MoveUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static int getSpeedEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static double getDirection() {
        float rotationYaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MoveUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static int getJumpEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static boolean checkTeleport(final double x, final double y, final double z, final double distBetweenPackets) {
        final double distx = MoveUtils.mc.thePlayer.posX - x;
        final double disty = MoveUtils.mc.thePlayer.posY - y;
        final double distz = MoveUtils.mc.thePlayer.posZ - z;
        final double dist = Math.sqrt(MoveUtils.mc.thePlayer.getDistanceSq(x, y, z));
        final double nbPackets = (double)(Math.round(dist / distBetweenPackets + 2.121906788E-314) - 1L);
        double xtp = MoveUtils.mc.thePlayer.posX;
        double ytp = MoveUtils.mc.thePlayer.posY;
        double ztp = MoveUtils.mc.thePlayer.posZ;
        for (int i = 1; i < nbPackets; ++i) {
            final double xdi = (x - MoveUtils.mc.thePlayer.posX) / nbPackets;
            xtp += xdi;
            final double zdi = (z - MoveUtils.mc.thePlayer.posZ) / nbPackets;
            ztp += zdi;
            final double ydi = (y - MoveUtils.mc.thePlayer.posY) / nbPackets;
            ytp += ydi;
            final AxisAlignedBB bb = new AxisAlignedBB(xtp - 4.24399158E-315, ytp, ztp - 4.24399158E-315, xtp + 4.24399158E-315, ytp + 1.697596633E-314, ztp + 4.24399158E-315);
            if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isRealCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 4.24399158E-315, MoveUtils.mc.thePlayer.posY + 0.0, MoveUtils.mc.thePlayer.posZ + 4.24399158E-315, MoveUtils.mc.thePlayer.posX + 4.24399158E-315, MoveUtils.mc.thePlayer.posY + 8.48798316E-315, MoveUtils.mc.thePlayer.posZ - 4.24399158E-315);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(4.24399158E-315 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(4.24399158E-315 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 4.24399158E-315 + dist)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 4.24399158E-315 - dist)).isEmpty();
    }
    
    public static void strafeHYT(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MoveUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static Block getBlockAtPosC(final double x, final double y, final double z) {
        final EntityPlayer inPlayer = (EntityPlayer)Minecraft.getMinecraft().thePlayer;
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }
    
    public static boolean isCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 4.24399158E-315, MoveUtils.mc.thePlayer.posY + 0.0, MoveUtils.mc.thePlayer.posZ + 4.24399158E-315, MoveUtils.mc.thePlayer.posX + 4.24399158E-315, MoveUtils.mc.thePlayer.posY + 0.0, MoveUtils.mc.thePlayer.posZ - 4.24399158E-315);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(4.24399158E-315 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(4.24399158E-315 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 4.24399158E-315 + dist)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 4.24399158E-315 - dist)).isEmpty();
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 4.24399158E-315, MoveUtils.mc.thePlayer.posY + MoveUtils.mc.thePlayer.getEyeHeight(), MoveUtils.mc.thePlayer.posZ + 4.24399158E-315, MoveUtils.mc.thePlayer.posX + 4.24399158E-315, MoveUtils.mc.thePlayer.posY + 0.0, MoveUtils.mc.thePlayer.posZ - 4.24399158E-315);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb).isEmpty();
    }
}
