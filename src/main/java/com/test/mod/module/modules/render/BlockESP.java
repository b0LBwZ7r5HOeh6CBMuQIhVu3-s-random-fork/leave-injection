package com.test.mod.module.modules.render;

import com.test.mod.Utils.ReflectUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import com.test.mod.Utils.HUDUtils;
import java.awt.Color;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import com.test.mod.Utils.Tools;
import java.util.TimerTask;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.ArrayList;

import com.test.mod.module.ModuleType;
import com.test.mod.Utils.TimerUtils;
import java.util.Timer;
import net.minecraft.util.BlockPos;
import java.util.List;
import java.util.stream.Collectors;

import com.test.mod.settings.IntegerSetting;
import com.test.mod.settings.EnableSetting;
import com.test.mod.module.Module;

public class BlockESP extends Module
{
    private EnableSetting chest;
    private EnableSetting bed;
    public IntegerSetting r;
    private List<BlockPos> ren;
    private Timer t;
    private TimerUtils timerUtils;
    public EBlockPos pos;
    public static List<BlockPos> toRender;
    
    public BlockESP() {
        super("BlockESP", 0, ModuleType.Render, false);
        this.chest = new EnableSetting("Chest", true);
        this.bed = new EnableSetting("Bed", true);
        this.r = new IntegerSetting("Delay", 0.0, 0.0, 0.0, 1);
        this.pos = new EBlockPos();
        this.add(this.chest, this.bed);
    }
    
    static {
        BlockESP.toRender = new ArrayList<BlockPos>();
    }

    
    public boolean test(BlockPos pos1) {
        return this.isTarget(pos1);
    }
    
    private int[] c(Block b) {
        int red = 0;
        int green = 0;
        int blue = 0;
        if (b.equals(Blocks.bed)) {
            red = 255;
        }
        else if (b.equals(Blocks.chest)) {
            red = 200;
            green = 200;
        }
        return new int[] { red, green, blue };
    }
    
//    private TimerTask t() {
//        return new TimerTask() {
//             BlockESP this$0;
//
//            void BlockESP$1() {
//                this.this$0 = this$0;
//            }
//
//            @Override
//            public void run() {
//                this.this$0.ren.clear();
//                int y;
//                for (int ra = y = (int)this.this$0.r.getCurrent(); y >= -ra; --y) {
//                    for (int x = -ra; x <= ra; ++x) {
//                        for (int z = -ra; z <= ra; ++z) {
//                            if (Tools.isPlayerInGame()) {
//                                BlockPos p = new BlockPos(Module.mc.thePlayer.posX + x, Module.mc.thePlayer.posY + y, Module.mc.thePlayer.posZ + z);
//                                Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
//                                if ((this.this$0.bed.getEnable() && bl.equals(Blocks.bed)) || (this.this$0.chest.getEnable() && bl.equals(Blocks.chest))) {
//                                    this.this$0.ren.add(p);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    
    public static void re(BlockPos bp, int color) {
        if (bp == null) {
            return;
        }
        double x = bp.getX() - getRenderPosX();
        double y = bp.getY() - getRenderPosY();
        double z = bp.getZ() - getRenderPosZ();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        float a = (color >> 24 & 0xFF) / 255.0f;
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        GL11.glColor4d((double)r, (double)g, (double)b, (double)a);
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (!Tools.isPlayerInGame()) {
            return;
        }
        WorldClient world = BlockESP.mc.theWorld;
        EntityPlayerSP player = BlockESP.mc.thePlayer;
        if (world != null && player != null) {
            int sx = (int)player.posX - 15;
            int sz = (int)player.posZ - 15;
            int endX = (int)player.posX + 15;
            int endZ = (int)player.posZ + 15;
            for (int x = sx; x <= endX; ++x) {
                this.pos.setX(x);
                for (int z = sz; z <= endZ; ++z) {
                    Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                    if (!chunk.isLoaded()) continue;
                    this.pos.setZ(z);
                    for (int y = 0; y <= 255; ++y) {
                        BlockPos poss;
                        this.pos.setY(y);
                        IBlockState blockState = chunk.getBlockState((BlockPos)this.pos);
                        Block block = blockState.getBlock();
                        if (block == Blocks.air || toRender.contains((Object)(poss = new BlockPos(x, y, z))) || !this.test(poss) || toRender.size() > 10) continue;
                        toRender.add(poss);
                    }
                }
            }
            List<BlockPos> list = toRender;
            toRender = list = list.stream().filter(this::test).collect(Collectors.toList());
            if (!toRender.isEmpty()) {
                ArrayList<BlockPos> tRen = new ArrayList<BlockPos>(toRender);
                for (BlockPos p : tRen) {
                    this.dr(p);
                }
            }
            this.timerUtils.reset();
        }
    }
    
    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    private void dr(BlockPos p) {
        int[] rgb = this.c(BlockESP.mc.theWorld.getBlockState(p).getBlock());
        if (rgb[0] + rgb[1] + rgb[2] != 0) {
            HUDUtils.re(p, new Color(rgb[0], rgb[1], rgb[2]).getRGB(), true);
        }
    }
    
    @Override
    public void onEnable() {
        this.timerUtils = new TimerUtils();
        BlockESP.toRender.clear();
        this.timerUtils.reset();
        BlockESP.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    public boolean isTarget(BlockPos pos) {
        Block block = BlockESP.mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.chest.equals(block)) {
            return this.chest.getEnable();
        }
        return Blocks.bed.equals(block) && this.bed.getEnable();
    }
    
    public static double getRenderPosY() {
        return (double)ReflectUtil.getField("renderPosY", "renderPosY", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosZ() {
        return (double)ReflectUtil.getField("renderPosZ", "renderPosZ", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosX() {
        return (double)ReflectUtil.getField("renderPosX", "renderPosX", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static void renderChest(BlockPos blockPos) {
        double d0 = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d2 = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d3 = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        glColor(Color.WHITE.getRGB());
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(d0, d2, d3, d0 + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void renderEnderChest(BlockPos blockPos) {
        double d0 = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d2 = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d3 = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(true);
        glColor(Color.WHITE.getRGB());
        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(d0, d2, d3, d0 + 1.0, d2 + 1.0, d3 + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 10.0f;
        float red = (hex >> 16 & 0xFF) / 255.0f;
        float green = (hex >> 8 & 0xFF) / 255.0f;
        float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, 15.0f);
    }
}
