package com.test.mod.module.modules.render;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.awt.Color;
import com.test.mod.Utils.RenderUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import java.util.ArrayList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.test.mod.Utils.ReflectUtil;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class Projectiles extends Module
{
    public Projectiles() {
        super("Projectiles", 0, ModuleType.Render, false);
    }
    
    public static <T> T copy(final T src, final T dst) {
        for (final Field f : getAllFields(src.getClass())) {
            if (!Modifier.isFinal(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    try {
                        f.set(dst, f.get((Object)src));
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        return dst;
    }
    
    public static Object getFieldValue(final Class<?> clazz, final String... fields) {
        for (final String string : fields) {
            try {
                final Field f = clazz.getDeclaredField(string);
                if (f != null) {
                    f.setAccessible(true);
                    try {
                        return f.get(null);
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return null;
                    }
                    catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                        return null;
                    }
                }
            }
            catch (NoSuchFieldException e3) {}
        }
        return null;
    }
    
    public static Object getFieldValue(final Object obj, final String... fields) {
        final Class clazz = obj.getClass();
        for (final String string : fields) {
            try {
                final Field f = clazz.getDeclaredField(string);
                if (f != null) {
                    f.setAccessible(true);
                    try {
                        return f.get(obj);
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return null;
                    }
                    catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                        return null;
                    }
                }
            }
            catch (NoSuchFieldException e3) {}
        }
        return null;
    }
    
    public static double getRenderPosY() {
        return (double)ReflectUtil.getField("renderPosY", "field_78726_c", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosZ() {
        return (double)ReflectUtil.getField("renderPosZ", "field_78728_n", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosX() {
        return (double)ReflectUtil.getField("renderPosX", "field_78725_b", Minecraft.getMinecraft().getRenderManager());
    }
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        final RenderManager renderManager = Projectiles.mc.getRenderManager();
        try {
            final double renderPosX = getRenderPosX();
            final double renderPosY = getRenderPosY();
            final double renderPosZ = getRenderPosZ();
            if (Module.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            final Item item = Module.mc.thePlayer.getHeldItem().getItem();
            boolean isBow = false;
            float motionFactor = 1.5f;
            float motionSlowdown = 0.99f;
            float gravity = 0.0f;
            float size = 0.0f;
            if (item instanceof ItemBow) {
                if (!Module.mc.thePlayer.isUsingItem()) {
                    return;
                }
                isBow = true;
                gravity = 0.05f;
                size = 0.3f;
                float power = Module.mc.thePlayer.getItemInUseDuration() / 20.0f;
                if ((power = (power * power + power * 2.0f) / 3.0f) < 0.1f) {
                    return;
                }
                if (power > 1.0f) {
                    power = 1.0f;
                }
                motionFactor = power * 3.0f;
            }
            else if (item instanceof ItemFishingRod) {
                gravity = 0.04f;
                size = 0.25f;
                motionSlowdown = 0.92f;
            }
            else if (item instanceof ItemPotion && ItemPotion.isSplash(Module.mc.thePlayer.getHeldItem().getItemDamage())) {
                gravity = 0.05f;
                size = 0.25f;
                motionFactor = 0.5f;
            }
            else {
                if (!(item instanceof ItemSnowball) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemEgg)) {
                    return;
                }
                gravity = 0.03f;
                size = 0.25f;
            }
            final float yaw = Module.mc.thePlayer.rotationYaw;
            final float pitch = Module.mc.thePlayer.rotationPitch;
            double posX = renderPosX - MathHelper.cos(yaw / 180.0f * 3.1415927f) * 0.16f;
            double posY = renderPosY + Module.mc.thePlayer.getEyeHeight() - 1.3262473694E-314;
            double posZ = renderPosZ - MathHelper.sin(yaw / 180.0f * 3.1415927f) * 0.16f;
            double motionX = -MathHelper.sin(yaw / 180.0f * 3.1415927f) * MathHelper.cos(pitch / 180.0f * 3.1415927f) * (isBow ? 1.0 : 1.273197475E-314);
            double motionY = -MathHelper.sin((pitch + ((item instanceof ItemPotion && ItemPotion.isSplash(Module.mc.thePlayer.getHeldItem().getItemDamage())) ? -20 : 0)) / 180.0f * 3.1415927f) * (isBow ? 1.0 : 1.273197475E-314);
            double motionZ = MathHelper.cos(yaw / 180.0f * 3.1415927f) * MathHelper.cos(pitch / 180.0f * 3.1415927f) * (isBow ? 1.0 : 1.273197475E-314);
            final float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            motionX /= distance;
            motionY /= distance;
            motionZ /= distance;
            motionX *= motionFactor;
            motionY *= motionFactor;
            motionZ *= motionFactor;
            MovingObjectPosition landingPosition = null;
            boolean hasLanded = false;
            boolean hitEntity = false;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            final List pos = new ArrayList();
            while (!hasLanded && posY > 0.0) {
                Object posBefore = new Vec3(posX, posY, posZ);
                Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                landingPosition = Module.mc.theWorld.rayTraceBlocks((Vec3)posBefore, posAfter, false, true, false);
                posBefore = new Vec3(posX, posY, posZ);
                posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                if (landingPosition != null) {
                    hasLanded = true;
                    posAfter = new Vec3(landingPosition.hitVec.xCoord, landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
                }
                final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size, posY + size, posZ + size).addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0);
                final int chunkMinX = MathHelper.floor_double((arrowBox.minX - 0.0) / 0.0);
                final int chunkMaxX = MathHelper.floor_double((arrowBox.maxX + 0.0) / 0.0);
                final int chunkMinZ = MathHelper.floor_double((arrowBox.minZ - 0.0) / 0.0);
                final int chunkMaxZ = MathHelper.floor_double((arrowBox.maxZ + 0.0) / 0.0);
                final List<Entity> collidedEntities = new ArrayList();
                int n = chunkMinX;
                if (n <= chunkMaxX) {
                    int x;
                    do {
                        x = n++;
                        int n2 = chunkMinZ;
                        if (n2 > chunkMaxZ) {
                            continue;
                        }
                        int z;
                        do {
                            z = n2++;
                            Module.mc.theWorld.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity((Entity)Module.mc.thePlayer, arrowBox, collidedEntities, (Predicate)null);
                        } while (z != chunkMaxZ);
                    } while (x != chunkMaxX);
                }
                for (final Entity possibleEntity : collidedEntities) {
                    if (possibleEntity.canBeCollidedWith() && possibleEntity != Module.mc.thePlayer) {
                        final AxisAlignedBB possibleEntityBoundingBox;
                        final MovingObjectPosition movingObjectPosition;
                        if ((movingObjectPosition = (possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox().expand((double)size, (double)size, (double)size)).calculateIntercept((Vec3)posBefore, posAfter)) == null) {
                            continue;
                        }
                        final MovingObjectPosition possibleEntityLanding = movingObjectPosition;
                        hitEntity = true;
                        hasLanded = true;
                        landingPosition = possibleEntityLanding;
                    }
                }
                if (Module.mc.theWorld.getBlockState(new BlockPos(posX += motionX, posY += motionY, posZ += motionZ)).getBlock().getMaterial() == Material.water) {
                    motionX *= 4.24399158E-315;
                    motionY *= 4.24399158E-315;
                    motionZ *= 4.24399158E-315;
                }
                else {
                    motionX *= motionSlowdown;
                    motionY *= motionSlowdown;
                    motionZ *= motionSlowdown;
                }
                motionY -= gravity;
                pos.add(new Vec3(posX - renderPosX, posY - renderPosY, posZ - renderPosZ));
            }
            GL11.glDepthMask(false);
            RenderUtils.enableGlCap(3042, 2848);
            RenderUtils.disableGlCap(2929, 3008, 3553);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            final Color color = hitEntity ? new Color(255, 140, 140) : new Color(140, 255, 140);
            RenderUtils.glColor(color);
            GL11.glLineWidth(2.0f);
            worldRenderer.begin(3, DefaultVertexFormats.POSITION);
            final boolean $i$f$forEach = false;
            for (final Object element$iv : pos) {
                final Vec3 it = (Vec3)element$iv;
                final boolean bl = false;
                worldRenderer.pos(it.xCoord, it.yCoord, it.zCoord).endVertex();
            }
            tessellator.draw();
            GL11.glPushMatrix();
            GL11.glTranslated(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);
            if (landingPosition != null) {
                switch (landingPosition.sideHit.getAxis().ordinal()) {
                    case 0:
                        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                        break;
                    case 2:
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        break;
                }
                RenderUtils.drawAxisAlignedBB(new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 1.273197475E-314, 0.0), color, true, true, 3.0f);
            }
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            RenderUtils.resetCaps();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        catch (NullPointerException ex) {}
    }
    
    public static void setFieldValue(final Object obj, final Object value, final String... fields) {
        final Class clazz = obj.getClass();
        for (final String string : fields) {
            try {
                final Field f = clazz.getDeclaredField(string);
                if (f != null) {
                    f.setAccessible(true);
                    try {
                        f.set(obj, value);
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }
                    catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
            catch (NoSuchFieldException e3) {}
        }
    }
    
    public static void setFieldValue(final Class<?> clazz, final Object value, final String... fields) {
        for (final String string : fields) {
            try {
                final Field f = clazz.getDeclaredField(string);
                if (f != null) {
                    f.setAccessible(true);
                    try {
                        f.set(null, value);
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            catch (NoSuchFieldException e3) {}
        }
    }
    
    public static Field[] getAllFields(Class<?> clazz) {
        final ArrayList<Field> fields = new ArrayList();
        do {
            for (final Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return fields.toArray(null);
    }
}
