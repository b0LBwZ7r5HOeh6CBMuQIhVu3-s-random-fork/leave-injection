package com.test.mod.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.lang.reflect.Method;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import java.util.List;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityEgg;
import java.util.Iterator;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.lang.reflect.Field;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class Utils
{
    public static final Minecraft mc;
    public static boolean lookChanged;
    public static float[] rotationsToBlock;
    private static final Random RANDOM;
    
    public Utils() {
        super();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        Utils.rotationsToBlock = null;
        RANDOM = new Random();
    }
    
    public static void copy(final String content) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
    }
    
    public static int random(final int min, final int max) {
        return Utils.RANDOM.nextInt(max - min) + min;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float getPitch(final Entity entity) {
        final double x = entity.posX - Utils.mc.thePlayer.posX;
        double y = entity.posY - Utils.mc.thePlayer.posY;
        final double z = entity.posZ - Utils.mc.thePlayer.posZ;
        y /= Utils.mc.thePlayer.getDistanceToEntity(entity);
        double pitch = Math.asin(y) * 2.187452604E-315;
        pitch = -pitch;
        return (float)pitch;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    public static int getDistanceFromMouse(final EntityLivingBase entity) {
        final float[] neededRotations = getRotationsNeeded((Entity)entity);
        if (neededRotations != null) {
            final float neededYaw = Utils.mc.thePlayer.rotationYaw - neededRotations[0];
            final float neededPitch = Utils.mc.thePlayer.rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_double((double)(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f));
            return (int)distanceFromMouse;
        }
        return -1;
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity, final float width, final float height) {
        if (entity.width == width && entity.height == height) {
            return;
        }
        entity.width = width;
        entity.height = height;
        final double d0 = width / 0.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    public static boolean checkEnemyNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        return !getEntityNameColor((EntityLivingBase)Utils.mc.thePlayer).equals(getEntityNameColor(entity));
    }
    
    public static int getCurrentPlayerSlot() {
        return Minecraft.getMinecraft().thePlayer.inventory.currentItem;
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
    
    public static boolean placeBlockScaffold(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(Utils.mc.thePlayer.posX, Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight(), Utils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3((Vec3i)pos).addVector(0.0, 0.0, 0.0)) < eyesPos.squareDistanceTo(new Vec3((Vec3i)neighbor).addVector(0.0, 0.0, 0.0)) && canBeClicked(neighbor)) {
                final Vec3 posVec = new Vec3((Vec3i)neighbor).addVector(0.0, 0.0, 0.0);
                final Vec3 dirVec = new Vec3(side2.getDirectionVec());
                final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.0, dirVec.yCoord * 0.0, dirVec.zCoord * 0.0));
                if (eyesPos.squareDistanceTo(hitVec) <= 0.0) {
                    faceVectorPacketInstant(hitVec);
                    swingMainHand();
                    while (true) {
                        if (Utils.mc.playerController.onPlayerRightClick(Utils.mc.thePlayer, Utils.mc.theWorld, Utils.mc.thePlayer.getItemInUse(), neighbor, side2, hitVec)) {
                            try {
                                final Field f = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "rightClickDelayTimer", "rightClickDelayTimer" });
                                f.setAccessible(true);
                                f.set(Utils.mc, 4);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        continue;
                    }
                }
            }
        }
        return false;
    }
    
    public static int getPlayerArmorColor(final EntityPlayer player, final ItemStack stack) {
        if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor)) {
            return -1;
        }
        final ItemArmor itemArmor = (ItemArmor)stack.getItem();
        if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) {
            return -1;
        }
        return itemArmor.getColor(stack);
    }
    
    public static double[] teleportToPosition(final double[] startPosition, final double[] endPosition, final double setOffset, final double slack, final boolean extendOffset, final boolean onGround) {
        boolean wasSneaking = false;
        if (Utils.mc.thePlayer.isSneaking()) {
            wasSneaking = true;
        }
        double startX = startPosition[0];
        double startY = startPosition[1];
        double startZ = startPosition[2];
        final double endX = endPosition[0];
        final double endY = endPosition[1];
        final double endZ = endPosition[2];
        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
            if (count > 120) {
                break;
            }
            final double offset = (extendOffset && (count & 0x1) == 0x0) ? (setOffset + 4.24399158E-315) : setOffset;
            final double diffX = startX - endX;
            final double diffY = startY - endY;
            final double diffZ = startZ - endZ;
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX += offset;
                }
                else {
                    startX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    startX -= offset;
                }
                else {
                    startX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY += offset;
                }
                else {
                    startY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > offset) {
                    startY -= offset;
                }
                else {
                    startY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ += offset;
                }
                else {
                    startZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    startZ -= offset;
                }
                else {
                    startZ -= Math.abs(diffZ);
                }
            }
            if (wasSneaking) {
                Utils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Utils.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            Utils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(startX, startY, startZ, onGround));
            ++count;
        }
        if (wasSneaking) {
            Utils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Utils.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return new double[] { startX, startY, startZ };
    }
    
    public static float[] getSmoothNeededRotations(final Vec3 vec, final float yaw, final float pitch) {
        final Vec3 eyesPos = getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float rotationPitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { updateRotation(Utils.mc.thePlayer.rotationYaw, rotationYaw, yaw / 4.0f), updateRotation(Utils.mc.thePlayer.rotationPitch, rotationPitch, pitch / 4.0f) };
    }
    
    public static float[] getNeededRotations(final Vec3 vec) {
        final Vec3 eyesPos = getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Utils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Utils.mc.thePlayer.rotationYaw), Utils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Utils.mc.thePlayer.rotationPitch) };
    }
    
    public static EntityLivingBase getWorldEntityByName(final String name) {
        EntityLivingBase entity = null;
        for (final Object object : getEntityList()) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entityForCheck = (EntityLivingBase)object;
                if (!entityForCheck.getName().contains(name)) {
                    continue;
                }
                entity = entityForCheck;
            }
        }
        return entity;
    }
    
    public static float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg((World)Utils.mc.theWorld);
        var4.posX = var0 + 0.0;
        var4.posY = var1 + 0.0;
        var4.posZ = var2 + 0.0;
        final EntityEgg entityEgg = var4;
        entityEgg.posX += var3.getDirectionVec().getX() * 0.0;
        final EntityEgg entityEgg2 = var4;
        entityEgg2.posY += var3.getDirectionVec().getY() * 0.0;
        final EntityEgg entityEgg3 = var4;
        entityEgg3.posZ += var3.getDirectionVec().getZ() * 0.0;
        return getDirectionToEntity((Entity)var4);
    }
    
    public static String getEntityNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        if (name.contains("§")) {
            if (name.contains("§1")) {
                return "§1";
            }
            if (name.contains("§2")) {
                return "§2";
            }
            if (name.contains("§3")) {
                return "§3";
            }
            if (name.contains("§4")) {
                return "§4";
            }
            if (name.contains("§5")) {
                return "§5";
            }
            if (name.contains("§6")) {
                return "§6";
            }
            if (name.contains("§7")) {
                return "§7";
            }
            if (name.contains("§8")) {
                return "§8";
            }
            if (name.contains("§9")) {
                return "§9";
            }
            if (name.contains("§0")) {
                return "§0";
            }
            if (name.contains("§e")) {
                return "§e";
            }
            if (name.contains("§d")) {
                return "§d";
            }
            if (name.contains("§a")) {
                return "§a";
            }
            if (name.contains("§b")) {
                return "§b";
            }
            if (name.contains("§c")) {
                return "§c";
            }
            if (name.contains("§f")) {
                return "§f";
            }
        }
        return "null";
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 4.24399158E-315;
        final double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 0.0 / 6.984873503E-315));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Utils.mc.thePlayer.posX;
        final double diffZ = entity.posZ - Utils.mc.thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 0.0 - (Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight());
        }
        final double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 0.0 / 6.984873503E-315) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 0.0 / 6.984873503E-315));
        return new float[] { Utils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Utils.mc.thePlayer.rotationYaw), Utils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Utils.mc.thePlayer.rotationPitch) };
    }
    
    private static float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + Utils.mc.thePlayer.rotationYaw, getPitch(var0) + Utils.mc.thePlayer.rotationPitch };
    }
    
    public static void faceVectorPacketInstant(final Vec3 vec) {
        Utils.rotationsToBlock = getNeededRotations(vec);
    }
    
    public static boolean isPlayer(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final String entityName = getPlayerName(player);
            final String playerName = getPlayerName((EntityPlayer)Utils.mc.thePlayer);
            if (entityName.equals(playerName)) {
                return true;
            }
        }
        return false;
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final int j = (int)left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            final int j = (int)top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void rect(final float x1, final float y1, final float x2, final float y2, final int fill) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        final float f = (fill >> 24 & 0xFF) / 255.0f;
        final float f2 = (fill >> 16 & 0xFF) / 255.0f;
        final float f3 = (fill >> 8 & 0xFF) / 255.0f;
        final float f4 = (fill & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void rect(final double x1, final double y1, final double x2, final double y2, final int fill) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        final float f = (fill >> 24 & 0xFF) / 255.0f;
        final float f2 = (fill >> 16 & 0xFF) / 255.0f;
        final float f3 = (fill >> 8 & 0xFF) / 255.0f;
        final float f4 = (fill & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static void assistFaceEntity(final Entity entity, final float yaw, final float pitch) {
        if (entity == null) {
            return;
        }
        final double diffX = entity.posX - Utils.mc.thePlayer.posX;
        final double diffZ = entity.posZ - Utils.mc.thePlayer.posZ;
        double yDifference;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight());
        }
        else {
            yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 0.0 - (Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight());
        }
        final double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float)(Math.atan2(diffZ, diffX) * 0.0 / 6.984873503E-315) - 90.0f;
        final float rotationPitch = (float)(-(Math.atan2(yDifference, dist) * 0.0 / 6.984873503E-315));
        if (yaw > 0.0f) {
            Utils.mc.thePlayer.rotationYaw = updateRotation(Utils.mc.thePlayer.rotationYaw, rotationYaw, yaw / 4.0f);
        }
        if (pitch > 0.0f) {
            Utils.mc.thePlayer.rotationPitch = updateRotation(Utils.mc.thePlayer.rotationPitch, rotationPitch, pitch / 4.0f);
        }
    }
    
    public static double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
    
    public static List<Entity> getEntityList() {
        return (List<Entity>)Utils.mc.theWorld.getLoadedEntityList();
    }
    
    public static boolean isMoving(final Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
    }
    
    public static float getDirection() {
        float var1 = Utils.mc.thePlayer.rotationYaw;
        if (Utils.mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Utils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (Utils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Utils.mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Utils.mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        var1 *= 0.017453292f;
        return var1;
    }
    
    public static boolean checkEnemyColor(final EntityPlayer enemy) {
        final int colorEnemy0 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
        final int colorEnemy2 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
        final int colorEnemy3 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
        final int colorEnemy4 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));
        final int colorPlayer0 = getPlayerArmorColor((EntityPlayer)Utils.mc.thePlayer, Utils.mc.thePlayer.inventory.armorItemInSlot(0));
        final int colorPlayer2 = getPlayerArmorColor((EntityPlayer)Utils.mc.thePlayer, Utils.mc.thePlayer.inventory.armorItemInSlot(1));
        final int colorPlayer3 = getPlayerArmorColor((EntityPlayer)Utils.mc.thePlayer, Utils.mc.thePlayer.inventory.armorItemInSlot(2));
        final int colorPlayer4 = getPlayerArmorColor((EntityPlayer)Utils.mc.thePlayer, Utils.mc.thePlayer.inventory.armorItemInSlot(3));
        return (colorEnemy0 != colorPlayer0 || colorPlayer0 == -1 || colorEnemy0 == 1) && (colorEnemy2 != colorPlayer2 || colorPlayer2 == -1 || colorEnemy2 == 1) && (colorEnemy3 != colorPlayer3 || colorPlayer3 == -1 || colorEnemy3 == 1) && (colorEnemy4 != colorPlayer4 || colorPlayer4 == -1 || colorEnemy4 == 1);
    }
    
    public static void hotkeyToSlot(final int slot) {
        Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
    }
    
    public static final ScaledResolution getScaledRes() {
        final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        return scaledRes;
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return BlockUtils.getBlock(pos).canCollideCheck(BlockUtils.getState(pos), false);
    }
    
    public static Vec3 getEyesPos() {
        return new Vec3(Utils.mc.thePlayer.posX, Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight(), Utils.mc.thePlayer.posZ);
    }
    
    public static void faceVectorPacket(final Vec3 vec) {
        final float[] rotations = getNeededRotations(vec);
        final EntityPlayerSP pl = Minecraft.getMinecraft().thePlayer;
        final float preYaw = pl.rotationYaw;
        final float prePitch = pl.rotationPitch;
        pl.rotationYaw = rotations[0];
        pl.rotationPitch = rotations[1];
        try {
            final Method onUpdateWalkingPlayer = pl.getClass().getDeclaredMethod(Mapping.onUpdateWalkingPlayer, (Class<?>[])new Class[0]);
            onUpdateWalkingPlayer.setAccessible(true);
            onUpdateWalkingPlayer.invoke(pl, new Object[0]);
        }
        catch (Exception ex) {}
        pl.rotationYaw = preYaw;
        pl.rotationPitch = prePitch;
    }
    
    public static void swingMainHand() {
        Utils.mc.thePlayer.swingItem();
    }
    
    public static String getPlayerName(final EntityPlayer player) {
        return (player.getGameProfile() != null) ? player.getGameProfile().getName() : player.getName();
    }
    
    public static float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
    
    private static BlockData getBlockData(final BlockPos pos) {
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(0, 1, 0))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 1, 0))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(0, 1, 0))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (isPosSolid(pos5.add(0, 1, 0))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(0, 1, 0))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (isPosSolid(pos3.add(0, 1, 0))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(0, 1, 0))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos5.add(0, 1, 0))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos10 = pos.add(0, -1, 0);
        if (isPosSolid(pos10.add(0, -1, 0))) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos10.add(0, 1, 0))) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos10.add(-1, 0, 0))) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos10.add(1, 0, 0))) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos10.add(0, 0, 1))) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos10.add(0, 0, -1))) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (isPosSolid(pos11.add(0, -1, 0))) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos11.add(0, 1, 0))) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos11.add(-1, 0, 0))) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos11.add(1, 0, 0))) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos11.add(0, 0, 1))) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos11.add(0, 0, -1))) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (isPosSolid(pos12.add(0, -1, 0))) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos12.add(0, 1, 0))) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos12.add(-1, 0, 0))) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos12.add(1, 0, 0))) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos12.add(0, 0, 1))) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos12.add(0, 0, -1))) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (isPosSolid(pos13.add(0, -1, 0))) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos13.add(0, 1, 0))) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos13.add(-1, 0, 0))) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos13.add(1, 0, 0))) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos13.add(0, 0, 1))) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos13.add(0, 0, -1))) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (isPosSolid(pos14.add(0, -1, 0))) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos14.add(0, 1, 0))) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.DOWN);
        }
        if (isPosSolid(pos14.add(-1, 0, 0))) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos14.add(1, 0, 0))) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos14.add(0, 0, 1))) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos14.add(0, 0, -1))) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    
    public static boolean screenCheck() {
        return !(Utils.mc.currentScreen instanceof GuiContainer) && !(Utils.mc.currentScreen instanceof GuiChat) && !(Utils.mc.currentScreen instanceof GuiScreen);
    }
    
    public static void attack(final Entity entity) {
        Utils.mc.playerController.attackEntity((EntityPlayer)Utils.mc.thePlayer, entity);
    }
    
    public static void faceEntity(final EntityLivingBase entity) {
        if (entity == null) {
            return;
        }
        final double d0 = entity.posX - Utils.mc.thePlayer.posX;
        final double d2 = entity.posY - Utils.mc.thePlayer.posY;
        final double d3 = entity.posZ - Utils.mc.thePlayer.posZ;
        final double d4 = Utils.mc.thePlayer.posY + Utils.mc.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double d5 = (double)MathHelper.sqrt_double(d0 * d0 + d3 * d3);
        final float f = (float)(Math.atan2(d3, d0) * 0.0 / 6.984873503E-315) - 90.0f;
        final float f2 = (float)(-(Math.atan2(d4, d5) * 0.0 / 6.984873503E-315));
        Utils.mc.thePlayer.rotationYaw = f;
        Utils.mc.thePlayer.rotationPitch = f2;
    }
    
    public static void selfDamage(final double posY) {
        if (!Utils.mc.thePlayer.onGround) {
            return;
        }
        for (int i = 0; i <= 0.0; ++i) {
            Utils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Utils.mc.thePlayer.posX, Utils.mc.thePlayer.posY + posY, Utils.mc.thePlayer.posZ, false));
            Utils.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Utils.mc.thePlayer.posX, Utils.mc.thePlayer.posY, Utils.mc.thePlayer.posZ, i == 0.0));
        }
        final EntityPlayerSP thePlayer = Utils.mc.thePlayer;
        thePlayer.motionX *= 1.273197475E-314;
        final EntityPlayerSP thePlayer2 = Utils.mc.thePlayer;
        thePlayer2.motionZ *= 1.273197475E-314;
        swingMainHand();
    }
    
    public static Vec3 getRandomCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 1.273197475E-314 * Math.random(), bb.minY + (bb.maxY - bb.minY) * Math.random() + 1.273197475E-314 * Math.random(), bb.minZ + (bb.maxZ - bb.minZ) * 1.273197475E-314 * Math.random());
    }
    
    public static float getYaw(final Entity entity) {
        final double x = entity.posX - Utils.mc.thePlayer.posX;
        final double y = entity.posY - Utils.mc.thePlayer.posY;
        final double z = entity.posZ - Utils.mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 2.187452604E-315;
        yaw = -yaw;
        return (float)yaw;
    }
    
    public static boolean isBlockEdge(final EntityLivingBase entity) {
        return Utils.mc.theWorld.getCollidingBoundingBoxes((Entity)entity, entity.getEntityBoundingBox().offset(0.0, 0.0, 0.0).expand(1.748524532E-314, 0.0, 1.748524532E-314)).isEmpty() && entity.onGround;
    }
    
    private static boolean isPosSolid(final BlockPos pos) {
        final Block block = Utils.mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }
    
    public static Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.0;
        double y = pos.getY() + 0.0;
        double z = pos.getZ() + 0.0;
        x += face.getFrontOffsetX() / 0.0;
        z += face.getFrontOffsetZ() / 0.0;
        y += face.getFrontOffsetY() / 0.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber(4.24399158E-315, 4.24399158E-315);
            z += randomNumber(4.24399158E-315, 4.24399158E-315);
        }
        else {
            y += randomNumber(4.24399158E-315, 4.24399158E-315);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += randomNumber(4.24399158E-315, 4.24399158E-315);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += randomNumber(4.24399158E-315, 4.24399158E-315);
        }
        return new Vec3(x, y, z);
    }
    
    public static float getYawChange(final float yaw, final double posX, final double posZ) {
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 0.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 0.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }
    
    public static float getPitchChange(final float pitch, final Entity entity, final double posY) {
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double deltaY = posY - 1.273197475E-314 + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        final double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5f;
    }
}
