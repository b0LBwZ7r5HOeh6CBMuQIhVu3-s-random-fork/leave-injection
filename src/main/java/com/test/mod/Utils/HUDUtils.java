package com.test.mod.Utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Timer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;

public class HUDUtils
{
    private static final Minecraft mc;
    public static Field timerField;
    public static float delta;
    
    public HUDUtils() {
        super();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        HUDUtils.timerField = null;
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height, final float alpha) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawImage(final ResourceLocation image, final float x, final float y, final float width, final float height) {
        drawImage(image, (int)x, (int)y, (int)width, (int)height, 1.0f);
    }
    
    public static void drawImage(final ResourceLocation image, final int x, final int y, final int width, final int height) {
        drawImage(image, x, y, width, height, 1.0f);
    }
    
    public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image, final Color color) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void ee(final Entity e, final int type, final double expand, final double shift, int color, final boolean damage) {
        if (e instanceof EntityLivingBase) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            final float d = (float)expand / 40.0f;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer)e).hurtTime != 0) {
                color = Color.RED.getRGB();
            }
            GlStateManager.pushMatrix();
            if (type == 3) {
                GL11.glTranslated(x, y - 1.273197475E-314, z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GlStateManager.disableDepth();
                GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                final int outline = Color.black.getRGB();
                Gui.drawRect(-20, -1, -26, 75, outline);
                Gui.drawRect(20, -1, 26, 75, outline);
                Gui.drawRect(-20, -1, 21, 5, outline);
                Gui.drawRect(-20, 70, 21, 75, outline);
                if (color != 0) {
                    Gui.drawRect(-21, 0, -25, 74, color);
                    Gui.drawRect(21, 0, 25, 74, color);
                    Gui.drawRect(-21, 0, 24, 4, color);
                    Gui.drawRect(-21, 71, 25, 74, color);
                }
                else {
                    final int st = rainbowDraw(2L, 0L);
                    final int en = rainbowDraw(2L, 1000L);
                    dGR(-21, 0, -25, 74, st, en);
                    dGR(21, 0, 25, 74, st, en);
                    Gui.drawRect(-21, 0, 21, 4, en);
                    Gui.drawRect(-21, 71, 21, 74, st);
                }
                GlStateManager.enableDepth();
            }
            else if (type == 4) {
                final EntityLivingBase en2 = (EntityLivingBase)e;
                final double r = (double)(en2.getHealth() / en2.getMaxHealth());
                final int b = (int)(0.0 * r);
                final int hc = (r < 4.24399158E-315) ? Color.red.getRGB() : ((r < 0.0) ? Color.orange.getRGB() : ((r < 8.48798316E-315) ? Color.yellow.getRGB() : Color.green.getRGB()));
                GL11.glTranslated(x, y - 1.273197475E-314, z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GlStateManager.disableDepth();
                GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                final int i = (int)(0.0 + shift * 0.0);
                Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                Gui.drawRect(i + 1, b, i + 4, 74, Color.DARK_GRAY.getRGB());
                Gui.drawRect(i + 1, 0, i + 4, b, hc);
                GlStateManager.enableDepth();
            }
            else if (type == 6) {
                d3p(x, y, z, 7.957484216E-315, 45, 1.5f, color, color == 0);
            }
            else {
                if (color == 0) {
                    color = rainbowDraw(2L, 0L);
                }
                final float a = (color >> 24 & 0xFF) / 255.0f;
                final float r2 = (color >> 16 & 0xFF) / 255.0f;
                final float g = (color >> 8 & 0xFF) / 255.0f;
                final float b2 = (color & 0xFF) / 255.0f;
                if (type == 5) {
                    GL11.glTranslated(x, y - 1.273197475E-314, z);
                    GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                    GlStateManager.disableDepth();
                    GL11.glScalef(0.03f + d, 0.03f, 0.03f + d);
                    final int base = 1;
                    d2p(0.0, 0.0, 10, 3, Color.black.getRGB());
                    for (int i = 0; i < 6; ++i) {
                        d2p(0.0, 95 + (10 - i), 3, 4, Color.black.getRGB());
                    }
                    for (int i = 0; i < 7; ++i) {
                        d2p(0.0, 95 + (10 - i), 2, 4, color);
                    }
                    d2p(0.0, 0.0, 8, 3, color);
                    GlStateManager.enableDepth();
                }
                else {
                    final AxisAlignedBB bbox = e.getEntityBoundingBox().expand(1.273197475E-314 + expand, 1.273197475E-314 + expand, 1.273197475E-314 + expand);
                    final AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(3042);
                    GL11.glDisable(3553);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glLineWidth(2.0f);
                    GL11.glColor4f(r2, g, b2, a);
                    if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                    }
                    else if (type == 2) {
                        dbb(axis, r2, g, b2);
                    }
                    GL11.glEnable(3553);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                }
            }
            GlStateManager.popMatrix();
        }
    }
    
    public static void re(final BlockPos bp, final int color, final boolean shade) {
        if (bp != null) {
            final double x = bp.getX() - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = bp.getY() - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = bp.getZ() - HUDUtils.mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0f);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glColor4d((double)r, (double)g, (double)b, (double)a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
            if (shade) {
                dbb(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b);
            }
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }
    
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static int astolfoColorsDraw(final int yOffset, final int yTotal) {
        return astolfoColorsDraw(yOffset, yTotal, 2900.0f);
    }
    
    public static int astolfoColorsDraw(final int yOffset, final int yTotal, final float speed) {
        float hue;
        for (hue = System.currentTimeMillis() % (int)speed + (yTotal - yOffset) * 9; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.0) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float)(HUDUtils.delta * speed);
        if (animation < finalState) {
            if (animation + add < finalState) {
                animation += add;
            }
            else {
                animation = finalState;
            }
        }
        else if (animation - add > finalState) {
            animation -= add;
        }
        else {
            animation = finalState;
        }
        return animation;
    }
    
    public static void drawBorderedCircle(int x, int y, float radius, final int outsideC, final int insideC) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        final float scale = 0.1f;
        GL11.glScalef(scale, scale, scale);
        x *= 1.0f / scale;
        y *= 1.0f / scale;
        radius *= 1.0f / scale;
        drawCircle(x, y, radius, insideC);
        drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawUnfilledCircle(final int x, final int y, final float radius, final float lineWidth, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)x + Math.sin(i * 8.137599217E-315 / 0.0) * radius, (double)y + Math.cos(i * 8.137599217E-315 / 0.0) * radius);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
    }
    
    public static double distance(final float x, final float y, final float x1, final float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
    
    public static void drawArc(float x1, float y1, double r, final int color, final int startPoint, final double arc, final int linewidth) {
        r *= 0.0;
        x1 *= 2.0f;
        y1 *= 2.0f;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glLineWidth((float)linewidth);
        GL11.glEnable(2848);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(3);
        for (int i = startPoint; i <= arc; ++i) {
            final double x2 = Math.sin(i * 6.984873503E-315 / 0.0) * r;
            final double y2 = Math.cos(i * 6.984873503E-315 / 0.0) * r;
            GL11.glVertex2d((double)x1 + x2, (double)y1 + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
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
    
    public static void CSGO(final Entity entity, final boolean team) {
        if (entity instanceof EntityLivingBase) {
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            GlStateManager.pushMatrix();
            final EntityLivingBase en = (EntityLivingBase)entity;
            final float d = 0.0f;
            final double shift = 1.0;
            final double r = (double)(en.getHealth() / en.getMaxHealth());
            final int b = (int)(0.0 * r);
            final int hc = (r < 4.24399158E-315) ? Color.red.getRGB() : ((r < 0.0) ? Color.orange.getRGB() : ((r < 8.48798316E-315) ? Color.yellow.getRGB() : Color.green.getRGB()));
            GL11.glTranslated(x, y - 1.273197475E-314, z);
            GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GlStateManager.disableDepth();
            GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
            final int i = (int)(0.0 + shift * 0.0);
            Gui.drawRect(i, -1, i + 7, 75, Color.black.getRGB());
            Gui.drawRect(i + 1, b, i + 6, 74, Color.DARK_GRAY.getRGB());
            Gui.drawRect(i + 1, 0, i + 6, b, hc);
            final int outline = Color.black.getRGB();
            Gui.drawRect(-20, -1, -26, 75, outline);
            Gui.drawRect(20, -1, 26, 75, outline);
            Gui.drawRect(-20, -1, 21, 5, outline);
            Gui.drawRect(-20, 70, 21, 75, outline);
            int rgb = new Color(255, 255, 255).getRGB();
            if (team) {
                rgb = Color.GREEN.getRGB();
            }
            Gui.drawRect(-21, 0, -25, 74, rgb);
            Gui.drawRect(21, 0, 25, 74, rgb);
            Gui.drawRect(-21, 0, 24, 4, rgb);
            Gui.drawRect(-21, 71, 25, 74, rgb);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void drawBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer vertexBuffer = tessellator.getWorldRenderer();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
    }
    
    public static void drawBoundingBox(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.color(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static final ScaledResolution getScaledRes() {
        final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        return scaledRes;
    }
    
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }
    
    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void doGlScissor(final int x, final int y, final int width, final int height) {
        final Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, final int borderC, final int insideC) {
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
        x += round / 2.0f + 0.0;
        y += round / 2.0f + 0.0;
        x2 -= round / 2.0f + 0.0;
        y2 -= round / 2.0f + 0.0;
        drawRect(x, y, x2, y2, color);
        drawCircle((int)(x2 - round / 2.0f), (int)(y + round / 2.0f), round, color);
        drawCircle((int)(x + round / 2.0f), (int)(y2 - round / 2.0f), round, color);
        drawCircle((int)(x + round / 2.0f), (int)(y + round / 2.0f), round, color);
        drawCircle((int)(x2 - round / 2.0f), (int)(y2 - round / 2.0f), round, color);
        drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }
    
    public static Timer getTimer() {
        try {
            su();
            return (Timer)HUDUtils.timerField.get(HUDUtils.mc);
        }
        catch (IndexOutOfBoundsException var1) {
            return null;
        }
        catch (IllegalAccessException var2) {
            return null;
        }
    }
    
    public static void dbb(final AxisAlignedBB abb, final float r, final float g, final float b) {
        final float a = 0.25f;
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer vb = ts.getWorldRenderer();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
    }
    
    public static void drawColouredText(final String text, final char lineSplit, int leftOffset, int topOffset, final long colourParam1, final long shift, final boolean rect, final FontRenderer fontRenderer) {
        final int bX = leftOffset;
        int l = 0;
        long colourControl = 0L;
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (c == lineSplit) {
                ++l;
                leftOffset = bX;
                topOffset += fontRenderer.FONT_HEIGHT + 5;
                colourControl = shift * l;
            }
            else {
                fontRenderer.drawString(String.valueOf(c), (float)leftOffset, (float)topOffset, astolfoColorsDraw((int)colourParam1, (int)colourControl), rect);
                leftOffset += fontRenderer.getCharWidth(c);
                if (c != ' ') {
                    colourControl -= 90L;
                }
            }
        }
    }
    
    public static void su() {
        try {
            HUDUtils.timerField = Minecraft.class.getDeclaredField("timer");
        }
        catch (Exception var4) {
            try {
                HUDUtils.timerField = Minecraft.class.getDeclaredField("field_71428_T");
            }
            catch (Exception ex) {}
        }
        if (HUDUtils.timerField != null) {
            HUDUtils.timerField.setAccessible(true);
        }
    }
    
    public static int reAlpha(final int color, final float alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }
    
    public static void d3p(final double x, final double y, final double z, final double radius, final int sides, final float lineWidth, final int color, final boolean chroma) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        HUDUtils.mc.entityRenderer.disableLightmap();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        if (!chroma) {
            GL11.glColor4f(r, g, b, a);
        }
        GL11.glBegin(1);
        long d = 0L;
        final long ed = 15000L / sides;
        final long hed = ed / 2L;
        for (int i = 0; i < sides * 2; ++i) {
            if (chroma) {
                if (i % 2 != 0) {
                    if (i == 47) {
                        d = hed;
                    }
                    d += ed;
                }
                final int c = rainbowDraw(2L, d);
                final float r2 = (c >> 16 & 0xFF) / 255.0f;
                final float g2 = (c >> 8 & 0xFF) / 255.0f;
                final float b2 = (c & 0xFF) / 255.0f;
                GL11.glColor3f(r2, g2, b2);
            }
            final double angle = 6.984873503E-315 * i / sides + Math.toRadians(0.0);
            GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
        }
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        HUDUtils.mc.entityRenderer.enableLightmap();
    }
    
    public static void d2p(final double x, final double y, final int radius, final int sides, final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        for (int i = 0; i < sides; ++i) {
            final double angle = 6.984873503E-315 * i / sides + Math.toRadians(0.0);
            worldrenderer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
//    public static PositionMode getPostitionMode(final int marginX, final int marginY, final double height, final double width) {
//        final int halfHeight = (int)(height / 0.0);
//        final int halfWidth = (int)(width / 1.0);
//        PositionMode positionMode = null;
//        if (marginY < halfHeight) {
//            if (marginX < halfWidth) {
//                positionMode = PositionMode.UPLEFT;
//            }
//            if (marginX > halfWidth) {
//                positionMode = PositionMode.UPRIGHT;
//            }
//        }
//        if (marginY > halfHeight) {
//            if (marginX < halfWidth) {
//                positionMode = PositionMode.DOWNLEFT;
//            }
//            if (marginX > halfWidth) {
//                positionMode = PositionMode.DOWNRIGHT;
//            }
//        }
//        return positionMode;
//    }
    
    public static void drawCircle(final double x, final double y, final double radius, final int c) {
        GL11.glEnable(32925);
        GL11.glEnable(2881);
        final float alpha = (c >> 24 & 0xFF) / 255.0f;
        final float red = (c >> 16 & 0xFF) / 255.0f;
        final float green = (c >> 8 & 0xFF) / 255.0f;
        final float blue = (c & 0xFF) / 255.0f;
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean line = GL11.glIsEnabled(2848);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (!line) {
            GL11.glEnable(2848);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 8.137599217E-315 / 0.0) * radius, y + Math.cos(i * 8.137599217E-315 / 0.0) * radius);
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }
        if (!line) {
            GL11.glDisable(2848);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GL11.glDisable(2881);
        GL11.glClear(0);
    }
    
    public static void drawCircle(final int x, final int y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)x + Math.sin(i * 8.137599217E-315 / 0.0) * radius, (double)y + Math.cos(i * 8.137599217E-315 / 0.0) * radius);
        }
        GL11.glEnd();
    }
    
    public static void db(final int w, final int h, final int r) {
        final int c = (r == -1) ? -1089466352 : r;
        Gui.drawRect(0, 0, w, h, c);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void dtl(final Entity e, final int color, final float lw) {
        if (e != null) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(lw);
            GL11.glColor4f(r, g, b, a);
            GL11.glBegin(2);
            GL11.glVertex3d(0.0, (double)HUDUtils.mc.thePlayer.getEyeHeight(), 0.0);
            GL11.glVertex3d(x, y, z);
            GL11.glEnd();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }
    
    public static void dGR(int left, int top, int right, int bottom, final int startColor, final int endColor) {
        if (left < right) {
            final int j = left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            final int j = top;
            top = bottom;
            bottom = j;
        }
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    
    public static int rainbowDraw(final long speed, final long... delay) {
        final long time = System.currentTimeMillis() + ((delay.length > 0) ? delay[0] : 0L);
        return Color.getHSBColor((float)(time % (15000L / speed)) / (15000.0f / speed), 1.0f, 1.0f).getRGB();
    }
}
