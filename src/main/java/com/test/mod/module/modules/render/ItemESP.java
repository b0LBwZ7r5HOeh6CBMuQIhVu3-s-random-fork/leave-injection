package com.test.mod.module.modules.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import com.test.mod.module.ModuleType;
import com.test.mod.module.Module;

public class ItemESP extends Module
{
    public ItemESP() {
        super("ItemESP", 0, ModuleType.Render, false);
    }
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        RenderHelper.enableGUIStandardItemLighting();
        for (final Object object : getEntityList()) {
            if (object instanceof EntityItem || object instanceof EntityArrow) {
                final Entity entity = (Entity)object;
                drawESP(entity, 255.0f, 255.0f, 255.0f, 1.0f, event.partialTicks);
            }
        }
        RenderHelper.disableStandardItemLighting();
    }
    
    public static List getEntityList() {
        return ItemESP.mc.theWorld.getLoadedEntityList();
    }
    
    public static void drawESP(final Entity entity, final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha, final float ticks) {
        try {
            final double exception = ItemESP.mc.getRenderManager().viewerPosX;
            final double renderPosY = ItemESP.mc.getRenderManager().viewerPosY;
            final double renderPosZ = ItemESP.mc.getRenderManager().viewerPosZ;
            final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks - exception;
            final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks + entity.height / 2.0f - renderPosY;
            final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks - renderPosZ;
            final float playerViewY = ItemESP.mc.getRenderManager().playerViewY;
            final float playerViewX = ItemESP.mc.getRenderManager().playerViewX;
            final boolean thirdPersonView = ItemESP.mc.getRenderManager().options.thirdPersonView == 2;
            GL11.glPushMatrix();
            GlStateManager.translate(xPos, yPos, zPos);
            GlStateManager.rotate(-playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)(thirdPersonView ? -1 : 1) * playerViewX, 1.0f, 0.0f, 0.0f);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
            GL11.glBegin(1);
            GL11.glVertex3d(0.0, 1.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 1.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
        catch (Exception exception2) {
            exception2.printStackTrace();
        }
    }
}
